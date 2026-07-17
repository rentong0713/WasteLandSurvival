package game.grounds;

import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.TrapExplosionAction;
import game.interfaces.TrapTriggerable;

/**
 * A hazardous terrain subclass representing permanently burned floor tiles ('⌂').
 * It uses a compositional wrapper around an internal {@link Fire} behavior routine to continually
 * damage anything walking over it while safely locking its state to prevent reversion.
 *
 * @author Tan Jia Hern
 * @version 1.0
 */
public class ScorchedEarth extends Ground implements TrapTriggerable {
    /**
     * The internal compositional fire tracking layer used to process environmental tick actions.
     */
    private final Fire underlyingFire;

    /**
     * Constructs a permanent ScorchedEarth instance. Wraps an underlying fire instance
     * configured with a near-infinite lifespan structure to simulate permanent environmental damage.
     *
     * @param originalGround the fallback ground reference object needed by the underlying fire tracker
     */
    public ScorchedEarth(Ground originalGround) {
        super('⌂', "Scorched Earth");
        // We initialize fire, but we will control its lifespan behaviour
        this.underlyingFire = new Fire(originalGround, 999999); // Set a massive duration
    }

    /**
     * Executes environmental processing tasks every game engine tick. Delegates active
     * actor health depletion to the underlying fire logic and forces an absolute override
     * back to this ground state if internal timer cleanups attempt to revert the tile.
     *
     * @param location the exact structural map grid cell coordinates where this ground sits
     */
    @Override
    public void tick(Location location) {
        // Run the damage logic from fire
        underlyingFire.tick(location);

        // Prevent it from ever reverting! If the fire tries to swap back to floor,
        // we lock the ground back to this ScorchedEarth instance.
        if (location.getGround() != this) {
            location.setGround(this);
        }
    }

    /**
     * Handles blast interactions while in a scorched state, passing execution metrics directly
     * into the underlying fire structure to stack durations or trigger contextual tracking cycles.
     *
     * @param action   the active blast execution action matrix context
     * @param location the map coordinates of this scorched tile
     */
    @Override
    public void reactToTrap(TrapExplosionAction action, Location location) {
        underlyingFire.reactToTrap(action, location);
    }
}