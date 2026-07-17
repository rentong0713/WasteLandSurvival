package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.enums.*;
import game.interfaces.Buyable;

/**
 * Abstract base class for all Access Cards in the game.
 * Handles common functionality like weight assignment, portability,
 * and the baseline ability to unlock doors based on clearance levels.
 *
 * @author Tan Jia Hern
 */
public abstract class AccessCard extends Item implements Buyable {

    private final ClearanceLevel level;

    /**
     * Constructor for the abstract AccessCard.
     *
     * @param name        The specific name of the card.
     * @param displayChar The character representing the card on the map.
     * @param level       The clearance level associated with this card.
     * @param weight      The weight of the card.
     */
    public AccessCard(String name, char displayChar, ClearanceLevel level, int weight) {
        super(name, displayChar);
        this.level = level;
        this.enableAbility(Ability.ACCESS_CARD_PERMISSION);

        // All access cards have a weight
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(weight));

        // All access cards can be picked up
        this.makePortable();

        // Enable the specific clearance level capability
        this.enableAbility(level);
    }

    /**
     * @return the ClearanceLevel of this card.
     */
    public ClearanceLevel getLevel(){
        return this.level;
    }
}