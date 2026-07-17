package game.actors;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import game.behaviours.InfectBehaviour;
import game.enums.Ability;
import game.interfaces.Spawnable;
import game.inventory.BasicInventory;
import edu.monash.fit2099.demo.forest.WanderBehaviour;
import edu.monash.fit2099.engine.actions.Action;

/**
 * A parasite that infects something nearby and dies.
 * <p>
 * Has 30 HP. Each turn it looks at its neighbours and tries to infect one.
 * If it can't find a target, it just wanders around. When it spawns next to
 * a worker, it also deals 2 damage to that worker.
 *
 * @author Chai
 * @version 1.2
 */
public class Parasite extends NPC implements Spawnable {

    /** Higher priority — try to infect first. */
    private static final int INFECT_BEHAVIOUR_PRIORITY = 0;

    /** Lower priority — wander if nothing to infect. */
    private static final int WANDER_BEHAVIOUR_PRIORITY = 1;

    /** Damage dealt to each adjacent worker when this Parasite spawns. */
    private static final int SPAWN_DAMAGE = 2;

    /**
     * Make a new Parasite with full HP, the infection behaviour, and a wander fallback.
     */
    public Parasite() {
        super("Parasite", 'x', 30, new BasicInventory());

        this.enableAbility(Ability.HOSTILE);

        this.behaviours.put(INFECT_BEHAVIOUR_PRIORITY, new InfectBehaviour());
        this.behaviours.put(WANDER_BEHAVIOUR_PRIORITY, new WanderBehaviour());
    }

    @Override
    public void onSpawn(Location location) {
        Display display = new Display();
        for (Exit exit : location.getExits()) {
            Location adjacent = exit.getDestination();
            if (adjacent.containsAnActor()) {
                Actor neighbour = adjacent.getActor();
                if (neighbour.hasAbility(Ability.WORKER)) {
                    neighbour.hurt(SPAWN_DAMAGE);
                    display.println("A Parasite tears into reality next to " + neighbour
                            + "! (-" + SPAWN_DAMAGE + " HP)");
                }
            }
        }

        Action immediateInfect = new InfectBehaviour().operate(this, location);

        if (immediateInfect != null) {
            display.println(immediateInfect.execute(this, location.map()));
        }
    }

}