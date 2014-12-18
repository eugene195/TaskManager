package Task.Client;

import Task.TaskClient;

import java.util.ArrayList;
public class TaskMultiply extends TaskClient{
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
