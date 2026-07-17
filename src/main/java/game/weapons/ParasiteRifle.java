package game.weapons;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
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
 * A long-range rifle that one-shots Parasites found along the wielder's row.
 *
 * <p>Each turn the rifle scans East and West along the wielder's row for the
 * first actor that has neither the {@code WORKER} nor the {@code HOSTILE}
 * ability (i.e. a Parasite). On hit, {@link ParasiteBlastEffect} removes the
 * target and replaces its tile with {@link game.grounds.ToxicWaste}.</p>
 *
 * <p>If no Parasite is found in either direction a miss action is offered; firing
 * it triggers a {@value #MISS_COOLDOWN}-turn cooldown via {@link #onMiss()}.
 * After {@value game.interfaces.Wieldable#MAX_USES} successful hits the standard
 * {@value game.interfaces.Wieldable#USE_COOLDOWN}-turn use cooldown applies.
 * Available from the Weapon Store for {@value #BUY_PRICE} credits.</p>
 *
 * @author Chai
 * @version 2.0
 */
public class ParasiteRifle extends Item implements Wieldable, Buyable {

    /** One-shot damage guaranteed to kill any Parasite. */
    private static final int DAMAGE = 99;

    /** Credit cost in the Weapon Store. */
    private static final int BUY_PRICE = 300;

    /** Cooldown duration applied when the rifle fires but finds no target. */
    private static final int MISS_COOLDOWN = 15;

    /** Number of successful uses since the last cooldown reset. */
    private int useCount = 0;

    /** Turns remaining before the rifle can be used again; 0 means ready. */
    private int cooldown = 0;

    /**
     * Constructs a new Parasite Rifle and registers its weight statistic.
     */
    public ParasiteRifle() {
        super("Parasite Rifle", 'R');
        this.makePortable();
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(8));
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
     * <p>Returns an empty list if the rifle is on cooldown. Otherwise scans East
     * and West for Parasite targets. If at least one target is found, a
     * {@link WieldAttackAction} is added for each direction that has a target.
     * If no target exists in either direction a single guaranteed-miss action
     * is offered.</p>
     *
     * @param owner the actor carrying the rifle.
     * @param map   the active game map.
     * @return all valid fire actions for this turn.
     */
    @Override
    public ActionList allowableActions(Actor owner, GameMap map) {
        ActionList actions = super.allowableActions(owner, map);
        if (cooldown > 0) {
            return actions;
        }

        int ownerX = map.locationOf(owner).x();
        int ownerY = map.locationOf(owner).y();

        Actor eastTarget = scanRow(map, ownerX, ownerY, +1);
        Actor westTarget = scanRow(map, ownerX, ownerY, -1);

        if (eastTarget != null) {
            actions.add(new WieldAttackAction(this, eastTarget, "East"));
        }
        if (westTarget != null) {
            actions.add(new WieldAttackAction(this, westTarget, "West"));
        }
        if (eastTarget == null && westTarget == null) {
            actions.add(new WieldAttackAction(this, null, "East (no target - will miss)"));
        }

        return actions;
    }

    /**
     * Scans horizontally along a single row for the first eligible Parasite target.
     *
     * <p>Traverses tiles one step at a time in the given direction until a map
     * boundary is reached. The first actor encountered that lacks both the
     * {@code WORKER} and {@code HOSTILE} abilities is returned. If the first
     * actor found has one of those abilities the scan stops early and returns
     * {@code null} (the actor is blocking the line of sight).</p>
     *
     * @param map       the active game map.
     * @param startX    the x-coordinate to begin scanning from (exclusive).
     * @param y         the y-coordinate (row) to scan along.
     * @param direction {@code +1} to scan East, {@code -1} to scan West.
     * @return the first valid Parasite target found, or {@code null} if none.
     */
    private Actor scanRow(GameMap map, int startX, int y, int direction) {
        int xMax = map.getXRange().max();
        int xMin = map.getXRange().min();
        int x = startX + direction;
        while (x >= xMin && x <= xMax) {
            Location location = map.at(x, y);
            if (location.containsAnActor()) {
                Actor candidate = location.getActor();
                if (!candidate.hasAbility(Ability.WORKER) && !candidate.hasAbility(Ability.HOSTILE)) {
                    return candidate;
                }
                return null;
            }
            x += direction;
        }
        return null;
    }

    /**
     * Applies a {@value #MISS_COOLDOWN}-turn cooldown when the rifle fires into
     * empty air.
     *
     * @return a log message informing the player of the miss penalty.
     */
    @Override
    public String onMiss() {
        cooldown = MISS_COOLDOWN;
        return "The Parasite Rifle fires into empty air! (15-turn cooldown)";
    }

    /**
     * Increments the use counter and starts the standard cooldown when
     * {@link #MAX_USES} is reached.
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
     * Returns whether the rifle is currently cooling down.
     *
     * @return {@code true} if {@code cooldown > 0}.
     */
    @Override
    public boolean isOnCooldown() {
        return cooldown > 0;
    }

    /**
     * Returns the one-shot damage value.
     *
     * @return {@value #DAMAGE}.
     */
    @Override
    public int getDamage() { return DAMAGE; }

    /**
     * Returns a new {@link ParasiteBlastEffect} for use after a successful hit.
     *
     * @return the parasite-blast post-hit effect.
     */
    @Override
    public WeaponEffect getWeaponEffect() { return new ParasiteBlastEffect(); }

    /**
     * Returns the display name used in action-menu descriptions.
     *
     * @return {@code "Parasite Rifle"}.
     */
    @Override
    public String getWeaponDescription() { return "Parasite Rifle"; }

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
     * @return {@code "Parasite Rifle"}.
     */
    @Override
    public String getBuyDescription() { return "Parasite Rifle"; }

    /**
     * Purchases this weapon for the given actor if they have sufficient credits.
     *
     * <p>Deducts {@value #BUY_PRICE} credits from the actor's balance and adds
     * a new {@link ParasiteRifle} to their inventory.</p>
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
        actor.getInventory().add(new ParasiteRifle());
        return actor + " purchased the Parasite Rifle for " + getBuyPrice() + " credits.";
    }

    /**
     * Returns a string representation showing either the remaining cooldown or
     * the current use count.
     *
     * @return display string with cooldown or use-count info.
     */
    @Override
    public String toString() {
        if (cooldown > 0) {
            return super.toString() + " (Cooldown: " + cooldown + ")";
        }
        return super.toString() + " (Uses: " + useCount + "/" + MAX_USES + ")";
    }
}