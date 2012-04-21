package jobs;

import it.sauronsoftware.cron4j.Task;
import it.sauronsoftware.cron4j.TaskExecutionContext;
import plugin.job.OnApplicationStop;

@OnApplicationStop
public class StopJob extends Task {

    @Override
    public void execute(TaskExecutionContext arg0) throws RuntimeException {
        System.out.println("Application Stop!");
    }

}