import cards.Card;
import cards.EffectCard;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;


import static org.junit.jupiter.api.Assertions.*;

class EffectCardTest {
    private EffectCard card;

    @BeforeEach
    void init(){
    card = new EffectCard("Anton", 2, "debuff", 2, 3);
    }

    @Test
    void constructorTest(){
    assertNotNull(card);
    assertEquals(card.getHealth(), 2);
    assertTrue(card.getHealth()>0 && card.getAttack()==0);
    }

    @Test
    void superConstructorTest(){

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