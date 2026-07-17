package game.interfaces;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;

/**
 * Interface for items or entities that can be sold by an Actor.
 * Useful for items that can be exchanged for credits at specific terminals or NPCs.
 *
 * @author Tan Jia Hern
 */
public interface Sellable {
    /**
     * @return the price for which the object can be sold.
     */
    int getSellPrice();

    /**
     * Defines the logic for selling the object.
     *
     * @param actor the actor performing the sale.
     * @param map the map the actor is currently on.
     * @return a description of the result of the sale.
     */
    String sell(Actor actor, GameMap map);

    /**
     * @return a description of the item suitable for a menu.
     */
    String getSellDescription();
}