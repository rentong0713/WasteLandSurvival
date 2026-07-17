package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.interfaces.Consumable;

/**
 * A generic action allowing an actor to consume any Consumable object.
 *
 * @author Low Ren Tong
 * @version 1.0
 */
public class ConsumeAction extends Action {
    private final Consumable consumable;

    /**
     * Constructor for ConsumeAction.
     *
     * @param consumable The consumable object that the actor will interact with.
     */
    public ConsumeAction(Consumable consumable) {
        this.consumable = consumable;
    }

    /**
     * Executes the consumption action.
     * It delegates the actual logic and effects of the consumption to the consumable object itself.
     *
     * @param actor The actor performing the consume action.
     * @param map   The map the actor is currently on.
     * @return A string describing the outcome of the consumption, suitable for the game's display.
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        return consumable.consume(actor, map);
    }

    /**
     * Returns a descriptive string to be displayed in the player's action menu.
     *
     * @param actor The actor performing the action.
     * @return A string formatted as "[Actor] consumes [Consumable Description]".
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " consumes " + consumable.getConsumeDescription();
    }
}