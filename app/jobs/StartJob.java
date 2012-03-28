package jobs;

import it.sauronsoftware.cron4j.Task;
import it.sauronsoftware.cron4j.TaskExecutionContext;
import job.OnApplicationStart;

@OnApplicationStart
public class StartJob extends Task {

	@Override
	public void execute(TaskExecutionContext arg0) throws RuntimeException {
		System.out.println("Application Start!");
	}

}
