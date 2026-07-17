package game.actors;

import edu.monash.fit2099.demo.forest.BasicInventory;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.ActorStatistics;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.StatisticOperations;
import edu.monash.fit2099.engine.weapons.IntrinsicWeapon;
import game.behaviours.AttackBehaviour;
import game.enums.Ability;
import game.interfaces.Infectable;
import game.interfaces.Spawnable;
import game.weapons.BareFist;
import game.behaviours.WanderBehaviour;

/**
 * Reanimated dead — an enemy that hunts workers with its bare fists.
 *
 * As a {@link Spawnable}, the moment an Undead is placed on the map its maximum HP
 * is permanently increased by 1 for every creature on an adjacent tile at that
 * instant. The Undead arrives correspondingly stronger.
 *
 * As an {@link Infectable}, being infected by a Parasite causes the Undead to
 * explode and die instantly — alien biology and reanimated dead do not mix.
 *
 * @author Rachel Chiew
 * @version 2.0
 */
public class Undead extends NPC implements Spawnable, Infectable {

    /**
     * Construct an Undead with 15 base HP, a bare-fist intrinsic weapon, and the
     * standard attack/wander behaviour stack.
     */
    public Undead() {
        super("Undead", 'Ѫ', 15, new BasicInventory());

        this.setIntrinsicWeapon(new BareFist());

        /** Behaviour priority for attacking adjacent targets. */
        int ATTACK_BEHAVIOUR_PRIORITY = 0;
        this.behaviours.put(ATTACK_BEHAVIOUR_PRIORITY, new AttackBehaviour());
        /** Behaviour priority for wandering. */
        int WANDER_BEHAVIOUR_PRIORITY = 1;
        this.behaviours.put(WANDER_BEHAVIOUR_PRIORITY, new WanderBehaviour());

        this.enableAbility(Ability.HOSTILE);
    }

    /**
     * Returns a fresh bare-fist intrinsic weapon.
     *
     * @return a new {@link BareFist} instance.
     */
    @Override
    public IntrinsicWeapon getIntrinsicWeapon() {
        return new BareFist();
    }

    /**
     * Environmental reaction triggered on spawn — count adjacent creatures and
     * permanently increase this Undead's maximum HP (and current HP) by that count.
     *
     * @param location the tile this Undead was just placed on.
     */
    @Override
    public void onSpawn(Location location) {
        int boost = 0;
        for (Exit exit : location.getExits()) {
            Location adjacent = exit.getDestination();
            if (adjacent.containsAnActor()) {
                boost++;
            }
        }
        if (boost > 0) {
            this.modifyStatisticMaximum(ActorStatistics.HEALTH, StatisticOperations.INCREASE, boost);
            this.modifyStatistic(ActorStatistics.HEALTH, StatisticOperations.INCREASE, boost);
            new Display().println("An Undead claws out of the ground, swollen by " + boost
                    + " nearby souls! (+" + boost + " Max HP)");
        }
    }

    @Override
    /**
     * Applies the infection effect to this Undead entity.
     *
     * The Undead rejects the infection due to incompatible biology, causing
     * it to immediately explode and die. Any remaining health is fully depleted,
     * and the entity is removed from the game map.
     *
     * @param source   the actor causing the infection
     * @param location the location of this entity in the game world
     */
    public void infect(Actor source, Location location) {
        new Display().println(this + " is rejected by the alien biology — it explodes and dies!");
        int currentHp = this.getStatistic(ActorStatistics.HEALTH);
        if (currentHp > 0) {
            this.hurt(currentHp);
        }
        if (location != null && location.map() != null) {
            location.map().removeActor(this);
        }
    }

    @Override
    /**
     * Performs per-turn infection behaviour.
     *
     * The Undead does not persist infection effects, as it dies immediately
     * upon infection.
     *
     * @param location the current location of the entity
     */
    public void tickInfection(Location location) {
        // Left empty, as the Undead dies immediately upon infection
    }

    @Override
    /**
     * Checks whether the infection is still active on this entity.
     *
     * @return always false because Undead do not retain infection status
     */
    public boolean isInfectionActive() {
        return false;
    }

    /**
     * @return a short label used when the Parasite logs its chosen target.
     */
    @Override
    public String getInfectionDescription() {
        return "an Undead";
    }

}