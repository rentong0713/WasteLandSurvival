package game.weapons;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.grounds.IceTile;


/**
 * Post-hit effect for the {@link FrostBlade}.
 * <p>
 * Replaces the primary target's tile with an IceTile that lasts 5 turns,
 * dealing cold damage and applying frostbite each tick to anyone standing on it.
 *
 * @author Chai
 * @version 1.0
 */
public class FrostEffect extends WeaponEffect {

    /**
     * Swap the target's tile for an IceTile.
     *
     * @param wielder         the actor who swung the Frost Blade.
     * @param target          the actor who was hit.
     * @param wielderLocation the wielder's tile (not used here).
     * @param map             the current game map.
     * @return a log message describing the terrain mutation.
     */
    @Override
    public String apply(Actor wielder, Actor target, Location wielderLocation, GameMap map) {
        Location targetLocation = map.locationOf(target);
        if (targetLocation == null) {
            return "";
        }
        IceTile ice = new IceTile(targetLocation.getGround());
        targetLocation.setGround(ice);
        return "The Frost Blade's cold seeps into the ground — "
                + target + "'s tile is now a frozen wasteland!";
    }
}