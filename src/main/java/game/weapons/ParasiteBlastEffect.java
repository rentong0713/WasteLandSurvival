package game.weapons;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.grounds.ToxicWaste;

/**
 * Post-hit effect for the {@link ParasiteRifle}.
 * <p>
 * Removes the Parasite actor from the map and replaces its tile with
 * {@link ToxicWaste} — the same hazardous ground already used in the game.
 *
 * @author Chai
 * @version 1.1
 */
public class ParasiteBlastEffect extends WeaponEffect {

    /**
     * Remove the target Parasite from the map and leave toxic waste behind.
     *
     * @param wielder         the worker who fired the rifle.
     * @param target          the Parasite that was hit.
     * @param wielderLocation the wielder's tile (not used here).
     * @param map             the current game map.
     * @return a log message describing the blast.
     */
    @Override
    public String apply(Actor wielder, Actor target, Location wielderLocation, GameMap map) {
        Location targetLocation = map.locationOf(target);

        if (targetLocation != null) {
            map.removeActor(target);
            targetLocation.setGround(new ToxicWaste());
            return target + " is obliterated by the Parasite Rifle! Toxic waste seeps into the ground.";
        }

        map.removeActor(target);
        return target + " is obliterated by the Parasite Rifle!";
    }
}