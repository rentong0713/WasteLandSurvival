package game.actors;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import game.behaviours.ConsumeBehaviour;
import game.enums.Ability;
import game.interfaces.Spawnable;
import game.inventory.BasicInventory;
import game.behaviours.WanderBehaviour;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple non-playable character that roams the map and consumes food.
 *
 * Its primary goal is to eat consumables found on the ground; if none are present,
 * it falls back to wandering randomly.
 *
 * As a {@link Spawnable}, the moment a Slime is placed on the map all workers on
 * adjacent tiles drop their entire inventory onto their own tile — the
 * environmental reaction defined by REQ4.
 *
 * @author Low Ren Tong
 * @version 2.0
 */
public class Slime extends NPC implements Spawnable {

    /**
     * Construct a Slime with 25 HP, an empty inventory, and the standard consume/wander
     * behaviour stack.
     */
    public Slime() {
        super("Slime", '⍾', 25, new BasicInventory());

        /** Behaviour priority for consuming food (lower = higher priority). */
        int CONSUME_BEHAVIOUR_PRIORITY = 0;
        this.behaviours.put(CONSUME_BEHAVIOUR_PRIORITY, new ConsumeBehaviour());
        /** Behaviour priority for wandering. */
        int WANDER_BEHAVIOUR_PRIORITY = 1;
        this.behaviours.put(WANDER_BEHAVIOUR_PRIORITY, new WanderBehaviour());
    }

    /**
     * Environmental reaction triggered on spawn — every worker on an adjacent tile
     * is forced to drop their entire inventory onto their own tile.
     *
     * @param location the tile this Slime was just placed on.
     */
    @Override
    public void onSpawn(Location location) {
        Display display = new Display();
        for (Exit exit : location.getExits()) {
            Location adjacent = exit.getDestination();
            if (!adjacent.containsAnActor()) {
                continue;
            }
            Actor neighbour = adjacent.getActor();
            if (!neighbour.hasAbility(Ability.WORKER)) {
                continue;
            }

            // Snapshot before mutation to avoid ConcurrentModificationException.
            List<Item> toDrop = new ArrayList<>(neighbour.getInventory().getItems());
            if (toDrop.isEmpty()) {
                continue;
            }
            for (Item item : toDrop) {
                neighbour.getInventory().remove(item);
                adjacent.addItem(item);
            }
            display.println("A Slime oozes near " + neighbour
                    + "! In panic they drop everything they were carrying.");
        }
    }

}