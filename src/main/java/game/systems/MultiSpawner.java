package game.systems;

import edu.monash.fit2099.engine.positions.Location;
import game.interfaces.Spawner;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * A composite Spawner that delegates to one of several sub-spawners chosen
 * uniformly at random per call.
 * <p>
 * Lets a single Hole or Vent spawn a varied roster (e.g. Slimes + Undead, or
 * Parasites + Slimes) without coupling the ground class to specific actor types.
 * Adding new roster combinations later means composing existing Spawners,
 * not modifying the consumers - the composite scales open/closed.
 *
 * @author Chai
 * @version 1.0
 */
public class MultiSpawner implements Spawner {

    /** The sub-spawners to choose between. */
    private final List<Spawner> spawners;

    /** Source of randomness for sub-spawner selection. */
    private final Random random = new Random();

    /**
     * Construct from a varargs list of sub-spawners.
     *
     * @param spawners the spawners to choose between; must be non-null and non-empty.
     * @throws IllegalArgumentException if no sub-spawners are provided.
     */
    public MultiSpawner(Spawner... spawners) {
        if (spawners == null || spawners.length == 0) {
            throw new IllegalArgumentException("MultiSpawner needs at least one sub-spawner.");
        }
        this.spawners = Arrays.asList(spawners);
    }

    /**
     * Choose one of the sub-spawners uniformly at random and delegate to it.
     *
     * @return the actor produced by the chosen sub-spawner.
     */
    @Override
    public boolean spawn(Location location) {
        // Pick a random spawner and tell IT to do the spawning!
        return spawners.get(random.nextInt(spawners.size())).spawn(location);
    }
}