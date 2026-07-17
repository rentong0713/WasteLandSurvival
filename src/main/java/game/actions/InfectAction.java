package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.interfaces.Infectable;

/**
 * An action that allows a Parasite to infect a target entity.
 *
 * Upon successful infection, the target's Infectable logic is triggered,
 * and the acting Parasite is immediately removed from the game.
 *
 * @author Ren Tong Low
 */
public class InfectAction extends Action {

    private final Infectable target;
    private final Location targetLocation;

    /**
     * Constructs an InfectAction targeting a specific Infectable entity.
     *
     * @param target         the entity to be infected
     * @param targetLocation the location of the target entity
     */
    public InfectAction(Infectable target, Location targetLocation) {
        this.target = target;
        this.targetLocation = targetLocation;
    }

    /**
     * Executes the infection action.
     *
     * @param actor the actor performing the infection (typically a Parasite)
     * @param map   the game map where the action occurs
     * @return a description of what happened during the infection
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        target.infect(actor, targetLocation);

        String result = actor + " latches onto " + target.getInfectionDescription() + " and dissolves into infection!";

        // The Parasite dies instantly upon successful infection
        map.removeActor(actor);

        return result;
    }

    /**
     * Returns a string describing this action in a menu.
     *
     * @param actor the actor performing the action
     * @return a human-readable menu description
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " infects " + target.getInfectionDescription();
    }
}