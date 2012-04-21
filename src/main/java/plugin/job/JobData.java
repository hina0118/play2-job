package plugin.job;

import it.sauronsoftware.cron4j.Task;

public class JobData {
	public enum Status {RUNNING, STOP};
	public String schedulingPattern;
	public String id;
	public Task job;
	public Status status;
	
	public JobData(String schedulingPattern, String id, Task job, Status status) {
		this.schedulingPattern = schedulingPattern;
		this.id = id;
		this.job = job;
		this.status = status;
	}
	
	public boolean isRunning() {
		return status == Status.RUNNING;
	}
	
	public String toString() {
		return job.toString();
	}
}
