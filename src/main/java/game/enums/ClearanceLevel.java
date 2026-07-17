package game.enums;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;

/**
 * Security Clearance Levels for Doors and Access Cards.
 */

public enum ClearanceLevel {
    NONE(0),
    LEVEL_1(1),
    LEVEL_2(2),
    LEVEL_3(3);

    private final int value;

    ClearanceLevel(int value){
        this.value = value;
    }

    public boolean hasClearance(ClearanceLevel required){
        return this.value >= required.value;
    }

    public static ClearanceLevel getHighestClearance(Actor actor){
        ClearanceLevel highest = NONE;

        for (Item item : actor.getInventory().getItems()){
            if (item.hasAbility(Ability.ACCESS_CARD_PERMISSION)){
                for (ClearanceLevel level : ClearanceLevel.values()) {
                    if (actor.hasAbility(level) && level.value > highest.value) {
                        highest = level;
                    }
                }
            }
        }

        return highest;
    }
}
