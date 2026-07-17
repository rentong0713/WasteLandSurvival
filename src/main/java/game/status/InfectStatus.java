package game.status;

import edu.monash.fit2099.engine.GameEntity;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.positions.Location;
import game.interfaces.Infectable;

/**
 * A shared status representing an active infection on a game entity.
 *
 * This status delegates infection behavior to the underlying Infectable
 * implementation of the host entity.
 *
 * @author Ren Tong Low
 * @version 1.0
 */
public class InfectStatus implements Status {

    private final Infectable host;

    /**
     * Constructs an InfectStatus for the given Infectable host.
     *
     * @param host the entity that can be infected and will handle infection logic
     */
    public InfectStatus(Infectable host) {
        this.host = host;
    }

    /**
     * Executes the infection logic for the host each game tick.
     *
     * @param currEntity the entity currently holding this status
     * @param location   the location of the entity in the game map
     */
    @Override
    public void tickStatus(GameEntity currEntity, Location location) {
        host.tickInfection(location);
    }

    /**
     * Checks whether the infection status is still active.
     *
     * @return true if the host is still infected, false otherwise
     */
    @Override
    public boolean isStatusActive() {
        return host.isInfectionActive();
    }
}