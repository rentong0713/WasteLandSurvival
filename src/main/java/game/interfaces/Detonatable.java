package game.interfaces;

import edu.monash.fit2099.engine.positions.Location;

/**
 * An interface applied to objects that function as proximity-based hazards or explosives.
 * Components implementing this interface are responsible for monitoring their spatial
 * coordinates to detect when an entity intersects their tile layer and trigger a detonation.
 *
 * @author Tan Jia Hern
 * @version 1.0
 */
public interface Detonatable {
    /**
     * Evaluates the specified map tile coordinate to determine if the criteria for
     * triggering a detonation sequence have been met (e.g., presence of an actor).
     *
     * @param location the precise map grid cell coordinates where the proximity sensor is active
     */
    void checkDetonation(Location location);
}