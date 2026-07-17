package game.weapons;

import edu.monash.fit2099.engine.weapons.IntrinsicWeapon;

/**
 * UndeadFist is a class representing the natural weapon used by Undead entities.
 *
 * @author Rachel Chiew
 * @version 1.0
 */
public class BareFist extends IntrinsicWeapon {

    /**
     * Constructor for the UndeadFist
     */
    public BareFist(){
        super(1, "punches", 10, "bare fist");
    }

}
