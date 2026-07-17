package game.flora;

import game.behaviours.WarpOnProximityBehaviour;

/**
 * A fully grown Warper Tree that actively disrupts nearby actors.
 *
 * The Warper Mature Tree is a hostile flora entity capable of
 * forcibly teleporting nearby actors using WarpOnProximityBehaviour.
 */
public class WarperMatureTree extends Flora {

    protected static final int BEHAVIOUR_PRIORITY_SPAWN = 1;
    /**
     * Constructs a Warper Mature Tree and initializes its behaviours.
     */
    public WarperMatureTree() {
        super('W', "Warper Mature Tree");

        // Priority 1: Grab and warp players
        this.behaviours.put(BEHAVIOUR_PRIORITY_SPAWN, new WarpOnProximityBehaviour());
    }
}