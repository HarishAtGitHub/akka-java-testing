package akkaimpl.actors.routers;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.DiagnosticLoggingAdapter;
import akka.event.Logging;
import akka.japi.Creator;
import akkaimpl.actors.performers.ChildActor1;
import akkaimpl.actors.performers.ScheduledChildActor;
import akkaimpl.factories.ActorRefCreatorFactory;
import akkaimpl.messages.testhelpers.GetStateMessage;
import akkaimpl.messages.testhelpers.StateMessage;
import akkaimpl.state.ChildActor1State;
import akkaimpl.messages.*;
import akkaimpl.state.ScheduledChildActorState;
import akkaimpl.state.State;

public class Router extends AbstractActor {
    State state;
    ActorRef childActor1;
    ActorRef scheduledChildActor;
    ActorRefCreatorFactory factory;

    /**
     * Logger
     */
    private final DiagnosticLoggingAdapter LOGGER = Logging.getLogger(this);

    public Router(State routerState, ActorRefCreatorFactory factory){
        this.state = routerState;
        this.factory = factory;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ChangeStateMessage.class, this::makeStateChange)
                .match(CommunicateWithActorMessage.class, this::communicateWithAnotherActor)
                .match(GetStateMessage.class, this::handleGetState).build();
    }

    public static Props props(final State initialRouterState, final ActorRefCreatorFactory factory) {
        return Props.create(Router.class, new Creator<Router>() {
            private static final long serialVersionUID = 1L;

            @Override public Router create() {
                return new Router(initialRouterState, factory);
            }
        });
    }

    private void makeStateChange(Message msg) {
        System.out.println("old state of router " + this.state);
        this.state.setState(msg.getContent());
        System.out.println("new state of router " + this.state);
    }

    private void communicateWithAnotherActor(Message msg) {
        ActorRef childActor1 = createChildIfNull();
        ChildActor1Message childActor1Message = new ChildActor1Message(11111);
        LOGGER.info("sending message to childActor1");
        childActor1.tell(childActor1Message, getSelf());
        ActorRef scheduledChildActor = createScheduledChildIfNull();
        StartSchedulerMessage scheduledChildActorMessage = new StartSchedulerMessage(0);
        scheduledChildActor.tell(scheduledChildActorMessage, getSelf());
    }

    private ActorRef createChildIfNull() {
        if (this.childActor1 == null) {
            this.childActor1 = factory.createActor(getContext(), ChildActor1.class, state);
        }
        return this.childActor1;
    }

    private ActorRef createScheduledChildIfNull() {
        if (this.scheduledChildActor == null) {
            ScheduledChildActorState state = new ScheduledChildActorState(2222);
            //this.scheduledChildActor = getContext().actorOf(ScheduledChildActor.props(state), "scheduledchildactor");
            this.scheduledChildActor = factory.createActor(getContext(), ScheduledChildActor.class, state);
        }
        return this.scheduledChildActor;
    }

    private void handleGetState(GetStateMessage message) {
        getSender().tell(new StateMessage(this.state), getSelf());
    }

    public State getState() {
        return this.state;
    }

    @Override
    public void preStart() {
        System.out.println("Router started with initial state " + this.state);
    }

    @Override
    public void postStop() {
        System.out.println("Router stopped with final state " + this.state);
    }

}
