package game.status;

import edu.monash.fit2099.engine.GameEntity;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.positions.Location;

/**
 * A status effect that paralyzes an actor over a set number of turns.
 * While active, this status restricts the entity's ability to act.
 *
 * @author Tan Jia Hern
 * @version 1.0
 */
public class StunStatus implements Status {
    private int duration;

    /**
     * Constructor for StunStatus.
     *
     * @param duration The number of turns the stun effect should last.
     */
    public StunStatus(int duration) {
        this.duration = duration;
    }

    /**
     * Called once per turn to decrement the remaining duration.
     * If the duration reaches zero, it handles the removal of the stun tag.
     *
     * @param currEntity The entity currently experiencing the stun status.
     * @param location   The current location of the entity on the map.
     */
    @Override
    public void tickStatus(GameEntity currEntity, Location location) {
        duration--;

        if (duration <= 0) {
            // Remove the stun tag when the countdown naturally expires
            currEntity.disableAbility(game.enums.Status.STUN);
        }
    }

    /**
     * Checks whether the stun effect is still active based on its remaining duration.
     *
     * @return true if the duration is greater than zero, false otherwise.
     */
    @Override
    public boolean isStatusActive() {
        return duration > 0;
    }
}