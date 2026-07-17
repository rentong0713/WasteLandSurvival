package game.behaviours;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.behaviours.Behaviour;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * WanderBehaviour class representing a behaviour that allows an actor to
 * move randomly to an adjacent available location
 *
 * <P>
 *     This behaviour is typically used as a fallback for NPC. It identifies all
 *     possible exits from the actor's current location and selects one at random.
 *
 * </P>
 *
 * @author Rachel Chiew
 * @version 1.0
 */
public class WanderBehaviour implements Behaviour<Actor, Action>{
    private final Random random = new Random();

    /**
     * Determines a random movement action for the actor.
     *
     * @param actor The entity performing the behaviour
     * @param location The current location of the actor
     * @return A MoveAction to a random adjacent tile.
     */

    public Action operate(Actor actor, Location location){
        List<Action> actions = new ArrayList<>();

        for (Exit exit : location.getExits()){
            Location destination = exit.getDestination();

            if (destination.canActorEnter(actor)){
                actions.add(exit.getDestination().getMoveAction(actor,
                        "around", exit.getHotKey()));
            }
        }

        if (!actions.isEmpty()){
            return actions.get(random.nextInt(actions.size()));
        }

        return null;
    }
}
