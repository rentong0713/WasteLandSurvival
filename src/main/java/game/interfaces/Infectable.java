package game.interfaces;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Location;

/**
 * Marks something that a Parasite can infect.
 *
 * Both actors (like Worker and Undead) and items on the ground (like Lantern and
 * Cookies) can implement this. The Parasite finds targets using
 * {@code asCapability(Infectable.class)}, so it doesn't need to use instanceof.
 *
 * Each host decides what infection means for itself — a worker attaches a status,
 * an Undead explodes, a Lantern starts losing fuel. That way the Parasite stays
 * simple.
 *
 * @author Chai
 * @version 1.0
 */
public interface Infectable {

    /**
     * Applies the initial infection from a source actor to this host.
     *
     * @param source   the actor causing the infection
     * @param location the location where the infection is applied
     */
    void infect(Actor source, Location location);

    /**
     * Defines the behavior that occurs to this infected host each game turn.
     *
     * @param location the current location of the infected host
     */
    void tickInfection(Location location);

    /**
     * Checks whether this host is still under an active infection.
     *
     * @return true if the infection is still active, false otherwise
     */
    boolean isInfectionActive();

    /**
     * Returns a short human-readable label for logging and display purposes.
     * Example: "the Worker", "a Lantern"
     *
     * @return a descriptive infection label
     */
    String getInfectionDescription();
}