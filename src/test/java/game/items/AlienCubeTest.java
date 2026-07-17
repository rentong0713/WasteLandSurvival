package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Location;
import game.TestUtils.DummyWorker;
import game.status.PoisonStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * AI Usage Acknowledgment:
 * I utilized Gemini to assist in structuring this unit test.
 * The tool helped draft the JUnit 5 setup and the logic for verifying the item removal
 * from the worker's inventory. I performed 2 iterations to ensure the inventory
 * possession check was logically sound within the test environment. I manually
 * modified the assertions to verify the destruction of the item and the application
 * of the PoisonStatus.
 *
 * Unit tests for the AlienCube class, focusing on inventory-based
 * cutting requirements and status effects.
 * @author Rachel Chiew
 * @version 1.0
 */
class AlienCubeTest {
    private AlienCube alienCube;
    private Actor dummyWorker;
    private Location mockLocation;

    @BeforeEach
    void setUp() {
        alienCube = new AlienCube();
        dummyWorker = new DummyWorker();
        mockLocation = mock(Location.class);
    }

    /**
     * Requirement Check: Verifies that the cutting action fails or is
     * unavailable when the cube is not in the actor's inventory.
     */
    @Test
    void testCut_NotInInventory_Fails() {
        // Attempt to cut without adding to inventory
        String result = alienCube.cut(dummyWorker, mockLocation);

        assertTrue(result.contains("not in their inventory"), "Action should fail if cube is not held.");
    }

    /**
     * Requirement Check: Verifies that cutting the cube while in the inventory
     * destroys the item and inflicts the Poison status on the actor.
     */
    @Test
    void testCut_InInventory_SuccessAndPoison() {
        // Add to inventory
        dummyWorker.getInventory().add(alienCube);

        // Execute
        String result = alienCube.cut(dummyWorker, mockLocation);

        // Verify state
        assertFalse(dummyWorker.getInventory().getItems().contains(alienCube), "Cube should be destroyed.");
        assertTrue(dummyWorker.hasStatus(PoisonStatus.class), "Worker should be poisoned.");
    }
}