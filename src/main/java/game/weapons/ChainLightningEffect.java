package game.weapons;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;

/**
 * Post-hit effect for the {@link ElectricRod}.
 * <p>
 * After the primary target takes damage, chain lightning arcs to every actor on
 * a tile adjacent to the wielder, dealing {@value #ARC_DAMAGE} damage each.
 * The arc never bounces back to the wielder itself. Any secondary victim that
 * dies from the arc is removed from the map immediately.
 *
 * @author Chai
 * @version 1.0
 */
public class ChainLightningEffect extends WeaponEffect {

    /** Damage dealt to each actor caught in the arc. */
    private static final int ARC_DAMAGE = 3;

    /**
     * Arc lightning to all actors adjacent to the wielder (excluding the wielder).
     *
     * @param wielder         the actor who swung the Electric Rod.
     * @param target          the primary target (already hit before this is called).
     * @param wielderLocation the wielder's tile.
     * @param map             the current game map.
     * @return a summary of every actor struck by the arc.
     */
    @Override
    public String apply(Actor wielder, Actor target, Location wielderLocation, GameMap map) {
        Display display = new Display();
        StringBuilder log = new StringBuilder("Chain lightning arcs from " + wielder + "!");

        for (Exit exit : wielderLocation.getExits()) {
            Location adjacent = exit.getDestination();
            if (!adjacent.containsAnActor()) {
                continue;
            }
            Actor victim = adjacent.getActor();
            if (victim == wielder) {
                continue;
            }

            victim.hurt(ARC_DAMAGE);
            log.append("\n  ⚡ ").append(victim).append(" is struck by lightning! (-").append(ARC_DAMAGE).append(" HP)");

            if (!victim.isConscious()) {
                String deathMsg = victim.unconscious(map);
                log.append(" ").append(deathMsg);
            }
        }

        return log.toString();
    }
}