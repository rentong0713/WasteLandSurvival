package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.statistics.StatisticOperations;
import game.enums.WorkerStatistics;
import game.enums.*;

/**
 * A basic access card providing clearance for Level 1 areas.
 *
 * @author Tan Jia Hern
 */
public class AccessCardLevel1 extends AccessCard {

    /**
     * Constructor.
     */
    public AccessCardLevel1() {
        super("Access Card (L1)", '▤', ClearanceLevel.LEVEL_1, 1);
        this.enableAbility(ClearanceLevel.LEVEL_1);
    }

    /**
     * @return the purchase price of the card.
     */
    @Override
    public int getBuyPrice() { return 1; }

    /**
     * @return a description of the card for the purchase menu.
     */
    @Override
    public String getBuyDescription() { return "Access Card (Level 1)"; }

    /**
     * Processes the purchase of a Level 1 Access Card.
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
        actor.getInventory().add(new AccessCardLevel1());
        return "Purchased Level 1 Access Card for 50 credits.";
    }
}