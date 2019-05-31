package akkaimpl.factories;

import akka.actor.*;
import scala.collection.Iterable;
import scala.concurrent.ExecutionContextExecutor;

public class ActorRefCreatorFactory implements ActorCreatorFactory {
    public ActorRef createActor(ActorContext context, final Class<? extends AbstractActor> actorClass, Object... args) {
        return context.actorOf(Props.create(actorClass, args));
    }
}
