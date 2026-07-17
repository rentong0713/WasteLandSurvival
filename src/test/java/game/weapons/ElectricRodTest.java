package game.weapons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link ElectricRod}.
 */
public class ElectricRodTest {

    private ElectricRod rod;

    @BeforeEach
    void setUp() {
        rod = new ElectricRod();
    }

    @Test
    void damage_is5() {
        assertEquals(5, rod.getDamage());
    }

    @Test
    void buyPrice_is150() {
        assertEquals(150, rod.getBuyPrice());
    }

    @Test
    void buyDescription_isElectricRod() {
        assertEquals("Electric Rod", rod.getBuyDescription());
    }

    @Test
    void weaponDescription_isElectricRod() {
        assertEquals("Electric Rod", rod.getWeaponDescription());
    }

    @Test
    void weaponEffect_isChainLightningEffect() {
        assertEquals(ChainLightningEffect.class, rod.getWeaponEffect().getClass());
    }

    @Test
    void notOnCooldown_initially() {
        assertFalse(rod.isOnCooldown());
    }

    @Test
    void notOnCooldown_after1Use() {
        rod.recordUse();
        assertFalse(rod.isOnCooldown());
    }

    @Test
    void notOnCooldown_after2Uses() {
        rod.recordUse();
        rod.recordUse();
        assertFalse(rod.isOnCooldown());
    }

    @Test
    void onCooldown_after3Uses() {
        rod.recordUse();
        rod.recordUse();
        rod.recordUse();
        assertTrue(rod.isOnCooldown());
    }

    @Test
    void onMiss_returnsEmptyString() {
        assertEquals("", rod.onMiss());
    }

    @Test
    void onMiss_doesNotTriggerCooldown() {
        rod.onMiss();
        assertFalse(rod.isOnCooldown());
    }

    @Test
    void toString_showsUses_whenNotOnCooldown() {
        assertTrue(rod.toString().contains("Uses: 0/3"));
    }

    @Test
    void toString_showsCooldown_whenOnCooldown() {
        rod.recordUse();
        rod.recordUse();
        rod.recordUse();
        assertTrue(rod.toString().contains("Cooldown"));
    }

    @Test
    void maxUses_is3() {
        assertEquals(3, rod.MAX_USES);
    }

    @Test
    void useCooldown_is10() {
        assertEquals(10, rod.USE_COOLDOWN);
    }
}