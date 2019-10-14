import cards.SpellCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class SpellCardTest {
    private SpellCard healer;
    private SpellCard attacker;

    @BeforeEach
    void innit() {
        healer = new SpellCard("Heal", false, 0, 2, "Eric", 2);
        attacker = new SpellCard("Attack", false, 5, 0, "Ted", 2);
    }

    @Test
    void constructorTest(){
        assertNotNull(healer);
        assertNotNull(attacker);

        assertEquals(healer.getDmg(), 0);
        assertEquals(attacker.getHeal(), 0);

    }

    @Test
    void superConstructorTest() {
        assertNull(new SpellCard().getName());
        assertEquals(new SpellCard().getCost(), 0);
        assertNotNull(healer.getId());
        assertNotEquals(healer.getId(), attacker.getId());

    }
    
}