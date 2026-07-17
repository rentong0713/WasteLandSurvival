package game.flora;

import game.behaviours.WarpOnProximityBehaviour;

/**
 * The final stage of the fleshy tree lifecycle.
 *
 * A Fleshy Monolith no longer grows or spawns creatures. Instead, it
 * possesses a proximity-based warping effect that can teleport nearby
 * actors when its behaviour is triggered.
 *
 * @author Ren Tong Low
 * @version 1.0
 */
public class FleshyMonolith extends Flora {

    /**
     * Constructs a Fleshy Monolith with a proximity-based warp behaviour.
     */
    public FleshyMonolith() {
        super('H', "Fleshy Monolith");

        this.behaviours.put(1, new WarpOnProximityBehaviour());
    }
}