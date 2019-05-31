package akkaimpl.state;

import akkaimpl.messages.testhelpers.StateMessage;

import java.util.Objects;

public class RouterState extends State {

    public RouterState(int state){
        super(state);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof State)) return false;
        State state = (State) o;
        return this.getState() == state.getState();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this);
    }
}
