import org.junit.jupiter.api.Test;
import utilities.Input;

import static org.junit.jupiter.api.Assertions.*;

class InputTest {

        private Input input;

    @Test
    void inputValidation() {

        input = new Input();

        assertFalse(input.inputValidation(5, 6));
        assertTrue(input.inputValidation(7,3));

    }
}