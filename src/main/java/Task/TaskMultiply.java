package Task;

import java.util.ArrayList;
public class TaskMultiply extends Task{
    ArrayList<Double> array;
    private Double result;
    public TaskMultiply(ArrayList<Double> array) {
        this.array = array;
        this.type = "MLT";
    }

    public ArrayList<Double> getArray() {
        return this.array;
    }
    public void setResult(Double result) {
        this.result = result;
    }
    public Double getResult() {
        return this.result;
    }
}
