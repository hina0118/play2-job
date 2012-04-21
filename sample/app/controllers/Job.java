package controllers;

import java.util.ArrayList;
import java.util.List;

import plugin.job.JobData;
import plugin.job.JobPlugin;
import play.Play;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.Job.index;

public class Job extends Controller {
	
	public static Result INDEX = redirect(routes.Job.index());
	
	public static Result index() {
		JobPlugin plugin = Play.application().plugin(JobPlugin.class);
		List<JobData> jobs = new ArrayList<JobData>(plugin.getJobMap().values());
		return ok(index.render(jobs));
	}

	public static Result launch(String id) {
		JobPlugin plugin = Play.application().plugin(JobPlugin.class);
		plugin.launch(id);
		return INDEX;
	}
	
	public static Result schedule(String id, String schedulingPattern) {
		JobPlugin plugin = Play.application().plugin(JobPlugin.class);
		plugin.schedule(schedulingPattern, id);
		return INDEX;
	}
	
	public static Result reschedule(String id, String schedulingPattern) {
		JobPlugin plugin = Play.application().plugin(JobPlugin.class);
		plugin.reschedule(schedulingPattern, id);
		return INDEX;
	}
	
	public static Result deschedule(String id) {
		JobPlugin plugin = Play.application().plugin(JobPlugin.class);
		plugin.deschedule(id);
		return INDEX;
	}
}