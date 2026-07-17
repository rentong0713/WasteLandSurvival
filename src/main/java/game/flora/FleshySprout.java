package game.flora;

import game.behaviours.GrowBehaviour;
import game.behaviours.SpawnOnProximityBehaviour;
import game.actors.Slime;
import game.systems.SlimeSpawner;

/**
 * A mid-stage alien flora that exhibits both growth and hostile spawning behaviour.
 *
 * The Fleshy Sprout can:
 * Spawn a Slime when an actor is nearby (priority 1)
 * Grow into a FleshySapling over time (priority 2)
 *
 * Behaviour execution follows priority order defined in Flora.
 */
public class FleshySprout extends Flora {

    protected static final int BEHAVIOUR_PRIORITY_SPAWN = 1;
    protected static final int BEHAVIOUR_PRIORITY_GROW = 2;
    /**
     * Constructs a Fleshy Sprout and initializes its behaviours.
     */
    public FleshySprout() {
        super('y', "Fleshy Sprout");

        //        this.behaviours.put(1, new SpawnOnProximityBehaviour(new ActorFactory() {
//            @Override
//            public Actor createActor() {
//                return new Slime();
//            }
//        }));

        this.behaviours.put(BEHAVIOUR_PRIORITY_SPAWN, new SpawnOnProximityBehaviour(new SlimeSpawner()));

        // Priority 2: Try to grow (20 turns, 25% chance)
        this.behaviours.put(BEHAVIOUR_PRIORITY_GROW, new GrowBehaviour(20, 25, new FleshySapling()));
    }
}