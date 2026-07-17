package game.grounds;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.enums.Ability;
import game.interfaces.Ignitable;
import game.enums.ClearanceLevel;

/**
 * A reinforced Iron Door that triggers environmental ignition when unlocked.
 * <p>
 * This door requires {@link ClearanceLevel#LEVEL_2} access and, upon unlocking,
 * causes surrounding tiles to ignite or extend existing fire effects.
 */
public class IronDoor extends Door {

    /**
     * Constructs an Iron Door with LEVEL_2 clearance requirement.
     */
    public IronDoor() {
        super('N', "Iron Door", ClearanceLevel.LEVEL_2);
    }

    /**
     * Unlocks the Iron Door and triggers fire effects in adjacent tiles.
     * <p>
     * If an adjacent ground implements {@link Ignitable}, it is ignited.
     * Otherwise, flammable ground is replaced with a {@link Fire} instance.
     *
     * @param actor the actor unlocking the door
     * @param map the game map containing the door
     * @return a message describing the unlocking event
     */
    @Override
    public String unlock(Actor actor, GameMap map) {
        this.isUnlocked = true;
        Location actorLocation = map.locationOf(actor);

        // Spawn fire logic
        for (Exit exit : map.locationOf(actor).getExits()) {
            Location surround = exit.getDestination();
            Ground ori_ground = surround.getGround();
            Ignitable fireGround = surround.getGroundAs(Ignitable.class);

            if (fireGround != null) {
                fireGround.ignite(2);
            } else if (surround.getGround().hasAbility(Ability.IS_FLAMMABLE)) {
                surround.setGround(new Fire(ori_ground, 2));
            }
        }
        return actor + " unlocks the Iron Door. Sparks fly, igniting the surroundings!";
    }

    /**
     * Returns the required clearance level for this door.
     *
     * @return the ordinal value of {@link ClearanceLevel#LEVEL_2}
     */
    @Override
    public int getRequiredClearanceLevel(){
        return ClearanceLevel.LEVEL_2.ordinal();
    }

    /**
     * Returns a short description used in unlock actions.
     *
     * @return the door description string
     */
    @Override
    public String getUnlockDescription() {
        return "Iron Door";
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