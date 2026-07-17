package game.interfaces;

import edu.monash.fit2099.engine.positions.Location;
import game.actions.TrapExplosionAction;

/**
 * An interface applied to any game world structure or entity (such as ground terrain tiles
 * or actor assets) that can interact with and respond to tactical explosion blast waves.
 * This enables polymorphic payload processing during an area-of-effect detonation event.
 *
 * @author Tan Jia Hern
 * @version 1.0
 */
public interface TrapTriggerable {
    /**
     * Executes custom reaction parameters when intercepted by a trap's explosion blast wave.
     * Allows targets to dynamically alter their state, take structural damage, or append
     * specific status capability flags based on the incoming trap archetype configuration.
     *
     * @param action   the explosion action execution instance delivering the payload matrix
     * @param location the precise map coordinate frame where the targeted component resides
     */
    void reactToTrap(TrapExplosionAction action, Location location);
}