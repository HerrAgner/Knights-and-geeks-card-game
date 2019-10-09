import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class CLITest {
    @Test
    void createPlayers() {
        CLI cli = new CLI();
        String input = "Anton\nKalle\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        assertTrue(cli.createPlayers());
        assertFalse(cli.getPlayerOneName().isEmpty() && cli.getPlayerTwoName().isEmpty());
        assertTrue(cli.getPlayerOneName() != cli.getPlayerTwoName());
        assertTrue(cli.getPlayerOneName().length() < 10 && cli.getPlayerTwoName().length() < 10);
    }

    @Test
    void gameloop() {
        fail("Not tested");
    }
}