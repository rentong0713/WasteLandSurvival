package game.factories;

import edu.monash.fit2099.engine.items.Item;
import game.enums.Ability;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the ResourceFactory class.
 *
 * This test suite verifies that the factory correctly generates valid resource items.
 * It ensures that all produced items are non-null and are marked as DEPOSITABLE,
 * confirming they can be used by hoarding and collection systems in the game.
 *
 * The test focuses on:
 * - Successful instantiation of resource items
 * - Correct assignment of the DEPOSITABLE ability to generated items
 */
class ResourceFactoryTest {

    /**
     * Tests that createRandomResource returns a valid item with the DEPOSITABLE ability.
     *
     * This ensures that:
     * - The factory produces non-null items
     * - Generated items can be correctly identified as depositable resources
     */
    @Test
    void testCreateRandomResourceReturnsValidItem() {
        ResourceFactory factory = new ResourceFactory();

        Item generatedItem = factory.createRandomResource();

        assertNotNull(generatedItem, "Factory should successfully instantiate an item.");
        assertTrue(generatedItem.hasAbility(Ability.DEPOSITABLE));
    }
}