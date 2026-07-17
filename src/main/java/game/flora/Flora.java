package game.flora;

import edu.monash.fit2099.engine.behaviours.Behaviour;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;

import java.util.Map;
import java.util.TreeMap;

/**
 * Base class for all Alien Flora.
 *
 * This class provides a shared framework for flora entities that execute
 * environmental behaviours in a controlled, single-threaded manner.
 * Behaviours are stored in priority order and executed sequentially
 * until one successfully performs an action.
 *
 * @author  Low Ren Tong
 * @version 1.0
 */
public abstract class Flora extends Ground {

    /**
     * A priority-ordered collection of behaviours that define the flora's
     * actions each tick. Lower keys are executed first.
     */
    protected Map<Integer, Behaviour<Ground, Boolean>> behaviours = new TreeMap<>();

    /**
     * Constructs a Flora object with a display character and name.
     *
     * @param displayChar the character used to represent the flora on the map
     * @param name the name of the flora entity
     */
    public Flora(char displayChar, String name) {
        super(displayChar, name);
    }

    /**
     * Executes flora behaviours in priority order each game tick.
     *
     * The first behaviour that returns true will consume the turn,
     * preventing any further behaviours from executing. This enforces a
     * single-action-per-turn rule for all flora entities.
     *
     * @param location the location of the flora on the game map
     */
    @Override
    public void tick(Location location) {
        // Iterate through behaviours in priority order
        for (Behaviour<Ground, Boolean> behaviour : behaviours.values()) {
            // If a behaviour successfully executes (returns true), stop ticking.
            // This satisfies the one action per turn requirement.
            if (behaviour.operate(this, location)) {
                return;
            }
        }
    }
}