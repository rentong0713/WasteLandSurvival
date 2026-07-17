package game.interfaces;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;

/**
 * Interface for objects that can be unlocked by an Actor.
 * Any object implementing this interface (such as doors) must define
 * its own unlocking logic and how it is described in the interaction menu.
 *
 *
 * @author Low Ren Tong
 * @version 1.0
 */
public interface Unlockable {
    /**
     * Executes the unlocking logic of the object.
     * This method is responsible for updating the state of the object to an
     * unlocked status and returning a message to be displayed.
     *
     * @param actor The actor attempting to perform the unlock.
     * @param map   The GameMap where the unlockable object is located.
     * @return A string describing the result of the unlocking action.
     */
    String unlock(Actor actor, GameMap map);

    /**
     * Provides the name of the unlockable object for the menu description.
     *
     * @return A string representing the name of the object.
     */
    String getUnlockDescription();

    int getRequiredClearanceLevel();
}