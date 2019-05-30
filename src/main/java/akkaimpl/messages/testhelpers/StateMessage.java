package akkaimpl.messages.testhelpers;

import akkaimpl.state.State;

import java.util.Objects;

public class StateMessage {
    private State state;
    public StateMessage(State state) {
        this.state = state;
    }

    public State getState() {
        return this.state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StateMessage)) return false;
        StateMessage stateMessage = (StateMessage) o;
        return this.state.getState() == stateMessage.getState().getState();
    }

    @Override
    public int hashCode() {
        return Objects.hash(state);
    }
}
