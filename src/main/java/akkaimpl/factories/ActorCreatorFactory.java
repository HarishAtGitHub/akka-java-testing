package akkaimpl.factories;

import akka.actor.AbstractActor;
import akka.actor.ActorContext;
import akka.actor.ActorRef;

public interface ActorCreatorFactory {
    public ActorRef createActor(ActorContext context, final Class<? extends AbstractActor> actorClass, Object... args);
}
