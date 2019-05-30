package akkaimpl.messages.testhelpers;

import akkaimpl.state.State;

public class StateMessage {
    private State state;
    public StateMessage(State state) {
        this.state = state;
    }

    public State getState() {
        return this.state;
    }
}
