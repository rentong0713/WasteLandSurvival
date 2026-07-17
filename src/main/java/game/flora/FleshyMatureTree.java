package game.flora;

import game.behaviours.SpawnOnProximityBehaviour;
import game.actors.Undead;
import game.systems.UndeadSpawner;

/**
 * A fully matured stage of the Fleshy Tree.
 *
 * The Fleshy Mature Tree is a hostile flora that can spawn
 * Undead entities when an actor is detected nearby.
 *
 * @author Ren Tong Low
 * @version 1.0
 */
public class FleshyMatureTree extends Flora {
    protected static final int BEHAVIOUR_PRIORITY_SPAWN = 1;

    /**
     * Constructs a Fleshy Mature Tree and initializes its behaviours.
     */
    public FleshyMatureTree() {
        super('Y', "Fleshy Mature Tree");

        //  this.behaviours.put(1, new SpawnOnProximityBehaviour(new ActorFactory() {
//            @Override
//            public Actor createActor() {
//                return new Undead();
//            }
//        }));

        this.behaviours.put(BEHAVIOUR_PRIORITY_SPAWN, new SpawnOnProximityBehaviour(new UndeadSpawner()));
    }
}