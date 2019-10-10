import cards.UnitCard;

import java.util.Scanner;

public class CLI {
    private String playerOneName, playerTwoName;
    private Scanner scan;
    private boolean running;
    private Game game;

    public CLI() {
        scan = new Scanner(System.in);
        running = true;
    }

    public void run() {
        createPlayers();
        gameloop();
    }

    private void createPlayers() {
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
        game = new Game(playerOneName, playerTwoName);
    }

    private void gameloop() {
        while(running) {

            Player activePlayer = game.getPlayers()[game.getActivePlayer()];
            System.out.println(activePlayer.getName() + "'s turn");
            activePlayer.getCardsOnTable().forEach(card -> System.out.printf("'%15s'", card.getName()));
            activePlayer.getCardsOnTable().forEach(card -> {
                UnitCard unitCard = (UnitCard) card;
                System.out.printf("'%15s'", unitCard.getHp());
            });
            activePlayer.getCardsOnTable().forEach(card -> {
                UnitCard unitCard = (UnitCard) card;
                System.out.printf("'%15s'", unitCard.getAttack());
            });
            System.out.println("Make a move!");
            String s = scan.next();
        }


    }

    public String getPlayerOneName() {
        return playerOneName;
    }

    public String getPlayerTwoName() {
        return playerTwoName;
    }
}
