package game.flora;

import game.behaviours.GrowBehaviour;

/**
 * A young stage of the Fleshy Tree lineage.
 *
 * The Fleshy Sapling can grow into a FleshyMatureTree
 * after a fixed number of turns, depending on probability.
 */
public class FleshySapling extends Flora {

    protected static final int BEHAVIOUR_PRIORITY_GROW = 1;
    /**
     * Constructs a Fleshy Sapling with its growth behaviour.
     *
     * This sapling attempts to grow into a mature tree after 25 turns,
     * with a 50% chance each growth attempt.
     */
    public FleshySapling() {
        super('v', "Fleshy Sapling");

        // Priority 1: Try to grow (25 turns, 50% chance)
        this.behaviours.put(BEHAVIOUR_PRIORITY_GROW, new GrowBehaviour(25, 50, new FleshyMatureTree()));
    }
}