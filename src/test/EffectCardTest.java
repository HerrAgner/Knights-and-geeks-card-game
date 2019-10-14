import cards.Card;
import cards.EffectCard;
import cards.UnitCard;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;


import static org.junit.jupiter.api.Assertions.*;

class EffectCardTest {
    private EffectCard card;
    private EffectCard card2;
    private EffectCard card3;

    @BeforeEach
    void init() {
        card = new EffectCard("Anton", 2, "debuffHp", 2);
        card2 = new EffectCard("Pelle", 3, "buffAtk", 4);
        card3 = new EffectCard("Eric", 1, "buffAtk", 3);
    }

    @Test
    void constructorTest() {
        assertNull(new EffectCard().getName());
        assertNotNull(card);
        assertEquals(card3.getName(), "Eric");
        assertEquals(card3.getEffectValue(), 3);
        assertEquals(card3.getCost(), 1);
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
        assertEquals(card.getType(), "debuffHp");
    }

    @Test void getEffectValue(){
        assertEquals(card2.getEffectValue(), 4);
    }
}