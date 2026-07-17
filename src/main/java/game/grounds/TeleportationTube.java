package game.grounds;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.TeleportAction;
import game.enums.Ability;
import game.interfaces.Ignitable;
import game.interfaces.TeleportDevice;
import game.status.BurnStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A teleportation structure that transports actors between linked tube destinations.
 * <p>
 * Teleportation Tubes may randomly misfire, sending actors to unintended locations.
 * Upon arrival, the environment may trigger burning effects and ignite nearby flammable ground.
 */
public class TeleportationTube extends Ground implements TeleportDevice {
    private List<Location> destinations = new ArrayList<>();
    private final Random random = new Random();

    /**
     * Constructs a Teleportation Tube.
     */
    public TeleportationTube() {
        super('Φ', "Teleportation Tube");
        this.enableAbility(Ability.TELEPORT);
    }

    /**
     * Adds a valid destination for teleportation.
     *
     * @param location the location to be added as a teleport destination
     */
    public void addDestination(Location location) {
        this.destinations.add(location);
    }

    /**
     * Provides teleportation actions available to actors interacting with the tube.
     *
     * @param actor the actor performing actions
     * @param location the current location of the tube
     * @param direction the direction relative to the actor
     * @return a list of teleport actions to available destinations
     */
    @Override
    public ActionList allowableActions(Actor actor, Location location, String direction) {
        ActionList actions = new ActionList();

        if (direction.isEmpty() && !destinations.isEmpty()) {
            for (Location destination : destinations) {
                actions.add(new TeleportAction(this, destination, "to " + destination.map().toString()));
            }
        }

        return actions;
    }

    /**
     * Triggers teleportation of an actor to a destination location.
     * <p>
     * There is a small chance of teleportation malfunction, sending the actor
     * to a random valid location instead.
     * <p>
     * Upon arrival, burning effects may be applied depending on terrain hazards,
     * and nearby flammable tiles may ignite.
     *
     * @param actor the actor being teleported
     * @param destination the intended destination location
     */
    @Override
    public void triggerTeleport(Actor actor, Location destination) {
        Display display = new Display();
        Location finalLocation = destination;

        if (random.nextDouble() < 0.0005) { //0.5
            int x, y;
            Location randomLocation;
            do {
                x = random.nextInt(destination.map().getXRange().max() + 1);
                y = random.nextInt(destination.map().getYRange().max() + 1);
                randomLocation = destination.map().at(x, y);

            } while (!randomLocation.getGround().canActorEnter(actor) || randomLocation.containsAnActor());

            finalLocation = randomLocation;
            display.println("Teleportation malfunction! " + actor +
                    " was sent to coordinates (" + finalLocation.x() + ", "
                    + finalLocation.y() + ")");
        }

        destination.map().moveActor(actor, finalLocation);

        if (finalLocation.getGround().hasAbility(Ability.BURNING)){
            actor.hurt(1);
            actor.addStatus(new BurnStatus(5, 1));
            display.println(actor + " arrived in a burst of flames and was burnt!");
        }

        for (Exit exit : finalLocation.getExits()) {
            Location adjacent = exit.getDestination();

            if (adjacent.getGround().hasAbility(Ability.IS_FLAMMABLE)) {
                Ignitable ignitable = adjacent.getGroundAs(Ignitable.class);
                if (ignitable != null) {
                    ignitable.ignite(3);
                } else{
                    adjacent.setGround(new Fire(adjacent.getGround(), 3));
                }
            }
        }
    }
}



