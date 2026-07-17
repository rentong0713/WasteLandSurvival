package game.weapons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link ParasiteRifle}.
 */
public class ParasiteRifleTest {

    private ParasiteRifle rifle;

    @BeforeEach
    void setUp() {
        rifle = new ParasiteRifle();
    }

    @Test
    void damage_is99() {
        assertEquals(99, rifle.getDamage());
    }

    @Test
    void buyPrice_is300() {
        assertEquals(300, rifle.getBuyPrice());
    }

    @Test
    void buyDescription_isParasiteRifle() {
        assertEquals("Parasite Rifle", rifle.getBuyDescription());
    }

    @Test
    void weaponDescription_isParasiteRifle() {
        assertEquals("Parasite Rifle", rifle.getWeaponDescription());
    }

    @Test
    void weaponEffect_isParasiteBlastEffect() {
        assertEquals(ParasiteBlastEffect.class, rifle.getWeaponEffect().getClass());
    }

    @Test
    void notOnCooldown_initially() {
        assertFalse(rifle.isOnCooldown());
    }

    @Test
    void notOnCooldown_after1Use() {
        rifle.recordUse();
        assertFalse(rifle.isOnCooldown());
    }

    @Test
    void notOnCooldown_after2Uses() {
        rifle.recordUse();
        rifle.recordUse();
        assertFalse(rifle.isOnCooldown());
    }

    @Test
    void onCooldown_after3Uses() {
        rifle.recordUse();
        rifle.recordUse();
        rifle.recordUse();
        assertTrue(rifle.isOnCooldown());
    }

    @Test
    void onMiss_triggersCooldown() {
        assertFalse(rifle.isOnCooldown());
        rifle.onMiss();
        assertTrue(rifle.isOnCooldown());
    }

    @Test
    void onMiss_returnsNonEmptyMessage() {
        String msg = rifle.onMiss();
        assertNotNull(msg);
        assertFalse(msg.isEmpty());
    }

    @Test
    void onMiss_messageContainsCooldownInfo() {
        String msg = rifle.onMiss();
        assertTrue(msg.contains("15") || msg.toLowerCase().contains("cooldown"));
    }

    @Test
    void toString_showsUses_whenNotOnCooldown() {
        assertTrue(rifle.toString().contains("Uses: 0/3"));
    }

    @Test
    void toString_showsCooldown_afterOnMiss() {
        rifle.onMiss();
        assertTrue(rifle.toString().contains("Cooldown"));
    }

    @Test
    void toString_showsCooldown_after3Uses() {
        rifle.recordUse();
        rifle.recordUse();
        rifle.recordUse();
        assertTrue(rifle.toString().contains("Cooldown"));
    }

    @Test
    void maxUses_is3() {
        assertEquals(3, rifle.MAX_USES);
    }

    @Test
    void useCooldown_is10() {
        assertEquals(10, rifle.USE_COOLDOWN);
    }
}