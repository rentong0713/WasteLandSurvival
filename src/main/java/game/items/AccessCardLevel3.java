package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.statistics.StatisticOperations;
import game.enums.*;

/**
 * A high-level access card providing clearance for Level 1, 2, and 3 areas.
 * Purchasing this card may involve hidden "predatory pricing" fees.
 *
 * @author Tan Jia Hern
 */
public class AccessCardLevel3 extends AccessCard {

    /**
     * Constructor.
     */
    public AccessCardLevel3() {
        super("Access Card (L3)", '◐', ClearanceLevel.LEVEL_3, 3);
        this.enableAbility(ClearanceLevel.LEVEL_1);
        this.enableAbility(ClearanceLevel.LEVEL_2);
        this.enableAbility(ClearanceLevel.LEVEL_3);
    }

    /**
     * @return the purchase price of the card.
     */
    @Override
    public int getBuyPrice() { return 3; }

    /**
     * @return a description of the card for the purchase menu.
     */
    @Override
    public String getBuyDescription() { return "Access Card (Level 3)"; }

    /**
     * Processes the purchase of a Level 3 Access Card.
     * There is a 50% chance of a hidden 50 credit fee being applied.
     *
     * @param actor The actor buying the card.
     * @param map   The map where the transaction occurs.
     * @return A string describing the transaction result.
     */
    @Override
    public String buy(Actor actor, GameMap map) {
        if (actor.getStatistic(WorkerStatistics.CREDITS) < getBuyPrice()) {
            return "Insufficient funds!";
        }

        actor.modifyStatistic(WorkerStatistics.CREDITS, StatisticOperations.DECREASE, getBuyPrice());
        String result = "Purchased Level 3 Card for 200 credits. ";

        if (Math.random() <= 0.5) {
            actor.modifyStatistic(WorkerStatistics.CREDITS, StatisticOperations.DECREASE, 50);
            result += "Predatory pricing applied a 50 credit hidden fee!";
        }

        actor.getInventory().add(new AccessCardLevel3());
        return result;
    }
}