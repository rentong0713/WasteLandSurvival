package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import game.enums.Ability;
import game.enums.ClearanceLevel;
import game.interfaces.Unlockable;
import game.items.AccessCard;

/**
 * A generic action allowing an actor to unlock any Unlockable object.
 * This action handles interactions with locked entities on the game map.
 *
 *
 * @author Low Ren Tong
 * @version 1.0
 */
public class UnlockAction extends Action {
    private final Unlockable unlockable;
    private final String direction;

    /**
     * Constructor for UnlockAction.
     *
     * @param unlockable The object to be unlocked.
     * @param direction  The direction where the unlockable object is located.
     */
    public UnlockAction(Unlockable unlockable, String direction) {
        this.unlockable = unlockable;
        this.direction = direction;
    }

    /**
     * Executes the unlocking action.
     * It delegates the specific unlocking logic to the
     * unlockable object itself and appends the direction to the output message.
     *
     * @param actor The actor performing the unlock action.
     * @param map   The map the actor is currently on.
     * @return A string describing the outcome of the unlock attempt, suitable for display.
     */
    @Override
    public String execute(Actor actor, GameMap map) {

        ClearanceLevel cardLevel = ClearanceLevel.getHighestClearance(actor);
        int requiredOrdinal = unlockable.getRequiredClearanceLevel();
        ClearanceLevel requiredLevel = ClearanceLevel.values()[requiredOrdinal];

        if (!cardLevel.hasClearance(requiredLevel)){
            return String.format("%s lacks Level %d clearance for the %s!",
                    actor, requiredOrdinal, unlockable.getUnlockDescription());
        }

        return unlockable.unlock(actor, map);
    }

    /**
     * Returns a descriptive string to be displayed in the player's action menu.
     *
     * @param actor The actor performing the action.
     * @return A string formatted as "[Actor] unlocks the [Unlockable Description] to the [Direction]".
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " unlocks the " + unlockable.getUnlockDescription() + " to the " + direction;
    }
}