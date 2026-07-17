package game.behaviours;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.actors.ScrapSnatcher;
import game.enums.Ability;
import game.interfaces.Spawner;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the SpawnOnProximityBehaviour class.
 *
 * This test suite verifies the proximity-based spawning logic used by flora entities.
 * It ensures that spawning only occurs when valid empty adjacent locations exist
 * and that spawners are not triggered when no valid space is available.
 *
 * The tests cover:
 * - Failing to spawn when no empty adjacent tiles are available
 * - Successfully spawning a creature when a valid empty tile exists
 */
class SpawnOnProximityBehaviourTest {

    /**
     * Tests that spawning fails when there are no empty adjacent tiles available.
     *
     * This ensures that:
     * - No spawn attempt is made when all adjacent locations are occupied
     * - The spawner is never invoked in invalid conditions
     * - The behaviour correctly returns false
     */
    @Test
    void testFailsToSpawnIfNoEmptyTiles() {
        Ground mockTree = mock(Ground.class);
        Location mockLocation = mock(Location.class);
        Spawner mockSpawner = mock(Spawner.class);

        Exit mockExit = mock(Exit.class);
        Location mockAdjacentLocation = mock(Location.class);

        Actor worker = new ScrapSnatcher();
        worker.enableAbility(Ability.WORKER);

        when(mockLocation.getExits()).thenReturn(List.of(mockExit));
        when(mockExit.getDestination()).thenReturn(mockAdjacentLocation);

        when(mockAdjacentLocation.containsAnActor()).thenReturn(true);
        when(mockAdjacentLocation.getActor()).thenReturn(worker);

        SpawnOnProximityBehaviour behaviour = new SpawnOnProximityBehaviour(mockSpawner);

        Boolean result = behaviour.operate(mockTree, mockLocation);

        assertFalse(result, "Behaviour should return false because there are no empty adjacent tiles to spawn on.");
        verify(mockSpawner, never()).spawn(any(Location.class)); // Ensure spawner was never called
    }

    /**
     * Tests that spawning succeeds when at least one valid empty adjacent tile exists.
     *
     * This ensures that:
     * - A valid empty location is correctly identified
     * - The spawner is invoked exactly once
     * - The behaviour returns true upon successful spawning
     */
    @Test
    void testSuccessfullySpawnsCreature() {
        Ground mockTree = mock(Ground.class);
        Location mockLocation = mock(Location.class);
        Spawner mockSpawner = mock(Spawner.class);

        Exit exitWithWorker = mock(Exit.class);
        Location locationWithWorker = mock(Location.class);

        Exit emptyExit = mock(Exit.class);
        Location emptyLocation = mock(Location.class);

        Actor worker = new ScrapSnatcher();
        worker.enableAbility(Ability.WORKER);

        when(mockLocation.getExits()).thenReturn(List.of(exitWithWorker, emptyExit));

        // Exit 1: Contains the worker to trigger the spawn condition
        when(exitWithWorker.getDestination()).thenReturn(locationWithWorker);
        when(locationWithWorker.containsAnActor()).thenReturn(true);
        when(locationWithWorker.getActor()).thenReturn(worker);

        // Exit 2: Is completely empty, giving the spawner a valid place to drop the creature
        when(emptyExit.getDestination()).thenReturn(emptyLocation);
        when(emptyLocation.containsAnActor()).thenReturn(false);

        // Tell our mock spawner to return true (simulating a successful spawn)
        when(mockSpawner.spawn(any(Location.class))).thenReturn(true);

        SpawnOnProximityBehaviour behaviour = new SpawnOnProximityBehaviour(mockSpawner);

        Boolean result = behaviour.operate(mockTree, mockLocation);

        assertTrue(result, "Behaviour must return true when a creature is successfully spawned.");

        verify(mockSpawner, times(1)).spawn(emptyLocation);
    }
}