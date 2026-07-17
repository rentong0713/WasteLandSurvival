package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import edu.monash.fit2099.engine.statistics.StatisticOperations;
import game.enums.Ability;
import game.enums.ItemStatistics;
import game.enums.Status;
import game.enums.WorkerStatistics;
import game.grounds.Fire;
import game.interfaces.Ignitable;
import game.interfaces.Infectable;
import game.interfaces.Sellable;
import game.status.BurnStatus;
import game.status.InfectStatus;

/**
 * A lantern that gives off light but can leak oil and set the ground on fire.
 * <p>
 * Selling it pays 5 credits per fuel unit left (max 50 for a full 10-fuel lantern).
 * The sale has a 50% chance to burn the seller and an independent 25% chance to
 * light all surrounding tiles on fire.
 * <p>
 * If a Parasite infects the lantern on the ground, the infection drains 1 fuel
 * each turn (handled by InfectedLanternStatus).
 *
 * @author Tan Jia Hern
 * @version 2.0
 */
public class Lantern extends Item implements Sellable, Infectable {

    /** Fuel left in the lantern. */
    private int fuel = 10;
    private boolean isInfected = false;

    /**
     * Make a Lantern with 10 fuel. Portable and sellable.
     */
    public Lantern() {
        super("Lantern", '&');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(7));
        this.makePortable();
        this.enableAbility(Ability.SELLABLE);
    }

    /**
     * Get the current fuel, used by the infection status.
     *
     * @return fuel left.
     */
    public int getFuel() {
        return fuel;
    }

    /**
     * Take some fuel away (used by the infection tick). Won't go below zero.
     *
     * @param amount how much fuel to remove.
     */
    public void drainFuel(int amount) {
        this.fuel = Math.max(0, this.fuel - amount);
    }

    /**
     * Every turn while being carried: 5% chance to leak oil and light the ground
     * under the carrier on fire, using up one fuel.
     *
     * @param currentLocation where the carrier is.
     * @param actor           the carrier.
     */
    @Override
    public void tick(Location currentLocation, Actor actor) {
        if (fuel > 0 && Math.random() <= 0.05) {
            fuel--;
            Ignitable fireGround = currentLocation.getGroundAs(Ignitable.class);
            if (fireGround != null) {
                fireGround.ignite(6);
            } else {
                Ground oriGround = currentLocation.getGround();
                currentLocation.setGround(new Fire(oriGround));
            }
        }
    }

    /**
     * @return name + fuel + "[INFECTED]" tag if infected.
     */
    @Override
    public String toString() {
        String infectedTag = this.hasAbility(Ability.INFECTED) ? " [INFECTED]" : "";
        return super.toString() + " (Fuel: " + fuel + ")" + infectedTag;
    }

    /**
     * @return 5 credits per fuel unit left.
     */
    @Override
    public int getSellPrice() { return fuel * 5; }

    /**
     * @return the menu label for this lantern.
     */
    @Override
    public String getSellDescription() { return "Lantern (" + fuel + " fuel)"; }

    /**
     * Sell the lantern. Pays 5 credits per fuel unit. There's a 50% chance the
     * seller gets burned, and an independent 25% chance the surrounding tiles
     * catch fire.
     *
     * @param actor the seller.
     * @param map   the seller's map.
     * @return what happened.
     */
    @Override
    public String sell(Actor actor, GameMap map) {
        actor.modifyStatistic(WorkerStatistics.CREDITS, StatisticOperations.INCREASE, getSellPrice());
        actor.getInventory().remove(this);

        String result = "Sold Lantern for " + getSellPrice() + " credits.";

        if (Math.random() <= 0.5) {
            actor.enableAbility(Status.BURN);
            actor.addStatus(new BurnStatus(3, 2));
            result += " You were burned handling the lantern!";
        }

        if (Math.random() <= 0.25) {
            for (Exit exit : map.locationOf(actor).getExits()) {
                Location dest = exit.getDestination();
                Ground oriGround = dest.getGround();
                Ignitable fireGround = dest.getGroundAs(Ignitable.class);
                if (fireGround != null) {
                    fireGround.ignite(6);
                } else {
                    dest.setGround(new Fire(oriGround));
                }
            }
            result += " The oil leaked and ignited the surroundings!";
        }
        return result;
    }

    /**
     * Infects this lantern.
     *
     * The infection feeds on the lantern's oil supply, draining fuel over time
     * until none remains. A lantern with no fuel cannot sustain an infection.
     *
     * @param source the actor causing the infection
     * @param location the location of the lantern
     */
    @Override
    public void infect(Actor source, Location location) {
        if (this.hasAbility(Ability.INFECTED)) {
            return;
        }
        if (this.fuel <= 0) {
            new Display().println("The infection finds no fuel in the lantern and dies out.");
            return;
        }
        this.isInfected = true;
        this.enableAbility(Ability.INFECTED);
        this.addStatus(new InfectStatus(this));
        new Display().println("A Lantern has been infected — its fuel will now be devoured.");
    }

    /**
     * Applies the per-turn effects of the infection.
     *
     * Each turn, the infection consumes one unit of fuel from the lantern.
     * When all fuel has been depleted, the infection ends automatically.
     *
     * @param location the current location of the infected lantern
     */
    @Override
    public void tickInfection(Location location) {
        if (!isInfected || this.fuel <= 0) return;

        this.drainFuel(1);
        new Display().println("The infection guzzles the lantern's oil. (Fuel: " + this.fuel + ")");

        if (this.fuel <= 0) {
            this.isInfected = false;
            this.disableAbility(Ability.INFECTED);
        }
    }

    /**
     * Checks whether the lantern is currently infected.
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
        return "a Lantern (fuel " + fuel + ")";
    }
}