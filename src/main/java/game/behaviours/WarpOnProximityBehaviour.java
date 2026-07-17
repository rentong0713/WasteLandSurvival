package game.behaviours;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.behaviours.Behaviour;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.enums.*;
import game.utils.TeleportUtils;
import edu.monash.fit2099.engine.positions.GameMap;

/**
 * A behaviour that teleports nearby workers when they move adjacent
 * to a Warper Tree.
 *
 * If an adjacent actor possesses the Ability#WORKER ability,
 * the actor is instantly teleported to a random valid location on the map.
 * Once a teleportation occurs, the behaviour consumes the action for
 * that turn.
 *
 * @author  Low Ren Tong
 * @version 1.0
 */
public class WarpOnProximityBehaviour implements Behaviour<Ground, Boolean> {

    /**
     * Detects nearby workers and teleports them to a random valid location.
     *
     * @param entity the ground entity performing the behaviour
     * @param location the location of the ground entity
     * @return true if an actor was teleported and the action was consumed,
     *         otherwise false
     */
    @Override
    public Boolean operate(Ground entity, Location location) {
        GameMap map = location.map();

        for (Exit exit : location.getExits()) {
            Location adjacent = exit.getDestination();
            if (adjacent.containsAnActor() && adjacent.getActor().hasAbility(Ability.WORKER)) {
                Actor worker = adjacent.getActor();

                // Using the TeleportUtils you built for the AlienArtifact!
                Location destination = TeleportUtils.getRandomLocation(map, worker);
                map.moveActor(worker, destination);

                new Display().println("The Fleshy Monolith violently warps " + worker + " across the map!");
                return true;
            }
        }
        return false;
    }
}