package TaskThread;

import TaskThread.TaskThread;
import Task.Task;
import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;


public class Mapper {
    private static ConcurrentHashMap<Long, TaskThread> taskThreads = new ConcurrentHashMap<>();
    private static ConcurrentSkipListSet<Task> tasks = new ConcurrentSkipListSet<>();

    public static TaskThread getTaskThread (long id) {
        return taskThreads.get(id);
    }

    public static void deleteTaskThread (long id) {

    }

    public static void addTaskThread(TaskThread newTaskThread) {
        taskThreads.put(newTaskThread.getId(), newTaskThread);
    }

    public static void addTask (Task task) {
        tasks.add(task);
    }

    public static ConcurrentSkipListSet<Task> getTasks () {
        ConcurrentSkipListSet<Task> returnedTasks = tasks;
        tasks = new ConcurrentSkipListSet<>();
        return returnedTasks;
    }

    public static Collection<TaskThread> getTaskThreads() {
        return taskThreads.values();
    }
}
