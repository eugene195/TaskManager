package TaskThread;

import Task.Task;
import Task.TaskService;
import Task.Service.TaskEmpty;
import Task.Service.TaskRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Queue;

import static java.lang.Thread.sleep;

/**
 * Created by eugene on 12/18/14.
 */
public abstract class TaskThread implements Runnable {
    boolean isMaster = false;
    protected long id;
    private Queue<Task> toDoList;

    private boolean taskRequested = false;
    protected int tasksDone = 0;

    protected String type;

    public boolean isMaster() {
        return isMaster;
    }
    public void promote() {
        this.isMaster = true;
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
            toDoList = Mapper.getTasks(id);
            if (toDoList.isEmpty()) {
                // Если у мастера нет задач - он стоит. Иначе создается таск для мастера с названием "Дай мне задачу"
                // Тут же будет анализ, делался такой таск уже или нет
                if (taskRequested) {

                }
                if (!isMaster) {
                    Mapper.sendTask(new TaskRequest(), getMasterId());
                    taskRequested = true;
                }
            }
            else {
                taskRequested = false;
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

    protected void runMasterTasks() {
        // "реальные задачи" - сортировки, умножения и т.п., а не служебные
        ArrayList<Task> serviceTasks = new ArrayList<>();

        // Взяли все такие реальные задачи
        for (Task task : toDoList) {
            if (task instanceof TaskService) {
                serviceTasks.add(task);
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
        int tasksCount = serviceTasks.size();

        // Дополнили реальные задачи пустыми до целого деления на кол-во потоков
        for (int i = 0; i < (tasksCount % threadsCount); i++)
            serviceTasks.add(new TaskEmpty());

        // Пошли распределять реальные задачи на все потоки, каждому потоку по tasksPerThread
        // После этого потоки проснутся, а у них задач куча...
        int tasksPerThread = (serviceTasks.size() / threadsCount);
        for (int i = 0; i < threadsCount; i++) {
            for (int j = 0; j < tasksPerThread; j++) {
                Mapper.sendTask(serviceTasks.get(i+j), threadIDs.get(i));
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
}
