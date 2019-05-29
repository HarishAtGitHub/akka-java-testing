package akkaimpl.actors.routers;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.Creator;
import akkaimpl.actors.performers.ChildActor1;
import akkaimpl.actors.performers.ScheduledChildActor;
import akkaimpl.state.ChildActor1State;
import akkaimpl.messages.*;
import akkaimpl.state.ScheduledChildActorState;
import akkaimpl.state.State;

public class Router extends AbstractActor {
    State routerState;
    ActorRef childActor1;
    ActorRef scheduledChildActor;

    public Router(State routerState){
        this.routerState = routerState;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ChangeStateMessage.class, this::makeStateChange)
                .match(CommunicateWithActorMessage.class, this::communicateWithAnotherActor).build();
    }

    public static Props props(final State initialRouterState) {
        return Props.create(Router.class, new Creator<Router>() {
            private static final long serialVersionUID = 1L;

            @Override public Router create() {
                return new Router(initialRouterState);
            }
        });
    }

    private void makeStateChange(Message msg) {
        System.out.println("old state of router " + this.routerState);
        this.routerState.setState(msg.getContent());
        System.out.println("new state of router " + this.routerState);
    }

    private void communicateWithAnotherActor(Message msg) {
        createChildIfNull();
        ChildActor1Message childActor1Message = new ChildActor1Message(msg.getContent());
        this.childActor1.tell(childActor1Message, getSelf());
        createScheduledChildIfNull();
        StartSchedulerMessage scheduledChildActorMessage = new StartSchedulerMessage(msg.getContent() + 1000);
        this.scheduledChildActor.tell(scheduledChildActorMessage, getSelf());
    }

    private void createChildIfNull() {
        if (this.childActor1 == null) {
            ChildActor1State state = new ChildActor1State(0);
            this.childActor1 = getContext().actorOf(ChildActor1.props(state), "childactor1");
        }
    }

    private void createScheduledChildIfNull() {
        if (this.scheduledChildActor == null) {
            ScheduledChildActorState state = new ScheduledChildActorState(1000);
            this.scheduledChildActor = getContext().actorOf(ScheduledChildActor.props(state), "scheduledchildactor");
        }
    }

}
