package game.interfaces;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;

/**
 * An interface for items that can be deposited into the Supercomputer
 * to contribute to the company's quota.
 *
 * <p>
 *     Implementing classes must define the credit value provided upon deposit,
 *     the description for UI display, and any specific post-deposit side effects
 * </p>
 *
 * @author Rachel Chiew
 * @version 1.0
 */
public interface Depositable {
    int getDepositValue();

    /**
     * Executes the specific deposit logic for the item.
     *
     * <p>
     *     This method handles both the credit addition to the terminal and
     *     any unique interaction effects, such as healing the actor or
     *     triggering a teleportation event.
     * </p>
     *
     * @param actor The actor performing the deposit action.
     * @param map The game map where the deposit is occurring.
     * @return A string detailing the result of the deposit.
     */
    String deposit(Actor actor, GameMap map);

    /**
     * Provides a display description for the deposit menu.
     *
     * @return A string representing the name or type of the depositable item.
     */
    String getDepositDescription();
}
