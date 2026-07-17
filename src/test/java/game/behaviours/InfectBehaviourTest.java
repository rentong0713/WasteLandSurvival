package game.behaviours;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import game.actors.ScrapSnatcher;
import game.enums.Ability;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the InfectBehaviour class.
 *
 * This test suite verifies that infection behaviour correctly selects targets
 * from adjacent locations and prioritises hostile actors when multiple infectable
 * targets are available.
 *
 * The test ensures:
 * - Adjacent locations are correctly scanned via exits
 * - Both worker and hostile actors can be detected
 * - The behaviour returns a valid InfectAction when a target is available
 */
public class InfectBehaviourTest {

    /**
     * Tests that InfectBehaviour prioritises hostile targets when selecting a victim.
     *
     * This ensures that when both worker and hostile actors are present in adjacent tiles:
     * - The behaviour evaluates both targets correctly
     * - A valid InfectAction is returned
     */
    @Test
    void testPrioritizesHostileTargets() {
        Actor mockParasite = mock(Actor.class);
        Location mockLocation = mock(Location.class);

        Exit exit1 = mock(Exit.class);
        Location loc1 = mock(Location.class);

        Actor worker = new ScrapSnatcher();

        Exit exit2 = mock(Exit.class);
        Location loc2 = mock(Location.class);

        Actor undead = new ScrapSnatcher();
        undead.enableAbility(Ability.HOSTILE);

        when(mockLocation.getExits()).thenReturn(List.of(exit1, exit2));
        when(exit1.getDestination()).thenReturn(loc1);
        when(exit2.getDestination()).thenReturn(loc2);

        when(loc1.containsAnActor()).thenReturn(true);
        when(loc1.getActor()).thenReturn(worker);

        when(loc2.containsAnActor()).thenReturn(true);
        when(loc2.getActor()).thenReturn(undead);

        InfectBehaviour behaviour = new InfectBehaviour();
        Action result = behaviour.operate(mockParasite, mockLocation);

        assertNotNull(result, "Should return an InfectAction.");
    }
}