package game.items;

import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.TrapExplosionAction;
import game.interfaces.Detonatable;

/**
 * A utility deployment item representing a Stun Mine (⚡).
 * Actively tracks its coordinate space to emit a paralyzing shockwave that
 * halts all physical control vectors of entities caught in the epicenter.
 *
 * @author Tan Jia Hern
 * @version 1.0
 */
public class StunMine extends Item implements Detonatable {
    /**
     * Constructs a StunMine instance with fixed item nomenclature and its unique tracking glyph symbol.
     */
    public StunMine() {
        super("Stun Mine", '⚡');
    }

    /**
     * The engine executes this method automatically every game round for ground-level items.
     * Triggers a proactive proximity scan of the current map tile coordinates.
     *
     * @param currentLoc the precise map coordinate context location of the item
     */
    @Override
    public void tick(Location currentLoc) {
        checkDetonation(currentLoc);
    }

    /**
     * Validates whether any living entity has intersected the current cell coordinates.
     * Immediately offloads execution tasks to a new explosion action block if triggered.
     *
     * @param location the map tile coordinate zone checked for entity placement
     */
    @Override
    public void checkDetonation(Location location) {
        if (location.containsAnActor()) {
            new TrapExplosionAction(this, location).execute(location.getActor(), location.map());
        }
    }
}