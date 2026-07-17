package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.interfaces.Buyable;

/**
 * An Action that allows an Actor to purchase a Buyable item.
 *
 * @author Tan Jia Hern
 */
public class BuyAction extends Action {
    /**
     * The item to be purchased.
     */
    private final Buyable item;

    /**
     * Constructor.
     *
     * @param item the Buyable item to be purchased.
     */
    public BuyAction(Buyable item) {
        this.item = item;
    }

    /**
     * Executes the purchase of the item.
     *
     * @param actor The actor performing the action.
     * @param map   The map the actor is on.
     * @return A description of the purchase outcome.
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        return item.buy(actor, map);
    }

    /**
     * Returns a descriptive string for the menu.
     *
     * @param actor The actor performing the action.
     * @return A string describing the purchase option.
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " buys " + item.getBuyDescription() + " for " + item.getBuyPrice() + " credits";
    }
}