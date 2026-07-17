package game.grounds;

import edu.monash.fit2099.engine.positions.Ground;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link IceTile}.
 */
public class IceTileTest {

    private IceTile iceTile;

    @BeforeEach
    void setUp() {
        Ground originalGround = new Floor();
        iceTile = new IceTile(originalGround);
    }

    @Test
    void displayChar_isStar() {
        assertEquals('*', iceTile.getDisplayChar());
    }

    @Test
    void name_isIceTile() {
        assertEquals("Ice Tile", iceTile.toString());
    }

    @Test
    void canActorEnter_returnsTrue() {
        assertTrue(iceTile.canActorEnter(null));
    }

}
