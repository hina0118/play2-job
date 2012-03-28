package job;

import it.sauronsoftware.cron4j.Scheduler;
import it.sauronsoftware.cron4j.Task;

import java.util.Set;

import play.Application;
import play.Logger;
import play.Plugin;

public class JobPlugin extends Plugin {
	private final Application application;
	private final Scheduler scheduler;
	private static final String DEFAULT_PACKAGE = "jobs";
	private final String jobPackage;
	
	public JobPlugin(Application application) {
		this.application = application;
		scheduler = new Scheduler();
		String pkg = application.configuration().getString("job.package");
		if (pkg == null) {
			jobPackage = DEFAULT_PACKAGE;
		} else {
			jobPackage = pkg;
		}
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
			final Task job = newJobInstance(name);
			scheduler.schedule(job.getClass().getAnnotation(On.class).value(), job);
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
			throw new JobException(message, e);
		} catch (InstantiationException e) {
			String message = String.format("%sがインスタンス化できませんでした");
			Logger.error(message, e);
			throw new JobException(message, e);
		} catch (IllegalAccessException e) {
			String message = String.format("%sがインスタンス化できませんでした");
			Logger.error(message, e);
			throw new JobException(message, e);
		}
		return job;
	}

}
