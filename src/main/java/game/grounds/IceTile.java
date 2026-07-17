package game.grounds;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.enums.Status;
import game.status.PoisonStatus;

/**
 * A frozen tile created when the {@link game.weapons.FrostBlade} hits a target.
 * <p>
 * Each turn any actor standing on this tile:
 * <ul>
 *   <li>takes 1 cold damage, and</li>
 *   <li>receives a frostbite {@link PoisonStatus} (1 dmg/turn for 3 turns).</li>
 * </ul>
 * After {@value #DURATION} turns the ice melts and the original ground is restored.
 *
 * @author Chai
 * @version 1.0
 */
public class IceTile extends Ground {

    /** How many turns this tile lasts before melting. */
    private static final int DURATION = 5;

    /** Cold damage dealt each turn to any standing actor. */
    private static final int COLD_DAMAGE = 1;

    /** The ground that was here before the ice formed. */
    private final Ground originalGround;

    /** Ticks remaining before the ice melts. */
    private int ticksLeft = DURATION;

    /**
     * Create an ice tile that will revert to {@code originalGround} after 5 turns.
     *
     * @param originalGround the ground to restore when the ice melts.
     */
    public IceTile(Ground originalGround) {
        super('*', "Ice Tile");
        this.originalGround = originalGround;
    }

    /**
     * Apply cold damage and frostbite to any actor on this tile, then count down
     * towards melting.
     *
     * @param location the tile's map location.
     */
    @Override
    public void tick(Location location) {
        if (location.containsAnActor()) {
            Actor actor = location.getActor();
            actor.hurt(COLD_DAMAGE);
            actor.enableAbility(Status.POISON);
            actor.addStatus(new PoisonStatus(3, 1));
            new Display().println(actor + " shivers on the ice tile! (-" + COLD_DAMAGE
                    + " HP, frostbite applied)");
        }

        ticksLeft--;
        if (ticksLeft <= 0) {
            location.setGround(originalGround);
            new Display().println("The ice tile at (" + location.x() + ", " + location.y() + ") melts away.");
        }
    }

    /**
     * Actors can still walk on ice.
     *
     * @param actor the actor attempting to enter.
     * @return always {@code true}.
     */
    @Override
    public boolean canActorEnter(Actor actor) {
        return true;
    }
}