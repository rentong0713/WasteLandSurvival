package game.behaviours;

import edu.monash.fit2099.engine.behaviours.Behaviour;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;

import java.util.Random;

/**
 * A behaviour that allows a ground entity to grow into another stage
 * after a specified number of turns and based on a probability chance.
 *
 * @author Low Ren Tong
 * @version 1.0
 */
public class GrowBehaviour implements Behaviour<Ground, Boolean> {
    private int turnCount = 0;
    private final int requiredTurns;
    private final int chance;
    private final Ground nextStage;
    private final Random random = new Random();

    /**
     * Constructs a grow behaviour.
     *
     * @param requiredTurns the number of turns required before attempting growth
     * @param chance the percentage chance (0–100) that growth will occur
     * @param nextStage the ground that replaces the current stage after growth
     */
    public GrowBehaviour(int requiredTurns, int chance, Ground nextStage) {
        this.requiredTurns = requiredTurns;
        this.chance = chance;
        this.nextStage = nextStage;
    }

    /**
     * Performs the growth operation for the ground entity.
     *
     * After the required number of turns has passed, the behaviour attempts
     * to grow the entity into the next stage based on the configured chance.
     *
     * @param entity the ground entity performing the behaviour
     * @param location the location of the ground entity
     * @return {@code true} if growth occurred and the action was consumed,
     *         otherwise {@code false}
     */
    @Override
    public Boolean operate(Ground entity, Location location) {
        turnCount++;
        if (turnCount >= requiredTurns) {
            turnCount = 0; // Reset counter
            if (random.nextInt(100) < chance) {
                location.setGround(nextStage);
                return true; // Growth occurred! Action consumed.
            }
        }
        return false; // Did not grow, allow other actions.
    }
}