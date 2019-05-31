import akka.actor.*;
import akka.testkit.EventFilter;
import akka.testkit.TestActorRef;
import akkaimpl.factories.ActorRefCreatorFactory;
import akkaimpl.messages.ChangeStateMessage;
import akkaimpl.messages.ChildActor1Message;
import akkaimpl.messages.CommunicateWithActorMessage;
import akkaimpl.messages.testhelpers.GetStateMessage;
import akkaimpl.messages.testhelpers.StateMessage;
import akkaimpl.state.RouterState;
import akkaimpl.state.State;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import mockit.*;
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
            ActorRefCreatorFactory factory = new ActorRefCreatorFactory();
            final Props props = Props.create(Router.class, routerInitialState, factory);
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
            ActorRefCreatorFactory factory = new ActorRefCreatorFactory();
            ActorRef router = system.actorOf(Router.props(routerInitialState, factory), "router");
            router.tell(new ChangeStateMessage(4), router);
            router.tell(new GetStateMessage(), testKit.testActor());
            testKit.expectMsg(new StateMessage(new State(4)));
        }};
    }

    @Test
    public void testActorCommunicationMultiThreaded(@Capturing final Logger LOGGER) throws InterruptedException {
        final TestKit testKit = new TestKit(system);
        new Expectations() {{
            new MockUp<ActorRefCreatorFactory>() {
                @Mock
                public ActorRef createActor(ActorContext context, final Class<? extends AbstractActor> actorClass, Object... args) {
                    return testKit.testActor();
                }
            };
        }};
        new TestKit(system) {{
            RouterState routerInitialState = new RouterState(0);
            ActorRefCreatorFactory factory = new ActorRefCreatorFactory();
            ActorRef router = system.actorOf(Router.props(routerInitialState, factory), "router");
            router.tell(new CommunicateWithActorMessage(4), router);
            testKit.expectMsg(new ChildActor1Message(11111));
        }};
    }

    @Test
    public void testLogEvent() throws InterruptedException {
        Config akkaConfig = ConfigFactory.parseString("akka.loggers = [akka.testkit.TestEventListener]");
        final ActorSystem system = ActorSystem.create("localactorsystem", akkaConfig);
        final TestKit testKit = new TestKit(system);
        new TestKit(system) {{
            RouterState routerInitialState = new RouterState(0);
            // use TestKit as it is multi threaded test.

            ActorRefCreatorFactory factory = new ActorRefCreatorFactory();
            ActorRef router = system.actorOf(Router.props(routerInitialState, factory), "router");
            router.tell(new CommunicateWithActorMessage(4), router);
            boolean receivedLogMsg = EventFilter.info("sending message to childActor1", null, null,
                    null, 1).intercept(()-> { return true; }, system);
            assertTrue(receivedLogMsg);
        }};
    }
}
