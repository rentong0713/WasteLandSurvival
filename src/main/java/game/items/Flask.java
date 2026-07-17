package game.items;

import edu.monash.fit2099.engine.actors.ActorStatistics;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.interfaces.Consumable;
import game.actions.ConsumeAction;
import game.enums.ItemStatistics;

/**
 * A portable container for restorative liquid that provides a limited number of uses.
 * The flask allows an actor to heal a small amount of health per charge used.
 * It is designed to be lightweight and portable, though it cannot be used
 * if the user is already at maximum health or if the flask is empty.
 * * Due to severe budget cuts, the flask is only permitted to hold five (5)
 * mouthfuls of liquid per deployment.
 *
 *
 * @author Low Ren Tong
 * @version 1.0
 */
public class Flask extends Item implements Consumable {
    protected int totalUsable = 5;

    /**
     * Constructor for the Flask.
     * Initializes the item with the name "Flask", display character 'u',
     * a weight statistic of 3, and makes it portable.
     */
    public Flask() {
        super("Flask", 'u');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(3));
        this.makePortable();
    }

    /**
     * Executes the logic for drinking from the flask.
     * If the actor is injured and charges remain, it heals the actor by 1 point
     * and decrements the charge count.
     *
     * @param actor The actor consuming the flask's contents.
     * @param map   The GameMap where the actor is located.
     * @return A string describing the result of the attempt to drink.
     */
    @Override
    public String consume(Actor actor, GameMap map) {

        if (totalUsable <= 0) {
            return "Flask is empty!";
        }

        if (actor.getStatistic(ActorStatistics.HEALTH) >= actor.getMaximumStatistic(ActorStatistics.HEALTH)) {
            return actor + " health is in good condition.";
        }
        totalUsable--;
        actor.heal(1);
        return actor + " drinks from the flask, healing 1 point of health (" + totalUsable + " uses left)";
    }

    /**
     * Provides a description of the flask for menu display.
     *
     * @return The string "Flask".
     */
    @Override
    public String getConsumeDescription() {
        return "Flask";
    }

    /**
     * Returns a list of actions that can be performed with this flask.
     * If there are charges remaining, a ConsumeAction is added to the list.
     *
     * @param owner The actor currently carrying the flask.
     * @param map   The GameMap where the owner is located.
     * @return An ActionList containing the available actions.
     */
    @Override
    public ActionList allowableActions(Actor owner, GameMap map) {
        ActionList actions = super.allowableActions(owner, map);
        if (totalUsable > 0) {
            actions.add(new ConsumeAction(this)); // Uses the generic action
        }
        return actions;
    }

    /**
     * Returns a string representation of the flask, including the number of remaining uses.
     *
     * @return A formatted string describing the flask and its current charge level.
     */
    @Override
    public String toString() {
        return super.toString() + " (" + totalUsable + " uses left)";
    }

}