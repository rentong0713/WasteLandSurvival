package game.utils;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import java.util.Random;

/**
 * Utility class for teleportation-related helper functions.
 * <p>
 * Provides shared logic for safely selecting valid random locations on a map
 * for teleportation systems.
 *
 * @author Rachel Chiew
 * @version 1.0
 */
public class TeleportUtils {
    private static final Random random = new Random();

    /**
     * A universally shared method to find a random, safe location on the map.
     */
    public static Location getRandomLocation(GameMap map, Actor actor) {
        int x, y;
        int xBound = map.getXRange().max() + 1;
        int yBound = map.getYRange().max() + 1;
        Location targetLocation;

        do {
            x = random.nextInt(xBound);
            y = random.nextInt(yBound);
            targetLocation = map.at(x, y);
        } while (!targetLocation.canActorEnter(actor) || targetLocation.containsAnActor());

        return targetLocation;
    }
}