package game.grounds;

import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.ConsumeAction;
import game.enums.*;
import game.status.*;
import game.interfaces.Consumable;

/**
 * A small, stationary body of mysterious liquid on the ground.
 * It could be anything from spilled engine coolant to
 * highly corrosive alien saliva.
 *
 *
 * @author Low Ren Tong
 * @version 1.0
 */
public class Puddle extends Ground implements Consumable {

    /**
     * Constructor for Puddle.
     * Initializes the ground with the '~' display character and the name "Puddle".
     */
    public Puddle() {

        super('~', "Puddle");
        this.enableAbility(Ability.IS_FLAMMABLE);
    }

    /**
     * Executes the logic for an actor drinking from the puddle.
     * If the actor has the STERILISE ability, they heal. Otherwise, they become
     * poisoned or have their current poison duration extended.
     *
     * @param actor The actor consuming the puddle water.
     * @param map   The GameMap where the puddle is located.
     * @return A string describing the effect of consumption on the actor.
     */
    @Override
    public String consume(Actor actor, GameMap map) {
        if (actor.hasAbility(Ability.STERILISE)) {
            actor.heal(1);
            return actor + " drinks purified puddle water, healing 1 HP.";
        }
        else {
            // Check if they are ALREADY poisoned
            if (actor.hasAbility(Status.POISON) && actor.hasStatus(PoisonStatus.class)) {

                // Find the existing sickness
                PoisonStatus existingPoison = actor.statusesOf(PoisonStatus.class).get(0);

                // Add 3 more turns to it
                existingPoison.increaseDuration(3);

                return actor + " drinks more toxic water! Poison duration extended by 3 turns.";
            }
            else {
                // They are not poisoned yet. Apply the standard sickness.
                actor.enableAbility(Status.POISON);
                actor.addStatus(new PoisonStatus(3, 1));

                return actor + " drinks toxic puddle water and gets poisoned! (1 DMG/turn for 3 turns)";
            }
        }
    }

    /**
     * Provides a description of the consumable item for display in menus.
     *
     * @return A string "Puddle Water".
     */
    @Override
    public String getConsumeDescription() {
        return "Puddle Water";
    }

    /**
     * Checks if the actor is standing on the puddle and adds a ConsumeAction if they are.
     *
     * @param actor     The actor potentially interacting with the puddle.
     * @param location  The location of the puddle.
     * @param direction The direction of the puddle relative to the actor.
     * @return A list of actions including ConsumeAction if the actor is on the puddle.
     */
    @Override
    public ActionList allowableActions(Actor actor, Location location, String direction) {
        ActionList actions = super.allowableActions(actor, location, direction);

        // direction.equals("") implies the actor is standing directly on top of the ground
        if (direction.equals("")) {
            actions.add(new ConsumeAction(this));
        }
        return actions;
    }
}