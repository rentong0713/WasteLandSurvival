package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.enums.Ability;
import game.enums.ItemStatistics;
import game.interfaces.Depositable;

/**
 * An item representing scrap metal recovered from facility doors.
 * Aluminium Scrap can be deposited into the Supercomputer
 * to contribute to the company quota, though handling it carries a risk of injury
 *
 * @author Rachel Chiew
 * @version 1.0
 */
public class AluminiumScrap extends Item implements Depositable {

    /**
     * Constructs AluminiumScrap with its default weight and portability settings.
     */
    public AluminiumScrap(){
        super("AluminiumScrap", '%');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(2));
        this.enableAbility(Ability.DEPOSITABLE);
        this.makePortable();
    }

    /**
     * Getters for the deposit value
     * @return The credit value of Aluminium Scrap
     */
    @Override
    public int getDepositValue(){
        return 50;
    }

    /**
     * Getters for the name
     * @return A description of the item.
     */
    @Override
    public String getDepositDescription(){
        return "Aluminium Scrap";
    }

    /**
     * Processes the deposit action for the item, applying a 20% chance of injury.
     *
     * @param actor The actor performing the deposit action.
     * @param map The game map where the deposit is occurring.
     * @return A description of the transaction and any injury sustained.
     */
    @Override
    public String deposit(Actor actor, GameMap map){
        return deposit(actor, map, Math.random() <= 0.20);
    }

    /**
     * Internal logic for the deposit, allowing for forced injury scenarios
     *
     * @param actor The actor performing the deposit
     * @param map The game map.
     * @param forceInjury True to simulate the injury effect
     * @return A message describing the deposit outcome.
     */
    public String deposit(Actor actor, GameMap map, boolean forceInjury){
        actor.getInventory().remove(this);
        String result = actor + " deposited Aluminium Scrap for 50 Company Credits.";

        if (forceInjury){
            actor.hurt(5);
            result += " Jagged metal sliced " + actor + " during the deposit, dealing 5 damage!";
        }

        return result;
    }
}
