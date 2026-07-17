package game.items;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.actions.ConsumeAction;
import game.interfaces.Consumable;
import edu.monash.fit2099.engine.actors.ActorStatistics;
import edu.monash.fit2099.engine.statistics.StatisticOperations;
import game.interfaces.Buyable;
import game.enums.*;

/**
 * A medical utility item that provides significant recovery
 * Upon consumption, the First Aid Kit fully restores the actor's current hit points to their
 * maximum and provides a permanent increase to the actor's maximum health statistic.
 * This item features a 20-turn cooldown mechanism that specifically only progresses while
 * the kit is being carried by an actor.
 *
 *
 * @author Tan Jia Hern
 * @version 1.0
 */
public class FirstAidKit extends Item implements Consumable, Buyable {
    private int cooldown = 0;

    /**
     * Constructor for the FirstAidKit.
     * Initializes the item with the name "First Aid Kit", display character '+',
     * a weight of 25, and makes it portable.
     */
    public FirstAidKit() {
        super("First Aid Kit", '+');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(25));
        this.makePortable();
    }

    /**
     * Updates the state of the item when it is carried in an actor's inventory.
     * Decrements the cooldown timer by one each turn.
     *
     * @param currentLocation The location of the actor carrying the item.
     * @param actor           The actor carrying the item.
     */
    @Override
    // there are two tick methods in game entity, this tick will only be updated when actor carries it
    public void tick(Location currentLocation, Actor actor) {
        if (cooldown > 0) {
            cooldown--;
        }
    }

    /**
     * Resets the usage cooldown to its maximum duration of 20 turns.
     */
    public void resetCooldown() {
        this.cooldown = 20;
    }

    /**
     * Returns a description of the item for use in consumption menus.
     *
     * @return The string "First Aid Kit".
     */
    @Override
    public String getConsumeDescription() {
        return "First Aid Kit";
    }

    /**
     * Executes the consumption logic for the First Aid Kit.
     * Increases the actor's maximum health by 1, fully restores current health to that
     * new maximum, and triggers the usage cooldown.
     *
     * @param actor The actor using the kit.
     * @param map   The map the actor is currently on.
     * @return A string describing the healing and health boost effect.
     */
    @Override
    public String consume(Actor actor, GameMap map) {
        this.resetCooldown();

        actor.modifyStatisticMaximum(ActorStatistics.HEALTH, StatisticOperations.INCREASE, 1);

        int maxHealth = actor.getMaximumStatistic(ActorStatistics.HEALTH);
        actor.modifyStatistic(ActorStatistics.HEALTH, StatisticOperations.UPDATE, maxHealth);

        return actor + " uses the First Aid Kit! Max HP increased and fully healed.";
    }

    /**
     * Returns the list of actions that can be performed with this item.
     * If the cooldown has reached zero, it adds a ConsumeAction to the list.
     *
     * @param owner The actor carrying the kit.
     * @param map   The map the actor is currently on.
     * @return An ActionList containing available actions based on the current cooldown state.
     */
    @Override
    public ActionList allowableActions(Actor owner,  GameMap map) {
        ActionList actions = new ActionList();
        if (cooldown == 0) {
            actions.add(new ConsumeAction(this));
        }
        return actions;
    }

    /**
     * Returns a string representation of the First Aid Kit.
     * Appends the remaining cooldown duration to the name if the item is currently on cooldown.
     *
     * @return A formatted string describing the kit and its cooldown status.
     */
    @Override
    public String toString() {
        if (cooldown > 0) {
            return super.toString() + " (Cooldown: " + cooldown + " turns)";
        }
        return super.toString();
    }

    @Override
    public int getBuyPrice() { return 1000; }

    @Override
    public String getBuyDescription() { return "First Aid Kit"; }

    @Override
    public String buy(Actor actor, GameMap map) {
        int credits = actor.getStatistic(WorkerStatistics.CREDITS);
        if (credits < getBuyPrice()) {
            actor.hurt(actor.getMaximumStatistic(edu.monash.fit2099.engine.actors.ActorStatistics.HEALTH));
            return "Insufficient funds! The Supercomputer executes you on the spot.";
        }
        actor.modifyStatistic(WorkerStatistics.CREDITS, StatisticOperations.DECREASE, getBuyPrice());
        actor.getInventory().add(new FirstAidKit());
        return "Purchased First Aid Kit for 1000 credits.";
    }
}