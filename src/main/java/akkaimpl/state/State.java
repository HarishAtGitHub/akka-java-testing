package akkaimpl.state;

public class State {
    private int state;
    public State(int state){
        this.state = state;
    }

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String toString() {
        return String.valueOf(this.state);
    }
}
