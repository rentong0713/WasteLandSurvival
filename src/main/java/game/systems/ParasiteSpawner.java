package game.systems;

import edu.monash.fit2099.engine.positions.Location;
import game.actors.Parasite;
import game.interfaces.Spawner;

/**
 * A concrete spawner dedicated to creating Parasite entities.
 * <p>
 * Used in composition with MultiSpawner to give grounds like Hole
 * and Vent the ability to produce Parasites without coupling them to the
 * Parasite class directly.
 *
 * @author Chai
 * @version 1.0
 */
public class ParasiteSpawner implements Spawner {

    /**
     * Attempts to spawn a Parasite at the specified location.
     *
     * @param location the location where the Parasite should be spawned
     * @return true if the Parasite was successfully spawned, false otherwise
     */
    @Override
    public boolean spawn(Location location) {
        if (location.containsAnActor()) {
            return false;
        }

        Parasite parasite = new Parasite();
        try {
            location.addActor(parasite);
        } catch (Exception e) {
            return false;
        }

        parasite.onSpawn(location);
        return true;
    }
}