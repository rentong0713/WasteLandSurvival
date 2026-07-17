package game.grounds;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.ActorStatistics;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.CutAction;
import game.enums.Ability;
import game.inventory.BasicInventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * AI Usage Acknowledgment:
 * I utilized Gemini to assist in generating test structures and mocking
 * strategies. The tool was used to draft the JUnit 5 and Mockito boilerplate. I performed approximately
 * 3 iterations to refine assertion logic. I manually modified the outputs to ensure strings matched
 * my production code messages, corrected logic regarding FIT2099 engine capabilities, and
 * verified that test isolation was maintained through the setUp() method.
 *
 * Unit tests for the AluminiumDoor class, focusing on cutting
 * interactions and hazard (explosion) behavior.
 *
 * @author Rachel Chiew
 * @version 1.0
 */
class AluminiumDoorTest {

    private AluminiumDoor door;
    private Location mockLocation;
    private GameMap mockMap;
    private Actor dummyWorker;

    /**
     * Helper class to provide a stable, minimal Actor implementation
     * for test isolation.
     */
    private static class DummyWorker extends Actor {
        public DummyWorker() {
            super("Test Worker", 'W', 100, new BasicInventory());
            this.enableAbility(Ability.WORKER);
            this.enableAbility(Ability.CUT);
        }
        @Override
        public edu.monash.fit2099.engine.actions.Action playTurn(ActionList actions, edu.monash.fit2099.engine.actions.Action lastAction, GameMap map, edu.monash.fit2099.engine.displays.Display display) {
            return new edu.monash.fit2099.engine.actions.DoNothingAction();
        }
    }

    @BeforeEach
    void setUp() {
        door = new AluminiumDoor();
        mockMap = mock(GameMap.class);
        mockLocation = mock(Location.class);
        dummyWorker = new DummyWorker();
    }

    /**
     * Requirement Check: Verifies that a successful cut without an explosion results
     * in the correct message and no damage to the worker.
     */
    @Test
    void testCut_SuccessNoExplosion() {
        // Pass false to ensure we test the safe path
        String result = door.cut(dummyWorker, mockLocation, false);

        assertTrue(result.contains("cut down"), "Should confirm successful cut.");
        assertFalse(result.contains("detonates"), "Should not explode on safe roll.");
        assertEquals(100, dummyWorker.getStatistic(ActorStatistics.HEALTH), "Worker should take no damage.");
    }

    /**
     * Requirement Check: Verifies that the explosion hazard correctly applies
     * 100 damage to the worker when the edge case flag is triggered.
     */
    @Test
    void testCut_ForcedExplosion() {
        // Pass true to force the explosion path (HD Edge Case)
        door.cut(dummyWorker, mockLocation, true);

        // Worker starts at 100, receives 100 damage = 0 HP
        assertEquals(0, dummyWorker.getStatistic(ActorStatistics.HEALTH),
                "Worker should be incapacitated by the 100 damage explosion.");
    }

    /**
     * Requirement Check: Verifies that attempting to cut without the required ability
     * results in a failure message, ensuring the interaction is gated correctly.
     */
    @Test
    void testCutAction_WithoutCutter_Fails() {
        CutAction cutAction = new CutAction(door, mockLocation, "North");

        // Execute WITHOUT adding PlasmaCutter to inventory
        String result = cutAction.execute(dummyWorker, mockMap);

        assertTrue(result.contains("requires a Plasma Cutter"),
                "Should inform player that the cutter is missing.");
    }
}