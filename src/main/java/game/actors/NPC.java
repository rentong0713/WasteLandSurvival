package game.actors;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.behaviours.Behaviour;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.items.Inventory;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;

// Mandatory project package imports to resolve "cannot find symbol" compilation errors
import game.actions.TrapExplosionAction;
import game.enums.Ability;
import game.items.FireMine;
import game.items.GasMine;
import game.items.StunMine;
import game.interfaces.TrapTriggerable;
import game.status.BurnStatus;
import game.status.PoisonStatus;
import game.status.StunStatus;

import java.util.Map;
import java.util.TreeMap;

/**
 * Base class for all non-playable characters. Automates turn execution using Behaviours.
 * By extending this class, specific NPCs can populate their behaviours map to define
 * their unique patterns (e.g., wandering, chasing, attacking).
 *
 * Auto-clean-up: at the start of every turn, an unconscious NPC removes itself from
 * the map via {@link Actor#unconscious(GameMap)} and reports its demise through a
 * {@link DeathReportAction}. This prevents "ghost turns" where a corpse continues
 * to execute behaviours and log misleading messages after dying from non-attack
 * sources (poison, fire, toxic waste) that don't call {@code unconscious} themselves.
 *
 * @author Rachel Chiew
 * @version 2.0
 */
public abstract class NPC extends Actor implements TrapTriggerable {
    // TreeMap ensures Behaviours are executed strictly in order of priority (0 is highest)
    protected final Map<Integer, Behaviour<Actor, Action>> behaviours = new TreeMap<>();

    /**
     * Constructor for the NPC.
     *
     * @param name        The name of the NPC.
     * @param displayChar The character that will represent the NPC on the map display.
     * @param hitPoints   The initial hit points for the NPC.
     * @param inventory   The NPC's starting inventory.
     */
    public NPC(String name, char displayChar, int hitPoints, Inventory inventory) {
        super(name, displayChar, hitPoints, inventory);
    }

    /**
     * Determines the action the NPC will take on its current turn.
     *
     * If the NPC is already unconscious when its turn starts (because it died from
     * a passive damage source like poison, fire, or toxic waste), it is removed
     * from the map immediately and a death message is returned. Otherwise, behaviours
     * are evaluated in priority order; the first one to produce a non-null action wins.
     *
     * @param actions    collection of possible Actions for this Actor
     * @param lastAction The Action this Actor took last turn
     * @param map        the map containing the Actor
     * @param display    the I/O object to which messages may be written
     * @return the action that the NPC will execute in the current turn
     */
    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        // Auto-clean-up: if we died from passive damage last turn, remove it.
        if (!this.isConscious()) {
            String message = this.unconscious(map);
            return new DeathReportAction(message);
        }

        if (this.hasAbility(game.enums.Status.STUN)) {
            return new edu.monash.fit2099.engine.actions.DoNothingAction();
        }

        // Iterate through behaviours by priority. Return the first valid action generated.
        for (Behaviour<Actor, Action> behaviour : behaviours.values()) {
            Action action = behaviour.operate(this, map.locationOf(this));
            if (action != null) {
                return action;
            }
        }

        return new DoNothingAction();
    }

    /**
     * Tiny Action used to carry the death-report string from {@code unconscious}
     * back to the engine's print step. Used in place of a {@code DoNothingAction}
     * after the NPC has already removed itself from the map.
     */
    private static class DeathReportAction extends Action {
        private final String message;

        DeathReportAction(String message) {
            this.message = message;
        }

        @Override
        public String execute(Actor actor, GameMap map) {
            return message;
        }

        @Override
        public String menuDescription(Actor actor) {
            return message;
        }
    }

    /**
     * Triggers dynamic response actions when intercepted by trap explosions.
     * Automatically applies custom ongoing status parameters across the entity structure.
     */
    @Override
    public void reactToTrap(TrapExplosionAction action, Location location) {
        // Replaced item instanceof with capability checks
        if (action.getTrapSource().hasAbility(Ability.BURNING)) {
            this.hurt(10);
            this.addStatus(new BurnStatus(3, 2));
            this.enableAbility(game.enums.Status.BURN);
            System.out.println(this + " is scorched by a roaring fire blast! (-10 HP, Burned)");
        }
        else if (action.getTrapSource().hasAbility(game.enums.Status.POISON)) {
            this.hurt(5);
            this.addStatus(new PoisonStatus(5, 1));
            this.enableAbility(game.enums.Status.POISON);
            System.out.println(this + " inhales noxious chemical gases! (-5 HP, Poisoned)");
        }
        else if (action.getTrapSource().hasAbility(game.enums.Status.STUN)) {
            this.addStatus(new StunStatus(2));
            this.enableAbility(game.enums.Status.STUN);
            System.out.println(this + " is completely paralyzed by a Stun Mine shockwave! (Stunned)");
        }
    }
}