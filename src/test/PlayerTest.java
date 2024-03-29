import cards.Card;
import cards.UnitCard;
import game.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    Player player;

    @BeforeEach
    void init() {
        player = new Player("TestName");
    }

    @Test
    void constructor() {
        String name = "testName";
        player = new Player(name);
        assertNotNull(player);
        assertSame(name, player.getName());
        player = new Player("");
        assertNull(player.getName());
    }

    @Test
    void getName() {
        assertNotNull(player.getName());
    }

    @Test
    void getHealth() {
        assertTrue(player.getHealth() > -1);
    }

    @Test
    void changeHealth() {
        int hpCurrent = player.getHealth();
        int hpChange = -5;
        player.changeHealth(hpChange);
        assertEquals(hpCurrent + hpChange, player.getHealth());
        hpCurrent = player.getHealth();
        hpChange = 5;
        player.changeHealth(hpChange);
        assertEquals(hpCurrent + hpChange, player.getHealth());
    }

    @Test
    void getMana() {
        assertTrue(player.getCurrentMana() > -1);
    }

    @Test
    void changeMana() {
        int manaCurrent = player.getCurrentMana();
        int manaChange = 5;
        player.changeMana(manaChange);
        assertEquals(manaCurrent + manaChange, player.getCurrentMana());
        manaCurrent = player.getCurrentMana();
        manaChange = -5;
        player.changeMana(manaChange);
        assertEquals(manaCurrent + manaChange, player.getCurrentMana());
        assertEquals(manaCurrent + manaChange, player.getCurrentMana());
        manaCurrent = player.getCurrentMana();
        player.changeMana(0);
        assertEquals(manaCurrent, player.getCurrentMana());
    }

    @Test
    void getCardsOnHand() {
        assertNotNull(player.getCardsOnHand());
    }

    @Test
    void addCardToHand() {
        Card testCard = new UnitCard("name", 1, 1, 1, "COMMON");
        assertTrue(player.addCardToHand(testCard));
        assertEquals(testCard, player.getCardFromHand(testCard.getId()));
        testCard = null;
        assertFalse(player.addCardToHand(testCard));
    }

    @Test
    void getCardFromHand() {
        Card testCard = new UnitCard("name", 1, 1, 1, "COMMON");
        player.addCardToHand(testCard);
        assertSame(testCard.getId(), player.getCardFromHand(testCard.getId()).getId());
        testCard = new UnitCard("name", 1, 1, 1, "COMMON");
        assertNull(player.getCardFromHand(testCard.getId()));
        assertNull(player.getCardFromHand(null));
    }

    @Test
    void removeCardFromHand() {
        Card testCard = new UnitCard("name", 1, 1, 1, "COMMON");
        player.addCardToHand(testCard);
        assertEquals(testCard, player.removeCardFromHand(testCard.getId()));
        assertTrue(player.getCardsOnHand().isEmpty());
        testCard = new UnitCard("name", 1, 1, 1, "COMMON");
        assertNull(player.removeCardFromHand(testCard.getId()));
        assertNull(player.removeCardFromHand(null));
    }

    @Test
    void getCardsOnTable() {
        assertNotNull(player.getCardsOnTable());
    }

    @Test
    void addCardToTable() {
        Card testCard = new UnitCard("name", 1, 1, 1, "COMMON");
        assertTrue(player.addCardToTable(testCard));
        assertEquals(testCard, player.getCardFromTable(testCard.getId()));
        testCard = null;
        assertFalse(player.addCardToTable(testCard));
    }

    @Test
    void getCardFromTable() {
        Card testCard = new UnitCard("name", 1, 1, 1, "COMMON");
        player.addCardToTable(testCard);
        assertSame(testCard.getId(), player.getCardFromTable(testCard.getId()).getId());
        testCard = new UnitCard("name", 1, 1, 1, "COMMON");
        assertNull(player.getCardFromTable(testCard.getId()));
        assertNull(player.getCardFromTable(null));
    }

    @Test
    void removeCardFromTable() {
        Card testCard = new UnitCard("name", 1, 1, 1, "COMMON");
        player.addCardToTable(testCard);
        assertEquals(testCard, player.removeCardFromTable(testCard.getId()));
        assertTrue(player.getCardsOnTable().isEmpty());
        testCard = new UnitCard("name", 1, 1, 1, "COMMON");
        assertNull(player.removeCardFromTable(testCard.getId()));
        assertNull(player.removeCardFromTable(null));
    }
}