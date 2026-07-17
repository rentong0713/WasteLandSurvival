package game.items;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import edu.monash.fit2099.engine.statistics.StatisticOperations;
import game.actions.CutAction;
import game.actions.TeleportAction;
import game.actors.Undead;
import game.enums.Ability;
import game.enums.ItemStatistics;
import game.enums.Status;
import game.enums.WorkerStatistics;
import game.grounds.ToxicWaste;
import game.interfaces.Cuttable;
import game.interfaces.JumpBehaviour;
import game.interfaces.Sellable;
import game.interfaces.TeleportDevice;
import game.status.PoisonStatus;
import game.utils.SpawnUtils;
import game.systems.*;

import java.util.Random;

/**
 * An alien cube the worker can pick up and use to warp around the map.
 * <p>
 * Using it pops up 3 random destinations on the current map. After teleporting,
 * the source tile and every flammable tile next to it turns into
 * {@link ToxicWaste}.
 * <p>
 * Selling it pays 25 credits, but as a last-resort defence the Supercomputer
 * spawns an {@link Undead} next to the seller. The Undead is spawned through
 * {@link SpawnUtils} so its own spawn reaction (max-HP boost from adjacent
 * creatures) still fires.
 *
 * @author Rachel
 * @version 2.0
 */
public class AlienCube extends Item implements TeleportDevice, Sellable, Cuttable {

    /** Optional jump strategy used during teleportation. */
    private JumpBehaviour jumpBehaviour;

    /** Random source for picking destinations. */
    private final Random random = new Random();

    /**
     * Make a new Alien Cube. Portable, sellable, weighs 10.
     */
    public AlienCube() {
        super("Alien Cube", '◈');
        this.makePortable();
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(10));
        this.enableAbility(Ability.SELLABLE);
    }

    /**
     * Alien Cubes pick their destinations randomly each use, so this method
     * doesn't actually store anything.
     *
     * @param location ignored.
     */
    @Override
    public void addDestination(Location location) {}

    /**
     * Set a jump strategy to customise how the teleport works.
     *
     * @param behaviour the jump strategy.
     */
    public void setJumpBehaviour(JumpBehaviour behaviour) {
        this.jumpBehaviour = behaviour;
    }

    /**
     * Warp the actor to the destination, then turn the source tile and its
     * flammable neighbours into Toxic Waste.
     *
     * @param actor       the actor being teleported.
     * @param destination where they end up.
     */
    @Override
    public void triggerTeleport(Actor actor, Location destination) {
        Display display = new Display();
        GameMap map = destination.map();
        Location source = map.locationOf(actor);

        if (this.jumpBehaviour != null) {
            this.jumpBehaviour.performJump(actor, destination);
        } else {
            map.moveActor(actor, destination);
        }

        corruptSource(source);
        display.println(actor + " vanished through a reality rupture, leaving toxic waste behind!");
    }

    /**
     * Turn the source tile and every flammable tile next to it into Toxic Waste.
     *
     * @param source the source tile of the teleport.
     */
    private void corruptSource(Location source) {
        if (source.getGround().hasAbility(Ability.IS_FLAMMABLE)) {
            source.setGround(new ToxicWaste());
        }

        for (Exit exit : source.getExits()) {
            Location adjacent = exit.getDestination();
            if (adjacent.getGround().hasAbility(Ability.IS_FLAMMABLE)) {
                adjacent.setGround(new ToxicWaste());
            }
        }
    }

    /**
     * Build 3 TeleportActions pointing at 3 random destinations.
     *
     * @param owner the carrier of the cube.
     * @param map   the carrier's map.
     * @return the 3 teleport options.
     */
    @Override
    public ActionList allowableActions(Actor owner, GameMap map) {
        ActionList actions = new ActionList();

        for (int i = 0; i < 3; i++) {
            Location randomLocation = getRandomLocation(map, owner);
            actions.add(new TeleportAction(this, randomLocation, "to a random reality-rupture"));
        }

        if (owner.hasAbility(Ability.CUT)){
            actions.add(new CutAction(this, map.locationOf(owner),"(in inventory)"));
        }
        return actions;
    }

    /**
     * Pick a random location on the map that the actor can actually stand on
     * (and isn't already occupied).
     *
     * @param map   the map to pick from.
     * @param actor the actor being teleported.
     * @return a valid random location.
     */
    private Location getRandomLocation(GameMap map, Actor actor) {
        int x, y;
        int xBound = map.getXRange().max() + 1;
        int yBound = map.getYRange().max() + 1;

        Location location;
        do {
            x = random.nextInt(xBound);
            y = random.nextInt(yBound);
            location = map.at(x, y);
        } while (!location.canActorEnter(actor) || location.containsAnActor());

        return location;
    }

    /**
     * @return flat sell price of 25 credits.
     */
    @Override
    public int getSellPrice() { return 25; }

    /**
     * Sell the cube. Pays 25 credits but spawns an Undead on an adjacent tile
     * (via SpawnUtils so the Undead's spawn reaction still fires).
     *
     * @param actor the seller.
     * @param map   the seller's map.
     * @return what happened.
     */
    @Override
    public String sell(Actor actor, GameMap map) {
        Location location = map.locationOf(actor);

        actor.modifyStatistic(WorkerStatistics.CREDITS, StatisticOperations.INCREASE, getSellPrice());
        String result = "Alien Cube sold for " + getSellPrice();

        for (Exit exit : location.getExits()) {
            Location spawnSpot = exit.getDestination();
            if (spawnSpot.canActorEnter(actor)) {

                if (new UndeadSpawner().spawn(spawnSpot)) {
                    result += " A rift opens! An Undead has spawned next to you!";
                    break;
                }
            }
        }

        actor.getInventory().remove(this);
        return result;
    }

    /**
     * @return the menu label for SellAction.
     */
    @Override
    public String getSellDescription() { return "AlienCube"; }

    /**
     * Getters for the name
     * @return a description string for this door
     */
    @Override
    public String getCutDescription(){
        return "Alien Cube";
    }

    /**
     * Internal implementation of the cutting process.
     *
     * @param actor The actor performing the cut
     * @param targetLocation The location of the door to be cut.
     * @return A string detailing the outcome, including any poison events.
     */
    @Override
    public String cut(Actor actor, Location targetLocation){
        actor.getInventory().remove(this);
        targetLocation.addItem(new AlienArtifact());

        actor.enableAbility(Status.POISON);
        actor.addStatus(new PoisonStatus(5, 1));

        return actor + " cracked open the Alien Cube, dropping an Artifact but poisoning themselves in the process!";
    }
}