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
        Game game = new Game("Anton", "Ted");
        game.createCardPile(80);
        ArrayList cardPile = game.getCardPile();
        Player[] players = game.getPlayers();

        assertTrue(players.length == 2);
        assertEquals(cardPile.size(), 80);

        int activePlayer = game.getActivePlayer();

        game.drawCard();
        assertEquals(cardPile.size(), 79);
        assertEquals(players[activePlayer].getCardsOnHand().size(), 1);
        game.setActivePlayer(1);
        game.drawCard();
        assertEquals(cardPile.size(), 78);
        assertEquals(players[activePlayer].getCardsOnHand().size(), 1);
        game.setActivePlayer(0);
        game.drawCard();
        assertEquals(cardPile.size(), 77);
        assertEquals(players[activePlayer].getCardsOnHand().size(), 2);
        game.setActivePlayer(1);
        game.drawCard();
        assertEquals(cardPile.size(), 76);
        assertEquals(players[activePlayer].getCardsOnHand().size(), 2);

        while(cardPile.size()!=0){
            game.drawCard();
        }
        game.drawCard();
        assertEquals(cardPile.size(), 0);



    }

    @Test
    void playCard() {
        Card card = new UnitCard("name", 1, 1, 1);
        game.getPlayers()[0].addCardToHand(card);
        game.playCard(card.getId());
        assertSame(game.getPlayers()[game.getActivePlayer()].getCardFromTable(card.getId()), card);
    }

    @Test
    void attackCard() {
        Game game = new Game("eric", "nisse");
        Player players[] = game.getPlayers();
        int defendingPlayer = game.getActivePlayer() == 0 ? 1 : 0;

        UnitCard attackingCard = new UnitCard("Krigaren", 3, 5, 6);
        UnitCard defendingCard = new UnitCard("Hästen", 4, 5, 4);

        players[game.getActivePlayer()].addCardToTable(attackingCard);
        players[defendingPlayer].addCardToTable(defendingCard);
        // SET UP -----------------------------------------------

        assertTrue(game.attackCard(attackingCard, defendingCard));
        assertFalse(game.attackCard(attackingCard, attackingCard));
        assertEquals(attackingCard.getHp(), 1);
        assertEquals(defendingCard.getHp(), -1);
        assertTrue(game.getTrashPile().size() >= 1);
        assertNull(players[defendingPlayer].getCardFromTable(defendingCard.getId()));
        assertNotNull(players[game.getActivePlayer()].getCardFromTable(attackingCard.getId()));
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

        assertEquals(1, game.getRound());
        assertEquals(0, game.getActivePlayer());
        assertTrue(game.finishTurn());
        assertEquals(2, game.getRound());
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

    @Test
    void shuffleTrashPile() {
        Game game = new Game("Anton", "Ted");
        game.createCardPile(80);

        assertEquals(0, game.getTrashPile().size());
        game.setTrashPile(new ArrayList<>(game.getCardPile()));
        game.getCardPile().clear();

        assertEquals(0, game.getCardPile().size());
        assertEquals(80, game.getTrashPile().size());

        assertTrue(game.shuffleTrashPile());
        assertEquals(80, game.getCardPile().size());
        assertEquals(0, game.getTrashPile().size());

    }
}