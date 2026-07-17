package game.actions;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import game.interfaces.JumpBehaviour;
import game.items.Flask;

/**
 * A jump behaviour that teleports an actor to a magic circle destination.
 *
 * After successfully teleporting, this behaviour attempts to spawn a
 * Flask in a valid adjacent location surrounding the destination.
 *
 * A valid location must:
 * Not contain another actor
 * Not contain any items
 * Be enterable by the actor
 *
 */

public class MagicJump implements JumpBehaviour {

    /**
     * Teleports the actor to the destination location if it is unoccupied.
     *
     * After teleportation, a Flask is spawned in the first valid
     * adjacent location found around the destination.
     *
     * @param actor the actor performing the magic jump
     * @param destination the target location to teleport to
     */
    @Override
    public void performJump(Actor actor, Location destination){
        Display display = new Display();

        if (!destination.containsAnActor()){
            destination.map().moveActor(actor, destination);
            display.println(actor + " arrived at a magic circle.");
        }

        for (Exit exit : destination.getExits()){
            Location adjacentLocation = exit.getDestination();

            if (!adjacentLocation.containsAnActor() &&
                    adjacentLocation.getItems().isEmpty() &&
                    adjacentLocation.canActorEnter(actor)){

                adjacentLocation.addItem(new Flask());
                display.println("Magical energy surges! A Flask materializes nearby.");

                break;
            }
        }
    }
}
