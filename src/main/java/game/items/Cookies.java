package game.items;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.ActorStatistics;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import edu.monash.fit2099.engine.statistics.StatisticOperations;
import game.actions.ConsumeAction;
import game.actors.Parasite;
import game.enums.Ability;
import game.enums.ItemStatistics;
import game.enums.WorkerStatistics;
import game.interfaces.Consumable;
import game.interfaces.Infectable;
import game.interfaces.Sellable;
import game.status.InfectStatus;
import game.utils.SpawnUtils;
import game.systems.ParasiteSpawner;

/**
 * A pack of 5 cookies.
 * <p>
 * Eating one cookie heals 1 HP if the eater is sterilized, otherwise it permanently
 * cuts their max HP by 1. Selling pays 1 credit per cookie left, but the seller
 * also loses 1 HP per cookie as an "organic processing fee".
 * <p>
 * If a Parasite infects the pack while it's on the ground, the infection eats one
 * cookie per turn and spawns new Parasites nearby (handled by InfectedCookiesStatus).
 *
 * @author Tan Jia Hern
 * @version 2.0
 */
public class Cookies extends Item implements Consumable, Sellable, Infectable {

    private int uses = 5;
    private boolean isInfected = false;

    /**
     * Make a full pack of 5 cookies. Portable and sellable.
     */
    public Cookies() {
        super("Cookies", '◍');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(2));
        this.makePortable();
        this.enableAbility(Ability.SELLABLE);
    }

    /**
     * Get how many cookies are left, used by the infection status.
     *
     * @return remaining cookies.
     */
    public int getUses() {
        return uses;
    }

    /**
     * Eat one cookie. Heals 1 HP if sterilised, otherwise loses 1 max HP.
     *
     * @param actor the eater.
     * @param map   the eater's map.
     * @return what happened.
     */
    @Override
    public String consume(Actor actor, GameMap map) {
        uses--;
        String result;

        if (actor.hasAbility(Ability.STERILISE)) {
            actor.heal(1);
            result = actor + " eats a sterilised Cookie, healing 1 HP.";
        } else {
            actor.modifyStatisticMaximum(ActorStatistics.HEALTH, StatisticOperations.DECREASE, 1);
            result = actor + " eats a toxic Cookie, permanently losing 1 Max HP!";
        }

        if (uses <= 0) {
            actor.getInventory().remove(this);
            map.locationOf(actor).removeItem(this);
            result += " (Cookies fully consumed)";
        } else {
            result += " (" + uses + " cookies left)";
        }
        return result;
    }

    /**
     * @return the label shown by ConsumeAction in the menu.
     */
    @Override
    public String getConsumeDescription() {
        return "Cookie";
    }

    /**
     * Lists actions the carrier can do with this pack. Adds a ConsumeAction if
     * there are cookies left.
     *
     * @param owner the carrier.
     * @param map   the carrier's map.
     * @return the action list.
     */
    @Override
    public ActionList allowableActions(Actor owner, GameMap map) {
        ActionList actions = super.allowableActions(owner, map);
        if (uses > 0) {
            actions.add(new ConsumeAction(this));
        }
        return actions;
    }

    /**
     * @return name + cookies left + "[INFECTED]" tag if infected.
     */
    @Override
    public String toString() {
        String infectedTag = this.hasAbility(Ability.INFECTED) ? " [INFECTED]" : "";
        return super.toString() + " (" + uses + " left)" + infectedTag;
    }

    /**
     * @return 1 credit per remaining cookie.
     */
    @Override
    public int getSellPrice() { return uses * 1; }

    /**
     * @return the menu label used by SellAction.
     */
    @Override
    public String getSellDescription() { return "Cookies (" + uses + " left)"; }

    /**
     * Sell the pack. Pays 1 credit per cookie and deals 1 HP damage per cookie.
     *
     * @param actor the seller.
     * @param map   the seller's map.
     * @return what happened.
     */
    @Override
    public String sell(Actor actor, GameMap map) {
        int price = getSellPrice();
        int healthPenalty = uses * 1;

        actor.modifyStatistic(WorkerStatistics.CREDITS, StatisticOperations.INCREASE, price);
        actor.getInventory().remove(this);

        actor.hurt(healthPenalty);
        return "Sold Cookies for " + price + " credits. Paid organic processing fee of " + healthPenalty + " HP!";
    }

    /**
     * Infects this cookie pack.
     *
     * Once infected, the cookie pack becomes a food source for the alien
     * infection. The infection consumes one cookie each turn and periodically
     * spawns new Parasites on adjacent tiles until the pack is empty.
     *
     * @param source the actor causing the infection
     * @param location the location of the cookie pack
     */
    @Override
    public void infect(Actor source, Location location) {
        if (this.hasAbility(Ability.INFECTED)) {
            return;
        }
        if (this.uses <= 0) {
            new Display().println("The cookie pack is already empty — infection finds nothing to feed on.");
            return;
        }
        this.isInfected = true;
        this.enableAbility(Ability.INFECTED);
        this.addStatus(new InfectStatus(this));
        new Display().println("A Cookie pack has been infected — parasites will erupt from it!");
    }

    /**
     * Applies the per-turn effects of the infection.
     *
     * The infection consumes one cookie from the pack and attempts to spawn
     * a Parasite on an adjacent empty tile. When all cookies have been
     * consumed, the infection ends and the cookie pack is removed.
     *
     * @param location the current location of the infected cookie pack
     */
    @Override
    public void tickInfection(Location location) {
        if (!isInfected || this.uses <= 0) return;

        this.uses -= 1;
        new Display().println("The infection devours a cookie. (" + this.uses + " left)");

        Parasite parasite = new Parasite();
        Location spawnSpot = SpawnUtils.pickRandom(SpawnUtils.adjacentEmptyLocationsFor(location, parasite));

        if (spawnSpot != null) {
            new ParasiteSpawner().spawn(spawnSpot);
        }

        if (this.uses <= 0) {
            this.isInfected = false;
            this.disableAbility(Ability.INFECTED);
            if (location != null) {
                location.removeItem(this);
            }
        }
    }

    /**
     * Checks whether the cookie pack is currently infected.
     *
     * @return true if the infection is active, otherwise false
     */
    @Override
    public boolean isInfectionActive() {
        return this.isInfected;
    }

    /**
     * @return short label for the Parasite's "latches onto..." log line.
     */
    @Override
    public String getInfectionDescription() {
        return "a Cookie pack (" + uses + " left)";
    }
}