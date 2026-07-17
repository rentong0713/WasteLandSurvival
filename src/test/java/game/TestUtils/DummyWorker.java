package game.TestUtils;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.inventory.BasicInventory;
import game.enums.Ability;

/**
 * AI Usage Acknowledgment:
 * I utilized Gemini to assist in structuring this test utility.
 * The tool helped draft the minimal implementation of the Actor class required for
 * unit testing. I manually modified the code to ensure it integrated correctly with my
 * project's custom BasicInventory and Ability enums, and to ensure
 * the {@code playTurn} method returned a {@code DoNothingAction} to prevent test interference.
 *
 * A helper class used for unit testing that provides a standard Worker actor
 * with baseline stats and abilities.
 * @author Rachel Chiew
 * @version 1.0
 */
public class DummyWorker extends Actor {

    /**
     * Constructs a standard DummyWorker with 100 health and the WORKER ability.
     */
    public DummyWorker() {
        super("Test Worker", 'W', 100, new BasicInventory());
        this.enableAbility(Ability.WORKER);
    }

    /**
     * Overridden playTurn to ensure the dummy worker performs no actions during tests.
     *
     * @param actions collection of possible Actions for this Actor
     * @param lastAction The Action this Actor took last turn. Can do
     * interesting things in conjunction with Action.getNextAction()
     * @param map the map containing the Actor
     * @param display the I/O object to which messages may be written
     * @return A DoNothingAction
     */
    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, edu.monash.fit2099.engine.displays.Display display) {
        return new edu.monash.fit2099.engine.actions.DoNothingAction();
    }
}