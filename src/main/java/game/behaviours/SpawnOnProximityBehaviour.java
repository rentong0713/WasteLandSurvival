package game.behaviours;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.behaviours.Behaviour;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.enums.Ability;
import game.interfaces.Spawner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Flora-side behaviour: when a worker is on an adjacent tile, produce one creature
 * (via the injected ActorFactory) and place it on a random empty adjacent
 * tile.
 *
 * Spawns are dispatched through SpawnUtils so the spawned creature's own
 * Spawnable.onSpawn reaction (e.g. Undead HP boost, Slime item drop,
 * Parasite worker damage) still fires when a tree triggers the spawn.
 *
 * @author Low Ren Tong
 * @version 2.0
 */
public class SpawnOnProximityBehaviour implements Behaviour<Ground, Boolean> {

    /** The spawner used to produce and place the creature. */
    private final Spawner spawner;

    /** Source of randomness for spawn-tile selection. */
    private final Random random = new Random();

    /**
     * Construct a proximity-spawn behaviour with the given creature factory.
     *
     * @param spawner produces the creature each time the behaviour fires.
     */
    public SpawnOnProximityBehaviour(Spawner spawner) {
        this.spawner = spawner;
    }

    /**
     * Execute one turn of the behaviour. Returns true (consuming the
     * flora's single-threaded turn slot) only if a spawn was performed.
     *
     * @param entity   the flora running the behaviour.
     * @param location the flora's location.
     * @return true if a spawn occurred; false otherwise.
     */
    @Override
    public Boolean operate(Ground entity, Location location) {
        boolean workerNearby = false;
        for (Exit exit : location.getExits()) {
            Location dest = exit.getDestination();
            if (dest.containsAnActor() && dest.getActor().hasAbility(Ability.WORKER)) {
                workerNearby = true;
                break;
            }
        }

        if (!workerNearby) {
            return false;
        }

        List<Location> validSpawnLocations = new ArrayList<>();
        for (Exit exit : location.getExits()) {
            Location dest = exit.getDestination();
            if (!dest.containsAnActor()) {
                validSpawnLocations.add(dest);
            }
        }

        if (validSpawnLocations.isEmpty()) {
            return false;
        }

        Location randomSpawnLocation = validSpawnLocations.get(random.nextInt(validSpawnLocations.size()));

        return spawner.spawn(randomSpawnLocation);
    }
}