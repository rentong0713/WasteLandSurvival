package game.grounds;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.positions.GameMap;
import game.enums.*;
import game.interfaces.Unlockable;
import game.actions.UnlockAction;

/**
 * Abstract base class for all Door types in the game.
 * <p>
 * Doors are initially locked and restrict actor movement until unlocked
 * via an {@link UnlockAction}. Once unlocked, actors are allowed to enter.
 */
public abstract class Door extends Ground implements Unlockable {

    /**
     * Indicates whether the door has been unlocked.
     */
    protected boolean isUnlocked = false;
    private final ClearanceLevel requiredClearance;

    /**
     * Constructs a Door with a display character, name, and required clearance level.
     *
     * @param displayChar the character used to represent the door on the map
     * @param name the name of the door
     * @param requiredClearance the clearance level required to unlock the door
     */
    public Door(char displayChar, String name, ClearanceLevel requiredClearance) {
        super(displayChar, name);
        this.requiredClearance = requiredClearance;
    }

    /**
     * Determines whether an actor can enter the door tile.
     * <p>
     * Only unlocked doors allow entry.
     *
     * @param actor the actor attempting to enter
     * @return true if the door is unlocked, false otherwise
     */
    @Override
    public boolean canActorEnter(Actor actor) {
        return isUnlocked;
    }


    /**
     * Provides allowable actions for an actor at this door location.
     * <p>
     * If the door is locked, an {@link UnlockAction} is added to the list
     * of possible actions.
     *
     * @param actor the actor performing actions
     * @param location the location of the door
     * @param direction the direction of the door relative to the actor
     * @return a list of allowable actions
     */
    @Override
    public ActionList allowableActions(Actor actor, Location location, String direction) {
        ActionList actions = super.allowableActions(actor, location, direction);

        // Now it dynamically checks for whatever ability THIS specific door requires
        if (!isUnlocked && !direction.isEmpty()) {
            actions.add(new UnlockAction(this, direction));
        }
        return actions;
    }
}