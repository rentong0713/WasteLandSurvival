package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.grounds.Supercomputer;
import game.interfaces.Depositable;

/**
 * An action class that allows a worker to deposit an item into the Supercomputer
 * @author Rachel Chiew
 * @version 1.0
 */
public class DepositAction extends Action {
    private final Depositable item;
    private final Supercomputer terminal;

    /**
     * Constructor for DepositAction.
     * @param item The item to be deposited
     * @param terminal The Supercomputer instance where the item is deposited
     */
    public DepositAction(Depositable item, Supercomputer terminal){
        this.item = item;
        this.terminal = terminal;
    }

    /**
     * Executes the deposit action. Updates the company quota in the Supercomputer and
     * triggers the item's specific deposit effects
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return A string detailing the result of the deposit.
     */
    @Override
    public String execute(Actor actor, GameMap map){
        terminal.addCompanyCredits(item.getDepositValue());
        return item.deposit(actor, map);
    }

    /**
     * Provides a description of the deposit action for the game menu.
     * @param actor The actor performing the action.
     * @return A string formatted for the menu display.
     */
    @Override
    public String menuDescription(Actor actor){
        return actor + " deposits " + item.getDepositDescription() + " for "
                + item.getDepositValue() + " Company Credits";
    }
}
