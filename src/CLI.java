import cards.Card;
import cards.EffectCard;
import cards.SpellCard;
import cards.UnitCard;
import utilities.Input;

import java.util.Collection;
import java.util.Scanner;

public class CLI {
    private String playerOneName, playerTwoName;
    private Scanner scan;
    private boolean running;
    private Game game;
    private Input input;

    public CLI() {
        scan = new Scanner(System.in);
        input = new Input();
        running = true;
    }

    public void run() {
        createPlayers();
        gameloop();
    }

    public void createPlayers() {
        System.out.println("Enter name for player 1");
        playerOneName = scan.nextLine();
        while (playerOneName.length() == 0 || playerOneName.length() > 10) {
            System.out.println("Invalid name. Please enter a new one.");
            playerOneName = scan.nextLine();
        }
        System.out.println("Enter name for player 2");
        playerTwoName = scan.nextLine();
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

            printBoardAndCardsOnHand(cardsOnHand, cardsOnTable, enemyCardsOnTable);
            printMenu();

            boolean menu = true;
            while (menu) {
                menu = menuSwitch(activePlayer, defendingPlayer, cardsOnHand, cardsOnTable, enemyCardsOnTable);
            }

        }
    }

    private void printMenu() {
        System.out.println("\nMake a move!");
        System.out.println("1. Print cards from hand and table\n" +
                "2. Print hp and mana\n" +
                "3. Play card\n" +
                "4. Attack with card\n" +
                "5. End turn\n");
    }

    private boolean menuSwitch(Player activePlayer, Player defendingPlayer, Collection<Card> cardsOnHand, Collection<Card> cardsOnTable, Collection<Card> enemyCardsOnTable) {

        int chosenCard;
        int chosenDefendingCard;
        int userInput;

        userInput = input.validatedInput(5);

        switch (userInput) {
            case 1:
                printBoardAndCardsOnHand(cardsOnHand, cardsOnTable, enemyCardsOnTable);
                break;
            case 2:
                printHpAndMana(activePlayer, defendingPlayer);
                break;
            case 3:
                // Play card
                System.out.println("Which card do you want to play?");
                // Print cards on hand
                printCards(cardsOnHand);
                // enter number on card

                //TODO Need validation for correct int here

                chosenCard = input.validatedInput(cardsOnHand.toArray().length);
//                chosenCard = scan.nextInt();
//
//                if (chosenCard > cardsOnHand.toArray().length) {
//                    System.out.println("Number too high.");
//                    break;
//                }

                Card card = (Card) cardsOnHand.toArray()[(chosenCard - 1)];

                Response[] response = game.playCard(card.getId());

                if (response[0] == Response.OK) {
                    UnitCard unitCard;
                    switch (response[1]) {
                        case SPELL_CARD:
                            SpellCard spellCard = (SpellCard) card;
                            if (spellCard.getType().equals("Healer")) {
                                printCards(cardsOnTable);
                                System.out.println("Which card do you want to heal? (0 to heal you)");
                                useSpell(spellCard, cardsOnHand);
                            } else if (spellCard.getType().equals("Attacker")) {
                                printCards(enemyCardsOnTable);
                                System.out.println("Which card do you want to attack? (0 to attack player)");
                                useSpell(spellCard, enemyCardsOnTable);
                            }
                            break;
                        case EFFECT_CARD:
                            EffectCard effectCard = (EffectCard) card;
                            if (effectCard.getEffectValue() < 0) {
                                printCards(enemyCardsOnTable);
                                System.out.println("Which card do you want to debuff?");

                                chosenDefendingCard = scan.nextInt();
                                unitCard = (UnitCard) enemyCardsOnTable.toArray()[chosenDefendingCard - 1];

                                game.useEffectCard(effectCard, unitCard);
                            } else {
                                printCards(cardsOnTable);
                                System.out.println("Which card do you want to buff?");

                                chosenDefendingCard = scan.nextInt();
                                unitCard = (UnitCard) cardsOnTable.toArray()[chosenDefendingCard - 1];

                                game.useEffectCard(effectCard, unitCard);
                            }

                            break;
                        case UNIT_CARD:
                            System.out.println("Played card " + card.getName());
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
                attackWithCard(cardsOnTable, enemyCardsOnTable);
                break;
            case 5:
                endPlayerTurn();
                return false;
            default:
                printMenu();
                break;
        }
        return game.finishGame();
    }

    private void printBoardAndCardsOnHand(Collection<Card> cardsOnHand, Collection<Card> cardsOnTable, Collection<Card> enemyCardsOnTable) {
        System.out.println("\nDefending cards on table:");
        printCards(enemyCardsOnTable);
        System.out.println("\nYour cards on table: ");
        printCards(cardsOnTable);
        System.out.println("\nCards on hand:");
        printCards(cardsOnHand);
    }

    private void printHpAndMana(Player activePlayer, Player defendingPlayer){
        System.out.println("Your hp: " + activePlayer.getHealth());
        System.out.println("Your mana: " + activePlayer.getMana());
        System.out.println("Enemy hp: " + defendingPlayer.getHealth());
        System.out.println("Enemy mana: " + defendingPlayer.getMana());
    }

    private void attackWithCard(Collection<Card> cardsOnTable, Collection<Card> enemyCardsOnTable ){
        int chosenCard;
        int chosenDefendingCard;
        if(cardsOnTable.size()>=1) {
            System.out.println("Choose card: ");
            printCards(cardsOnTable);
            chosenCard = scan.nextInt();
            var attackingCard = (UnitCard) cardsOnTable.toArray()[chosenCard - 1];
            System.out.println("Attack card or player (0 for player): ");
            printCards(enemyCardsOnTable);
            chosenDefendingCard = scan.nextInt();

            if (chosenDefendingCard == 0) {
                running = game.attackPlayer(attackingCard);
            } else if (chosenDefendingCard <= enemyCardsOnTable.toArray().length) {
                var defendingCard = (UnitCard) enemyCardsOnTable.toArray()[chosenDefendingCard - 1];
                game.attackCard(attackingCard, defendingCard);
            }
        }
        else{
            System.out.println("\nNo cards on table. Choose another option");
        }
    }

    private void endPlayerTurn(){
        System.out.println("Ending turn.");
        System.out.println("---------------------------------------------------------------------------------------------");
        System.out.println("\n\n");
        game.finishTurn();
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
            outputCost.append(String.format("%-30s", "Cost: " + card.getCost()));
            if (card instanceof UnitCard) {
                UnitCard unitCard = (UnitCard) card;
                outputHp.append(String.format("%-30s", "Hp: " + unitCard.getCurrentHealth() + " max:(" + unitCard.getMaxHealth() + ")"));
                outputAtk.append(String.format("%-30s", "Atk: " + unitCard.getAttack()));
                outputType.append(String.format("%-30s", "Type: Unit card"));
            } else if (card instanceof SpellCard) {
                SpellCard spellCard = (SpellCard) card;
                String type = spellCard.getType().equals("Attacker") ? "Dmg: " : "heal: ";
                outputHp.append(String.format("%-30s", type + spellCard.getValue()));
                outputAtk.append(String.format("%-30s", "Aoe: " + spellCard.isMany()));
                outputType.append(String.format("%-30s", "Type: Spell card"));
            } else if (card instanceof EffectCard) {
                EffectCard effectCard = (EffectCard) card;
                String target = effectCard.getEffectValue() < 0 ? "Debuff card" : "Buff card";
                String type = effectCard.getType().equals("Hp") ? "max hp" : "atk";
                String increase = effectCard.getEffectValue() < 0 ? "Decrease " : "increase ";
                outputHp.append(String.format("%-30s", "Effect: Will " + increase + type));
                outputAtk.append(String.format("%-30s", "Amount: " + effectCard.getEffectValue()));
                outputType.append(String.format("%-30s", "Type: " + target));

            }
            ref.index++;
        });
        System.out.println(outputNumber);
        System.out.println("\u001B[32m" + outputName + "\u001B[0m");
        System.out.println(outputHp);
        System.out.println(outputAtk);
        System.out.println();
        System.out.println(outputCost);
        System.out.println(outputType);
    }

    private void useSpell(SpellCard spellCard, Collection<Card> cards) {
        int chosenDefendingCard = scan.nextInt();
        if (chosenDefendingCard == 0) {
            game.useSpellOnPlayer(spellCard);
        } else if (spellCard.isMany()) {
            game.useSpellOnCard(spellCard);
        } else {
            UnitCard unitCard = (UnitCard) cards.toArray()[chosenDefendingCard - 1];
            game.useSpellOnCard(spellCard, unitCard);
        }
    }

    public String getPlayerOneName() {
        return playerOneName;
    }

    public String getPlayerTwoName() {
        return playerTwoName;
    }
}
