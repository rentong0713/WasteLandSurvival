package game.weapons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link FrostBlade}.
 */
public class FrostBladeTest {

    private FrostBlade blade;

    @BeforeEach
    void setUp() {
        blade = new FrostBlade();
    }

    @Test
    void damage_is6() {
        assertEquals(6, blade.getDamage());
    }

    @Test
    void buyPrice_is200() {
        assertEquals(200, blade.getBuyPrice());
    }

    @Test
    void buyDescription_isFrostBlade() {
        assertEquals("Frost Blade", blade.getBuyDescription());
    }

    @Test
    void weaponDescription_isFrostBlade() {
        assertEquals("Frost Blade", blade.getWeaponDescription());
    }

    @Test
    void weaponEffect_isFrostEffect() {
        assertEquals(FrostEffect.class, blade.getWeaponEffect().getClass());
    }

    @Test
    void notOnCooldown_initially() {
        assertFalse(blade.isOnCooldown());
    }

    @Test
    void notOnCooldown_after1Use() {
        blade.recordUse();
        assertFalse(blade.isOnCooldown());
    }

    @Test
    void notOnCooldown_after2Uses() {
        blade.recordUse();
        blade.recordUse();
        assertFalse(blade.isOnCooldown());
    }

    @Test
    void onCooldown_after3Uses() {
        blade.recordUse();
        blade.recordUse();
        blade.recordUse();
        assertTrue(blade.isOnCooldown());
    }

    @Test
    void onMiss_returnsEmptyString() {
        assertEquals("", blade.onMiss());
    }

    @Test
    void onMiss_doesNotTriggerCooldown() {
        blade.onMiss();
        assertFalse(blade.isOnCooldown());
    }

    @Test
    void toString_showsUses_whenNotOnCooldown() {
        assertTrue(blade.toString().contains("Uses: 0/3"));
    }

    @Test
    void toString_showsCooldown_whenOnCooldown() {
        blade.recordUse();
        blade.recordUse();
        blade.recordUse();
        assertTrue(blade.toString().contains("Cooldown"));
    }

    @Test
    void maxUses_is3() {
        assertEquals(3, blade.MAX_USES);
    }

    @Test
    void useCooldown_is10() {
        assertEquals(10, blade.USE_COOLDOWN);
    }
}