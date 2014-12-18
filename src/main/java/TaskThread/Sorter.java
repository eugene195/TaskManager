package TaskThread;

import Task.*;
import java.util.Collections;


public class Sorter extends TaskThread {

    public Sorter(boolean isMaster, long id) {
        super(isMaster, id);
        this.type = "SRT";
    }

    @Override
    protected void runConcreteTask() {
        if(this.currentTask != null) {
            TaskSort task = (TaskSort) this.currentTask;
            Collections.sort(task.getArray());
            task.setDone(true);
            this.currentTask = null;
            ++this.tasksDone;
        }
    }

    @Override
    protected void setTask(Task task) throws IllegalAccessException{
        if(!task.getType().equals("SRT")) {
            throw new IllegalAccessException("TaskThread.Sorter got wrong type of task!");
        }
        this.currentTask = task;
    }
}
