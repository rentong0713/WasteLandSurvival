package game.weapons;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import edu.monash.fit2099.engine.statistics.StatisticOperations;
import game.actions.WieldAttackAction;
import game.enums.Ability;
import game.enums.ItemStatistics;
import game.enums.WorkerStatistics;
import game.interfaces.Buyable;
import game.interfaces.Wieldable;

/**
 * A melee weapon that deals 5 damage on a primary hit and then arcs chain
 * lightning to every actor adjacent to the wielder for 3 damage each.
 *
 * <p>After {@value game.interfaces.Wieldable#MAX_USES} uses a
 * {@value game.interfaces.Wieldable#USE_COOLDOWN}-turn cooldown is applied
 * before the rod can swing again. Available from the Weapon Store for
 * {@value #BUY_PRICE} credits.</p>
 *
 * @author Chai
 * @version 2.0
 */
public class ElectricRod extends Item implements Wieldable, Buyable {

    /** Primary-hit damage dealt to the target. */
    private static final int DAMAGE = 5;

    /** Credit cost in the Weapon Store. */
    private static final int BUY_PRICE = 150;

    /** Number of uses taken since the last cooldown reset. */
    private int useCount = 0;

    /** Turns remaining before the rod can be used again; 0 means ready. */
    private int cooldown = 0;

    /**
     * Constructs a new Electric Rod and registers its weight statistic.
     */
    public ElectricRod() {
        super("Electric Rod", '†');
        this.makePortable();
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(5));
    }

    /**
     * Decrements the cooldown counter by one each game turn.
     *
     * @param currentLocation the tile this item currently occupies.
     * @param actor           the actor carrying this item.
     */
    @Override
    public void tick(Location currentLocation, Actor actor) {
        if (cooldown > 0) {
            cooldown--;
        }
    }

    /**
     * Builds the list of attack actions available to the owner this turn.
     *
     * <p>Returns an empty list if the rod is on cooldown. Otherwise, adds a
     * WieldAttackAction for each adjacent non-worker actor.</p>
     *
     * @param owner the actor carrying the rod.
     * @param map   the active game map.
     * @return all valid attack actions for this turn.
     */
    @Override
    public ActionList allowableActions(Actor owner, GameMap map) {
        ActionList actions = super.allowableActions(owner, map);
        if (cooldown > 0) {
            return actions;
        }
        for (Exit exit : map.locationOf(owner).getExits()) {
            var dest = exit.getDestination();
            if (dest.containsAnActor()) {
                Actor target = dest.getActor();
                if (!target.hasAbility(Ability.WORKER)) {
                    actions.add(new WieldAttackAction(this, target, exit.getName()));
                }
            }
        }
        return actions;
    }

    /**
     * Returns an empty string because the Electric Rod has no miss penalty.
     *
     * @return empty string.
     */
    @Override
    public String onMiss() {
        return "";
    }

    /**
     * Increments the use counter and starts the cooldown when {@link #MAX_USES}
     * is reached.
     */
    @Override
    public void recordUse() {
        useCount++;
        if (useCount >= MAX_USES) {
            cooldown = USE_COOLDOWN;
            useCount = 0;
        }
    }

    /**
     * Returns whether the rod is currently cooling down.
     *
     * @return true if cooldown > 0.
     */
    @Override
    public boolean isOnCooldown() {
        return cooldown > 0;
    }

    /**
     * Returns the primary-hit damage value.
     *
     * @return {@value #DAMAGE}.
     */
    @Override
    public int getDamage() { return DAMAGE; }

    /**
     * Returns a new ChainLightningEffect for use after a successful hit.
     *
     * @return the chain-lightning post-hit effect.
     */
    @Override
    public WeaponEffect getWeaponEffect() { return new ChainLightningEffect(); }

    /**
     * Returns the display name used in action-menu descriptions.
     *
     * @return {@code "Electric Rod"}.
     */
    @Override
    public String getWeaponDescription() { return "Electric Rod"; }

    /**
     * Returns the purchase price in credits.
     *
     * @return {@value #BUY_PRICE}.
     */
    @Override
    public int getBuyPrice() { return BUY_PRICE; }

    /**
     * Returns the label shown in the Weapon Store buy menu.
     *
     * @return {@code "Electric Rod"}.
     */
    @Override
    public String getBuyDescription() { return "Electric Rod"; }

    /**
     * Purchases this weapon for the given actor if they have sufficient credits.
     *
     * <p>Deducts #BUY_PRICE credits from the actor's balance and adds
     * a new ElectricRod to their inventory.</p>
     *
     * @param actor the actor attempting to buy the weapon.
     * @param map   the active game map.
     * @return a confirmation message, or {@code "Insufficient funds!"} if the
     *         actor cannot afford the purchase.
     */
    @Override
    public String buy(Actor actor, GameMap map) {
        if (actor.getStatistic(WorkerStatistics.CREDITS) < getBuyPrice()) {
            return "Insufficient funds!";
        }
        actor.modifyStatistic(WorkerStatistics.CREDITS, StatisticOperations.DECREASE, getBuyPrice());
        actor.getInventory().add(new ElectricRod());
        return actor + " purchased the Electric Rod for " + getBuyPrice() + " credits.";
    }

    /**
     * Returns a string representation showing either the remaining cooldown or
     * the current use count.
     *
     * @return display string with cooldown or use count info.
     */
    @Override
    public String toString() {
        if (cooldown > 0) {
            return super.toString() + " (Cooldown: " + cooldown + ")";
        }
        return super.toString() + " (Uses: " + useCount + "/" + MAX_USES + ")";
    }
}