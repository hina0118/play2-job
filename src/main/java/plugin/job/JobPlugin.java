package plugin.job;

import it.sauronsoftware.cron4j.Scheduler;
import it.sauronsoftware.cron4j.Task;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import play.Application;
import play.Logger;
import play.Plugin;

public class JobPlugin extends Plugin {
	private final Application application;
	private final Scheduler scheduler;
	private static final String DEFAULT_PACKAGE = "jobs";
	private final String jobPackage;
	private final Map<String, JobData> jobMap;
	
	public JobPlugin(Application application) {
		this.application = application;
		scheduler = new Scheduler();
		String pkg = application.configuration().getString("job.package");
		jobPackage = pkg == null ? DEFAULT_PACKAGE : pkg;
		jobMap = new LinkedHashMap<String, JobData>();
	}

    @Override
	public void onStart() {
		scheduler.start();
		Set<String> startJobNames = application.getTypesAnnotatedWith(jobPackage, OnApplicationStart.class);
		for (String name : startJobNames) {
			scheduler.launch(newJobInstance(name));
		}
		Set<String> jobNames = application.getTypesAnnotatedWith(jobPackage, On.class);
		for (String name : jobNames) {
			Task job = newJobInstance(name);
			String schedulingPattern = job.getClass().getAnnotation(On.class).value();
			String id = scheduler.schedule(schedulingPattern, job);
			jobMap.put(id, new JobData(schedulingPattern, id, job, JobData.Status.RUNNING));
		}
	}

	@Override
	public void onStop() {
		Set<String> stopJobNames = application.getTypesAnnotatedWith(jobPackage, OnApplicationStop.class);
		for (String name : stopJobNames) {
			scheduler.launch(newJobInstance(name));
		}
		scheduler.stop();
	}
	
	private Task newJobInstance(String name) {
		Task job = null;
		try {
			Class<?> cls = Class.forName(name);
			job = (Task) cls.newInstance();
		} catch (ClassNotFoundException e) {
			String message = String.format("%sが見つかりませんでした");
			Logger.error(message, e);
			throw new RuntimeException(message, e);
		} catch (InstantiationException e) {
			String message = String.format("%sがインスタンス化できませんでした");
			Logger.error(message, e);
			throw new RuntimeException(message, e);
		} catch (IllegalAccessException e) {
			String message = String.format("%sがインスタンス化できませんでした");
			Logger.error(message, e);
			throw new RuntimeException(message, e);
		}
		return job;
	}
	
	public synchronized Map<String, JobData> getJobMap() {
		return jobMap;
	}
	
	public synchronized void launch(String id) {
		Logger.info(String.format("[%s] を実行します。", jobMap.get(id)));
		scheduler.launch(jobMap.get(id).job);
	}
	
	public synchronized String schedule(String schedulingPattern, String id) {
		Logger.info(String.format("[%s] を開始します。(%s)", jobMap.get(id), schedulingPattern));
		JobData jobData = jobMap.remove(id);
		String newId = scheduler.schedule(schedulingPattern, jobData.job);
		jobData = new JobData(schedulingPattern, newId, jobData.job, JobData.Status.RUNNING);
		jobMap.put(newId, jobData);
		return newId;
	}
	
	public synchronized void reschedule(String schedulingPattern, String id) {
		Logger.info(String.format("[%s] を再設定します。(%s)", jobMap.get(id), schedulingPattern));
		scheduler.reschedule(id, schedulingPattern);
		JobData jobData = jobMap.get(id);
		jobData.schedulingPattern = schedulingPattern;
	}
	
	public synchronized void deschedule(String id) {
		Logger.info(String.format("[%s] を停止します。", jobMap.get(id)));
		scheduler.deschedule(id);
		jobMap.get(id).status = JobData.Status.STOP;
	}
}
