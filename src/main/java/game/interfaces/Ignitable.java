package game.interfaces;

/**
 * Interface for ground types or objects that can be set on fire.
 * Implementing classes must define how they handle being ignited, typically by
 * adding or refreshing fire effects or "stacks" on their current location.
 *
 *
 * @author Tan Jia Hern
 */
public interface Ignitable {
    /**
     * Adds an independent stack of fire to the ground.
     * This method is triggered when a fire-starting event (like an oil leak or explosion)
     * interacts with the object.
     */
    void ignite(int duration);
}