package game.grounds;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.enums.Ability;
import game.interfaces.Spawner;
import game.items.IndustrialFan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VentTest {
    private Vent vent;
    private Spawner mockSpawner;
    private Location mockLocation;
    private GameMap mockMap;
    private Actor mockWorker;

    @BeforeEach
    void setUp() {
        mockSpawner = mock(Spawner.class);
        vent = new Vent(mockSpawner);
        mockLocation = mock(Location.class);
        mockMap = mock(GameMap.class);
        mockWorker = mock(Actor.class);

        when(mockLocation.map()).thenReturn(mockMap);
    }

    @Test
    void testTick_WhenWorkerAdjacent_AttemptsSpawn() {
        // Setup: One exit that contains a worker
        Location adjacentLocation = mock(Location.class);
        Exit mockExit = new Exit("North", adjacentLocation, "N");
        List<Exit> exits = new ArrayList<>();
        exits.add(mockExit);

        when(mockLocation.getExits()).thenReturn(exits);
        when(adjacentLocation.containsAnActor()).thenReturn(true);
        when(adjacentLocation.getActor()).thenReturn(mockWorker);
        when(mockWorker.hasAbility(Ability.WORKER)).thenReturn(true);

        // When: Tick is called
        vent.tick(mockLocation);

        // Then: Spawner should have been triggered
        verify(mockSpawner).spawn(any(Location.class));
    }

    @Test
    void testCut_TransformsGroundAndSpawns() {
        // When: Cut is executed
        vent.cut(mockWorker, mockLocation);

        // Then: Ground becomes floor and item is added
        verify(mockLocation).setGround(any(Floor.class));
        verify(mockLocation).addItem(any(IndustrialFan.class));
    }
}