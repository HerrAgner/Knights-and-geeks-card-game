import cards.Card;
import cards.EffectCard;
import cards.UnitCard;
import game.Game;
import org.junit.jupiter.api.Test;
import utilities.Input;

import static org.junit.jupiter.api.Assertions.*;

class InputTest {

    private Input input;
    private Game game;

    @Test
    void inputValidation() {

        input = new Input();

        assertFalse(input.inputValidation(5, 6));
        assertTrue(input.inputValidation(7, 3));

    }

    @Test
    void validateEmptyTable() {
        input = new Input();
        game = new Game("Arne", "Kjell", 100);
        UnitCard testCard = new UnitCard("Test", 1, 2, 3, "COMMON");
        EffectCard buff = new EffectCard("Buff", 1, "Hp", 2);
        EffectCard debuff = new EffectCard("Debuff", 1, "Hp", -2);

        game.getCurrentPlayer().addCardToHand(buff);
        game.getCurrentPlayer().addCardToHand(debuff);

        assertTrue(input.validateEmptyTable(debuff.getId(), game.getCurrentPlayer(), game.getDefendingPlayer()));
        assertTrue(input.validateEmptyTable(buff.getId(), game.getCurrentPlayer(), game.getDefendingPlayer()));
        assertEquals(0, game.getCurrentPlayer().getCardsOnTable().size());

        game.getCurrentPlayer().addCardToTable(testCard);
        game.getDefendingPlayer().addCardToTable(testCard);
        assertFalse(input.validateEmptyTable(buff.getId(), game.getCurrentPlayer(), game.getDefendingPlayer()));
        assertFalse(input.validateEmptyTable(debuff.getId(), game.getCurrentPlayer(), game.getDefendingPlayer()));
        assertEquals(1, game.getCurrentPlayer().getCardsOnTable().size());
    }
}