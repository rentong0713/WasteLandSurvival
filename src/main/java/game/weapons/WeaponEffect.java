package game.weapons;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;

/**
 * Abstract base for every special effect a wieldable weapon can trigger after a
 * successful primary hit.
 * <p>
 * Subclasses override {@link #apply(Actor, Actor, Location, GameMap)} to define
 * the concrete effect (chain lightning, frost terrain mutation, parasite blast,
 * etc.). The wielder's location is passed in so area effects can iterate exits
 * without having to ask the map again.
 *
 * @author Chai
 * @version 1.0
 */
public abstract class WeaponEffect {

    /**
     * Apply this weapon effect after the primary attack lands.
     *
     * @param wielder        the actor who swung the weapon.
     * @param target         the actor who was hit by the primary attack.
     * @param wielderLocation the tile the wielder is standing on.
     * @param map            the current game map.
     * @return a log message describing what the effect did.
     */
    public abstract String apply(Actor wielder, Actor target, Location wielderLocation, GameMap map);
}