package game.behaviours;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Location;
import game.enums.Ability;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the HoardBehaviour class.
 *
 * This test suite verifies the item collection logic used by snatcher-type actors.
 * It ensures that the behaviour correctly identifies and interacts with items
 * based on whether they are marked as DEPOSITABLE.
 *
 * The tests cover:
 * - Picking up items that have the DEPOSITABLE ability
 * - Ignoring items that do not have the DEPOSITABLE ability
 *
 * @author Ren Tong Low
 * @version 1.0
 */
class HoardBehaviourTest {

    /**
     * Tests that HoardBehaviour correctly selects and returns a pickup action
     * when a depositable item is present in the current location.
     *
     * This ensures that snatchers can successfully identify and collect valid items.
     */
    @Test
    void testPicksUpDepositableItem() {
        Actor mockSnatcher = mock(Actor.class);
        Location mockLocation = mock(Location.class);
        Item mockDepositableItem = mock(Item.class);

        when(mockLocation.getItems()).thenReturn(List.of(mockDepositableItem));
        when(mockDepositableItem.hasAbility(Ability.DEPOSITABLE)).thenReturn(true);

        HoardBehaviour behaviour = new HoardBehaviour();

        Action result = behaviour.operate(mockSnatcher, mockLocation);

        assertNotNull(result, "Behaviour should return a PickUpAction for a depositable item.");
    }

    /**
     * Tests that HoardBehaviour ignores items that are not marked as DEPOSITABLE.
     *
     * This ensures that only valid items are considered for collection,
     * preventing unwanted or invalid item hoarding behaviour.
     */
    @Test
    void testIgnoresNonDepositableItem() {
        Actor mockSnatcher = mock(Actor.class);
        Location mockLocation = mock(Location.class);
        Item mockStandardItem = mock(Item.class);

        when(mockLocation.getItems()).thenReturn(List.of(mockStandardItem));
        when(mockStandardItem.hasAbility(Ability.DEPOSITABLE)).thenReturn(false);

        HoardBehaviour behaviour = new HoardBehaviour();

        Action result = behaviour.operate(mockSnatcher, mockLocation);

        assertNull(result, "Behaviour should ignore items without the DEPOSITABLE ability.");
    }
}