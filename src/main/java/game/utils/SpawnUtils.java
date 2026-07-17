package game.utils;

import edu.monash.fit2099.engine.GameEngineException;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import game.interfaces.Spawnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Helper class for spawning creatures and finding nearby empty tiles.
 * <p>
 * Every spawn in the game should go through spawn(Location, Actor) so
 * each creature's spawn reaction (Undead's HP boost, Slime's drop-items, Parasite's
 * damage) always fires no matter who placed it.
 * <p>
 * {@link #adjacentEmptyLocationsFor(Location, Actor)} and pickRandom(List)
 * are here because Hole, Vent, SpawnOnProximityBehaviour, InfectedWorkerStatus,
 * and InfectedCookiesStatus all need the same "find an empty neighbour tile and
 * pick one at random" step. Keeping it here avoids copy-pasted code.
 *
 * @author Chai
 * @version 2.0
 */
public final class SpawnUtils {

    /**
     * Shared random source for the helper methods.
     */
    private static final Random RANDOM = new Random();

    /**
     * Stop anyone from making an instance of this utility class.
     */
    private SpawnUtils() {
        // utility class — no instances
    }

    /**
     * Get all the tiles next to a given tile that are empty AND that the creature
     * is allowed to stand on. Used for "where can I spawn this thing?" checks.
     *
     * @param origin   the centre tile.
     * @param creature the creature we want to place (used to check canActorEnter).
     * @return list of valid neighbour tiles (can be empty).
     */
    public static List<Location> adjacentEmptyLocationsFor(Location origin, Actor creature) {
        List<Location> candidates = new ArrayList<>();
        for (Exit exit : origin.getExits()) {
            Location dest = exit.getDestination();
            if (!dest.containsAnActor() && dest.getGround().canActorEnter(creature)) {
                candidates.add(dest);
            }
        }
        return candidates;
    }

    /**
     * Pick one random location from a list. Returns {@code null} for an empty or
     * null list.
     *
     * @param locations the list to pick from.
     * @return a random location, or {@code null} if the list is empty/null.
     */
    public static Location pickRandom(List<Location> locations) {
        if (locations == null || locations.isEmpty()) {
            return null;
        }
        return locations.get(RANDOM.nextInt(locations.size()));
    }
}