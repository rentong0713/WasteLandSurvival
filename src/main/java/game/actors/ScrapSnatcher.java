package game.actors;

import edu.monash.fit2099.demo.forest.BasicInventory;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.weapons.IntrinsicWeapon;
import game.behaviours.AttackBehaviour;
import game.behaviours.HoardBehaviour;
import game.behaviours.WanderBehaviour;
import game.enums.Ability;
import game.interfaces.Infectable;
import game.interfaces.Spawnable;
import game.factories.ResourceFactory;
import game.status.InfectStatus;

/**
 *
 * Upon spawning, it triggers a "loot explosion," scattering random resources
 * onto adjacent tiles using a ResourceFactory.
 *
 * As an Infectable, if a Parasite infects the Scrap Snatcher, it drops
 * its hoarding behavior, becomes permanently rabid (prioritizing attacks), and
 * takes continuous damage until death.
 *
 * @author Low Ren Tong
 * @version 3.0
 */
public class ScrapSnatcher extends NPC implements Spawnable, Infectable {

    public static final int HOARD_BEHAVIOUR_PRIORITY = 1;
    public static final int WANDER_BEHAVIOUR_PRIORITY = 2;

    private final ResourceFactory resourceFactory = new ResourceFactory();

    private boolean isInfected = false;
    private boolean infectedSwapped = false;

    /**
     * Constructs a Scrap Snatcher with standard behaviors and a basic inventory
     * for hoarding items.
     */
    public ScrapSnatcher() {
        super("Scrap Snatcher", 's', 25, new BasicInventory());

        this.behaviours.put(HOARD_BEHAVIOUR_PRIORITY, new HoardBehaviour());
        this.behaviours.put(WANDER_BEHAVIOUR_PRIORITY, new WanderBehaviour());
    }

    /**
     * Executes the loot explosion the moment the entity is spawned.
     * Drops exactly one random depositable resource on every valid adjacent tile.
     *
     * @param location the location where the Scrap Snatcher spawned
     */
    @Override
    public void onSpawn(Location location) {
        new Display().println("A Scrap Snatcher bursts onto the scene, scattering loot!");

        for (Exit exit : location.getExits()) {
            Location adjacent = exit.getDestination();
            adjacent.addItem(resourceFactory.createRandomResource());
        }
    }

    /**
     * Applies infection to the Scrap Snatcher, triggering its rabid state.
     *
     * @param source   the actor causing the infection
     * @param location the entity's current location
     */
    @Override
    public void infect(Actor source, Location location) {
        if (this.hasAbility(Ability.INFECTED)) {
            new Display().println(this + " is already infected — it continues its rampage.");
            return;
        }

        this.isInfected = true;
        this.enableAbility(Ability.INFECTED);
        this.addStatus(new InfectStatus(this));

        new Display().println(this + " is infected! It begins to twitch erratically.");
    }

    /**
     * Executes per-turn infection effects on the Scrap Snatcher.
     *
     * The Snatcher permanently swaps its to an attack focus on the first tick
     * and takes 1 damage every subsequent turn.
     *
     * @param location the entity's current location
     */
    @Override
    public void tickInfection(Location location) {
        if (!this.isConscious()) {
            this.isInfected = false;
            return;
        }

        if (!infectedSwapped) {
            this.behaviours.remove(HOARD_BEHAVIOUR_PRIORITY);
            this.behaviours.put(0, new AttackBehaviour()); // 0 is highest priority
            new Display().println(this + " goes completely rabid and stops hoarding!");
            infectedSwapped = true;
        }

        this.hurt(1);
        new Display().println(this + " writhes in pain from the infection. (-1 HP)");
    }

    /**
     * Checks whether the Scrap Snatcher's infection is still active.
     *
     * @return true if infected and still conscious, false otherwise
     */
    @Override
    public boolean isInfectionActive() {
        return this.isInfected && this.isConscious();
    }

    /**
     * Returns a human-readable description of this infected entity.
     *
     * @return string representation of the Scrap Snatcher
     */
    @Override
    public String getInfectionDescription() {
        return this.toString();
    }

    /**
     * Overrides the default attack to use bare fists when the Snatcher is rabid.
     *
     * @return the intrinsic weapon to be used in combat
     */
    @Override
    public IntrinsicWeapon getIntrinsicWeapon() {
        if (this.hasAbility(Ability.INFECTED)) {
            return new IntrinsicWeapon(1, "punches", 10, "bare fist") {};
        }
        return super.getIntrinsicWeapon();
    }

}