import cards.Card;
import cards.UnitCard;
import org.junit.jupiter.api.*;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


class GameTest {

    @Test
    void constructorTest() {
        Game game = new Game("Ted", "Anton");

        assertNotNull(game.getPlayers());
        assertEquals(0, game.getActivePlayer());
        assertEquals(1, game.getRound());
        assertEquals(2, game.getPlayers().length);

        assertEquals("Ted", game.getPlayers()[0].getName());
        assertEquals("Anton", game.getPlayers()[1].getName());
        assertNotEquals(game.getPlayers()[0], game.getPlayers()[1]);
        assertEquals(0, game.getTrashPile().size());

        try {
            game = new Game("", "");
            assertNull(game.getPlayers());
        } catch (Exception ignored) {}

        try {
            game = new Game(null, null);
            assertNull(game.getPlayers());
        } catch (Exception ignored) {}

        try {
            game = new Game("Ted", "Ted");
            assertNull(game.getPlayers());
        } catch (Exception ignored) {}

    }

    @Test
    void getRound() {
    }

    @Test
    void getActivePlayer() {
    }

    @Test
    void getCardPile() {
    }

    @Test
    void getTrashPile() {
    }

    @Test
    void getPlayers() {
    }

    @Test
    void setCardPile() {
    }

    @Test
    void setTrashPile() {
    }

    @Test
    void setActivePlayer() {
    }

    @Test
    void setRound() {
    }

    @Test
    void drawCard() {
    }

    @Test
    void playCard() {
    }

    @Test
    void attackCard() {
        Game game = new Game("eric", "nisse");
        UnitCard card1 = new UnitCard("Krigaren", 3, "Warrior", 5, 3);
        UnitCard card2 = new UnitCard("Hästen", 4, "Horse", 9, 2);

        assertTrue(game.attackCard(card1, card2));





    }

    @Test
    void attackPlayer() {
    }

    @Test
    void finishTurn() {
    }

    @Test
    void finishGame() {
    }

    @Test
    void initGame() {
    }

    @Test
    void createCardPile(){
        Game game = new Game("Ted", "Anton");
        int amountOfCards = 80;

        assertTrue(game.createCardPile(amountOfCards));
        assertEquals(40, game.getCardPile().stream().distinct().count());

        assertNotNull(game.getCardPile());
        assertEquals(amountOfCards, game.getCardPile().size());

        assertFalse(game.createCardPile(101));
        assertFalse(game.createCardPile(49));

        assertTrue(game.createCardPile(50));
        assertTrue(game.createCardPile(100));


    }
}