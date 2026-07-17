package game.systems;

import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import game.actors.ScrapSnatcher;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the SnatcherSpawner class.
 *
 * This test suite verifies the spawning logic for ScrapSnatcher entities.
 * It ensures that snatchers are only spawned on valid, unoccupied locations
 * and that spawning is safely aborted when the tile is already occupied.
 *
 * The tests cover:
 * - Failure when the target location already contains an actor
 * - Successful spawning when the target location is empty
 */
class SnatcherSpawnerTest {

    /**
     * Tests that spawning fails when the target location is already occupied.
     *
     * This ensures that:
     * - No new actor is spawned on an occupied tile
     * - The spawn operation returns false
     * - No attempt is made to add an actor to the location
     */
    @Test
    void testSpawnFailsWhenLocationIsOccupied() throws Exception {
        SnatcherSpawner spawner = new SnatcherSpawner();
        Location mockLocation = mock(Location.class);

        when(mockLocation.containsAnActor()).thenReturn(true);

        boolean result = spawner.spawn(mockLocation);

        assertFalse(result, "Spawner must return false and abort if an actor is already on the tile.");
        verify(mockLocation, never()).addActor(any());
    }

    /**
     * Tests that spawning succeeds when the target location is empty.
     *
     * This ensures that:
     * - The spawner correctly detects empty tiles
     * - A ScrapSnatcher is added to the location
     * - The spawn operation returns true upon success
     */
    @Test
    void testSpawnSucceedsWhenLocationIsEmpty() throws Exception {
        SnatcherSpawner spawner = new SnatcherSpawner();
        Location mockLocation = mock(Location.class);
        Exit mockExit = mock(Exit.class);
        Location mockAdjacentLocation = mock(Location.class);

        when(mockLocation.containsAnActor()).thenReturn(false);

        when(mockLocation.getExits()).thenReturn(List.of(mockExit));
        when(mockExit.getDestination()).thenReturn(mockAdjacentLocation);

        boolean result = spawner.spawn(mockLocation);

        assertTrue(result, "Spawner must return true when successfully spawning on an empty tile.");

        verify(mockLocation, times(1)).addActor(any(ScrapSnatcher.class));
    }
}