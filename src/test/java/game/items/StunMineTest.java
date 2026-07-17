package game.items;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StunMineTest {
    private StunMine stunMine;

    @BeforeEach
    void setUp() {
        stunMine = new StunMine();
    }

    @Test
    void testInitialization() {
        // Verifies name assignment
        assertEquals("Stun Mine", stunMine.toString(), "Name should be correctly initialized as 'Stun Mine'");

        // Verifies display character assignment
        assertEquals('⚡', stunMine.getDisplayChar(), "Display character should be '⚡'");
    }
}