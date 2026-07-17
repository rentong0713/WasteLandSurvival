package game.items;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.statistics.StatisticOperations;
import game.TestUtils.DummyWorker;
import game.actions.BuyAction;
import game.enums.Ability;
import game.enums.ItemStatistics;
import game.enums.WorkerStatistics;
import game.enums.Status; // Assuming you have a Status.BURNED or similar
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * AI Usage Acknowledgment:
 * I utilized Gemini to assist in structuring this unit test.
 * The tool helped draft the JUnit 5 and Mockito boilerplate. I performed 3 iterations
 * to refine the boundary condition testing (exact credits vs. insufficient credits)
 * and verified that damage and burn status application logic was correctly asserted.
 * I manually modified the code to integrate my custom DummyWorker and verify that the
 * ActorStatistics and inventory states were updated exactly as required.
 *
 * Unit tests for the PlasmaCutter class, verifying purchasing logic,
 * stat penalties, and item properties.
 * * @author Rachel Chiew
 * @version 1.0
 */
class PlasmaCutterTest {

    private PlasmaCutter plasmaCutter;
    private GameMap mockMap;
    private Actor dummyWorker;

    @BeforeEach
    void setUp() {
        mockMap = mock(GameMap.class);
        dummyWorker = new DummyWorker();
        plasmaCutter = new PlasmaCutter();
    }

    /**
     * Requirement Check: Verifies that the Plasma Cutter maintains the correct weight
     * and is strictly prohibited from being sold back to the Supercomputer.
     */
    @Test
    void testProperties_CorrectWeightAndNotSellable() {
        // 1. Verify weight is exactly 7 using your ItemStatistics enum
        assertTrue(plasmaCutter.hasStatistic(ItemStatistics.WEIGHT), "Plasma Cutter must have a WEIGHT statistic.");
        assertEquals(7, plasmaCutter.getStatistic(ItemStatistics.WEIGHT), "Plasma Cutter weight must be exactly 7.");

        // 2. Verify it CANNOT be sold back to the company
        assertFalse(plasmaCutter.hasAbility(Ability.SELLABLE), "Plasma Cutter must NOT be sellable.");
    }

    /**
     * Requirement Check: Verifies that purchasing with exact credits succeeds,
     * deducting the correct amount and applying the mandatory health and burn penalties.
     */
    @Test
    void testPurchase_ExactCredits_SucceedsAndAppliesPenalties() {
        // Set worker credits to EXACTLY 50 (Boundary condition)
        dummyWorker.modifyStatistic(WorkerStatistics.CREDITS, StatisticOperations.UPDATE, 50);

        // Execute the BuyAction (Adapt this if your BuyAction constructor requires different parameters)
        BuyAction buyAction = new BuyAction(plasmaCutter);
        String result = buyAction.execute(dummyWorker, mockMap);

        // 1. Verify credits were deducted (50 - 50 = 0)
        assertEquals(0, dummyWorker.getStatistic(WorkerStatistics.CREDITS), "Worker should have 0 credits after buying the cutter.");

        // 2. Verify the worker took exactly 5 damage (starts at 100 -> 95)
        assertEquals(95, dummyWorker.getStatistic(edu.monash.fit2099.engine.actors.ActorStatistics.HEALTH),
                "Worker must take exactly 5 damage from the searing temperatures.");

        // 3. Verify the burn effect was applied.
        // NOTE: Change Ability.BURNED to whatever enum/status you used to track fire/burn effects!
        boolean isBurned = dummyWorker.hasAbility(Ability.BURNING);
        assertTrue(isBurned, "Worker must receive a Burn status/ability upon purchase.");

        // 4. Verify item is now in inventory
        assertTrue(dummyWorker.getInventory().getItems().contains(plasmaCutter), "Plasma Cutter must be in the inventory.");
    }

    /**
     * Requirement Check: Verifies that purchasing with insufficient credits fails,
     * ensuring no credit deduction, health damage, or item addition occurs.
     */
    @Test
    void testPurchase_InsufficientCredits_FailsAndNoPenaltiesApplied() {
        // Set worker credits to 49 (1 short of the requirement)
        dummyWorker.modifyStatistic(WorkerStatistics.CREDITS, StatisticOperations.UPDATE, 49);

        BuyAction buyAction = new BuyAction(plasmaCutter);
        String result = buyAction.execute(dummyWorker, mockMap);

        // 1. Verify credits were NOT deducted
        assertEquals(49, dummyWorker.getStatistic(WorkerStatistics.CREDITS), "Credits should not be deducted for a failed purchase.");

        // 2. Verify NO damage was taken (still at 100 HP)
        assertEquals(100, dummyWorker.getStatistic(edu.monash.fit2099.engine.actors.ActorStatistics.HEALTH),
                "Worker should NOT take damage if the purchase fails.");

        // 3. Verify NO burn effect was applied
        assertFalse(dummyWorker.hasAbility(Ability.BURNING), "Worker should NOT be burned if the purchase fails.");

        // 4. Verify the item is NOT in the inventory
        assertFalse(dummyWorker.getInventory().getItems().contains(plasmaCutter), "Plasma Cutter should not be given if purchase fails.");
    }
}