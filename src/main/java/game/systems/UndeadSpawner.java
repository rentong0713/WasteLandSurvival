package game.systems;

import edu.monash.fit2099.engine.positions.Location;
import game.actors.Undead;
import game.interfaces.Spawner;

/**
 * A concrete spawner responsible for creating and placing Undead entities in the world.
 *
 * This spawner ensures that an Undead is only spawned if the target location is unoccupied.
 * It also handles safely inserting the actor into the location and triggering any
 * spawn-time initialization logic defined by the entity.
 *
 * @author Low Ren Tong
 * @version 1.0
 */
public class UndeadSpawner implements Spawner {
    /**
     * Attempts to spawn an Undead at the specified location.
     *
     * @param location the location where the Undead should be spawned
     * @return true if the Undead was successfully spawned, false otherwise
     */
    @Override
    public boolean spawn(Location location) {
        if (location.containsAnActor()) {
            return false;
        }

        Undead undead = new Undead();
        try {
            location.addActor(undead);
        } catch (Exception e) {
            return false;
        }

        undead.onSpawn(location);
        return true;
    }
}