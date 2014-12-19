package TaskThread;

import Task.Task;
import Task.Service.TaskEmpty;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;

import static java.lang.Thread.sleep;

/**
 * Created by eugene on 12/18/14.
 */
public abstract class TaskThread implements Runnable {
    boolean isMaster = false;
    protected long id;
    protected boolean isAlive = true;
    protected ConcurrentSkipListSet<Task> toDoList;
    protected Task currentTask;
    protected long upgradeTime = 0;

    private boolean taskRequested = false;
    protected int tasksDone = 0;

    protected String type;
    public long getUpgadeTime() {
        return upgradeTime;
    }

    public boolean isMaster() {
        return isMaster;
    }

    public void upgrade() {
        this.isMaster = true;
    }
    public void downgrade() {
        this.isMaster = true;
    }

    public void resolve() {
        // Resolve logics
    }

    public long getId () {
        return id;
    }
    public TaskThread(boolean isMaster, long id) {
        this.isMaster = isMaster;
        this.id = id;
    }

    protected abstract void runConcreteTask(Task task);
    protected String getType() {
        return this.type;
    }

    protected long getMasterId () {
        return findMaster().getId();
    }

    protected TaskThread findMaster() {
        if(this.isMaster) {
            return this;
        } else {
            for(TaskThread taskThread : Mapper.getTaskThreads()) {
                if (taskThread.isMaster())
                    return taskThread;
            }
        }
        return null;
    }

    @Override
    public void run() {
        while (true) {
            fetchTasks();
            try {
                sleep(500);
            }
            catch (InterruptedException exc) {
                exc.printStackTrace();
            }
        }
    }
//
//    protected ConcurrentLinkedQueue<Task> fetchFromMaster() {
//        ConcurrentLinkedQueue<Task> returnedQueue = new ConcurrentLinkedQueue<>();
//        if (isAlive && isMaster) {
//            if (toDoList.isEmpty()) {
//                Task empty = new TaskEmpty();
//                returnedQueue.add(empty);
//            } else {
//                returnedQueue.add(toDoList.poll());
//            }
//        }
//        return returnedQueue;
//    }

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

    protected void setTask(Task task) throws IllegalAccessException {
        if(!task.getType().equals(this.getType())) {
            throw new IllegalAccessException("TaskThread." + this.type + " got wrong type of task!");
        }
        this.currentTask = task;
    }

    protected void fetchTasks() {
        if (this.isMaster) {
            this.toDoList.addAll(Mapper.getTasks());
        }
        else {
            TaskThread master = findMaster();
            master.giveTask(this);
            if (this.currentTask == null) {
                this.upgradeTime = System.currentTimeMillis();
                upgrade();
                Mapper.deleteTaskThread(master.getId());
                Collection<TaskThread> siblings = Mapper.getTaskThreads();
                for (TaskThread sibling : siblings) {
                    if (sibling.isMaster() && this.isMaster) {
                        if (this.upgradeTime < sibling.getUpgadeTime()) {
                            this.downgrade();
                        }
                    }
                }
            }
            else {
                this.toDoList.forEach(this::runConcreteTask);
            }
        }
    }
}
