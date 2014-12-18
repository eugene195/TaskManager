import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by eugene on 12/18/14.
 */
public class Mapper {
    private static ConcurrentHashMap<Long, TaskThread> taskThreads = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<Long, ConcurrentLinkedQueue<Task>> messages = new ConcurrentHashMap<>();

    public static TaskThread getTaskThread (long id) {
        return taskThreads.get(id);
    }

    public static void addTask (TaskThread newTaskThread) {
        taskThreads.put(newTaskThread.getId(), newTaskThread);
        messages.put(newTaskThread.getId(), new ConcurrentLinkedQueue<>());
    }

    public static void sendTask (Task msg, Long addressTo) {
        messages.get(addressTo).add(msg);
    }

    public static void deleteTask (long id) {
        taskThreads.remove(id);
        messages.remove(id);
    }

    public static Queue<Task> getTasks (long id) {
        return messages.get(id);
    }

    public static Collection<TaskThread> getTaskThreads() {
        return taskThreads.values();
    }
}
