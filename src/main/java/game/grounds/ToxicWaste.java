package game.grounds;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.enums.Ability;

/**
 * A hazardous ground type representing toxic waste.
 * <p>
 * Actors standing on toxic waste take damage each turn and may eventually die.
 * The ground is also flammable and can interact with fire-based mechanics.
 */
public class ToxicWaste extends Ground {
    private static final int TICK_DAMAGE = 1;

    /**
     * Constructs a Toxic Waste ground tile.
     */
    public ToxicWaste(){
        super('≈', "Toxic Waste");
        this.enableAbility(Ability.IS_FLAMMABLE);
    }

    /**
     * Applies toxic damage to any actor standing on this tile.
     * <p>
     * If the actor survives, a burn message is displayed. Otherwise,
     * a death message is shown.
     *
     * @param location the location of the toxic waste tile
     */
    @Override
    public void tick(Location location){
        Display display = new Display();
        if (location.containsAnActor()) {
            Actor actor = location.getActor();

            actor.hurt(TICK_DAMAGE);

            if (actor.isConscious()){
                display.println(actor + " is burned by toxic waste at (" +
                        location.x() + ", " + location.y() + ")");
            } else{
                display.println(actor + " succumbed to the toxic waste at (" +
                        location.x() + ", " + location.y() + ")");
            }
        }
    }

    /**
     * Determines whether actors can enter this tile.
     *
     * @param actor the actor attempting to enter
     * @return always true, as toxic waste does not block movement
     */
    @Override
    public boolean canActorEnter(Actor actor){
        return true;
    }
}
