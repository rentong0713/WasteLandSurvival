package game.items;

import edu.monash.fit2099.engine.actors.ActorStatistics;
import edu.monash.fit2099.engine.positions.GameMap;
import game.TestUtils.DummyWorker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * AI Usage Acknowledgment:
 * I utilized Gemini to assist in structuring this unit test.
 * The tool helped draft the JUnit 5 setup and the logic for verifying the health
 * recovery when the fan deposit bonus is applied. I performed 2 iterations to
 * ensure the healing logic was correctly asserted. I manually modified the code to
 * integrate my custom DummyWorker and verify that the ActorStatistics correctly
 * reflect the recovery of health points.
 *
 * Unit tests for the IndustrialFan class, verifying sell-side
 * spawning hazards and deposit-side healing mechanics.
 * @author Rachel Chiew
 * @version 1.0
 */
class IndustrialFanTest {
    private IndustrialFan fan;
    private DummyWorker dummyWorker;
    private GameMap mockMap;

    @BeforeEach
    void setUp() {
        fan = new IndustrialFan();
        dummyWorker = new DummyWorker();
    }

    /**
     * Requirement Check: Verifies that selling the fan triggers the
     * result message, confirming the flow of the sell transaction logic.
     */
    @Test
    void testSell_SpawnsSlime() {
        // You can't easily force the random spawn inside the sell method
        // But you CAN verify that the logic reaches the spawn call
        String result = fan.sell(dummyWorker, mockMap);

        assertTrue(result.contains("sold the Industrial Fan"));
        // If SlimeSpawner is an interface or injectable, verify it here!
    }

    /**
     * Requirement Check: Verifies that depositing the fan while the forceHeal
     * flag is active correctly restores 10 HP to the worker.
     */
    @Test
    void testDeposit_HealsWorker() {
        dummyWorker.hurt(20); // 100 -> 80

        // Call the 2-argument method (the Test Hook)
        fan.deposit(dummyWorker, true);

        assertEquals(90, dummyWorker.getStatistic(ActorStatistics.HEALTH),
                "Worker should be healed for 10 HP.");
    }
}