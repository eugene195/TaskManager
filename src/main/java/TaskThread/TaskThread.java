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
                // Если у мастера нет задач - он стоит. Иначе создается таск для мастера с названием "Дай мне задачу"
                // Тут же будет анализ, делался такой таск уже или нет
                if (!isMaster)
                    Mapper.sendTask(new TaskRequest(), getMasterId());
            }
            else {
                // Задачи есть, Ваш КО
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
        // "реальные задачи" - сортировки, умножения и т.п., а не служебные
        ArrayList<Task> performanceTasks = new ArrayList<>();

        // Взяли все такие реальные задачи
        for (Task task : toDoList) {
            if (task.getType().equals("performance")) {
                performanceTasks.add(task);
            }
        }

        Collection<TaskThread> threads = Mapper.getTaskThreads();
        int threadsCount = threads.size() - 1;
        ArrayList<Long> threadIDs = new ArrayList<>();

        // Взяли все потоки, которым эти задачи можно дать
        for (TaskThread thread : threads) {
            if (this.id != thread.getId())
                threadIDs.add(thread.getId());
        }
        int tasksCount = performanceTasks.size();

        // Дополнили реальные задачи пустыми до целого деления на кол-во потоков
        for (int i = 0; i < (tasksCount % threadsCount); i++)
            performanceTasks.add(new TaskEmpty());

        // Пошли распределять реальные задачи на все потоки, каждому потоку по tasksPerThread
        // После этого потоки проснутся, а у них задач куча...
        int tasksPerThread = (performanceTasks.size() / threadsCount);
        for (int i = 0; i < threadsCount; i++) {
            for (int j = 0; j < tasksPerThread; j++) {
                Mapper.sendTask(performanceTasks.get(i+j), threadIDs.get(i));
            }
        }
    }

    protected void runSlaveTasks () {
        for (Task task : toDoList) {
            if (task.getType().equals(getType())) {
                runConcreteTask(task);
            }
            else {
                System.out.print("TaskThread." + this.id + " got wrong type of task!" + task.getType());
            }
        }
    }


    protected abstract void runConcreteTask(Task task);
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
