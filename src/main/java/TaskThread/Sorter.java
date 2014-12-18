package TaskThread;

import Task.TaskSort;

import java.util.Collections;


public class Sorter extends TaskThread {

    public Sorter(boolean isMaster, long id) {
        super(isMaster, id);
        this.type = "SRT";
    }

    @Override
    protected void runConcreteTask() {
        if(this.currentTask != null && !this.currentTask.isDone()) {
            TaskSort task = (TaskSort) this.currentTask;
            Collections.sort(task.getArray());
            task.setDone(true);
            this.currentTask = null;
            ++this.tasksDone;
        }
    }

}
