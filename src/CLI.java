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
            Player activePlayer = game.getCurrentPlayer();
            Player defendingPlayer = game.getDefendingPlayer();
            Collection<Card> cardsOnHand = activePlayer.getCardsOnHand();
            Collection<Card> cardsOnTable = activePlayer.getCardsOnTable();
            Collection<Card> enemyCardsOnTable = defendingPlayer.getCardsOnTable();

            game.startTurn();

            System.out.println(activePlayer.getName() + "'s turn");

            printBoardAndCardsOnHand();

            System.out.println("\nMake a move!");
            System.out.println("1. Print cards from hand and table\n" +
                    "2. Print hp and mana\n" +
                    "3. Play card\n" +
                    "4. Attack with card\n" +
                    "5. End turn\n");

            int chosenCard;
            int chosenDefendingCard;

            boolean menu = true;
            while (menu) {
                int input = scan.nextInt();
                switch (input) {
                    case 1:
                        //Print cards from hand and table
                        printBoardAndCardsOnHand();
                        break;
                    case 2:
                        System.out.println("Your hp: " + activePlayer.getHealth());
                        System.out.println("Your mana: " + activePlayer.getMana());
                        System.out.println("Enemy hp: " + defendingPlayer.getHealth());
                        System.out.println("Enemy mana: " + defendingPlayer.getMana());
                        break;
                    case 3:
                        // Play card
                        System.out.println("Which card do you want to play?");
                        // Print cards on hand
                        printCards(cardsOnHand);
                        // enter number on card

                        //TODO Need validation for correct int here
                        chosenCard = scan.nextInt();

                        if (chosenCard > cardsOnHand.toArray().length) {
                            System.out.println("Number too high.");
                            break;
                        }

                        var unitCard = (Card) cardsOnHand.toArray()[chosenCard - 1];

                        Response[] response = game.playCard(unitCard.getId());

                        if (response[0] == Response.OK) {
                            switch (response[1]) {
                                case SPELL_CARD:
                                    System.out.println("Which card do you want to heal? (0 to heal you)");
                                    printCards(cardsOnTable);
                                    System.out.println("Which card do you want to attack? (0 to attack player)");
                                    printCards(enemyCardsOnTable);
                                    break;
                                case EFFECT_CARD:
                                    System.out.println("Which card do you want to buff?");
                                    printCards(cardsOnTable);
                                    System.out.println("Which card do you want to debuff?");
                                    printCards(enemyCardsOnTable);
                                    break;
                                case UNIT_CARD:
                                    System.out.println("Played card " + unitCard.getName());
                                    // Play card here
                                    printCards(activePlayer.getCardsOnTable());
                                    break;
                                default:
                                    // Crazy place! How did you get here?
                                    break;
                            }
                        } else if (response[0] == Response.ERROR) {
                            switch (response[1]) {
                                case TABLE_FULL:
                                    break;
                                case COST:
                                    break;
                            }
                        }
                        break;
                    case 4:
                        //Attack with card
                        System.out.println("Choose card: ");
                        // Print cards on your table
                        printCards(cardsOnTable);
                        // enter number on card
                        chosenCard = scan.nextInt();
                        var attackingCard = (UnitCard) cardsOnTable.toArray()[chosenCard - 1];
                        System.out.println("Attack card or player (0 for player): ");
                        // print cards on defending player table
                        printCards(enemyCardsOnTable);
                        // Enter number
                        chosenDefendingCard = scan.nextInt();

                        if (chosenDefendingCard == 0) {
                            game.attackPlayer(attackingCard);
                        } else if (chosenDefendingCard <= enemyCardsOnTable.toArray().length) {
                            var defendingCard = (UnitCard) enemyCardsOnTable.toArray()[chosenDefendingCard - 1];
                            System.out.println(attackingCard.getName());
                            System.out.println(attackingCard.getCurrentHealth());
                            System.out.println(attackingCard.getHp());

                            System.out.println(defendingCard.getName());
                            System.out.println(defendingCard.getHp());
                            game.attackCard(attackingCard, defendingCard);
                        }
                        break;
                    case 5:
                        // End turn
                        System.out.println("Ending turn.");
                        game.finishTurn();
                        menu = false;
                        break;
                    default:
                        break;
                }
            }


        }
    }

    private void printBoardAndCardsOnHand() {
        Player activePlayer = game.getCurrentPlayer();
        Player defendingPlayer = game.getDefendingPlayer();
        Collection<Card> cardsOnHand = activePlayer.getCardsOnHand();
        Collection<Card> cardsOnTable = activePlayer.getCardsOnTable();
        Collection<Card> enemyCardsOnTable = defendingPlayer.getCardsOnTable();

        System.out.println("\nDefending cards on table:");
        printCards(enemyCardsOnTable);
        System.out.println("\nYour cards on table: ");
        printCards(cardsOnTable);
        System.out.println("\nCards on hand:");
        printCards(cardsOnHand);
    }

    private void printCards(Collection<Card> cards) {
        StringBuilder outputNumber = new StringBuilder();
        StringBuilder outputName = new StringBuilder();
        StringBuilder outputHp = new StringBuilder();
        StringBuilder outputAtk = new StringBuilder();
        StringBuilder outputCost = new StringBuilder();
        StringBuilder outputType = new StringBuilder();
        var ref = new Object() {
            int index = 1;
        };

        cards.forEach(card -> {
            outputNumber.append(String.format("%-30s", "Card #: " + ref.index));
            outputName.append(String.format("%-30s", card.getName()));
            if (card instanceof UnitCard) {
                UnitCard unitCard = (UnitCard) card;
                outputHp.append(String.format("%-30s (%s)", "Hp: " + unitCard.getCurrentHealth(), "max: " + unitCard.getHp()));
                outputAtk.append(String.format("%-30s", "Atk: " + unitCard.getAttack()));
                outputCost.append(String.format("%-30s", "Cost: " + card.getCost()));
                outputType.append(String.format("%-30s", "Type: Unit card"));
            }
            ref.index++;
        });
        System.out.println(outputNumber);
        System.out.println(outputName);
        System.out.println(outputHp);
        System.out.println(outputAtk);
        System.out.println(outputCost);
        System.out.println(outputType);
    }

    public String getPlayerOneName() {
        return playerOneName;
    }

    public String getPlayerTwoName() {
        return playerTwoName;
    }
}
