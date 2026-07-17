package game.behaviours;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.behaviours.Behaviour;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.AttackAction;
import game.enums.Ability;

/**
 * AttackBehaviour class representing a behaviour that allows an actor to automatically
 * attack nearby targets.
 *
 * <p>
 *     This behaviour scans the immediate exits of the actor's current location.
 * </p>
 *
 * @author Rachel Chiew
 * @version 1.0
 */
public class AttackBehaviour implements Behaviour<Actor, Action> {

    /**
     * Determines if an attack can be performed on an adjacent actor.
     *
     * @param actor The entity performing the behaviour
     * @param location The current location of the actor
     * @return An AttackAction if a target is adjacent, else, null.
     */

    public Action operate(Actor actor, Location location){

        for (Exit exit : location.getExits()){
            Location destination = exit.getDestination();

            if (destination.containsAnActor()){
                Actor target = destination.getActor();

                if (!target.hasAbility(Ability.HOSTILE)){
                    return new AttackAction(target, exit.getName());
                }
            }
        }

        return null;
    }
}
