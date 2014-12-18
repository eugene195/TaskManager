package TaskThread;

import Task.Task;
import TaskThread.TaskThread;

/**
 * Created by Nikita on 18.12.2014.
 */
public class Multiplier extends TaskThread {
    public Multiplier(boolean isMaster, long id) {
        super(isMaster, id);
        this.type = "MLT";
    }

    @Override
    protected void runConcreteTask() {

    }

    @Override
    protected void setTask(Task task) {

    }

    @Override
    protected String getType() {
        return null;
    }

}
