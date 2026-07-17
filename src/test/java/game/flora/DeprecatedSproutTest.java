package game.flora;

import edu.monash.fit2099.engine.behaviours.Behaviour;
import game.behaviours.GrowBehaviour;
import game.behaviours.SpawnOnProximityBehaviour;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the DeprecatedSprout class.
 *
 * This test suite verifies that the DeprecatedSprout is correctly initialized with:
 * - The correct display character ('y')
 * - A SpawnOnProximityBehaviour at priority 1 for spawning Undead
 * - A GrowBehaviour at priority 2 for progression into a Mature Tree
 *
 * These tests ensure that both the visual representation and behavioural logic
 * of the sprout are correctly configured upon instantiation.
 */
class DeprecatedSproutTest {

    /**
     * Verifies that the sprout is created with the correct
     * display character and behaviours.
     */
    @Test
    void testSproutInitializationAndBehaviours() {
        DeprecatedSprout sprout = new DeprecatedSprout();

        assertEquals('y', sprout.getDisplayChar(), "Sprout must be represented by 'y'");

        Behaviour spawnBehaviour = sprout.behaviours.get(1);
        Behaviour growBehaviour = sprout.behaviours.get(2);

        assertNotNull(spawnBehaviour, "Sprout must have a behaviour at priority 1.");

        assertEquals(SpawnOnProximityBehaviour.class, spawnBehaviour.getClass(),
                "Sprout must have a proximity spawner for the Undead.");

        assertNotNull(growBehaviour, "Sprout must have a behaviour at priority 2.");

        assertEquals(GrowBehaviour.class, growBehaviour.getClass(),
                "Sprout must have a growth behaviour to skip Sapling and become Mature.");
    }
}