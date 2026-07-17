package game.systems;

import edu.monash.fit2099.engine.positions.Location;
import game.actors.ScrapSnatcher;
import game.interfaces.Spawner;

/**
 * A concrete spawner responsible for creating and placing ScrapSnatcher entities in the world.
 *
 * This spawner ensures that a Scrap Snatcher is only created if the target location is unoccupied.
 * It is typically used by flora behaviours or environmental systems that dynamically introduce
 * Scrap Snatchers into the game world.
 *
 * @author Ren Tong Low
 * @version 1.0
 */
public class SnatcherSpawner implements Spawner {

    /**
     * Attempts to spawn a Scrap Snatcher at the specified location.
     *
     * @param location the location where the Scrap Snatcher should be spawned
     * @return true if the Scrap Snatcher was successfully spawned, false otherwise
     */
    @Override
    public boolean spawn(Location location) {
        if (location.containsAnActor()) return false;

        ScrapSnatcher snatcher = new ScrapSnatcher();
        try {
            location.addActor(snatcher);
        } catch (Exception e) {
            return false;
        }

        snatcher.onSpawn(location);
        return true;
    }
}