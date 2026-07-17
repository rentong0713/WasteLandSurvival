package game.grounds;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.MagicJump;
import game.actions.TeleportAction;
import game.enums.Ability;
import game.interfaces.JumpBehaviour;
import game.interfaces.TeleportDevice;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A magical ground tile that allows teleportation between connected Magic Circles.
 * <p>
 * Magic Circles resonate with each other and enable actors to teleport
 * to other circles on the map using a {@link TeleportAction}.
 */
public class MagicCircle extends Ground implements TeleportDevice {
    private JumpBehaviour jumpBehaviour;
    private final Random rand = new Random();

    private List<Location> cachedOtherCircles = new ArrayList<>();
    private boolean isInitialized = false;

    /**
     * Constructs a Magic Circle and enables its teleportation ability.
     */
    public MagicCircle(){
        super('◎', "Magic Circle");
        this.enableAbility(Ability.CAN_RESONATE_MAGIC);
        this.setJumpBehaviour(new MagicJump());
    }

    /**
     * Sets the jump behaviour used when teleportation is triggered.
     *
     * @param behaviour the teleportation behaviour
     */
    public void setJumpBehaviour(JumpBehaviour behaviour){
        this.jumpBehaviour = behaviour;
    }

    @Override
    public void addDestination(Location location) {

    }

    /**
     * Triggers teleportation of an actor to the given destination.
     * <p>
     * If a jump behaviour is defined, it will be used; otherwise,
     * the actor is directly moved to the destination.
     *
     * @param actor the actor being teleported
     * @param destination the target location
     */
    @Override
    public void triggerTeleport(Actor actor, Location destination){
        Display display = new Display();
        if (this.jumpBehaviour != null){
            this.jumpBehaviour.performJump(actor, destination);
        } else{
            destination.map().moveActor(actor, destination);
            display.println( actor + " arrived at the magic circle.");
        }
    }

    /**
     * Provides teleportation actions available to an actor standing on this tile.
     *
     * @param actor the actor interacting with the circle
     * @param location the current location of the actor
     * @param direction direction relative to actor
     * @return list of available teleport actions
     */
    @Override
    public ActionList allowableActions(Actor actor, Location location, String direction){
        ActionList actions = new ActionList();

        if (!isInitialized){
            this.cachedOtherCircles = findOtherCircles(location);
            this.isInitialized = true;
        }

        if (!cachedOtherCircles.isEmpty()){
            Location randomDest = cachedOtherCircles.get(rand.nextInt(cachedOtherCircles.size()));
            actions.add(new TeleportAction(this, randomDest, "to another Magic Circle"));
        }

        return actions;
    }

    private List<Location> findOtherCircles(Location currentLocation){
        List<Location> circles = new ArrayList<>();

        for (int x : currentLocation.map().getXRange()){
            for (int y : currentLocation.map().getYRange()){
                Location location = currentLocation.map().at(x, y);

                if (location.getGround().hasAbility(Ability.CAN_RESONATE_MAGIC) && location != currentLocation){
                    circles.add(location);
                }
            }
        }
        return circles;
    }
}
