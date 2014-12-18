import TaskThread.TaskThread;
import TaskThread.Sorter;
import java.util.ArrayList;

/**
 * Created by eugene on 12/18/14.
 */
public class Main {
    public static void main(String[] args) {
        ArrayList<TaskThread> taskThreads = new ArrayList<TaskThread>();
       for(int i = 0; i < 5; ++i) {
           taskThreads.add(new Sorter(false, i));
       }
       taskThreads.get(0).promote();

    }

}
