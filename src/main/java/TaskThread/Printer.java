package TaskThread;

import Task.Client.TaskPrint;
import Task.Task;

/**
 * Created by eugene on 12/19/14.
 */
public class Printer extends TaskThread {

    public Printer(boolean isMaster, long id) {
        super(isMaster, id);
    }

    @Override
    protected void runConcreteTask(Task task) {
        TaskPrint concrete = (TaskPrint) task;
        System.out.print(concrete.message);
    }
}
