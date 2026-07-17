package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.weapons.Weapon;

/**
 * AttackAction class is an Action to attack another Actor.
 *
 * <p>
 *     This class handles the logic of selecting weapon and applying damage
 *     to the targeted actor.
 * </p>
 *
 * @author Rachel Chiew
 * @version 1.0
 */
public class AttackAction extends Action {

    private Actor target;
    private String direction;

    /**
     * Constructor for the AttackAction.
     *
     * @param target The target to be attacked
     * @param direction The direction where the target is located
     */
    public AttackAction(Actor target, String direction){
        this.target = target;
        this.direction = direction;
    }

    /**
     * Executes the attack logic on the target actor.
     *
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return A string describing the result of the attack
     */
    @Override
    public String execute(Actor actor, GameMap map){
        Weapon weapon = actor.getIntrinsicWeapon();

        for (Item item : actor.getInventory().getItems()){
            var capability = item.asCapability(Weapon.class);

            if (capability.isPresent()){
                weapon = capability.get();
                break;
            }
        }

        String result = weapon.attack(actor, target, map);

        if (!target.isConscious()){
            result += "\n" + target.unconscious(map);
        }
        return result;
    }

    /**
     * Provides a description of the attack action for the player's menu.
     *
     * @param actor The actor performing the action.
     * @return A string describing the action
     */
    @Override
    public String menuDescription(Actor actor){
        return actor+ " attacks " + target + " at " + direction;
    }

}
