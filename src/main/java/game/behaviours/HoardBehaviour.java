package game.behaviours;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.behaviours.Behaviour;
import edu.monash.fit2099.engine.items.Item;
import game.actions.PickUpAction;
import edu.monash.fit2099.engine.positions.Location;
import game.enums.Ability;

/**
 * A behaviour that causes an actor to collect depositable items from its current location.
 *
 * When executed, this behaviour searches all items on the ground at the actor's
 * current location. If a depositable item is found, the actor will perform a
 * PickUpAction to collect it. If no suitable item is available, the
 * behaviour returns null so that other behaviours may be considered.
 *
 * @author Ren Tong Low
 * @version 1.0
 */
public class HoardBehaviour implements Behaviour<Actor, Action> {

    /**
     * Determines whether the actor should pick up a depositable item.
     *
     * @param actor    the actor performing this behaviour
     * @param location the actor's current location
     * @return a PickUpAction for the first depositable item found,
     *         or null if no depositable items are present
     */
    @Override
    public Action operate(Actor actor, Location location) {
        for (Item item : location.getItems()) {
            if (item.hasAbility(Ability.DEPOSITABLE)) {
                return new PickUpAction(item);
            }
        }
        return null;
    }
}