import cards.Card;
import org.junit.jupiter.api.*;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


class GameTest {

    @Test
    void constructorTest() {
        Game game = new Game("Ted", "Anton");

        assertNotNull(game.getPlayers());
        assertEquals(1, game.getActivePlayer());
        assertEquals(1, game.getRound());
        assertEquals(2, game.getPlayers().length);

        assertEquals("Ted", game.getPlayers()[0].getName());
        assertEquals("Anton", game.getPlayers()[1].getName());
        assertNotEquals(game.getPlayers()[0], game.getPlayers()[1]);

        try {
            game = new Game("", "");
            assertNull(game);
        } catch (Exception e) {
            assert (false);
        }
        try {
            game = new Game(null, null);
            assertNull(game);
        } catch (Exception e) {
            assert (false);
        }




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
        
    }
}