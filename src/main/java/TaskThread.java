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

    public TaskThread() {
        this.id = -1;
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

        } else {
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

    public boolean giveTask (TaskThread taskThread) {
        if (!isMaster) {
            System.out.print("Achtung! Not a master inside giveTask");
            return false;
        }
        for(Task task: toDoList) {
            if(task.getType().equals(taskThread.getType())) {
                taskThread.setTask(task);
                return true;
            }
        }
        return false;
    }


    protected abstract void runConcreteTask();
    protected abstract void setTask(Task task);
    private String type;
    protected abstract String getType();
    protected abstract void setType(String type);

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
