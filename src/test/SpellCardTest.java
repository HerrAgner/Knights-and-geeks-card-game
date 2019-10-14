import cards.SpellCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class SpellCardTest {
    private SpellCard healer;
    private SpellCard attacker;

    @BeforeEach
    void innit() {
        healer = new SpellCard("Heal", false, 0, "Eric", 2);
        attacker = new SpellCard("Attack", false, 5, "Ted", 2);
    }

    @Test
    void constructorTest(){
        assertNotNull(healer);
        assertNotNull(attacker);
    }
}