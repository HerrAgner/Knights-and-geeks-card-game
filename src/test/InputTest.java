import cards.Card;
import cards.EffectCard;
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
        EffectCard card = new EffectCard("Buff", 1, "Hp", 2);
        EffectCard card1 = new EffectCard("Debuff", 1, "Hp", -2);

        game.getCurrentPlayer().addCardToHand(card);
        game.getDefendingPlayer().addCardToHand(card1);


        assertFalse(input.validateEmptyTable(card.getId(), game.getCurrentPlayer(), game.getDefendingPlayer()));
        assertTrue(input.validateEmptyTable(card1.getId(), game.getCurrentPlayer(), game.getDefendingPlayer()));
    }
}