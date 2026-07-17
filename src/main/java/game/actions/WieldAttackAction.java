package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.interfaces.Wieldable;
import game.weapons.WeaponEffect;

/**
 * Action that executes a primary hit with a Wieldable weapon and then
 * triggers the weapon's WeaponEffect.
 *
 * <p>On a successful hit Wieldable#recordUse() is called so the weapon
 * can track its 3-use / 10-turn cooldown cycle. On a miss
 * Wieldable#onMiss() is called so each weapon may apply its own
 * miss consequence.</p>
 *
 * @author Chai
 * @version 2.0
 */
public class WieldAttackAction extends Action {

    /** The weapon being used to attack. */
    private final Wieldable weapon;

    /** The actor being attacked; {@code null} means no target (miss). */
    private final Actor target;

    /** The cardinal direction the attack is aimed at (used in menu text). */
    private final String direction;

    /**
     * Creates a new wield-attack action.
     *
     * @param weapon    the wieldable weapon performing the attack.
     * @param target    the target actor, or {@code null} for a guaranteed miss.
     * @param direction the direction label shown in the action menu.
     */
    public WieldAttackAction(Wieldable weapon, Actor target, String direction) {
        this.weapon = weapon;
        this.target = target;
        this.direction = direction;
    }

    /**
     * Executes the attack: deals primary damage, fires the weapon effect, and
     * records the use (or miss).
     *
     * <p>If target is null the attack automatically misses and
     * Wieldable#onMiss() is invoked. Otherwise, the target takes
     * Wieldable#getDamage() damage, the weapon effect is applied, and
     * the use count is incremented via Wieldable#recordUse().</p>
     *
     * @param actor the actor performing the action.
     * @param map   the active game map.
     * @return a multi-line log string describing every event that occurred.
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        if (target == null) {
            return actor + " fires the " + weapon.getWeaponDescription()
                    + " but finds no target! " + weapon.onMiss();
        }

        target.hurt(weapon.getDamage());
        StringBuilder log = new StringBuilder();
        log.append(actor).append(" strikes ").append(target)
                .append(" with ").append(weapon.getWeaponDescription())
                .append(" for ").append(weapon.getDamage()).append(" damage!");

        if (!target.isConscious()) {
            log.append("\n").append(target.unconscious(map));
        }

        Location wielderLocation = map.locationOf(actor);
        WeaponEffect effect = weapon.getWeaponEffect();
        if (effect != null && wielderLocation != null) {
            log.append("\n").append(effect.apply(actor, target, wielderLocation, map));
        }

        weapon.recordUse();
        if (weapon.isOnCooldown()) {
            log.append("\n").append(weapon.getWeaponDescription())
                    .append(" has been used 3 times and needs a 10-turn cooldown!");
        }

        return log.toString();
    }

    /**
     * Returns the text shown in the actor's action menu for this attack.
     *
     * @param actor the actor whose menu is being built.
     * @return a description of the attack option.
     */
    @Override
    public String menuDescription(Actor actor) {
        if (target == null) {
            return actor + " fires " + weapon.getWeaponDescription() + " " + direction;
        }
        return actor + " attacks " + target + " with " + weapon.getWeaponDescription()
                + " (" + direction + ")";
    }
}