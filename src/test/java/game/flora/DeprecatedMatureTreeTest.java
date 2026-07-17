package game.flora;

import edu.monash.fit2099.engine.behaviours.Behaviour;
import game.behaviours.GrowBehaviour;
import game.behaviours.SpawnOnProximityBehaviour;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the DeprecatedMatureTree class.
 *
 * This test suite verifies that the DeprecatedMatureTree is correctly initialized with:
 * - The correct display character ('Y')
 * - A SpawnOnProximityBehaviour at priority 1 for spawning Scrap Snatchers
 * - A GrowBehaviour at priority 2 for progression into a Monolith
 *
 * These tests ensure that both the visual representation and behavioural logic
 * of the mature tree are correctly configured upon instantiation.
 */
class DeprecatedMatureTreeTest {

    /**
     * Verifies that the mature tree is created with the correct
     * display character and behaviours.
     */
    @Test
    void testMatureTreeInitializationAndBehaviours() {
        DeprecatedMatureTree matureTree = new DeprecatedMatureTree();

        assertEquals('Y', matureTree.getDisplayChar(), "Mature Tree must be represented by 'Y'");

        Behaviour spawnBehaviour = matureTree.behaviours.get(1);
        Behaviour growBehaviour = matureTree.behaviours.get(2);

        assertNotNull(spawnBehaviour, "Mature tree must have a behaviour at priority 1.");

        assertEquals(SpawnOnProximityBehaviour.class, spawnBehaviour.getClass(),
                "Mature tree must have a proximity spawner for the Scrap Snatcher.");

        assertNotNull(growBehaviour, "Mature tree must have a behaviour at priority 2.");

        assertEquals(GrowBehaviour.class, growBehaviour.getClass(),
                "Mature tree must have a growth behaviour to become a Monolith.");
    }
}