import cards.Card;
import cards.UnitCard;
import org.junit.jupiter.api.*;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


class GameTest {
    Game game;

    @BeforeEach
    void init() {
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
        } catch (Exception ignored) {
        }

        try {
            game = new Game(null, null);
            assertNull(game.getPlayers());
        } catch (Exception ignored) {
        }

        try {
            game = new Game("Ted", "Ted");
            assertNull(game.getPlayers());
        } catch (Exception ignored) {
        }

    }

    @Test
    void getRound() {
        assertEquals(1, game.getRound());
    }

    @Test
    void getActivePlayer() {
        assertEquals(0, game.getActivePlayer());
    }

    @Test
    void getCardPile() {
        Game game = new Game("Ted", "Anton");
        game.createCardPile(80);
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
    }

    @Test
    void playCard() {
        Card card = new UnitCard("name", 1, 1, 1);
        game.getPlayers()[game.getActivePlayer()].addCardToTable(card);
    }

    @Test
    void attackCard() {
        Game game = new Game("eric", "nisse");
        Player players[] = game.getPlayers();
        int defendingPlayer = game.getActivePlayer() == 0 ? 1 : 0;

        Card active = new UnitCard("Krigaren", 3, 5, 6);
        Card passive = new UnitCard("Hästen", 4, 5, 4);

        players[game.getActivePlayer()].addCardToTable(active);
        players[defendingPlayer].addCardToTable(passive);

        //Check so the cards played are not null and they have more than 0hp
        assertNotNull(players[game.getActivePlayer()].getCardFromTable(active.getId()));
        assertNotNull(players[defendingPlayer].getCardFromTable(passive.getId()));
        assertTrue(((UnitCard) active).getHp() > 0);
        assertTrue(((UnitCard) passive).getHp() > 0);

        ((UnitCard) active).setHp(((UnitCard) active).getHp() - ((UnitCard) passive).getAttack());
        ((UnitCard) passive).setHp(((UnitCard) passive).getHp() - ((UnitCard) active).getAttack());

        //Check so damage went through
        assertEquals(((UnitCard) active).getHp(), 1);
        assertEquals(((UnitCard) passive).getHp(), -1);

        //If one of the cards dies -> check so its removed from hand
        if (((UnitCard) active).getHp() < 1) {
            players[game.getActivePlayer()].removeCardFromHand(active.getId());
            assertNull(players[game.getActivePlayer()].getCardFromHand(active.getId()));
        } else if (((UnitCard) passive).getHp() < 1) {
            players[defendingPlayer].removeCardFromHand(passive.getId());
            assertNull(players[defendingPlayer].getCardFromHand(passive.getId()));
        }
    }

    @Test
    void attackPlayer() {
        Game game = new Game("Eric", "Ted");
        UnitCard card = new UnitCard("Pelle", 5, 9, 5);
        UnitCard card2 = new UnitCard("Håkan", 5, 5, 10);
        UnitCard card3 = new UnitCard("Anton", 5, 3, 4);

        game.getPlayers()[1].changeHealth(-25);
        assertFalse(game.attackPlayer(card2));
        game.getPlayers()[1].changeHealth(10);
        assertTrue(game.attackPlayer(card3));

        game.setActivePlayer(1);
        assertTrue(game.attackPlayer(card));
        assertEquals(25, game.getPlayers()[0].getHealth());
    }

    @Test
    void finishTurn() {
        Game game = new Game("Ted", "Anton");

        assertEquals(0, game.getActivePlayer());
        assertTrue(game.finishTurn());
        assertEquals(1, game.getActivePlayer());
    }

    @Test
    void finishGame() {

    }

    @Test
    void initGame() {
    }

    @Test
    void createCardPile() {
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