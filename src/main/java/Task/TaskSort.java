package Task;

import java.util.ArrayList;

/**
 * Created by Nikita on 18.12.2014.
 */
public class TaskSort extends Task {
    private ArrayList<Integer> array;

    public TaskSort(ArrayList<Integer> array) {
        this.array = array;
        this.type = "SRT";
    }

    public ArrayList<Integer> getArray() {
        return array;
    }


}
