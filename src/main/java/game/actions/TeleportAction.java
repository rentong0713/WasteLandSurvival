package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.interfaces.TeleportDevice;

/**
 * An action that allows an actor to teleport using a TeleportDevice.
 *
 * When executed, the action triggers the teleportation behaviour of the
 * associated teleport device and moves the actor to the specified destination.
 */
public class TeleportAction extends Action {
    private final TeleportDevice device;
    private final Location destination;
    private final String description;

    /**
     * Constructs a teleport action.
     *
     * @param device the teleport device responsible for performing the teleport
     * @param destination the target location to teleport the actor to
     * @param description a short description of the teleport destination or effect
     */
    public TeleportAction(TeleportDevice device, Location destination, String description){
        this.device = device;
        this.destination = destination;
        this.description = description;
    }

    /**
     * Executes the teleportation action.
     *
     * The teleport device is triggered to move the actor to the destination.
     *
     * @param actor the actor performing the teleport action
     * @param map the game map the actor is currently on
     * @return a description of the teleportation event
     */
    @Override
    public String execute(Actor actor, GameMap map){
        device.triggerTeleport(actor, destination);

        return actor + " stepped into the teleportation field " + description;
    }

    /**
     * Returns the menu description shown to the player.
     *
     * @param actor the actor performing the action
     * @return a string describing the teleport action in the menu
     */
    @Override
    public String menuDescription(Actor actor){
        return actor + " teleports " + description;
    }
}
