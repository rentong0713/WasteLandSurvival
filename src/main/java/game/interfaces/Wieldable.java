package game.interfaces;

import game.weapons.WeaponEffect;

/**
 * Marks an item as a wieldable weapon that applies a special effect after hitting.
 * <p>
 * Each wieldable tracks usage — after {@value #MAX_USES} uses a 10-turn cooldown
 * is applied before the weapon can be used again.
 *
 * @author Chai
 * @version 2.0
 */
public interface Wieldable {

    /** Number of uses before the cooldown kicks in. */
    int MAX_USES = 3;

    /** Cooldown duration in turns after MAX_USES is reached. */
    int USE_COOLDOWN = 10;

    /**
     * @return how much damage the primary hit deals to the target.
     */
    int getDamage();

    /**
     * @return the post-hit effect that fires after a successful primary attack.
     */
    WeaponEffect getWeaponEffect();

    /**
     * @return the label shown in the WieldAttackAction menu entry.
     */
    String getWeaponDescription();

    /**
     * @return the purchase price in credits.
     */
    int getBuyPrice();

    /**
     * Record one use of the weapon. Implementations should increment their
     * internal use counter and trigger the cooldown when MAX_USES is reached.
     */
    void recordUse();

    /**
     * Called by WieldAttackAction when the attack misses (target was null).
     * Each weapon defines its own miss behaviour — e.g. ParasiteRifle applies
     * a 15-turn cooldown, melee weapons do nothing.
     *
     * @return a log message describing the miss consequence.
     */
    String onMiss();

    /**
     * @return true if the weapon is currently on cooldown and cannot be used.
     */
    boolean isOnCooldown();
}