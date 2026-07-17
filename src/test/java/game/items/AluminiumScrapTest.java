package game.items;

import edu.monash.fit2099.engine.positions.GameMap;
import game.TestUtils.DummyWorker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * AI Usage Acknowledgment:
 * I utilized Gemini to assist in structuring this test suite.
 * The tool helped draft the JUnit 5 setup and the logic for verifying the health deduction
 * when the deposit injury hazard is triggered. I performed 2 iterations to ensure
 * the damage logic was correctly asserted. I manually modified the code to integrate
 * my custom DummyWorker and verify that the ActorStatistics correctly reflect the
 * health loss.
 *
 * Unit tests for the AluminiumScrap class, verifying the deposit
 * mechanics and risk of injury.
 * @author Rachel Chiew
 * @version 1.0
 */
class AluminiumScrapTest {
    private AluminiumScrap scrap;
    private DummyWorker dummyWorker;
    private GameMap mockMap;

    @BeforeEach
    void setUp() {
        scrap = new AluminiumScrap();
        dummyWorker = new DummyWorker();
    }

    /**
     * Requirement Check: Verifies that depositing Aluminium Scrap while the
     * forceInjury flag is active correctly deducts 5 HP from the worker.
     */
    @Test
    void testDeposit_Injury_TakesDamage() {
        // Force the injury to happen
        scrap.deposit(dummyWorker, mockMap, true);

        assertEquals(95, dummyWorker.getStatistic(edu.monash.fit2099.engine.actors.ActorStatistics.HEALTH),
                "Worker should take exactly 5 damage.");
    }

    /**
     * Requirement Check: Verifies that depositing Aluminium Scrap when the
     * injury risk does not trigger results in no damage to the worker.
     */
    @Test
    void testDeposit_Safe_NoDamage() {
        // Force NO injury
        scrap.deposit(dummyWorker, mockMap, false);

        assertEquals(100, dummyWorker.getStatistic(edu.monash.fit2099.engine.actors.ActorStatistics.HEALTH),
                "Worker should take no damage.");
    }
}