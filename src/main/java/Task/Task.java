package Task;

/**
 * Created by eugene on 12/18/14.
 */
public abstract class Task {
    protected String type;
    private boolean done = false;

    public void execute() {

    }

    public void setDone(boolean isDone) {
        this.done = isDone;
    }
    public boolean isDone() {
        return this.done;
    }

    public String getType() {
        return type;
    }
}
