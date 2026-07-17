package game.grounds;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.CutAction;
import game.actors.Undead;
import game.enums.Ability;
import game.enums.Status;
import game.interfaces.Cuttable;
import game.interfaces.Spawner;
import game.items.IndustrialFan;
import game.status.PoisonStatus;
import game.utils.SpawnUtils;
import game.systems.UndeadSpawner;
import java.util.*;
/**
 * A vent on the floor that spawns enemies only when a worker is standing next
 * to it.
 * <p>
 * Unlike a Hole, the Vent doesn't spawn on a timer — it only fires
 * when motion is detected (a worker is adjacent). When it does spawn, it picks
 * a random empty neighbour tile to put the creature on, then poisons every
 * actor next to the vent (the new creature too, since it's now adjacent) for
 * POISON_DURATION turns at POISON_DAMAGE damage each turn.
 *
 * @author Chai, Rachel Chiew
 * @version 2.0
 */
public class Vent extends Ground implements Cuttable {

    /** How many turns the poison lasts on each victim. */
    private static final int POISON_DURATION = 5;

    /** Damage per turn from the poison. */
    private static final int POISON_DAMAGE = 1;

    /** The Spawner used to make new creatures. */
    private final Spawner spawner;
    private final Random random = new Random();

    /**
     * Build a Vent with the given spawn strategy.
     *
     * @param spawner the strategy that produces creatures when the vent triggers.
     */
    public Vent(Spawner spawner) {
        super('V', "Vent");
        this.spawner = spawner;
        this.enableAbility(Ability.CUTTABLE);
    }

    /**
     * Run one turn of the vent. If a worker is next to it, try to spawn a
     * creature on a random empty neighbour tile and poison everything adjacent.
     *
     * @param location the vent's tile.
     */
    @Override
    public void tick(Location location) {
        if (location.containsAnActor() || !isWorkerAdjacent(location)) {
            return;
        }

        List<Location> emptyAdjacents = new ArrayList<>();
        for (Exit exit : location.getExits()) {
            Location dest = exit.getDestination();
            if (!dest.containsAnActor()) {
                emptyAdjacents.add(dest);
            }
        }

        if (emptyAdjacents.isEmpty()) {
            return; // Nowhere to spawn
        }

        // Pick a random empty adjacent tile
        Location spawnSpot = emptyAdjacents.get(random.nextInt(emptyAdjacents.size()));

        // Delegate to the Spawner
        if (spawner.spawn(spawnSpot)) {
            new Display().println("Something erupts from the Vent at ("
                    + location.x() + ", " + location.y() + ") onto ("
                    + spawnSpot.x() + ", " + spawnSpot.y() + ")!");
            poisonAdjacent(location);
        }

    }

    /**
     * Check if any worker is on a tile next to this vent.
     *
     * @param location the vent's tile.
     * @return true if a worker is adjacent.
     */
    private boolean isWorkerAdjacent(Location location) {
        for (Exit exit : location.getExits()) {
            Location adjacent = exit.getDestination();
            if (adjacent.containsAnActor() && adjacent.getActor().hasAbility(Ability.WORKER)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Apply poison to every actor on a tile next to this vent. The new creature
     * is included because it's standing on an adjacent tile too.
     *
     * @param ventLocation the vent's tile.
     */
    private void poisonAdjacent(Location ventLocation) {
        Display display = new Display();
        for (Exit exit : ventLocation.getExits()) {
            Location adjacent = exit.getDestination();
            if (!adjacent.containsAnActor()) {
                continue;
            }
            Actor target = adjacent.getActor();
            target.enableAbility(Status.POISON);
            target.addStatus(new PoisonStatus(POISON_DURATION, POISON_DAMAGE));
            display.println("Toxic gas spews from the Vent — " + target + " is poisoned!");
        }
    }

    /**
     * Provides available actions for the vent, specifically the CutAction if the
     * actor is capable of cutting
     *
     * @param actor the Actor acting
     * @param location the current Location
     * @param direction the direction of the Ground from the Actor
     * @return An ActionList of available actions
     */
    @Override
    public ActionList allowableActions(Actor actor, Location location, String direction){
        ActionList actions = super.allowableActions(actor, location, direction);

        if (actor.hasAbility(Ability.CUT)){
                String dir = (direction == null || direction.isEmpty()) ? "underneath you" : direction;
            actions.add(new CutAction(this, location, dir));
        }

        return actions;
    }

    /**
     * Returns a short description of the vent used for UI and menu interactions
     * @return A string representing the name or description of the vent.
     */
    @Override
    public String getCutDescription(){
        return "Vent";
    }

    /**
     * Executes the cutting process on the vent. Replaces the ground with a floor,
     * drops an Industrial Fan, and spawns an Undead
     *
     * @param actor The actor performing the cut.
     * @param targetLocation The location of the vent.
     * @return A description of the outcome, including the Undead spawn.
     */
    @Override
    public String cut(Actor actor, Location targetLocation){
        targetLocation.setGround(new Floor());
        targetLocation.addItem(new IndustrialFan());
        new UndeadSpawner().spawn(targetLocation);
        return actor + " ripped the Industrial Fan out of the Vent. An Undead spawned!";
    }
}