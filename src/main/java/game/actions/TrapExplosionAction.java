package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.interfaces.TrapTriggerable;
import game.enums.Ability;

/**
 * An action that represents a localized $3 \times 3$ multi-tile tactical explosion.
 * It sweeps the surrounding area of an epicenter to polymorphically damage terrain
 * and actors or apply specific environmental and status debuffs depending on the source trap type.
 *
 * @author Tan Jia Hern
 * @version 1.0
 */
public class TrapExplosionAction extends Action {
    private final Item trapSource;
    private final Location explosionLoc;

    public TrapExplosionAction(Item trapSource, Location explosionLoc) {
        this.trapSource = trapSource;
        this.explosionLoc = explosionLoc;
    }

    @Override
    public String execute(Actor actor, GameMap map) {
        int centerX = explosionLoc.x();
        int centerY = explosionLoc.y();

        for (int xOffset = -1; xOffset <= 1; xOffset++) {
            for (int yOffset = -1; yOffset <= 1; yOffset++) {
                int targetX = centerX + xOffset;
                int targetY = centerY + yOffset;

                if (map.getXRange().contains(targetX) && map.getYRange().contains(targetY)) {
                    Location currentTile = map.at(targetX, targetY);

                    // 1. Structural terrain reaction (Replaced instanceof with Reflection)
                    if (TrapTriggerable.class.isAssignableFrom(currentTile.getGround().getClass())) {
                        ((TrapTriggerable) currentTile.getGround()).reactToTrap(this, currentTile);
                    }

                    // 2. Creature reaction
                    if (currentTile.containsAnActor()) {
                        Actor targetActor = currentTile.getActor();

                        if (TrapTriggerable.class.isAssignableFrom(targetActor.getClass())) {
                            ((TrapTriggerable) targetActor).reactToTrap(this, currentTile);
                        }

                        else if (targetActor.getClass().getSimpleName().equals("ContractedWorker")) {
                            game.actors.ContractedWorker worker = (game.actors.ContractedWorker) targetActor;

                            if (this.getTrapSource().hasAbility(game.enums.Status.POISON)) {
                                worker.hurt(5);
                                worker.addStatus(new game.status.PoisonStatus(5, 1));
                                worker.enableAbility(game.enums.Status.POISON);
                                System.out.println(worker + " inhales noxious chemical gases! (-5 HP, Poisoned)");
                            }
                            else if (this.getTrapSource().hasAbility(Ability.BURNING)) {
                                worker.hurt(10);
                                worker.addStatus(new game.status.BurnStatus(3, 2));
                                worker.enableAbility(game.enums.Status.BURN);
                                System.out.println(worker + " is scorched by a roaring fire blast! (-10 HP, Burned)");
                            }
                            else if (this.getTrapSource().hasAbility(game.enums.Status.STUN)) {
                                worker.addStatus(new game.status.StunStatus(2));
                                worker.enableAbility(game.enums.Status.STUN);
                                System.out.println(worker + " is completely paralyzed by a Stun Mine shockwave! (Stunned)");
                            }
                        }
                    }
                }
            }
        }

        explosionLoc.removeItem(trapSource);
        return trapSource + " detonated violently!";
    }

    @Override
    public String menuDescription(Actor actor) {
        return actor + " triggers " + trapSource;
    }

    public Item getTrapSource() {
        return this.trapSource;
    }
}