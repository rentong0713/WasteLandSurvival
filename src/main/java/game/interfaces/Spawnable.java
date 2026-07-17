package game.interfaces;

import edu.monash.fit2099.engine.positions.Location;

/**
 * Marks a creature that does something when it appears on the map.
 * <p>
 * Putting the reaction on the creature itself (instead of on each spawn site)
 * makes sure it happens no matter who spawned it — a Hole, a Vent, a Fleshy Tree,
 * an Alien Cube being sold, or a Parasite spawning from an infected worker.
 * <p>
 * This splits two ideas: {@link Spawner} decides WHICH creature to make, while
 * this interface handles WHAT happens when it shows up.
 *
 * @author Chai
 * @version 1.0
 */
public interface Spawnable {
    /**
     * Called once, right after this creature has been placed on the map.
     *
     * @param location the tile the creature just appeared on.
     */
    void onSpawn(Location location);
}