package game.interfaces;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;

/**
 * Interface for items or objects that can be consumed by an Actor.
 * Any object implementing this interface must define the effects of consumption
 * and provide a description for the user interface.
 *
 *
 * @author Tan Jia Hern
 */
public interface Consumable {
    /**
     * Executes the consumption logic of the object.
     * This method handles the internal state changes for the actor (e.g., healing,
     * applying status effects) and the map.
     *
     * @param actor The actor performing the consumption.
     * @param map   The map the actor is currently on.
     * @return A string describing the result of the consumption.
     */
    String consume(Actor actor, GameMap map);

    /**
     * Provides the name of the consumable for the menu description.
     *
     * @return A string representing the name or type of the consumable.
     */
    String getConsumeDescription();
}