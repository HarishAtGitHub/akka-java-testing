import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.JavaTestKit;
import akka.testkit.TestActorRef;
import akka.testkit.TestProbe;
import akkaimpl.actors.performers.ChildActor1;
import akkaimpl.messages.ChangeStateMessage;
import akkaimpl.messages.CommunicateWithActorMessage;
import akkaimpl.messages.testhelpers.GetStateMessage;
import akkaimpl.messages.testhelpers.StateMessage;
import akkaimpl.state.RouterState;
import akkaimpl.state.State;
import javafx.util.Duration;
import org.junit.Before;
import org.junit.Test;
import akka.testkit.TestKit;
import akkaimpl.actors.routers.Router;
import static org.junit.Assert.*;

public class TestRouter {
    static ActorSystem system;

    @Before
    public void setup() {
        system = ActorSystem.create();
    }



    @Test
    public void testStateChangeSingleThreaded() {
        new TestKit(system) {{
            RouterState routerInitialState = new RouterState(0);
            final Props props = Props.create(Router.class, routerInitialState);
            final TestActorRef<Router> testActorRef = TestActorRef.create(system,
                    props, "Router");

            testActorRef.tell(new ChangeStateMessage(4), ActorRef.noSender());
            Router testActor = testActorRef.underlyingActor();
            State internalState = testActor.getState();
            assertEquals(new RouterState(4), internalState);
        }};
    }

    @Test
    public void testStateChangeMultiThreaded() {
        new TestKit(system) {{
            RouterState routerInitialState = new RouterState(0);
            // use TestKit as it is multi threaded test.
            final TestKit testKit = new TestKit(system);
            ActorRef router = system.actorOf(Router.props(routerInitialState), "router");
            router.tell(new ChangeStateMessage(4), router);
            router.tell(new GetStateMessage(), testKit.testActor());
            testKit.expectMsg(new StateMessage(new State(4)));
        }};
    }
}
