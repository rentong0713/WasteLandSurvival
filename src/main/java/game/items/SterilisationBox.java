package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.enums.*;
import game.interfaces.Buyable;
import edu.monash.fit2099.engine.statistics.StatisticOperations;

/**
 * A portable box that sterilises consumables.
 * <p>
 * Carrying this item gives the actor the STERILISE ability, so they can safely
 * eat or drink things that would otherwise hurt them (toxic apples, cookies,
 * puddles).
 * <p>
 * Costs 750 credits to buy at the Supercomputer. When bought, the box's
 * radiation immediately erases one random item from the buyer's inventory.
 *
 * @author Tan Jia Hern
 * @version 1.0
 */
public class SterilisationBox extends Item implements Buyable {

    /**
     * Make a new Sterilisation Box. Weighs 7, portable, and gives the carrier
     * the STERILISE ability.
     */
    public SterilisationBox() {
        super("Sterilisation Box", '▣');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(7));
        this.enableAbility(Ability.STERILISE);
        this.makePortable();
    }

    /**
     * @return the price in credits (currently 1 for testing; spec value is 750).
     */
    @Override
    public int getBuyPrice() { return 1; } //750

    /**
     * @return the label shown by BuyAction in the menu.
     */
    @Override
    public String getBuyDescription() { return "Sterilisation Box"; }

    /**
     * Buy the box from the Supercomputer. Pays the price, adds a new box to the
     * buyer's inventory, then deletes one random item from the inventory because
     * of the box's radiation.
     *
     * @param actor the buyer.
     * @param map   the buyer's map.
     * @return what happened.
     */
    @Override
    public String buy(Actor actor, GameMap map) {
        int credits = actor.getStatistic(WorkerStatistics.CREDITS);
        if (credits < getBuyPrice()) return "Insufficient funds!";

        actor.modifyStatistic(WorkerStatistics.CREDITS, StatisticOperations.DECREASE, getBuyPrice());
        actor.getInventory().add(new SterilisationBox());

        String result = "Purchased Sterilisation Box.";

        // Erase a random item due to radiation
        java.util.List<Item> items = new java.util.ArrayList<>(actor.getInventory().getItems());
        if (!items.isEmpty()) {
            Item toErase = items.get(new java.util.Random().nextInt(items.size()));
            actor.getInventory().remove(toErase);
            result += " Radiation erased your " + toErase + "!";
        }
        return result;
    }

}