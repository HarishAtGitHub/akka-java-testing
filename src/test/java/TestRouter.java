import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.JavaTestKit;
import akka.testkit.TestProbe;
import akkaimpl.messages.ChangeStateMessage;
import akkaimpl.messages.testhelpers.GetStateMessage;
import akkaimpl.messages.testhelpers.StateMessage;
import akkaimpl.state.RouterState;
import akkaimpl.state.State;
import javafx.util.Duration;
import org.junit.Before;
import org.junit.Test;
import akka.testkit.TestKit;
import akkaimpl.actors.routers.Router;
import scala.concurrent.duration.FiniteDuration;

import java.util.concurrent.TimeUnit;

public class TestRouter {
    static ActorSystem system;

    @Before
    public void setup() {
        system = ActorSystem.create();
    }

    @Test
    public void testStateChange() {
        new TestKit(system) {{
            RouterState routerInitialState = new RouterState(0);
            TestProbe probe = new TestProbe(system);
            ActorRef router = system.actorOf(Router.props(routerInitialState), "router");
            router.tell(new ChangeStateMessage(4), router);
            router.tell(new GetStateMessage(), probe.ref());
            probe.expectMsg(new StateMessage(new State(4)));
        }};
    }


    public void testCommunicateWithActor() {

    }
}
