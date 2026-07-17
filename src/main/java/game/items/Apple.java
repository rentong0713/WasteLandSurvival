package game.items;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.actions.ConsumeAction;
import game.enums.*;
import game.interfaces.Consumable;
import game.interfaces.Sellable;
import edu.monash.fit2099.engine.statistics.StatisticOperations;
import game.status.*;

/**
 * An apple the worker (or a Slime) can eat.
 * <p>
 * If the eater has the STERILISE ability, the apple heals 3 HP. Otherwise it
 * poisons them for 5 turns (1 damage/turn). Either way the apple is consumed.
 * <p>
 * Selling pays 1 credit, but if the seller isn't sterilised the handling itself
 * poisons them too (2 damage/turn for 2 turns).
 *
 * @author Tan Jia Hern
 * @version 1.0
 */
public class Apple extends Item implements Consumable, Sellable {

    /**
     * Make a new Apple. Light (weight 1), portable, sellable.
     */
    public Apple() {
        super("Apple", 'ó');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(1));
        this.makePortable();
        this.enableAbility(Ability.SELLABLE);
    }

    /**
     * Eat the apple. Heals 3 HP if the eater is sterilised, otherwise poisons
     * them for 5 turns. The apple is removed afterwards.
     *
     * @param actor the eater.
     * @param map   the eater's map.
     * @return what happened.
     */
    @Override
    public String consume(Actor actor, GameMap map) {
        // Remove the apple from inventory or ground, whichever applies.
        actor.getInventory().remove(this);
        map.locationOf(actor).removeItem(this);

        if (actor.hasAbility(Ability.STERILISE)) {
            actor.heal(3);
            return actor + " eats a sterilised Apple, healing 3 HP.";
        }
        else {
            actor.enableAbility(Status.POISON);
            actor.addStatus(new PoisonStatus(5, 1));
            return actor + " eats a toxic Apple and is poisoned! (1 DMG/turn for 5 turns)";
        }
    }

    /**
     * @return the label shown by ConsumeAction in the menu.
     */
    @Override
    public String getConsumeDescription() {
        return "Apple";
    }

    /**
     * Lists actions the carrier can do with this apple, including a
     * ConsumeAction so they can eat it.
     *
     * @param owner the carrier.
     * @param map   the carrier's map.
     * @return the action list.
     */
    @Override
    public ActionList allowableActions(Actor owner, GameMap map) {
        ActionList actions = super.allowableActions(owner, map);
        actions.add(new ConsumeAction(this));
        return actions;
    }

    /**
     * @return flat sell price of 1 credit.
     */
    @Override
    public int getSellPrice() { return 1; }

    /**
     * @return the menu label for SellAction.
     */
    @Override
    public String getSellDescription() { return "Apple"; }

    /**
     * Sell the apple. Pays 1 credit. If the seller isn't sterilised, the apple
     * poisons them (2 damage/turn for 2 turns).
     *
     * @param actor the seller.
     * @param map   the seller's map.
     * @return what happened.
     */
    @Override
    public String sell(Actor actor, GameMap map) {
        actor.modifyStatistic(WorkerStatistics.CREDITS, StatisticOperations.INCREASE, getSellPrice());
        actor.getInventory().remove(this);

        String result = "Sold Apple for 1 credit.";
        if (!actor.hasAbility(Ability.STERILISE)) {
            actor.enableAbility(Status.POISON);
            actor.addStatus(new PoisonStatus(2, 2)); // 2 damage per turn for 2 turns
            result += " The unsterilised apple poisoned you during handling!";
        }
        return result;
    }

}