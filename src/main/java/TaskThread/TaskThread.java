package TaskThread;

import Task.Task;
import Task.TaskEmpty;
import Task.TaskRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Queue;

import static java.lang.Thread.sleep;

/**
 * Created by eugene on 12/18/14.
 */
public abstract class TaskThread implements Runnable {
    boolean isMaster = false;
    private Queue<Task> toDoList;
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
        while (true) {
            toDoList = Mapper.getTasks(id);

            if (toDoList.isEmpty()) {
                if (!isMaster)
                    Mapper.sendTask(new TaskRequest(), getMasterId());
            }
            else {
                if (isMaster) {
                    runMasterTasks();
                }
                else {
                    runSlaveTasks();
                }
            }

            try {
                sleep(500);
            }
            catch (InterruptedException exc) {
                exc.printStackTrace();
            }
        }
    }


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

    protected void runMasterTasks() {
        ArrayList<Task> performanceTasks = new ArrayList<>();

        for (Task task : toDoList) {
            if (task.getType().equals("performance")) {
                performanceTasks.add(task);
            }
        }

        Collection<TaskThread> threads = Mapper.getTaskThreads();
        int threadsCount = threads.size() - 1;
        ArrayList<Long> threadIDs = new ArrayList<>();

        for (TaskThread thread : threads) {
            if (this.id != thread.getId())
                threadIDs.add(thread.getId());
        }
        int tasksCount = performanceTasks.size();

        for (int i = 0; i < (tasksCount % threadsCount); i++)
            performanceTasks.add(new TaskEmpty());

        int tasksPerThread = (performanceTasks.size() / threadsCount);
        for (int i = 0; i < threadsCount; i++) {
            for (int j = 0; j < tasksPerThread; j++) {
                Mapper.sendTask(performanceTasks.get(i+j), threadIDs.get(i));
            }
        }
    }

    protected void runSlaveTasks () {

    }


    protected abstract void runConcreteTask();
    protected void setTask(Task task) throws IllegalAccessException {
        if(!task.getType().equals(this.getType())) {
            throw new IllegalAccessException("TaskThread." + this.type + " got wrong type of task!");
        }
    }
    protected String type;
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
}
