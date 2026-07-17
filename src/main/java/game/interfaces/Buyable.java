package game.interfaces;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;

/**
 * Interface for items or entities that can be purchased by an Actor.
 * Any class implementing this must define the price, the transaction logic,
 * and how it appears in a purchase menu.
 *
 * @author Tan Jia Hern
 */
public interface Buyable {
    /**
     * @return the price to purchase the object.
     */
    int getBuyPrice();

    /**
     * Defines the logic for purchasing the object.
     *
     * @param actor the actor performing the purchase.
     * @param map the map the actor is currently on.
     * @return a description of the result of the purchase.
     */
    String buy(Actor actor, GameMap map);

    /**
     * @return a description of the item suitable for a menu.
     */
    String getBuyDescription();
}