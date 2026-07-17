package game.systems;

import edu.monash.fit2099.engine.positions.Location;
import game.actors.Slime;
import game.interfaces.Spawner;

/**
 * A concrete spawner responsible for creating and placing Slime entities in the world.
 *
 * This spawner ensures that a Slime is only created if the target location is unoccupied.
 * It also handles safely adding the actor to the location and triggering any spawn-time
 * initialization logic.
 *
 *
 * @author Low Ren Tong
 * @version 1.0
 */
public class SlimeSpawner implements Spawner {

    /**
     * Attempts to spawn a Slime at the given location.
     *
     * @param location the location where the Slime should be spawned
     * @return true if the Slime was successfully spawned, false otherwise
     */
    @Override
    public boolean spawn(Location location) {
        if (location.containsAnActor()) {
            return false;
        }

        Slime slime = new Slime();
        try {
            location.addActor(slime);
        } catch (Exception e) {
            return false;
        }

        slime.onSpawn(location);
        return true;
    }
}