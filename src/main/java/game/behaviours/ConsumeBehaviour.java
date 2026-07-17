package game.behaviours;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.behaviours.Behaviour;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.ConsumeAction;
import game.interfaces.Consumable;
import java.util.*;

/**
 * Behaviour that allows an actor to automatically consume a random item at their current location.
 *
 * @author Low Ren Tong
 * @version 1.0
 */
public class ConsumeBehaviour implements Behaviour<Actor, Action> {
    private final Random random = new Random();

    /**
     * Scans the ground for consumables and generates an action to consume one at random.
     *
     * @param entity   The acting entity.
     * @param location The actor's current location.
     * @return A ConsumeAction, or null if no consumables are present.
     */
    @Override
    public Action operate(Actor entity, Location location) {
        List<Consumable> validConsumables = new ArrayList<>();

        // Gather ALL consumables on the current ground
        for (Item item : location.getItems()) {
            Consumable consumable = item.asCapability(Consumable.class).orElse(null);
            if (consumable != null) {
                validConsumables.add(consumable);
            }
        }

        // If there are any consumables, pick one completely at random
        if (!validConsumables.isEmpty()) {
            Consumable chosenFood = validConsumables.get(random.nextInt(validConsumables.size()));
            return new ConsumeAction(chosenFood);
        }

        return null;
    }
}