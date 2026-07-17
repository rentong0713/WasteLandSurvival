package game.grounds;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.enums.Ability;
import game.interfaces.Spawner;
import game.utils.SpawnUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A hole on the ground that spits out enemies every few turns.
 * <p>
 * Every turn, it asks its Spawner for a new creature and places it on its own tile.
 * Spawns go through SpawnUtils so each creature's own spawn reaction
 * (Slime drops items from workers, Undead gets HP boost, Parasite deals damage)
 * still happens.
 * <p>
 * After a successful spawn there's a 1% chance the hole grows — it turns one
 * nearby tile into another Hole with the same spawner and the same interval, so
 * a fast hole spawns more fast holes.
 * <p>
 * The spawn interval is stored per hole, so different maps can have different
 * speeds without needing a separate class for each.
 *
 * @author Low Ren Tong
 * @version 3.1
 */
public class Hole extends Ground {

    /** Default turns between spawns if no interval is given. */
    private static final int DEFAULT_SPAWN_INTERVAL = 20;

    /** Chance (0.0 – 1.0) that the hole grows after a successful spawn. */
    private static final double GROWTH_CHANCE = 0.01;

    /** Counts turns since the hole was placed. */
    private int tickCounter = 0;

    /** The Spawner used to make new creatures. */
    private final Spawner spawner;

    /** Turns between spawn attempts for this specific hole. */
    private final int spawnInterval;

    /** Used for random rolls (growth chance, picking a tile). */
    private final Random random = new Random();

    /**
     * Build a Hole with the default spawn interval of {@value #DEFAULT_SPAWN_INTERVAL} turns.
     *
     * @param spawner the strategy that produces creatures each spawn cycle.
     *                Use a MultiSpawner if you want the hole to spawn more than one type.
     */
    public Hole(Spawner spawner) {
        this(spawner, DEFAULT_SPAWN_INTERVAL);
    }

    /**
     * Build a Hole with a custom spawn interval.
     *
     * @param spawner       the strategy that produces creatures each spawn cycle.
     * @param spawnInterval how many turns between spawn attempts. Must be at
     *                      least 1. Smaller = faster spawning.
     * @throws IllegalArgumentException if spawnInterval is less than 1.
     */
    public Hole(Spawner spawner, int spawnInterval) {
        super('O', "Hole");
        if (spawnInterval < 1) {
            throw new IllegalArgumentException("spawnInterval must be at least 1, got " + spawnInterval);
        }
        this.spawner = spawner;
        this.spawnInterval = spawnInterval;
    }

    /**
     * Run one turn of the hole. Every spawnInterval-th turn it tries to spawn a
     * creature; if that works, it might also grow.
     *
     * @param location the hole's tile.
     */
    @Override
    public void tick(Location location) {
        tickCounter++;

        if (tickCounter % spawnInterval != 0) {
            return;
        }
        if (location.containsAnActor()) {
            return;
        }

        boolean spawned = spawner.spawn(location);
         if (!spawned) {
            return;
        }

        new Display().println("Something crawls out of the Hole at ("
                + location.x() + ", " + location.y() + ")!");

        if (random.nextDouble() < GROWTH_CHANCE) {
            growIntoAdjacent(location);
        }
    }

    /**
     * Pick a random flammable tile next to this hole and turn it into another
     * Hole with the same spawner and interval.
     *
     * @param location this hole's tile.
     */
    private void growIntoAdjacent(Location location) {
        List<Location> candidates = new ArrayList<>();
        for (Exit exit : location.getExits()) {
            Location adjacent = exit.getDestination();
            Ground ground = adjacent.getGround();
            if (ground != null && ground.hasAbility(Ability.IS_FLAMMABLE)) {
                candidates.add(adjacent);
            }
        }
        if (candidates.isEmpty()) {
            return;
        }
        Location chosen = candidates.get(random.nextInt(candidates.size()));
        chosen.setGround(new Hole(this.spawner, this.spawnInterval));
        new Display().println("The hole at (" + location.x() + ", " + location.y()
                + ") widens — a new Hole opens at (" + chosen.x() + ", " + chosen.y() + ")!");
    }
}