package game.items;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FireMineTest {
    private FireMine fireMine;

    @BeforeEach
    void setUp() {
        fireMine = new FireMine();
    }

    @Test
    void testInitialization() {
        // Verifies name assignment
        assertEquals("Fire Mine", fireMine.toString(), "Name should be correctly initialized as 'Fire Mine'");

        // Verifies display character assignment
        assertEquals('☼', fireMine.getDisplayChar(), "Display character should be '☼'");
    }
}