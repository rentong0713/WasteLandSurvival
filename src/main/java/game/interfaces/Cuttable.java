package game.interfaces;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;

/**
 * An interface for objects in the game world that can be interacted with using
 * an item that possesses the CUT capability.
 *
 * <p>
 *     Classes that implement this interface are expected to define custom logic
 *     for the destruction or transformation of the object when the cut method is called.
 * </p>
 *
 * @author Rachel Chiew
 * @version 1.0
 */
public interface Cuttable {

    /**
     * Executes the cutting logic on the target object.
     *
     * <p>
     *     Implementations should handle all side effects, such as dropping items,
     *     changing the ground type, or triggering hazards (e.g., explosions, spawns).
     * </p>
     * @param actor The actor performing the cutting action.
     * @param targetLocation The location where the target exists on the map.
     * @return A string indicating the outcome of the cutting action.
     */
    String cut(Actor actor, Location targetLocation);

    /**
     * Provides a display description for the cutting interaction.
     * @return A string representing the name or type of the cuttable object.
     */
    String getCutDescription();
}
