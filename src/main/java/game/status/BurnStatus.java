package game.status;

import edu.monash.fit2099.engine.GameEntity;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.positions.Location;

/**
 * A status effect that deals burn damage to an actor over a set number of turns.
 * This status can be attached to a GameEntity and will
 * automatically deduct hit points each turn until its duration expires.
 *
 * @author Low Ren Tong
 * @version 1.0
 */
public class BurnStatus implements Status {
    private int duration;
    private final int damagePerTurn;

    /**
     * Constructor for BurnStatus.
     *
     * @param duration      The number of turns the burn effect should last.
     * @param damagePerTurn The amount of damage dealt per turn.
     */
    public BurnStatus(int duration, int damagePerTurn) {
        this.duration = duration;
        this.damagePerTurn = damagePerTurn;
    }

    /**
     * Called once per turn to apply the burn damage to the affected entity and
     * decrement the remaining duration. If the duration reaches zero, it checks
     * and handles the removal of the burn tag from the entity.
     *
     * @param currEntity The entity currently experiencing the burn status.
     * @param location   The current location of the entity on the map.
     */
    @Override
    public void tickStatus(GameEntity currEntity, Location location) {
        // Actor implements game entity

        Actor actor = currEntity.asCapability(Actor.class).orElse(null);
        // If it successfully returned an Actor, we can safely hurt it!
        if (actor != null) {
            actor.hurt(damagePerTurn);
        }

        duration--;
        if (duration <= 0) {
            // Only remove the burn enum tag if this is the last BurnStatus attached to the entity
            // If the size is 1, it means this object is the only one left
            if (currEntity.statusesOf(BurnStatus.class).size() <= 1) {
                currEntity.disableAbility(game.enums.Status.BURN);
            }
        }
    }

    /**
     * Checks whether the burn effect is still active based on its remaining duration.
     *
     * @return true if the duration is greater than zero, false otherwise.
     */
    @Override
    public boolean isStatusActive() {
        return duration > 0;
    }
}