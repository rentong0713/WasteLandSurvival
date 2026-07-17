package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import edu.monash.fit2099.engine.statistics.StatisticOperations;
import game.grounds.Fire;
import game.interfaces.Ignitable;
import game.interfaces.Sellable;
import game.enums.*;

/**
 * A heavy piece of hardware that can be sold for credits.
 * Selling it has a chance to either heal the user or cause a dangerous electrical fire.
 *
 * @author Tan Jia Hern
 */
public class CRTMonitor extends Item implements Sellable {

    /**
     * Constructor.
     */
    public CRTMonitor() {
        super("CRT Monitor", '◙');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(30));
        this.makePortable();
        this.enableAbility(Ability.SELLABLE);
    }

    /**
     * @return the sale price of the CRT Monitor.
     */
    @Override
    public int getSellPrice() { return 25; }

    /**
     * @return a description of the item for the sale menu.
     */
    @Override
    public String getSellDescription() { return "CRT Monitor"; }

    /**
     * Processes the sale of the CRT Monitor.
     * Has a 20% chance to explode (dealing damage and spawning fire)
     * and an 80% chance to heal the actor.
     *
     * @param actor The actor selling the item.
     * @param map   The map the actor is currently on.
     * @return A string describing the outcome of the sale.
     */
    @Override
    public String sell(Actor actor, GameMap map) {
        actor.modifyStatistic(WorkerStatistics.CREDITS, StatisticOperations.INCREASE, getSellPrice());
        actor.getInventory().remove(this);

        String result = "Sold CRT Monitor for 25 credits. ";

        if (Math.random() <= 0.20) {
            actor.hurt(2);
            for (Exit exit : map.locationOf(actor).getExits()) {
                Location dest = exit.getDestination();
                Ground ori_ground = dest.getGround();
                Ignitable fireGround = dest.getGroundAs(Ignitable.class);
                if (fireGround != null) {
                    fireGround.ignite(6);
                } else {
                    dest.setGround(new Fire(ori_ground));
                }
            }
            result += "The hardware shorted out, dealing 2 DMG and spawning fire!";
        } else {
            actor.heal(5);
            result += "Relief of the weight healed you for 5 HP!";
        }
        return result;
    }
}