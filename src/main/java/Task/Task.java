package Task;

/**
 * Created by eugene on 12/18/14.
 */
public abstract class Task {
    protected String type;
    private boolean done = false;

    public void setDone(boolean isDone) {
        this.done = isDone;
    }
    public String getType() {
        return type;
    }
}
