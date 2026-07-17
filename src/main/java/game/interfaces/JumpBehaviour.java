package game.interfaces;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Location;

/**
 * An interface that represents the ability or strategy to perform a jumping action.
 * <p>
 * This interface is designed to decouple the specific mechanics of jumping from
 * the concrete Actor or action classes. By implementing this interface,
 * different entities or actions can define their own unique jumping behaviors,
 * @author Rachel Chiew
 * @version 1.0
 */
public interface JumpBehaviour {
    void performJump(Actor actor, Location destination);
}
