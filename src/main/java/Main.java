import TaskThread.TaskThread;
import TaskThread.Sorter;
import TaskThread.Printer;
import java.util.ArrayList;

/**
 * Created by eugene on 12/18/14.
 */
public class Main {
    public static void main(String[] args) {
        ArrayList<TaskThread> taskThreads = new ArrayList<>();
        for(int i = 0; i < 5; ++i) {
           taskThreads.add(new Printer(false, i));
        }
        taskThreads.get(0).upgrade();
        for( TaskThread worker : taskThreads) {
            Thread thread = new Thread(worker);
            thread.start();
            thread.join();
        }

    }

}
