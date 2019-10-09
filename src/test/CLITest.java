import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class CLITest {
    @ParameterizedTest
    @ValueSource(strings = {"Anton\nKalle\n", "Anton\nAnton\nKalle", "aaaaaaaaaaaaaaaaaaa\nAlle\n\n\n\nAlle\nRalle"})
    void createPlayers(String input) {
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        CLI cli = new CLI();
        assertFalse(cli.getPlayerOneName().isEmpty() && cli.getPlayerTwoName().isEmpty());
        assertNotSame(cli.getPlayerOneName(), cli.getPlayerTwoName());
        assertTrue(cli.getPlayerOneName().length() < 10 && cli.getPlayerTwoName().length() < 10);
    }

    @Test
    void gameloop() {
        fail("Not tested");
    }
}