package game.grounds;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.enums.Ability;
import game.enums.ClearanceLevel;

/**
 * A high-security Titanium Door that requires the highest clearance level to unlock.
 * <p>
 * Unlocking this door rewards the actor with healing due to the environmental
 * effect released upon activation.
 */
public class TitaniumDoor extends Door {
    /**
     * Constructs a Titanium Door requiring LEVEL_3 clearance.
     */
    public TitaniumDoor() {
        super('M', "TitaniumDoor", ClearanceLevel.LEVEL_3);
    }

    /**
     * Unlocks the Titanium Door and heals the actor.
     *
     * @param actor the actor unlocking the door
     * @param map the game map containing the door
     * @return a message describing the unlocking event
     */
    @Override
    public String unlock(Actor actor, GameMap map) {
        this.isUnlocked = true;
        actor.heal(5);
        return actor + " unlocks the Aluminium Door. The soothing mist heals 5 HP.";
    }

    /**
     * Returns the required clearance level for this door.
     *
     * @return the ordinal value of {@link ClearanceLevel#LEVEL_3}
     */
    @Override
    public int getRequiredClearanceLevel(){
        return ClearanceLevel.LEVEL_3.ordinal();
    }

    /**
     * Returns a description used in unlock actions.
     *
     * @return the door's unlock description
     */
    @Override
    public String getUnlockDescription() {
        return "Titanium Door";
    }

    /**
     * Determines whether an actor can enter this door.
     *
     * @param actor the actor attempting to enter
     * @return true if the door is unlocked, false otherwise
     */
    @Override
    public boolean canActorEnter(Actor actor) {
        return this.isUnlocked;
    }
}

