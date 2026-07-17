package game.behaviours;

import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the GrowBehaviour class.
 *
 * This test suite verifies the growth logic of flora entities based on:
 * - Turn-based growth timing requirements
 * - Random chance-based growth success
 *
 * The tests ensure that growth only occurs after the required number of turns
 * have elapsed and that the probabilistic growth mechanism correctly prevents
 * growth when the success chance is 0%.
 *
 * @author Ren Tong Low
 * @version 1.0
 */
class GrowBehaviourTest {

    /**
     * Tests that growth only occurs after the required number of turns have passed.
     *
     * This test simulates multiple turns and verifies that:
     * - Growth does not occur before the required turn count
     * - Growth succeeds exactly on the expected turn
     * - The ground is replaced with the next growth stage upon success
     */
    @Test
    void testGrowthRequiresExactTurnCount() {
        Ground mockNextStage = mock(Ground.class);
        Location mockLocation = mock(Location.class);
        Ground mockSprout = mock(Ground.class);

        GrowBehaviour growBehaviour = new GrowBehaviour(20, 100, mockNextStage);

        for (int i = 1; i <= 19; i++) {
            Boolean result = growBehaviour.operate(mockSprout, mockLocation);
            assertFalse(result, "Should not grow before 20 turns have passed. Turn: " + i);
        }

        // Turn 20: It should finally trigger
        Boolean finalResult = growBehaviour.operate(mockSprout, mockLocation);
        assertTrue(finalResult, "Should successfully grow on the 20th turn.");

        // Verify it actually tried to place the new stage on the map
        verify(mockLocation, times(1)).setGround(mockNextStage);
    }

    /**
     * Tests that growth fails when the random chance is set to 0%.
     *
     * This ensures that even if the turn requirement is met,
     * the behaviour will not trigger growth if RNG disallows it.
     */
    @Test
    void testGrowthFailsIfRNGFails() {
        Ground mockNextStage = mock(Ground.class);
        Location mockLocation = mock(Location.class);
        Ground mockSprout = mock(Ground.class);

        GrowBehaviour growBehaviour = new GrowBehaviour(1, 0, mockNextStage);

        Boolean result = growBehaviour.operate(mockSprout, mockLocation);

        assertFalse(result, "Should not grow because the percentage chance was 0%.");
    }
}