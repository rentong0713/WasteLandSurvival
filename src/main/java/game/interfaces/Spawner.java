package game.interfaces;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.*;

/**
 * Interface for generating specific actors.
 * This allows grounds or other mechanics to spawn entities without
 * being tightly coupled to specific Actor classes.
 * @author Low Ren Tong
 * @version 1.0
 */
public interface Spawner {
    /**
     * Spawns the actor at the location and triggers its spawn effect.
     * @return true if spawn was successful, false if tile was blocked.
     */
    boolean spawn(Location location);
}