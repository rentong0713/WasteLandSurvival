package game.flora;

import game.behaviours.GrowBehaviour;

/**
 * A young stage of the Warper Tree.
 *
 * The Warper Sapling can grow into a WarperMatureTree
 * after a fixed number of turns, based on a probability chance.
 */
public class WarperSapling extends Flora {

    protected static final int BEHAVIOUR_PRIORITY_GROW = 1;
    /**
     * Constructs a Warper Sapling and initializes its growth behaviour.
     *
     * This sapling attempts to grow into a mature Warper Tree after 20 turns
     * with a 25% chance each growth attempt.
     */
    public WarperSapling() {
        super('w', "Warper Sapling");

        // Priority 1: Try to grow (20 turns, 25% chance)
        this.behaviours.put(BEHAVIOUR_PRIORITY_GROW, new GrowBehaviour(20, 25, new WarperMatureTree()));
    }
}