import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class CLITest {
    @Test
    void createPlayers() {
        String input = "Anton\nKalle\n";
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