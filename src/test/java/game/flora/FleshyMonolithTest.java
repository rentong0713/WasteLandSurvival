package game.flora;

import edu.monash.fit2099.engine.behaviours.Behaviour;
import game.behaviours.WarpOnProximityBehaviour;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the FleshyMonolith class.
 *
 * This test suite verifies that the FleshyMonolith is correctly initialized with:
 * - The correct display character ('H')
 * - A WarpOnProximityBehaviour at priority 1 to teleport workers
 * - No additional behaviour at priority 2, confirming it does not grow further
 *
 * These tests ensure that the monolith is a terminal flora stage with only
 * proximity-based warping behaviour and no further evolution logic.
 */
class FleshyMonolithTest {

    /**
     * Verifies that the monolith is created with the correct
     * display character and behaviours.
     */
    @Test
    void testMonolithInitializationAndBehaviours() {
        FleshyMonolith monolith = new FleshyMonolith();

        assertEquals('H', monolith.getDisplayChar(), "Monolith must be represented by 'H'");

        Behaviour warpBehaviour = monolith.behaviours.get(1);
        Behaviour priority2Behaviour = monolith.behaviours.get(2);

        assertNotNull(warpBehaviour, "Monolith must have a behaviour at priority 1.");

        assertEquals(WarpOnProximityBehaviour.class, warpBehaviour.getClass(),
                "Monolith must possess the WarpOnProximityBehaviour to teleport workers.");

        assertNull(priority2Behaviour,
                "Monolith cannot grow any further and should not have a second behaviour.");
    }
}