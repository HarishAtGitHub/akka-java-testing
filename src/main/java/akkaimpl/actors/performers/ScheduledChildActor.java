package akkaimpl.actors.performers;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.Creator;
import akkaimpl.messages.Message;
import akkaimpl.messages.StartSchedulerMessage;
import akkaimpl.state.State;

public class ScheduledChildActor extends AbstractActor {
    State scheduledChildActorState;

    public ScheduledChildActor(State state){
        this.scheduledChildActorState = state;
    }

    public static Props props(final State scheduledChildActorState) {
        return Props.create(ScheduledChildActor.class, new Creator<ScheduledChildActor>() {
            private static final long serialVersionUID = 1L;

            @Override public ScheduledChildActor create() {
                return new ScheduledChildActor(scheduledChildActorState);
            }
        });
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(StartSchedulerMessage.class, this::scheduleThisActor)
                .build();
    }

    public void scheduleThisActor(Message message) {
        System.out.println("scheduler reached");
    }
}
