import cards.EffectCard;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;


import static org.junit.jupiter.api.Assertions.*;

class EffectCardTest {
    private EffectCard card;

    @BeforeEach
    void init(){
    card = new EffectCard("Anton", 2, "debuff", 0, 3);
    }

    @Test
    void constructorTest(){
    assertNotNull(card);
    assertEquals(card.getAttack(), 3);
    }

    @Test
    void superConstructorTest(){

    }

    @Test
    void getType() {
    }

    @Test
    void getHealth() {
    }

    @Test
    void getAttack() {
    }
}