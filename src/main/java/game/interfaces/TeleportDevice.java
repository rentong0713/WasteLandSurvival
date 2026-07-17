package game.interfaces;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Location;

/**
 * Represents a device capable of teleporting actors between locations.
 * <p>
 * Implementations define how destinations are registered and how teleportation
 * is executed (e.g., instant movement, behaviour-based teleport, or random warping).
 *
 * @author Rachel Chiew
 * @version 1.0
 */
public interface TeleportDevice {
    /**
     * Registers a possible teleport destination for this device.
     *
     * @param location the location that can be used as a teleport destination
     */
    void addDestination(Location location);

    /**
     * Triggers teleportation of an actor to a specified destination.
     * <p>
     * The exact behaviour of teleportation is defined by the implementing class.
     *
     * @param actor the actor being teleported
     * @param destination the target location to teleport the actor to
     */
    void triggerTeleport(Actor actor, Location destination);
}
