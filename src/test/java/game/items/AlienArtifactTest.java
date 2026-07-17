package game.items;

import edu.monash.fit2099.engine.positions.GameMap;
import game.TestUtils.DummyWorker;
import game.status.PoisonStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * AI Usage Acknowledgment:
 * I utilized Gemini to assist in structuring this test suite.
 * The tool helped generate the JUnit 5 boilerplate and the Mockito verification logic
 * for the GameMap movement. I performed 2 iterations to refine the poison status assertion
 * and verified that the moveActor method is correctly triggered in the deposit flow.
 * I manually modified the code to integrate my custom DummyWorker and ensure assertion
 * messages accurately reflected the game's mechanics.
 *
 * Unit tests for the AlienArtifact class, verifying the dual-economy
 * interaction mechanics (selling vs. depositing).
 * @author Rachel Chiew
 * @version 1.0
 */
class AlienArtifactTest {
    private AlienArtifact artifact;
    private DummyWorker dummyWorker;
    private GameMap mockMap;

    @BeforeEach
    void setUp() {
        artifact = new AlienArtifact();
        dummyWorker = new DummyWorker();
        mockMap = mock(GameMap.class);
    }

    /**
     * Requirement Check: Verifies that selling the artifact triggers the
     * mandatory poisoning effect when the forcePoison flag is active.
     */
    @Test
    void testSell_ForcesPoison() {
        artifact.sell(dummyWorker, mockMap, true);
        assertTrue(dummyWorker.hasStatus(PoisonStatus.class), "Worker should be poisoned.");
    }

    /**
     * Requirement Check: Verifies that depositing the artifact successfully
     * triggers the teleportation mechanism via the GameMap moveActor call.
     */
    @Test
    void testDeposit_TeleportsWorker() {
        artifact.deposit(dummyWorker, mockMap, true);
        verify(mockMap).moveActor(eq(dummyWorker), any());
    }
}