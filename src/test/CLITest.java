import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CLITest {
    @Test
    void createPlayers() {
        String playerOneName = "a";
        String playerTwoName = "b";
        assertFalse(playerOneName.isEmpty() && playerTwoName.isEmpty());
        assertTrue(playerOneName != playerTwoName);
        assertTrue(playerOneName.length() < 10 && playerTwoName.length() < 10);
    }

    @Test
    void gameloop() {
        fail("Not tested");
    }
}