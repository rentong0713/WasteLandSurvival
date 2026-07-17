package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.statistics.StatisticOperations;
import game.enums.*;

/**
 * A mid-level access card providing clearance for Level 1 and 2 areas.
 * Purchasing this card requires a biological sacrifice (blood sample).
 *
 * @author Tan Jia Hern
 */
public class AccessCardLevel2 extends AccessCard {

    /**
     * Constructor.
     */
    public AccessCardLevel2() {
        super("Access Card (L2)", 'α', ClearanceLevel.LEVEL_2, 2);
        this.enableAbility(ClearanceLevel.LEVEL_1);
        this.enableAbility(ClearanceLevel.LEVEL_2);
    }

    /**
     * @return the purchase price of the card.
     */
    @Override
    public int getBuyPrice() { return 2; }

    /**
     * @return a description of the card for the purchase menu.
     */
    @Override
    public String getBuyDescription() { return "Access Card (Level 2)"; }

    /**
     * Processes the purchase of a Level 2 Access Card.
     * The terminal extracts blood, dealing damage to the actor.
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
        actor.hurt(1);
        actor.getInventory().add(new AccessCardLevel2());

        return "Purchased Level 2 Card. The terminal extracted a blood sample, dealing 5 damage!";
    }
}