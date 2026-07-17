package game.actors;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.weapons.IntrinsicWeapon;
import game.enums.Ability;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the ScrapSnatcher class.
 *
 * This test suite verifies the full lifecycle and core mechanics of the ScrapSnatcher entity,
 * including spawning effects, infection system, combat state changes, and health behavior.
 *
 * The tests cover:
 * - Loot explosion behavior upon spawning
 * - Infection status application and lifecycle tracking
 * - Infection damage over time via tick system
 * - Weapon transformation after infection
 * - Base health validation
 * - Behaviour changes before and after infection
 * - Safe handling of repeated infection calls
 * - Infection cleanup upon death
 * - Full end-to-end lifecycle transition under sustained infection
 *
 * @author Ren Tong Low
 * @version 1.0
 */
class ScrapSnatcherTest {

    private ScrapSnatcher snatcher;
    private Location mockLocation;
    private Actor mockParasite;

    @BeforeEach
    void setUp() {
        snatcher = new ScrapSnatcher();
        mockLocation = mock(Location.class);
        mockParasite = mock(Actor.class);
    }

    /**
     * Tests that ScrapSnatcher generates loot upon spawning.
     *
     * This ensures that:
     * - Adjacent locations are detected correctly
     * - Items are dropped into nearby tiles on spawn
     */
    @Test
    void testLootExplosionOnSpawn() {
        Exit mockExit = mock(Exit.class);
        Location mockAdjacentLocation = mock(Location.class);

        when(mockLocation.getExits()).thenReturn(List.of(mockExit));
        when(mockExit.getDestination()).thenReturn(mockAdjacentLocation);

        snatcher.onSpawn(mockLocation);

        verify(mockAdjacentLocation, times(1)).addItem(any(Item.class));
    }

    /**
     * Tests that infection correctly applies status and ability flags.
     *
     * This ensures that:
     * - The INFECTED ability is added
     * - Infection tracking is activated
     */
    @Test
    void testInfectAppliesStatusAndAbility() {
        snatcher.infect(mockParasite, mockLocation);

        assertTrue(snatcher.hasAbility(Ability.INFECTED), "Snatcher must gain INFECTED ability.");
        assertTrue(snatcher.isInfectionActive(), "Infection tracking must be active.");
    }

    /**
     * Tests that infection tick applies damage over time.
     *
     * This ensures that:
     * - Infection causes periodic damage
     * - The actor remains conscious after initial ticks if health permits
     */
    @Test
    void testTickInfectionAppliesDamage() {
        snatcher.infect(mockParasite, mockLocation);

        snatcher.tickInfection(mockLocation);

        assertTrue(snatcher.isConscious(), "Snatcher should survive the first 1-damage tick.");
    }

    /**
     * Tests that the intrinsic weapon changes after infection.
     *
     * This ensures that:
     * - The default weapon is replaced upon infection
     * - A new rabid weapon instance is created
     */
    @Test
    void testRabidWeaponEquippedOnInfection() {
        IntrinsicWeapon defaultWeapon = snatcher.getIntrinsicWeapon();

        snatcher.infect(mockParasite, mockLocation);
        IntrinsicWeapon rabidWeapon = snatcher.getIntrinsicWeapon();

        assertNotNull(rabidWeapon, "Rabid weapon must not be null after infection.");
        assertNotSame(defaultWeapon, rabidWeapon,
                "The Snatcher should switch to a brand new IntrinsicWeapon when infected.");
    }

    /**
     * Tests that ScrapSnatcher has exactly 25 base health.
     *
     * This ensures correct balancing of damage and survival thresholds.
     */
    @Test
    void testBaseHealthIsExactly25() {
        snatcher.hurt(24);
        assertTrue(snatcher.isConscious(), "Snatcher should survive 24 damage because its base health is 25.");

        snatcher.hurt(1);
        assertFalse(snatcher.isConscious(), "Snatcher should die exactly at 25 damage.");
    }

    /**
     * Tests correct behavior transitions before and after infection.
     *
     * This ensures:
     * - Pre-infection state is normal
     * - Post-infection state correctly updates abilities and tracking
     */
    @Test
    void testBeforeAndAfterInfectionBehaviors() {
        assertFalse(snatcher.hasAbility(Ability.INFECTED), "Before infection: Snatcher should be normal.");
        assertFalse(snatcher.isInfectionActive(), "Before infection: Tracking should be false.");

        snatcher.infect(mockParasite, mockLocation);
        snatcher.tickInfection(mockLocation);

        assertTrue(snatcher.hasAbility(Ability.INFECTED), "After infection: Snatcher must be infected.");
        assertTrue(snatcher.isInfectionActive(), "After infection: Tracking must be active.");
    }

    /**
     * Tests that multiple infection attempts do not cause instability.
     *
     * This ensures:
     * - Re-infecting an already infected actor is safe
     * - Infection state remains consistent
     */
    @Test
    void testInfectTwiceDoesNotDuplicate() {
        snatcher.infect(mockParasite, mockLocation);
        snatcher.infect(mockParasite, mockLocation); // Hit it again

        assertTrue(snatcher.isInfectionActive(), "Snatcher should remain infected.");
    }

    /**
     * Tests that infection stops when the actor is unconscious.
     *
     * This ensures:
     * - Infection is deactivated upon death
     * - No further infection processing occurs
     */
    @Test
    void testTickInfectionWhenUnconscious() {
        snatcher.infect(mockParasite, mockLocation);
        snatcher.hurt(999); // Instantly kill it

        snatcher.tickInfection(mockLocation);

        assertFalse(snatcher.isInfectionActive(), "Infection should deactivate when the host dies.");
    }

    /**
     * Tests full lifecycle transition under infection.
     *
     * This ensures:
     * - Behaviour transitions correctly (Hoard -> Attack)
     * - Infection damage is applied consistently over time
     * - Actor eventually dies after cumulative damage
     */
    @Test
    void testFullEcosystemLifecycleStateTransition() {
        assertFalse(snatcher.hasAbility(Ability.INFECTED), "Initial State: Should not be rabid.");
        assertNotNull(snatcher.behaviours.get(ScrapSnatcher.HOARD_BEHAVIOUR_PRIORITY),
                "Initial State: Must possess HoardBehaviour.");

        snatcher.infect(mockParasite, mockLocation);
        assertTrue(snatcher.hasAbility(Ability.INFECTED), "Transition State: Must register INFECTED ability.");

        snatcher.tickInfection(mockLocation);

        assertNull(snatcher.behaviours.get(ScrapSnatcher.HOARD_BEHAVIOUR_PRIORITY),
                "Post-Infection: HoardBehaviour must be permanently stripped.");
        assertNotNull(snatcher.behaviours.get(0),
                "Post-Infection: Highest priority (0) must now hold AttackBehaviour.");

        for (int i = 0; i < 23; i++) {
            snatcher.tickInfection(mockLocation);
        }
        assertTrue(snatcher.isConscious(), "Snatcher should cling to life at exactly 1 remaining HP.");

        snatcher.tickInfection(mockLocation);
        assertFalse(snatcher.isConscious(), "Snatcher must perish after taking 25 units of cumulative status damage.");
    }
}