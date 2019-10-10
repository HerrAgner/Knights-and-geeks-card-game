import cards.Card;
import cards.UnitCard;
import org.junit.jupiter.api.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class GameTest {
    Game game;

    @BeforeEach
    void init () {
        game = new Game("Ted", "Anton");
    }

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
        assertEquals(1,game.getRound());
    }

    @Test
    void getActivePlayer() {
        assertEquals(0,game.getActivePlayer());
    }

    @Test
    void getCardPile() {
        assertNotNull(game.getCardPile());
    }

    @Test
    void getTrashPile() {
        assertNotNull(game.getTrashPile());
    }

    @Test
    void getPlayers() {
        assertNotNull(game.getPlayers());
    }

    @Test
    void setCardPile() {
        ArrayList<Card> testList = new ArrayList<>();
        game.setCardPile(testList);
        assertSame(testList, game.getCardPile());
    }

    @Test
    void setTrashPile() {
        ArrayList<Card> testList = new ArrayList<>();
        game.setTrashPile(testList);
        assertSame(testList, game.getTrashPile());
    }

    @Test
    void setActivePlayer() {
        game.setActivePlayer(1);
        assertEquals(1, game.getActivePlayer());
    }

    @Test
    void setRound() {
        game.setRound(2);
        assertEquals(2, game.getRound());
    }

    @Test
    void drawCard() {
        Game game = new Game("Anton", "Ted");
        game.createCardPile(80);
        ArrayList cardPile = game.getCardPile();
        Player[]players = game.getPlayers();

        assertTrue(players.length == 2);
        assertEquals(cardPile.size(), 80);

        int activePlayer=game.getActivePlayer();

        game.drawCard();
        assertEquals(cardPile.size(), 79);
        assertEquals(activePlayer, 0);
        game.drawCard();
        assertEquals(cardPile.size(), 78);
        assertEquals(activePlayer, 1);

    }

    @Test
    void playCard() {
        Card card = new UnitCard("name", 1, "type", 1, 1);
        game.getPlayers()[game.getActivePlayer()].addCardToTable(card);
    }

   /* @Test
    void attackCard() {
        Game game = new Game("eric", "nisse");
        UnitCard card1 = new UnitCard("Krigaren", 3, "Warrior", 5, 3);
        UnitCard card2 = new UnitCard("Hästen", 4, "Horse", 9, 2);

        assertTrue(game.attackCard(card1, card2));





    }*/

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

        assertTrue(game.createCardPile(51));
        assertEquals(51, game.getCardPile().size());

        assertFalse(game.createCardPile(101));
        assertFalse(game.createCardPile(49));

        assertTrue(game.createCardPile(50));
        assertTrue(game.createCardPile(100));


    }
}