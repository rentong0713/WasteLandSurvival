package game.behaviours;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.actors.ScrapSnatcher;
import game.enums.Ability;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the WarpOnProximityBehaviour class.
 *
 * This test suite verifies the proximity-based warping logic used by monolith-type flora.
 * It ensures that only valid worker actors are selected and successfully warped,
 * while non-eligible or empty adjacent tiles are ignored.
 *
 * The tests cover:
 * - Ignoring empty adjacent tiles
 * - Ignoring non-worker actors
 * - Successfully warping valid worker actors to a new location
 */
public class WarpOnProximityBehaviourTest {

    /**
     * Tests that the behaviour ignores empty adjacent tiles and returns false.
     *
     * This ensures that no warping attempt is made when there are no actors nearby.
     */
    @Test
    void testOperateIgnoresEmptyTiles() {
        Ground mockMonolith = mock(Ground.class);
        Location mockLocation = mock(Location.class);
        GameMap mockMap = mock(GameMap.class);
        Exit mockExit = mock(Exit.class);
        Location mockAdjacentLocation = mock(Location.class);

        when(mockLocation.map()).thenReturn(mockMap);
        when(mockLocation.getExits()).thenReturn(List.of(mockExit));
        when(mockExit.getDestination()).thenReturn(mockAdjacentLocation);
        when(mockAdjacentLocation.containsAnActor()).thenReturn(false);

        WarpOnProximityBehaviour behaviour = new WarpOnProximityBehaviour();
        Boolean result = behaviour.operate(mockMonolith, mockLocation);

        assertFalse(result, "Behaviour should return false when no actors are adjacent.");
    }

    /**
     * Tests that the behaviour ignores adjacent actors that are not workers.
     *
     * This ensures that only actors with the WORKER ability are considered valid targets.
     */
    @Test
    void testOperateIgnoresNonWorkers() {
        Ground mockMonolith = mock(Ground.class);
        Location mockLocation = mock(Location.class);
        GameMap mockMap = mock(GameMap.class);
        Exit mockExit = mock(Exit.class);
        Location mockAdjacentLocation = mock(Location.class);

        Actor monster = new ScrapSnatcher();

        when(mockLocation.map()).thenReturn(mockMap);
        when(mockLocation.getExits()).thenReturn(List.of(mockExit));
        when(mockExit.getDestination()).thenReturn(mockAdjacentLocation);
        when(mockAdjacentLocation.containsAnActor()).thenReturn(true);
        when(mockAdjacentLocation.getActor()).thenReturn(monster);

        WarpOnProximityBehaviour behaviour = new WarpOnProximityBehaviour();
        Boolean result = behaviour.operate(mockMonolith, mockLocation);

        assertFalse(result, "Behaviour should return false when adjacent actor is not a worker.");
    }

    /**
     * Tests that the behaviour successfully warps a valid worker actor.
     *
     * This verifies that:
     * - A worker in an adjacent tile is detected
     * - A valid destination is chosen
     * - The actor is moved to a new location via the GameMap
     */
    @Test
    void testOperateSuccessfullyWarpsWorker() {
        Ground mockMonolith = mock(Ground.class);
        Location mockLocation = mock(Location.class);
        GameMap mockMap = mock(GameMap.class);
        Exit mockExit = mock(Exit.class);
        Location mockAdjacentLocation = mock(Location.class);

        Actor worker = new ScrapSnatcher();
        worker.enableAbility(Ability.WORKER);

        when(mockLocation.map()).thenReturn(mockMap);
        when(mockLocation.getExits()).thenReturn(List.of(mockExit));
        when(mockExit.getDestination()).thenReturn(mockAdjacentLocation);

        when(mockAdjacentLocation.containsAnActor()).thenReturn(true);
        when(mockAdjacentLocation.getActor()).thenReturn(worker);

        Location mockValidDestination = mock(Location.class);
        when(mockValidDestination.canActorEnter(worker)).thenReturn(true); // Tell TeleportUtils this tile is safe
        when(mockValidDestination.containsAnActor()).thenReturn(false);    // Tell TeleportUtils this tile is empty

        when(mockMap.getXRange()).thenReturn(new edu.monash.fit2099.engine.positions.NumberRange(0, 10));
        when(mockMap.getYRange()).thenReturn(new edu.monash.fit2099.engine.positions.NumberRange(0, 10));

        when(mockMap.at(anyInt(), anyInt())).thenReturn(mockValidDestination);

        WarpOnProximityBehaviour behaviour = new WarpOnProximityBehaviour();
        Boolean result = behaviour.operate(mockMonolith, mockLocation);

        assertTrue(result, "Behaviour must return true when an adjacent worker is successfully processed.");
        verify(mockMap, times(1)).moveActor(eq(worker), any(Location.class));
    }
}