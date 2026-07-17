package game.grounds;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.positions.NumberRange;
import game.TestUtils.DummyWorker;
import game.actions.BuyAction;
import game.actions.DepositAction;
import game.actions.SellAction;
import game.enums.Ability;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * AI Usage Acknowledgment:
 * I utilized Gemini to assist in structuring the test environment for the
 * Supercomputer. The tool helped draft the Mockito configurations for the GameMap scan and the tick
 * cycle simulation. I performed 3 iterations to ensure the loop logic correctly advanced the quota
 * and triggered the firing mechanism. I manually modified the outputs to integrate my custom
 * DummyWorker classes and to correct logic specific to the FIT2099 engine's unconsciousness state.
 *
 * Unit tests for the Supercomputer class, covering trade generation and quota management.
 * @author Rachel Chiew
 * @version 1.0
 */
class SupercomputerTest {

    private Supercomputer supercomputer;
    private GameMap mockMap;
    private Location mockLocation;
    private Actor dummyWorker;

    @BeforeEach
    void setUp() {
        supercomputer = new Supercomputer();
        mockMap = mock(GameMap.class);
        mockLocation = mock(Location.class);

        // Completely REAL actor. No spy(), no mock().
        dummyWorker = new DummyWorker();
        dummyWorker.enableAbility(Ability.WORKER);

        NumberRange wideRange = new NumberRange(0, 5);
        when(mockMap.getXRange()).thenReturn(wideRange);
        when(mockMap.getYRange()).thenReturn(wideRange);

        // Guarantee that any tile checked by the loop returns our mocked location
        when(mockMap.at(anyInt(), anyInt())).thenReturn(mockLocation);

        when(mockLocation.map()).thenReturn(mockMap);
        when(mockLocation.containsAnActor()).thenReturn(true);
        when(mockLocation.getActor()).thenReturn(dummyWorker);
    }

    /**
     * Requirement Check: Verifies that the Supercomputer correctly identifies
     * sellable and depositable items in the worker's inventory.
     */
    @Test
    void testAllowableActions_GeneratesBuySellAndDepositActions() {
        Item realSellableItem = new game.items.CRTMonitor();
        Item realDepositableItem = new game.items.IndustrialFan();

        dummyWorker.getInventory().add(realSellableItem);
        dummyWorker.getInventory().add(realDepositableItem);

        ActionList actions = supercomputer.allowableActions(dummyWorker, mockLocation, "North");

        boolean hasBuy = false, hasSell = false, hasDeposit = false;
        for (edu.monash.fit2099.engine.actions.Action action : actions) {
            if (action.getClass() == BuyAction.class) hasBuy = true;
            if (action.getClass() == SellAction.class) hasSell = true;
            if (action.getClass() == DepositAction.class) hasDeposit = true;
        }

        assertTrue(hasBuy, "Requirement Check: Supercomputer must offer BuyActions.");
        assertTrue(hasSell, "Requirement Check: Supercomputer must offer a SellAction for SELLABLE items.");
        assertTrue(hasDeposit, "Requirement Check: Supercomputer must offer a DepositAction for DEPOSITABLE items.");
    }

    /**
     * Requirement Check: Verifies that the shop remains functional when the quota is met.
     */
    @Test
    void testTick_QuotaMetExactly_ResetsAndKeepsShopOpen() {
        supercomputer.addCompanyCredits(100);

        for (int i = 0; i < 200; i++) {
            supercomputer.tick(mockLocation);
        }

        ActionList actions = supercomputer.allowableActions(dummyWorker, mockLocation, "North");

        boolean hasBuyActions = false;
        for (edu.monash.fit2099.engine.actions.Action action : actions) {
            if (action.getClass() == BuyAction.class) hasBuyActions = true;
        }

        assertTrue(hasBuyActions, "Requirement Check: The shop must remain open if the exact quota is met.");
    }

    /**
     * Requirement Check: Verifies that failing the quota initiates the termination
     * protocol (firing adjacent workers) and disables shop interactions.
     */
    @Test
    void testTick_QuotaMissed_FiresAdjacentWorkersAndLocksShop() {
        supercomputer.addCompanyCredits(99);

        Actor targetWorker = new DummyWorker() {
            @Override
            public String unconscious(GameMap map) {
                this.hurt(9999); // Annihilate HP
                return "Fired by Supercomputer!";
            }
        };
        targetWorker.enableAbility(Ability.WORKER);

        Exit mockExit = mock(Exit.class);
        Location mockAdjacentLocation = mock(Location.class);

        when(mockLocation.getExits()).thenReturn(java.util.Collections.singletonList(mockExit));
        when(mockExit.getDestination()).thenReturn(mockAdjacentLocation);
        when(mockAdjacentLocation.containsAnActor()).thenReturn(true);
        when(mockAdjacentLocation.getActor()).thenReturn(targetWorker);

        // Execute 200 turns (Now the clock will actually tick!)
        for (int i = 0; i < 200; i++) {
            supercomputer.tick(mockLocation);
        }

        // 1. Verify the worker was fired!
        assertFalse(targetWorker.isConscious(), "Requirement Check: Supercomputer must fire workers if the quota is missed.");

        // 2. Verify the shop locked down
        ActionList actions = supercomputer.allowableActions(dummyWorker, mockLocation, "North");

        boolean hasBuyActions = false;
        for (edu.monash.fit2099.engine.actions.Action action : actions) {
            if (action.getClass() == BuyAction.class) hasBuyActions = true;
        }

        assertFalse(hasBuyActions, "Requirement Check: The Supercomputer terminal must shut down after failing the quota.");
    }
}