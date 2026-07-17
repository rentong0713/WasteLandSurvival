package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import edu.monash.fit2099.engine.statistics.StatisticOperations;
import game.interfaces.Sellable;
import game.enums.*;

/**
 * A lightweight electronic storage device that can be sold to a Supercomputer.
 * Selling this item carries a risk of a credit-draining glitch.
 *
 * @author Tan Jia Hern
 */
public class FloppyDisk extends Item implements Sellable {

    /**
     * Constructor.
     */
    public FloppyDisk() {
        super("Floppy Disk", '⊟');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(1));
        this.makePortable();
        this.enableAbility(Ability.SELLABLE);
    }

    /**
     * @return the sale price of the Floppy Disk.
     */
    @Override
    public int getSellPrice() { return 1; }

    /**
     * @return a description of the item for the sale menu.
     */
    @Override
    public String getSellDescription() { return "Floppy Disk"; }

    /**
     * Processes the sale of the Floppy Disk.
     * Has a 50% chance to trigger a glitch that drains 50 credits from the actor.
     *
     * @param actor The actor selling the item.
     * @param map   The map the actor is currently on.
     * @return A string describing the outcome of the sale.
     */
    @Override
    public String sell(Actor actor, GameMap map) {
        actor.modifyStatistic(WorkerStatistics.CREDITS, StatisticOperations.INCREASE, getSellPrice());
        actor.getInventory().remove(this);

        String result = "Sold Floppy Disk for 1 credit.";

        if (Math.random() <= 0.5) {
            actor.modifyStatistic(WorkerStatistics.CREDITS, StatisticOperations.DECREASE, 50);
            result += " A glitch caused the Supercomputer to drain 50 credits!";
        }
        return result;
    }
}