package TaskThread;

import TaskThread.TaskThread;

import java.util.Collection;
import java.util.HashMap;


public class Mapper {
    private volatile static HashMap<Long, TaskThread> tasks;

    public static TaskThread getTask (long id) {
        return tasks.get(id);
    }

    public static void addTask (TaskThread newTaskThread) {
        synchronized (tasks) {
            tasks.put(newTaskThread.getId(), newTaskThread);
        }
    }

    public static void deleteTask (long id) {
        synchronized (tasks) {
            tasks.remove(id);
        }
    }

    public static Collection<TaskThread> getTasks () {
        return tasks.values();
    }
}
