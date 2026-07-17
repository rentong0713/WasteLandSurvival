package game.actors;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.displays.Menu;
import edu.monash.fit2099.engine.items.Inventory;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import edu.monash.fit2099.engine.statistics.StatisticOperations;
import game.enums.Ability;
import game.enums.Status;
import game.enums.WorkerStatistics;
import game.interfaces.Infectable;
import game.status.InfectStatus;
import game.utils.SpawnUtils;
import game.systems.*;

/**
 * The player-controlled worker capable of performing survival tasks such as
 * interacting with the environment, managing resources, and exploring the map.
 *
 * <p>
 * Each worker starts with a credit balance tracked via {@link WorkerStatistics#CREDITS}.
 * </p>
 *
 * <p>
 * As an {@link Infectable}, infection transforms the worker into a living hive,
 * dealing damage over time and spawning Parasites periodically.
 * </p>
 *
 * @author Low Ren Tong
 * @version 3.0
 */
public class ContractedWorker extends Actor implements Infectable {

    private boolean isInfected = false;
    private int infectionTickCounter = 0;

    /**
     * Constructs a ContractedWorker with a name, display character, health,
     * and initial inventory.
     *
     * @param name       the name of the worker
     * @param displayChar the character used to represent the worker on the map
     * @param hitPoints  the starting health of the worker
     * @param inventory  the worker's starting inventory
     */
    public ContractedWorker(String name, char displayChar, int hitPoints, Inventory inventory) {
        super(name, displayChar, hitPoints, inventory);

        this.enableAbility(Status.HOSTILE_TO_ENEMY);
        this.enableAbility(Ability.WORKER);

        this.addNewStatistic(WorkerStatistics.CREDITS, new BaseStatistic(1000));
        this.modifyStatistic(WorkerStatistics.CREDITS, StatisticOperations.UPDATE, 0);
    }

    /**
     * Executes the worker's turn, including status updates, UI display,
     * and action selection.
     *
     * @param actions    available actions for this turn
     * @param lastAction the last action performed
     * @param map        the game map
     * @param display    the game display
     * @return the chosen action for this turn
     */
    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        if (!this.isConscious()) {
            this.unconscious(map);
            return new DoNothingAction();
        }

        // 🚨 ADDED: STUN STATUS CHECK INTERCEPTOR 🚨
        // If the worker is stunned, bypass the menu entirely and force them to skip their turn!
        if (this.hasAbility(Status.STUN)) {
            display.println(this + " is completely paralyzed and cannot move!");
            return new DoNothingAction();
        }

        int currentCredits = this.getStatistic(WorkerStatistics.CREDITS);
        display.println(String.format("💰 Current Balance: %d credits", currentCredits));

        if (lastAction != null && lastAction.getNextAction() != null)
            return lastAction.getNextAction();

        Menu menu = new Menu(actions);
        return menu.showMenu(this, display);
    }

    /**
     * Applies infection to the worker, turning them into a host for Parasites.
     *
     * @param source   the actor causing the infection
     * @param location the worker's current location
     */
    @Override
    public void infect(Actor source, Location location) {
        if (this.hasAbility(Ability.INFECTED)) {
            new Display().println(this + " is already infected — the new parasite finds no foothold.");
            return;
        }

        this.isInfected = true;
        this.enableAbility(Ability.INFECTED);
        this.addStatus(new InfectStatus(this));

        new Display().println(this + " is infected! Their body becomes a living hive.");
    }

    /**
     * Executes per-turn infection effects on the worker.
     * <p>
     * The worker takes damage each turn and periodically spawns Parasites.
     * </p>
     *
     * @param location the worker's current location
     */
    @Override
    public void tickInfection(Location location) {
        if (!this.isConscious()) {
            this.isInfected = false;
            return;
        }

        this.hurt(1);
        new Display().println(this + " writhes as the alien hive grows inside them. (-1 HP)");

        infectionTickCounter++;

        if (infectionTickCounter % 5 == 0) {
            Parasite parasite = new Parasite();
            Location spawnSpot = SpawnUtils.pickRandom(
                    SpawnUtils.adjacentEmptyLocationsFor(location, parasite)
            );

            if (spawnSpot != null && new ParasiteSpawner().spawn(spawnSpot)) {
                new Display().println("The hive bursts! A Parasite erupts from " + this + "'s body.");
            }
        }
    }

    /**
     * Checks whether the worker's infection is still active.
     *
     * @return true if infected and still conscious, false otherwise
     */
    @Override
    public boolean isInfectionActive() {
        return this.isInfected && this.isConscious();
    }

    /**
     * Returns a human-readable description of this infected entity.
     *
     * @return string representation of the worker
     */
    @Override
    public String getInfectionDescription() {
        return this.toString();
    }
}