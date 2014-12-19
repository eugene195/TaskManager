package TaskThread;

import Task.Task;
import Task.Client.TaskSort;

import java.util.Collections;


public class Sorter extends TaskThread {

    public Sorter(boolean isMaster, long id) {
        super(isMaster, id);
        this.type = "SRT";
    }

    @Override
    protected void runConcreteTask(Task inTask) {
        if(inTask != null && !inTask.isDone()) {
            TaskSort task = (TaskSort) this.currentTask;
            Collections.sort(task.getArray());
            task.setDone(true);
            this.currentTask = null;
            ++this.tasksDone;
        }
    }

}
