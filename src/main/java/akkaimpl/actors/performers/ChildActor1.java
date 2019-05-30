package akkaimpl.actors.performers;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.Creator;
import akkaimpl.actors.routers.Router;
import akkaimpl.messages.ChildActor1Message;
import akkaimpl.messages.Message;
import akkaimpl.state.ChildActor1State;
import akkaimpl.state.RouterState;
import akkaimpl.state.State;

public class ChildActor1 extends AbstractActor {
    State childActor1State;

    public ChildActor1(State state){
        this.childActor1State = state;
    }

    public static Props props(final State childActor1State) {
        return Props.create(ChildActor1.class, new Creator<ChildActor1>() {
            private static final long serialVersionUID = 1L;

            @Override public ChildActor1 create() {
                return new ChildActor1(childActor1State);
            }
        });
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ChildActor1Message.class, this::handleChildActor1Message)
                .build();
    }

    public void handleChildActor1Message(Message message) {
        System.out.println("message received in child actor 1 is : " + message);
    }

    @Override
    public void preStart() {
        System.out.println("ChildActor1 started with initial state " + this.childActor1State );
    }

    @Override
    public void postStop() {
        System.out.println("ChildActor1 stopped with final state " + this.childActor1State );
    }
}
