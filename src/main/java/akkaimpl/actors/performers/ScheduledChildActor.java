package akkaimpl.actors.performers;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.Creator;
import akkaimpl.messages.Message;
import akkaimpl.messages.StartSchedulerMessage;
import akkaimpl.messages.TickMessage;
import akkaimpl.state.State;

import java.time.Duration;

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
                .match(TickMessage.class, this::doSomething)
                .build();
    }

    public void scheduleThisActor(Message message) {
        System.out.println("scheduler reached");
        getContext().system().scheduler().scheduleOnce(Duration.ofSeconds(4), self(), new TickMessage(), getContext().system().dispatcher(), self());
    }

    public void doSomething(TickMessage message) {
        System.out.println("tick received");
    }
}
