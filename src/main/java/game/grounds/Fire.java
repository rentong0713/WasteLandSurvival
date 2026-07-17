package game.grounds;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.enums.*;
import game.interfaces.Ignitable;
import game.interfaces.TrapTriggerable;
import game.status.*;
import game.actions.TrapExplosionAction;
import game.items.FireMine;

import java.util.ArrayList;
import java.util.List;

/**
 * A hazardous ground type representing an active fire.
 * Fire can be stacked multiple times on the same tile. Actors stepping into the fire
 * will receive burn statuses. Highly flammable actors will catch on fire.
 *
 * @author Low Ren Tong
 * @version 2.0
 */
public class Fire extends Ground implements Ignitable, TrapTriggerable {

    private final Ground originalGround;
    private final List<Integer> lifespans = new ArrayList<>();
    private Actor lastActorBurned = null;

    /**
     * Constructor for Fire.
     * * @param originalGround The ground object to restore once all fire stacks expire.
     *
     * @param initialDuration The number of turns the first fire stack will last.
     */
    public Fire(Ground originalGround, int initialDuration) {
        super('^', "Fire");
        this.originalGround = originalGround;
        this.lifespans.add(initialDuration);
        this.enableAbility(Ability.BURNING);
    }

    /**
     * Overloaded constructor that defaults to 5 turns.
     */
    public Fire(Ground originalGround) {
        this(originalGround, 5);
    }

    /**
     * Adds a new fire stack with a specified duration.
     *
     * @param duration the lifespan of the new fire stack
     */
    @Override
    public void ignite(int duration) {
        this.lifespans.add(duration);
        this.lastActorBurned = null;
    }

    /**
     * Updates fire behaviour each turn.
     * <p>
     * - Damages any actor standing on the tile
     * - Decreases all fire stack lifespans
     * - Removes expired stacks
     * - Restores original ground when fire is fully gone
     *
     * @param location the location of this fire ground
     */
    @Override
    public void tick(Location location) {
        Actor actor = location.getActor();

        // Handle Damage
        if (actor != null && !lifespans.isEmpty()) {
            // Strictly reduce life by 1 per turn, regardless of stacks
            actor.hurt(1);

            // Only print the message if it's a "new" burn event for this actor on this tile
            if (actor != lastActorBurned) {
                // Change "by fire" to dynamically fetch the ground name (e.g., "by Scorched Earth")
                String groundName = location.getGround().toString().toLowerCase();
                new Display().println(actor + " is burnt by " + groundName + " at " + location.x() + ", " + location.y() + ".");
                lastActorBurned = actor;
            }
        } else {
            // Reset tracker if the actor leaves the tile or fire is gone
            lastActorBurned = null;
        }

        // Handle Timers (This must happen every tick)
        lifespans.replaceAll(integer -> integer - 1);
        lifespans.removeIf(lifespan -> lifespan <= 0);

        // Restore Ground
        if (lifespans.isEmpty()) {
            location.setGround(originalGround);
        }
    }

    /**
     * Returns a string representation of the fire, including remaining durations.
     *
     * @return string representation of the fire state
     */
    @Override
    public String toString() {
        if (lifespans.isEmpty()) {
            return super.toString();
        }
        return super.toString() + " (Durations left: " + lifespans.toString() + ")";
    }
    @Override
    public void reactToTrap(TrapExplosionAction action, Location location) {
        if (action.getTrapSource().hasAbility(Ability.BURNING)) {
            this.ignite(5);
        }
    }
}