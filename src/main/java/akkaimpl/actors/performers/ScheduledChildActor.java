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
        //getContext().system().scheduler().scheduleOnce(Duration.ofSeconds(4), self(), new TickMessage(), getContext().system().dispatcher(), self());
        getContext().system().scheduler().schedule(Duration.ZERO, Duration.ofSeconds(1), getSelf(), new TickMessage(), getContext().system().dispatcher(), getSender());
    }

    public void doSomething(TickMessage message) {
        System.out.println("tick received");
    }

    @Override
    public void preStart() {
        System.out.println("ScheduledChildActor started with initial state " + this.scheduledChildActorState );
    }

    @Override
    public void postStop() {
        System.out.println("ScheduledChildActor stopped with final state " + this.scheduledChildActorState );
    }
}
