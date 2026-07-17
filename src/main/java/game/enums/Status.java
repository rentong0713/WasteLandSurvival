package game.enums;

/**
 * An enumeration of statuses that can be applied to game entities.
 * These act as tag-like capabilities used by the game engine to easily identify
 * specific states of an entity at runtime.
 *
 * * @author Low Ren Tong
 * @version 1.0
 */
public enum Status {
    HOSTILE_TO_ENEMY,  // Other actor can be hostile to enemy other than worker in the future
    BURN,
    POISON,
    STUN
}