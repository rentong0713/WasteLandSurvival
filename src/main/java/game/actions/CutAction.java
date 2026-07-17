package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.enums.Ability;
import game.interfaces.Cuttable;

/**
 * Action class for cutting objects in the simulation.
 * @author Rachel Chiew
 * @version 1.0
 */

public class CutAction extends Action {
    private final Cuttable target;
    private final Location targetLocation;
    private final String direction;

    /**
     * Constructor for CutAction.
     * @param target The object to be cut.
     * @param targetLocation The location of the target.
     * @param direction The direction of the target from the actor.
     */
    public CutAction(Cuttable target, Location targetLocation, String direction){
        this.target = target;
        this.targetLocation = targetLocation;
        this.direction = direction;
    }

    /**
     * Executes the cutting action. Checks if the actor possesses the required cutting ability
     * before triggering the target's cut method.
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return A sting indicating the outcome of the action
     */
    @Override
    public String execute(Actor actor, GameMap map){
        if (!actor.hasAbility(Ability.CUT)){
            return actor + " does not have a cutting tool!";
        }
        return target.cut(actor, targetLocation);
    }

    /**
     * Provides a description of the action for the game menu.
     * @param actor The actor performing the action.
     * @return A string formatted for the menu display describing the action and direction.
     */
    @Override
    public String menuDescription(Actor actor){
        if (direction.isEmpty()){
            return actor + " cuts the " + target.getCutDescription();
        }
        return actor + " cuts the " + target.getCutDescription() + " to the " + direction;
    }
}
