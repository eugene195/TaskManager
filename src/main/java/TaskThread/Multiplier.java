package TaskThread;

import Task.TaskMultiply;

import java.util.ArrayList;

/**
 * Created by Nikita on 18.12.2014.
 */
public class Multiplier extends TaskThread {
    public Multiplier(boolean isMaster, long id) {
        super(isMaster, id);
        this.type = "MLT";
    }

    @Override
    protected void runConcreteTask() {
        if(this.currentTask != null && !this.currentTask.isDone()) {
            TaskMultiply task = (TaskMultiply) this.currentTask;
            ArrayList<Double> array = task.getArray();
            Double result = 1.;
            for(Double n : array) {
                result *= n;
            }
            task.setResult(result);
            task.setDone(true);
            this.currentTask = null;
            ++this.tasksDone;
        }
    }

    @Override
    protected String getType() {
        return this.type;
    }

}
