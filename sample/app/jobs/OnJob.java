package jobs;

import it.sauronsoftware.cron4j.Task;
import it.sauronsoftware.cron4j.TaskExecutionContext;
import plugin.job.On;

import org.joda.time.DateTime;

@On("* * * * *")
public class OnJob extends Task {

    @Override
    public void execute(TaskExecutionContext arg0) throws RuntimeException {
        System.out.println(DateTime.now().toString());
    }

}