import java.util.Scanner;

public class CLI {
    private String playerOneName, playerTwoName;

    public CLI() {
    }

    public boolean createPlayers() {
        Scanner scan = new Scanner(System.in);

        System.out.println("Enter name for player 1");
        playerOneName = scan.next();
        while (playerOneName.length() == 0 || playerOneName.length() > 10) {
            System.out.println("Invalid name. Please enter a new one.");
            playerOneName = scan.nextLine();
        }
        System.out.println("Enter name for player 2");
        playerTwoName = scan.next();
        while (playerTwoName.length() == 0 || playerTwoName.length() > 10 || playerOneName.equals(playerTwoName)) {
            System.out.println("Invalid name. Please enter a new one.");
            playerTwoName = scan.nextLine();
        }
        new Game(playerOneName, playerTwoName);
        return true;
    }

    private boolean gameloop() {
        return true;
    }

    public String getPlayerOneName() {
        return playerOneName;
    }

    public String getPlayerTwoName() {
        return playerTwoName;
    }
}
