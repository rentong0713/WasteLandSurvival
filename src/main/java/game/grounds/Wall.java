package game.grounds;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Ground;

public class Wall extends Ground {

    public Wall() {
        super('#', "Wall");
    }

    /**
     * This is the magic method that stops actors.
     * Returning false means NO actor can step here.
     */
    @Override
    public boolean canActorEnter(Actor actor) {
        return false;
    }

    @Override
    public boolean blocksThrownObjects() {
        return true; // Usually walls stop thrown things too!
    }
}