import cards.UnitCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import enums.*;

class UnitCardTest {
    private UnitCard card;
    private UnitCard card2;

    @BeforeEach
    void init() {
        card = new UnitCard("Eric", 4, 10, 3, "RARE");
        card2 = new UnitCard("Eric", 4, 10, 3, "COMMON");
    }

    @Test
    void superConstructorTest() {
        assertNull(new UnitCard().getName());
        assertEquals(0, new UnitCard().getCost());

        assertNotNull(card.getId());
        assertNotEquals(card.getId(), card2.getId());

    }

    @Test
    void constructorTest() {
        assertNotNull(card);
        assertEquals(3, card.getAttack());
        assertEquals(10, card.getMaxHealth());
        assertEquals(4, card.getCost());
        assertEquals("Eric", card.getName());
        assertNull(new UnitCard().getName());
        assertSame(Rarity.RARE, card.getRarity());
    }

    @Test
    void getHp() {
        assertEquals(10, card.getMaxHealth());
    }

    @Test
    void getAttack() {
        assertEquals(3, card.getAttack());
    }

    @Test
    void changeMaxHealth() {
        card.changeMaxHealth(5);
        assertEquals(15, card.getMaxHealth());
        card.changeMaxHealth(-5);
        assertEquals(10, card.getMaxHealth());
        card.changeMaxHealth(-20);
        assertEquals(1, card.getMaxHealth());
    }

    @Test
    void changeAttack() {
        card.changeAttack(5);
        assertEquals(8, card.getAttack());
        card.changeAttack(-5);
        assertEquals(3, card.getAttack());
        card.changeAttack(-100);
        assertEquals(1, card.getAttack());
    }

    @Test
    void setFatigue() {
        card.setFatigue(true);
        assertTrue(card.getFatigue());

        card.setFatigue(false);
        assertFalse(card.getFatigue());
    }

    @Test
    void getFatigue() {
        assertNotNull(card.getFatigue());
    }

}