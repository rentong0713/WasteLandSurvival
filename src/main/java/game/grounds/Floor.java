package game.grounds;

import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.TrapExplosionAction;
import game.enums.Ability;
import game.interfaces.TrapTriggerable;
import game.items.FireMine;

/**
 * A standard, safe facility structural floor tile represented by the display character '_'.
 * It implements {@link TrapTriggerable} and is designated as flammable, allowing it to react
 * dynamically to thermal explosions by mutating its state.
 *
 * @author Tan Jia Hern
 * @version 1.0
 */
public class Floor extends Ground implements TrapTriggerable {

    /**
     * Constructs a standard Floor instance with its default display glyph and
     * sets its initial flammability capability state.
     */
    public Floor() {
        super('_', "Floor");
        this.enableAbility(Ability.IS_FLAMMABLE);
    }

    /**
     * Reacts to an explosion blast payload. If the source of the blast is a {@link FireMine},
     * this tile completely replaces itself on the game map layout grid with a permanent
     * {@link ScorchedEarth} instance.
     *
     * @param action   the active explosion action structural context causing the interaction
     * @param location the precise map coordinates of this floor tile
     */
    @Override
    public void reactToTrap(TrapExplosionAction action, Location location) {
        // Replaced item instanceof with a capability check
        if (action.getTrapSource().hasAbility(Ability.BURNING)) {
            location.setGround(new ScorchedEarth(this));
            System.out.println("The floor at (" + location.x() + ", " + location.y() + ") gets scorched!");
        }
    }
}