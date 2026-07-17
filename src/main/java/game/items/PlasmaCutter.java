package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import edu.monash.fit2099.engine.statistics.StatisticOperations;
import game.enums.Ability;
import game.enums.ItemStatistics;
import game.enums.Status;
import game.enums.WorkerStatistics;
import game.interfaces.Buyable;
import game.status.BurnStatus;

/**
 * A specialized tool used to dismantle facility structures like Doors, Vents, and Alien Cubes.
 *
 * @author Rachel Chiew
 * @version 1.0
 */
public class PlasmaCutter extends Item implements Buyable {

    /**
     * Constructs a Plasma Cutter with a weight of 7 and the CUT capability.
     */
    public PlasmaCutter(){
        super("Plasma Cutter" , '>');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(7));
        this.enableAbility(Ability.CUT);
    }

    /**
     * Getters for buying price
     * @return The cost to purchase the Plasma Cutter.
     */
    @Override
    public int getBuyPrice(){
        return 50;
    }

    /**
     * Getters for the name
     * @return A description of the item for the store menu
     */
    @Override
    public String getBuyDescription(){
        return "Plasma Cutter";
    }

    /**
     * Purchases the item for the actor. Deducts credits, adds the item to inventory,
     * and applies the burn status and initial damage.
     *
     * @param actor the actor performing the purchase.
     * @param map the map the actor is currently on.
     * @return A message describing the purchase and the resulting injury.
     */
    @Override
    public String buy(Actor actor, GameMap map){
        if (actor.getStatistic(WorkerStatistics.CREDITS) < getBuyPrice()){
            return actor + " lacks sufficient credits to purchase the Plasma Cutter!";
        }

        actor.modifyStatistic(WorkerStatistics.CREDITS, StatisticOperations.DECREASE, getBuyPrice());
        actor.getInventory().add(new PlasmaCutter());

        actor.enableAbility(Status.BURN);
        actor.addStatus(new BurnStatus(5, 1));

        return actor + " purchased a Plasma Cutter! It ejects from the chute white-hot, dealing 5 damage and burning them!";
    }
}
