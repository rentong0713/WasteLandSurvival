package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.interfaces.Sellable;

/**
 * An Action that allows an Actor to sell a Sellable item.
 *
 * @author Tan Jia Hern
 */
public class SellAction extends Action {
    /**
     * The item to be sold.
     */
    private final Sellable item;

    /**
     * Constructor.
     *
     * @param item the Sellable item to be sold.
     */
    public SellAction(Sellable item) {
        this.item = item;
    }

    /**
     * Executes the sale of the item.
     *
     * @param actor The actor performing the action.
     * @param map   The map the actor is on.
     * @return A description of the sale outcome.
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        return item.sell(actor, map);
    }

    /**
     * Returns a descriptive string for the menu.
     *
     * @param actor The actor performing the action.
     * @return A string describing the sale option.
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " sells " + item.getSellDescription() + " for " + item.getSellPrice() + " credits";
    }
}