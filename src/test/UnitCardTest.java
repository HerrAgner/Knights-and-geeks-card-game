import cards.Card;
import cards.UnitCard;
import cards.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnitCardTest {
    private UnitCard card;

    @BeforeEach
    void init(){
        card = new UnitCard("Eric", 4, "Warrior", 10, 3);
    }

    @Test
    void superConstructorTest() {
        assertNull(new Card().getName());
        assertNull(new Card().getType());
        assertEquals(new Card().getCost(), 0);
    }

    @Test
    void constructorTest() {
        assertNotNull(card);
        assertEquals(card.getAttack(), 3);
        assertEquals(card.getHp(), 10);
        assertEquals(card.getCost(), 4);
        assertEquals(card.getName(), "Eric");
        assertEquals(card.getType(), "Warrior");
        assertNull(new UnitCard().getName());
        assertNull(new UnitCard().getType());
    }

    @Test
    void getHp() {
        assertEquals(card.getHp(), 10);
    }

    @Test
    void getAttack() {
        assertEquals(card.getAttack(), 3);
    }

    @Test
    void setHp() {
        card.setHp(5);
        assertEquals(card.getHp(), 5);

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

}