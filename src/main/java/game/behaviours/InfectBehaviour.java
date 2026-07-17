package game.behaviours;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.behaviours.Behaviour;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.*;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.InfectAction;
import game.enums.Ability;
import game.interfaces.Infectable;

/**
 * A behaviour that allows an actor to search for nearby
 * Infectable targets and attempt to infect them.
 *
 * The search prioritises hostile actors first (e.g., Undead), then non-hostile
 * actors (e.g., Workers), and finally infectable items in adjacent tiles.
 *
 * @author Ren Tong Low
 */
public class InfectBehaviour implements Behaviour<Actor, Action> {

    /**
     * Finds an Infectable target in adjacent locations and returns an infection action.
     *
     * @param actor  the actor performing the behaviour
     * @param location the current location of the actor
     * @return an InfectAction if a valid target exists, otherwise null
     */
    @Override
    public Action operate(Actor actor, Location location) {
        Action action = findActorInfectAction(location, true); // Prefer Hostiles

        if (action == null) {
            action = findActorInfectAction(location, false); // Then Non-Hostiles
        }
        if (action == null) {
            action = findItemInfectAction(location); // Then Items
        }

        return action;
    }

    /**
     * Searches adjacent tiles for an Infectable actor target.
     *
     * @param location      the current location
     * @param preferHostile  whether hostile (Undead) targets are prioritised
     * @return an InfectionTarget if found, otherwise null
     */
    private Action findActorInfectAction(Location location, boolean preferHostile) {
        for (Exit exit : location.getExits()) {
            Location dest = exit.getDestination();
            if (dest.containsAnActor()) {
                Actor candidate = dest.getActor();
                Infectable infectable = candidate.asCapability(Infectable.class).orElse(null);

                if (infectable != null && !infectable.isInfectionActive()) {
                    boolean isHostile = candidate.hasAbility(Ability.HOSTILE);

                    if ((preferHostile && isHostile) || (!preferHostile && !isHostile)) {
                        // We found the target, immediately build and return the Action
                        return new InfectAction(infectable, dest);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Searches adjacent tiles for an Infectable item target.
     *
     * @param location the current location
     * @return an InfectionTarget if an item is found, otherwise null
     */
    private Action findItemInfectAction(Location location) {
        for (Exit exit : location.getExits()) {
            Location dest = exit.getDestination();
            for (Item item : dest.getItems()) {
                Infectable infectable = item.asCapability(Infectable.class).orElse(null);
                if (infectable != null) {
                    // We found the target, immediately build and return the Action
                    return new InfectAction(infectable, dest);
                }
            }
        }
        return null;
    }
}