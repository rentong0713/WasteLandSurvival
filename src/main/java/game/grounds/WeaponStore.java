package game.grounds;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.BuyAction;
import game.enums.Ability;
import game.interfaces.Buyable;
import game.weapons.ElectricRod;
import game.weapons.FrostBlade;
import game.weapons.ParasiteRifle;

import java.util.List;

/**
 * A static weapon store stall that sells three special weapons to adjacent
 * contracted workers.
 * <p>
 * Implemented as a Ground (like Supercomputer) so the engine correctly calls
 * allowableActions when a worker stands adjacent to it. The stall cannot be
 * entered or moved.
 * <p>
 * Stock:
 * <ul>
 *   <li>{@link ElectricRod} — 150 credits</li>
 *   <li>{@link FrostBlade} — 200 credits</li>
 *   <li>{@link ParasiteRifle} — 300 credits</li>
 * </ul>
 *
 * @author Chai
 * @version 2.0
 */
public class WeaponStore extends Ground {

    private final List<Buyable> stock = List.of(
            new ElectricRod(),
            new FrostBlade(),
            new ParasiteRifle()
    );

    /**
     * Construct a Weapon Store stall.
     */
    public WeaponStore() {
        super('⚔', "Weapon Store");
    }

    /**
     * Expose BuyActions to any adjacent worker.
     */
    @Override
    public ActionList allowableActions(Actor actor, Location location, String direction) {
        ActionList actions = super.allowableActions(actor, location, direction);
        if (!direction.isEmpty() && actor.hasAbility(Ability.WORKER)) {
            for (Buyable weapon : stock) {
                actions.add(new BuyAction(weapon));
            }
        }
        return actions;
    }

    /**
     * No actor can walk onto the store tile.
     */
    @Override
    public boolean canActorEnter(Actor actor) {
        return false;
    }
}