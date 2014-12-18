import java.util.ArrayList;

/**
 * Created by eugene on 12/18/14.
 */
public class Sorter extends TaskThread {

    private ArrayList<Integer> array;

    public void sort(ArrayList<Integer> arrayToSort) {
        this.array = arrayToSort;
    }

    public Sorter(boolean isMaster, long id) {
        super(isMaster, id);
    }

    @Override
    protected void runConcreteTask() {
        //blablabla
    }

    @Override
    protected void setTask(Task task) {

    }

    @Override
    protected String getType() {
        return null;
    }

    @Override
    protected void setType(String type) {

    }
}
