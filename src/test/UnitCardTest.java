import cards.UnitCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnitCardTest {
    private UnitCard card;
    private UnitCard card2;
    @BeforeEach
    void init(){
        card = new UnitCard("Eric", 4, 10, 3);
        card2 = new UnitCard("Eric", 4, 10, 3);
    }

    @Test
    void superConstructorTest() {
        assertNull(new UnitCard().getName());
        assertEquals(new UnitCard().getCost(), 0);

        assertNotNull(card.getId());
        assertNotEquals(card.getId(), card2.getId());

    }

    @Test
    void constructorTest() {
        assertNotNull(card);
        assertEquals(card.getAttack(), 3);
        assertEquals(card.getMaxHealth(), 10);
        assertEquals(card.getCost(), 4);
        assertEquals(card.getName(), "Eric");
        assertNull(new UnitCard().getName());
    }

    @Test
    void getHp() {
        assertEquals(card.getMaxHealth(), 10);
    }

    @Test
    void getAttack() {
        assertEquals(card.getAttack(), 3);
    }

    @Test
    void setHp() {
        card.setHp(5);
        assertEquals(card.getMaxHealth(), 5);

        assertFalse(card.setHp(100));
        assertFalse(card.setHp(-4));
    }

    @Test
    void setAttack() {
        card.setAttack(5);
        assertEquals(card.getAttack(), 5);

        assertFalse(card.setAttack(100));
        assertFalse(card.setAttack(-4));
    }

    @Test
    void setFatigue(){
        card.setFatigue(true);
        assertTrue(card.getFatigue());

        card.setFatigue(false);
        assertFalse(card.getFatigue());
    }

    @Test
    void getFatigue(){
        assertNotNull(card.getFatigue());
    }

}