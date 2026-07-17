package game.flora;

import game.behaviours.GrowBehaviour;
import game.behaviours.SpawnOnProximityBehaviour;
import game.systems.SnatcherSpawner;

/**
 * A mature stage of the fleshy tree lifecycle.
 *
 * The Deprecated Mature Tree can spawn Scrap Snatchers when actors are nearby
 * and continues its evolution into a FleshyMonolith after reaching
 * the required age threshold.
 *
 * @author Ren Tong Low
 * @version 1.0
 */
public class DeprecatedMatureTree extends Flora {

    /**
     * Constructs a Deprecated Mature Tree with behaviours for spawning
     * Scrap Snatchers and growing into a Fleshy Monolith.
     */
    public DeprecatedMatureTree() {
        super('Y', "Deprecated Mature Tree");

        // Spawns Scrap Snatcher
        this.behaviours.put(1, new SpawnOnProximityBehaviour(new SnatcherSpawner()));
        // Grows to Monolith
        this.behaviours.put(2, new GrowBehaviour(35, 50, new FleshyMonolith()));
    }
}