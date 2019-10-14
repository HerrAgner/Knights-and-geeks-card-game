import cards.Card;
import cards.EffectCard;
import cards.UnitCard;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;


import static org.junit.jupiter.api.Assertions.*;

class EffectCardTest {
    private EffectCard card;
    private EffectCard card2;

    @BeforeEach
    void init() {
        card = new EffectCard("Anton", 2, "debuff", 2, 3);
        card2 = new EffectCard("Pelle", 3, "buff", 4, 2);
    }

    @Test
    void constructorTest() {
        assertNotNull(card);
        assertEquals(card.getHealth(), 2);
        assertTrue(card.getHealth() > 0 && card.getAttack() == 0);
    }

    @Test
    void superConstructorTest() {
        assertNull(new UnitCard().getName());
        assertEquals(new UnitCard().getCost(), 0);

        assertNotNull(card.getId());
        assertNotEquals(card.getId(), card2.getId());
    }

    @Test
    void getType() {
        assertEquals(card.getType(), "debuff");
    }

    @Test
    void getHealth() {
        assertEquals(card.getHealth(), 2);
    }

    @Test
    void getAttack() {
    }
}