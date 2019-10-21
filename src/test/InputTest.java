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
        assertTrue(input.inputValidation(7,3));

    }

}