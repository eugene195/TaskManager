package TaskThread;

import TaskThread.TaskThread;
import Task.Task;
import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;


public class Mapper {
    private static ConcurrentHashMap<Long, TaskThread> taskThreads = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<Long, ConcurrentLinkedQueue<Task>> tasks = new ConcurrentHashMap<>();

    public static TaskThread getTaskThread (long id) {
        return taskThreads.get(id);
    }

    public static void addTask (TaskThread newTaskThread) {
        taskThreads.put(newTaskThread.getId(), newTaskThread);
        tasks.put(newTaskThread.getId(), new ConcurrentLinkedQueue<>());
    }

    public static void sendTask (Task msg, Long addressTo) {
        tasks.get(addressTo).add(msg);
    }

    public static void deleteTask (long id) {
        taskThreads.remove(id);
        tasks.remove(id);
    }

    public static Queue<Task> getTasks (long id) {
        Queue<Task> tasksForId = tasks.get(id);
        tasks.remove(id);
        return tasksForId;
    }

    public static Collection<TaskThread> getTaskThreads() {
        return taskThreads.values();
    }
}
