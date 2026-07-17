package game.items;

import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.TrapExplosionAction;
import game.interfaces.Detonatable;

/**
 * A tactical chemical deployment item representing a Gas Mine (☁).
 * Constantly scans its location tile to instantly flood the grid space with noxious fumes,
 * applying immediate health point reduction and toxic status poisoning over time.
 *
 * @author Tan Jia Hern
 * @version 1.0
 */
public class GasMine extends Item implements Detonatable {

    /**
     * Constructs a GasMine instance with fixed item nomenclature and its distinctive gas display symbol.
     */
    public GasMine() {
        super("Gas Mine", '☁');
    }

    /**
     * The engine calls this method every single turn for items resting on the ground layer.
     * We use it to constantly scan if an actor has stepped onto the mine's tile.
     *
     * @param currentLoc the coordinates identifying where the item currently rests on the map
     */
    @Override
    public void tick(Location currentLoc) {
        checkDetonation(currentLoc);
    }

    /**
     * Checks if a player or NPC is sharing the tile, then detonates.
     * Instantly schedules a new explosion action to parse the area and injects lingering poison.
     *
     * @param location the location context grid coordinates to scan for actors
     */
    @Override
    public void checkDetonation(Location location) {
        if (location.containsAnActor()) {
            new TrapExplosionAction(this, location).execute(location.getActor(), location.map());
        }
    }
}