package game.status;

import edu.monash.fit2099.engine.GameEntity;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.positions.Location;

/**
 * A status effect that deals poison damage to an actor over a set number of turns.
 * This status can be applied to a GameEntity to gradually
 * drain their hit points over time until the effect's duration naturally expires.
 *
 *
 * @author Low Ren Tong
 * @version 1.0
 */
public class PoisonStatus implements Status {
    private int duration;
    private final int damagePerTurn;

    /**
     * Constructor for PoisonStatus.
     *
     * @param duration      The initial number of turns the poison effect should last.
     * @param damagePerTurn The amount of damage dealt per turn.
     */
    public PoisonStatus(int duration, int damagePerTurn) {
        this.duration = duration;
        this.damagePerTurn = damagePerTurn;
    }

    /**
     * Extends the duration of the current poison effect.
     * Useful for when an entity is poisoned multiple times and the effect stacks.
     *
     * @param extraTurns The number of additional turns to add to the remaining duration.
     */
    public void increaseDuration(int extraTurns) {
        this.duration += extraTurns;
    }

    /**
     * Called once per turn to apply the poison damage to the affected entity and
     * decrement the remaining duration. If the duration drops to zero or below,
     * it removes the poison status from the entity entirely.
     *
     * @param currEntity The entity currently experiencing the poison status.
     * @param location   The current location of the entity on the map.
     */
    @Override
    public void tickStatus(GameEntity currEntity, Location location) {
        // Apply damage if the entity is an actor
        Actor actor = currEntity.asCapability(Actor.class).orElse(null);
        if (actor != null) {
            actor.hurt(damagePerTurn);
        }
        System.out.println(actor + " loses " + damagePerTurn + " HP due to the agonizing effects of poison! (" + (duration - 1) + " rounds remaining)");
        // Decrease remaining turns
        duration--;

        // Remove poison tag if expired
        if (duration <= 0) {
            currEntity.disableAbility(game.enums.Status.POISON);
        }
    }

    /**
     * Checks whether the poison effect is still active based on its remaining duration.
     *
     * @return true if the duration is greater than zero, false otherwise.
     */
    @Override
    public boolean isStatusActive() {
        return duration > 0;
    }
}