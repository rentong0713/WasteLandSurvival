package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import edu.monash.fit2099.engine.statistics.StatisticOperations;
import game.enums.Ability;
import game.enums.ItemStatistics;
import game.enums.Status;
import game.enums.WorkerStatistics;
import game.interfaces.Depositable;
import game.interfaces.Sellable;
import game.status.PoisonStatus;
import game.utils.TeleportUtils;

/**
 * An item that can be cut to reveal an Alien Artifact.
 * Must be held in the inventory to be cut.
 *
 * @author Rachel Chiew
 * @version 1.0
 */
public class AlienArtifact extends Item implements Sellable, Depositable {

    /**
     * Constructs an Alien Artifact with its default weight and capabilities.
     */
    public AlienArtifact(){
        super("Alien Artifact", '?');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(1));
        this.enableAbility(Ability.SELLABLE);
        this.enableAbility(Ability.DEPOSITABLE);
        this.makePortable();
    }

    /**
     * Getters for the sell price
     * @return The selling price of the artifact
     */
    @Override
    public int getSellPrice(){
        return 200;
    }

    /**
     * Getters for the name
     * @return A description of the item for the sell menu
     */
    @Override
    public String getSellDescription(){
        return "Alien Artifact";
    }

    /**
     * Sells the artifact. There is a 50% chance the actor will be poisoned.
     *
     * @param actor the actor performing the sale.
     * @param map the map the actor is currently on.
     * @return A message describing the transaction and any poison status effect.
     */
    @Override
    public String sell(Actor actor, GameMap map) {
        return sell(actor, map, Math.random() <= 0.50);
    }

    /**
     * Internal selling logic allowing for forced poisoning.
     *
     * @param actor The actor selling the artifact
     * @param map The game map.
     * @param forcePoison True to simulate the poisoning effect
     * @return A message describing the result
     */
    public String sell(Actor actor, GameMap map, boolean forcePoison){
        actor.modifyStatistic(WorkerStatistics.CREDITS, StatisticOperations.INCREASE, getSellPrice());
        actor.getInventory().remove(this);

        String result = actor + " sold the Alien Artifact for 200 credits";

        if (forcePoison){
            actor.enableAbility(Status.POISON);
            actor.addStatus(new PoisonStatus(5, 1));
            result += " Mishandling the unstable artifact poison " + actor + "!";
        }

        return result;
    }

    /**
     * Getters for deposit value
     * @return The company credit value of the artifact
     */
    @Override
    public int getDepositValue(){
        return 100;
    }

    /**
     * Getters for name
     * @return A description of the item for the deposit menu.
     */
    @Override
    public String getDepositDescription(){
        return "Alien Artifact";
    }

    /**
     * Deposits the artifact into the terminal, teleporting the actor to a
     * random valid location on the map.
     *
     * @param actor The actor performing the deposit action.
     * @param map The game map where the deposit is occurring.
     * @return A message confirming the deposit and the teleportation.
     */
    @Override
    public String deposit(Actor actor, GameMap map) {
        return deposit(actor, map, true);
    }

    /**
     * Internal logic for deposit, allowing for forced teleportation scenarios
     *
     * @param actor The actor depositing the artifact.
     * @param map The game map.
     * @param forceTeleport Always true for standard gameplay.
     * @return A message describing the outcome.
     */
    public String deposit(Actor actor, GameMap map, boolean forceTeleport){
        actor.getInventory().remove(this);

        Location destination = TeleportUtils.getRandomLocation(map, actor);
        map.moveActor(actor, destination);

        return actor + " deposited the Artifact for 100 Company Credits and was instantly teleported away to resume work!";
    }
}
