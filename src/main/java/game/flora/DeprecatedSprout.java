package game.flora;

import game.behaviours.GrowBehaviour;
import game.behaviours.SpawnOnProximityBehaviour;
import game.systems.UndeadSpawner;

/**
 * The initial growth stage of the fleshy tree lifecycle.
 *
 * A Deprecated Sprout can spawn undead creatures when actors are nearby
 * and gradually grows into a DeprecatedMatureTree after reaching
 * the required age threshold.
 *
 * @author Ren Tong Low
 * @version 1.0
 */
public class DeprecatedSprout extends Flora {

    /**
     * Constructs a Deprecated Sprout with behaviours for spawning undead
     * creatures and growing into a Deprecated Mature Tree.
     */
    public DeprecatedSprout() {
        super('y', "Deprecated Sprout");

        // Spawns Undead
        this.behaviours.put(1, new SpawnOnProximityBehaviour(new UndeadSpawner()));
        // Grows directly to Mature Tree
        this.behaviours.put(2, new GrowBehaviour(20, 25, new DeprecatedMatureTree()));
    }
}