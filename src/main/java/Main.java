import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akkaimpl.actors.routers.Router;
import akkaimpl.state.RouterState;
import akkaimpl.messages.*;

public class Main {
    ActorSystem actorSystem;
    ActorRef router;

    public void execute() {
        setUpActorSystem();
        startCommunication();
    }

    public static void main(String... args) {
        System.out.println("Main program called");
        Main main = new Main();
        main.execute();
    }

    private void startCommunication() {

        this.router.tell(new ChangeStateMessage(1), this.router);
        System.out.println("=========== first message sent");
        this.router.tell(new CommunicateWithActorMessage(2), this.router);
        System.out.println("=========== second message sent");
    }

    public void setUpActorSystem() {
        this.actorSystem = ActorSystem.create("actortestsample");
        RouterState routerInitialState = new RouterState(0);
        this.router = actorSystem.actorOf(Router.props(routerInitialState), "router");
    }
}
