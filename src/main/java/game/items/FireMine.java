package game.items;

import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.TrapExplosionAction;
import game.interfaces.Detonatable;

/**
 * An offensive high-yield explosive item representing a Fire Mine (☼).
 * Constantly monitors floor intersection events to initiate an explosive thermal payload,
 * turning normal structure floor pathways into permanently un-safe scorched terrain.
 *
 * @author Tan Jia Hern
 * @version 1.0
 */
public class FireMine extends Item implements Detonatable {
    /**
     * Constructs a FireMine instance with fixed item nomenclature and its solar blast display symbol.
     */
    public FireMine() {
        super("Fire Mine", '☼');
    }

    /**
     * The engine automatically calls this process hook every turn cycle for ground-based elements.
     * Triggers active area confirmation sweeps.
     *
     * @param currentLoc the map tile context location to process
     */
    @Override
    public void tick(Location currentLoc) {
        checkDetonation(currentLoc);
    }

    /**
     * Checks if any entity profile intersects the exact location tile array.
     * Immediately deploys a dynamic thermal action payload if an intersection evaluates true.
     *
     * @param location the coordinate frame target scanned for actor assets
     */
    @Override
    public void checkDetonation(Location location) {
        if (location.containsAnActor()) {
            new TrapExplosionAction(this, location).execute(location.getActor(), location.map());
        }
    }
}