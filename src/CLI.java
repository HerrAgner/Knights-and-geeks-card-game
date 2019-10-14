import cards.Card;
import cards.UnitCard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

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
        while (running) {

            Player activePlayer = game.getPlayers()[game.getActivePlayer()];
            System.out.println(activePlayer.getName() + "'s turn");
            List<Card> asd = new ArrayList<>(activePlayer.getCardsOnHand());

            printCards(asd);

//            activePlayer.getCardsOnHand().forEach(card -> System.out.printf("%-30s", card.getName()));
//            System.out.println();
//            activePlayer.getCardsOnHand().forEach(card -> {
//                UnitCard unitCard = (UnitCard) card;
//                System.out.printf("%-30s", "HP: " + unitCard.getHp());
//            });
//            System.out.println();
//            activePlayer.getCardsOnHand().forEach(card -> {
//                UnitCard unitCard = (UnitCard) card;
//                System.out.printf("%-30s", "Atk: " + unitCard.getAttack());
//            });
            System.out.println();
            System.out.println("Make a move!");
            String s = scan.next();
        }


    }

    private void printCards(List<Card> cards) {
        StringBuilder output = new StringBuilder();

        cards.forEach(card -> {
            output.append(String.format("%-30s", card.getName()));
        });
        output.append("\n");
        cards.forEach(card -> {
            if (card instanceof UnitCard) {
                UnitCard unitCard = (UnitCard) card;
                output.append(String.format("%-30s", "Hp: " + unitCard.getHp()));
            }
        });
        output.append("\n");
        cards.forEach(card -> {
            if (card instanceof UnitCard) {
                UnitCard unitCard = (UnitCard) card;
                output.append(String.format("%-30s", "Atk: " + unitCard.getAttack()));
            }
        });
        output.append("\n");
        cards.forEach(card -> {
            output.append(String.format("%-30s", "Cost: " + card.getCost()));
        });

        System.out.println(output);
    }

    public String getPlayerOneName() {
        return playerOneName;
    }

    public String getPlayerTwoName() {
        return playerTwoName;
    }
}
