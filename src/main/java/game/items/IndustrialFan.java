package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import edu.monash.fit2099.engine.statistics.StatisticOperations;
import game.enums.Ability;
import game.enums.ItemStatistics;
import game.enums.WorkerStatistics;
import game.interfaces.Depositable;
import game.interfaces.Sellable;
import game.systems.SlimeSpawner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * An industrial cooling component salvaged from the facility.
 * It can be sold for personal credits (triggering a Slime spawn) or
 * deposited into the Supercomputer to contribute to the company quota.
 */
public class IndustrialFan extends Item implements Sellable, Depositable {
    private final Random random = new Random();

    /**
     * Constructs an Industrial Fan with its default weight and capabilities.
     */
    public IndustrialFan(){
        super("Industrial Fan", '@');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(5));
        this.enableAbility(Ability.DEPOSITABLE);
        this.enableAbility(Ability.SELLABLE);
        this.makePortable();
    }

    /**
     * Getters for the selling price
     * @return The selling price of the fan
     */
    @Override
    public int getSellPrice(){
        return 150;
    }

    /**
     * Getters for the name
     * @return A description of the item for the sell menu.
     */
    @Override
    public String getSellDescription(){
        return "Industrial Fan";
    }

    /**
     * Sells the fan for personal credits. Triggers a Slime spawn in an adjacent tile.
     *
     * @param actor the actor performing the sale.
     * @param map the map the actor is currently on.
     * @return A message describing the transaction and the resulting hazard
     */
    @Override
    public String sell(Actor actor, GameMap map){
        Location location = map.locationOf(actor);

        actor.modifyStatistic(WorkerStatistics.CREDITS, StatisticOperations.INCREASE, getSellPrice());
        actor.getInventory().remove(this);

        String result = actor + " sold the Industrial Fan for 150 personal credits.";

        // Find an empty adjacent tile
        List<Location> emptyAdjacents = new ArrayList<>();
        for (Exit exit : location.getExits()) {
            Location dest = exit.getDestination();
            if (!dest.containsAnActor()) {
                emptyAdjacents.add(dest);
            }
        }

        // If there's room, pick a random tile and delegate to the Spawner
        if (!emptyAdjacents.isEmpty()) {
            Location spawnSpot = emptyAdjacents.get(random.nextInt(emptyAdjacents.size()));

            if (new SlimeSpawner().spawn(spawnSpot)){
                result += " A Slime spawned nearby!";
            }
        }

        return result;

    }

    /**
     * Getters for deposit value
     * @return The company credit value of the fan
     */
    @Override
    public int getDepositValue(){
        return 10;
    }

    /**
     * Getters for the name
     * @return A description of the item for the deposit menu
     */
    @Override
    public String getDepositDescription(){
        return "Industrial Fan";
    }

    /**
     * Deposits the fan for company credits and triggers a healing effect
     *
     * @param actor The actor performing the deposit action.
     * @param map The game map where the deposit is occurring.
     * @return A message confirming the deposit and the healing effect.
     */
    @Override
    public String deposit(Actor actor, GameMap map) {
        return deposit(actor, true);
    }

    /**
     * Internal logic for deposit, allowing for the healing bonus.
     *
     * @param actor The actor performing the deposit.
     * @param forceHeal True to apply the 10 HP heal.
     * @return A message describing the outcome.
     */
    public String deposit(Actor actor, boolean forceHeal){
        if (!actor.getInventory().getItems().contains(this)){
            return "Item not in inventory.";
        }
        actor.getInventory().remove(this);
        if (forceHeal) {
            actor.heal(10);
        }
        return actor + " deposited the Industrial Fan for 10 Company Credits. The ventilation override blasted fresh oxygen, healing them for 10 HP!";
    }
}
