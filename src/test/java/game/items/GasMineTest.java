package game.items;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GasMineTest {
    private GasMine gasMine;

    @BeforeEach
    void setUp() {
        gasMine = new GasMine();
    }

    @Test
    void testInitialization() {
        // Verifies name assignment
        assertEquals("Gas Mine", gasMine.toString(), "Name should be correctly initialized as 'Gas Mine'");

        // Verifies display character assignment
        assertEquals('☁', gasMine.getDisplayChar(), "Display character should be '☁'");
    }
}