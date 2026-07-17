package game.grounds;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.CutAction;
import game.enums.Ability;
import game.enums.ClearanceLevel;
import game.interfaces.Cuttable;
import game.items.AluminiumScrap;

/**
 * A locked Aluminium Door that can be opened by actors with sufficient clearance.
 * <p>
 * This door requires at least {@link ClearanceLevel#LEVEL_1} access to unlock.
 * Unlocking the door causes minor damage due to its sharp edges.
 * @author Rachel Chiew
 * @version 2.0
 */
public class AluminiumDoor extends Door implements Cuttable {

    /**
     * Constructs an Aluminium Door with LEVEL_1 clearance requirement.
     */
    public AluminiumDoor() {
        // Requires a standard UNLOCK_DOOR ability (Level 1, 2, or 3 cards)
        super('=', "Aluminium Door", ClearanceLevel.LEVEL_1);
        this.enableAbility(Ability.CUTTABLE);
    }

    /**
     * Unlocks the Aluminium Door.
     * <p>
     * The actor takes damage when interacting with the sharp edges of the door.
     *
     * @param actor the actor attempting to unlock the door
     * @param map the game map the door exists on
     * @return a message describing the unlocking event
     */
    @Override
    public String unlock(Actor actor, GameMap map) {
        this.isUnlocked = true;
        actor.hurt(2);
        return actor + " unlocks the Aluminium Door, taking 2 damage from sharp edges.";
    }

    /**
     * Returns the required clearance level to unlock this door.
     *
     * @return the ordinal value of {@link ClearanceLevel#LEVEL_1}
     */
    @Override
    public int getRequiredClearanceLevel(){
        return ClearanceLevel.LEVEL_1.ordinal();
    }

    /**
     * Returns a short description used for unlocking interactions.
     *
     * @return a description string for this door
     */
    @Override
    public String getUnlockDescription() {
        return "Aluminium Door";
    }

    /**
     * Returns a list of actions that the actor can perform on this door,
     * including the cutting action if the actor possesses the necessary capability
     * @param actor the actor performing actions
     * @param location the location of the door
     * @param direction the direction of the door relative to the actor
     * @return an ActionList containing all available actions
     */
    @Override
    public ActionList allowableActions(Actor actor, Location location, String direction){
        ActionList actions = super.allowableActions(actor, location, direction);

        if (actor.hasAbility(Ability.CUT) && !direction.isEmpty()){
            actions.add(new CutAction(this, location, direction));
        }

        return actions;
    }

    @Override
    public String getCutDescription(){
        return "Aluminium Door";
    }

    /**
     * Internal implementation of the cutting process. Transforms the ground into a floor,
     * drops Aluminium Scrap, and carries a 25% chance of triggering an explosion that
     * deals 100 damage to any actors on adjacent tiles.
     *
     * @param actor The actor performing the cut
     * @param targetLocation The location of the door to be cut.
     * @param forceExplosion A flag that could be used for testing or forced failure scenarios.
     * @return A string detailing the outcome, including any explosion events.
     */
    public String cut(Actor actor, Location targetLocation, boolean forceExplosion){
        targetLocation.setGround(new Floor());
        targetLocation.addItem(new AluminiumScrap());

        StringBuilder result = new StringBuilder(actor + " cut down the Aluminium Door! It collapses into scrap.");

        if (Math.random() <= 0.25){
            result.append("\nCritical failure! The door mechanism detonates!");
            for (Exit exit : targetLocation.getExits()){
                Location adjacent = exit.getDestination();
                if (adjacent.containsAnActor()){
                    adjacent.getActor().hurt(100);
                }
            }
        }
        return result.toString();
    }
    /**
     * Internal implementation of the cutting process. This serves as the primary interface
     * for the cutting interaction.
     *
     * @param actor The actor performing the cut
     * @param targetLocation The location of the door to be cut
     * @return A string detailing the outcome, including any explosion events.
     */
    @Override
    public String cut(Actor actor, Location targetLocation){
        return cut(actor, targetLocation, false);
    }
}