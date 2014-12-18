package TaskThread;

import Task.Task;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

/**
 * Created by eugene on 12/18/14.
 */
abstract public class TaskThread implements Runnable {
    boolean isMaster = false;

    public boolean isMaster() {
        return isMaster;
    }

    protected long id;

    public long getId () {
        return id;
    }

    public TaskThread(boolean isMaster, long id) {
        this.isMaster = isMaster;
        this.id = id;
    }

    public void promote() {
        this.isMaster = true;
    }


    private boolean hasTask = false;
    @Override
    public void run() {
        if (isMaster) {

        } else {пол
            try {
                while (true) {
                    if (hasTask) {
                        runConcreteTask();
                    } else {
                        hasTask = findMaster().giveTask(this);
                        if (!hasTask) {
                            sleep(5000);
                        }
                    }
                }
            }
            catch (InterruptedException exc) {
                exc.printStackTrace();
            }
        }
    }

    private ArrayList<Task> toDoList;


    protected Task currentTask = null;
    protected int tasksDone = 0;
    public boolean giveTask (TaskThread taskThread) {
        if (!isMaster) {
            System.out.print("Achtung! Not a master inside giveTask");
            return false;
        }
        for(Task task: toDoList) {
            if(task.getType().equals(taskThread.getType())) {
                try {
                    taskThread.setTask(task);
                }
                catch (IllegalAccessException exc) {
                    exc.printStackTrace();
                }
                return true;
            }
        }
        return false;
    }


    protected abstract void runConcreteTask();

    protected void setTask(Task task) throws IllegalAccessException {
        if(!task.getType().equals(this.getType())) {
            throw new IllegalAccessException("TaskThread." + this.type + " got wrong type of task!");
        }
        this.currentTask = task;
    }
    protected String type;
    protected String getType() {
        return this.type;
    }


    protected TaskThread findMaster() {
        if(this.isMaster) {
            return this;
        } else {
            for(TaskThread taskThread : Mapper.getTasks()) {
                if (taskThread.isMaster())
                    return taskThread;
            }
        }
        return null;
    }
}
