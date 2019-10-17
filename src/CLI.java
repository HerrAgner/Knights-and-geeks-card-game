import cards.Card;
import cards.EffectCard;
import cards.SpellCard;
import cards.UnitCard;
import utilities.Input;
import enums.*;

import java.util.Collection;
import java.util.Scanner;

import static utilities.CLIColors.*;

public class CLI {
    private String playerOneName, playerTwoName;
    private Scanner scan;
    private boolean running;
    private Game game;
    private Input input;
    private Player activePlayer;
    private Player defendingPlayer;
    private Collection<Card> cardsOnHand;
    private Collection<Card> cardsOnTable;
    private Collection<Card> enemyCardsOnTable;

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
        if (playerOneName.toLowerCase().equals("cheetah") && playerTwoName.toLowerCase().equals("zebra")) {
            Lurig lurig = new Lurig();
        }
        game = new Game(playerOneName, playerTwoName, choseCardPileSize());
    }

    public int choseCardPileSize() {
        System.out.println("Enter your desired card pile size");
        int cardSize = input.validatedInput(100);
        while (cardSize < 45 || cardSize > 100) {
            System.out.println("Invalid size, chose number between 45-100");
            cardSize = input.validatedInput(100);
        }
        return cardSize;
    }

    private void gameloop() {
        while (running) {
            activePlayer = game.getCurrentPlayer();
            defendingPlayer = game.getDefendingPlayer();
            cardsOnHand = activePlayer.getCardsOnHand();
            cardsOnTable = activePlayer.getCardsOnTable();
            enemyCardsOnTable = defendingPlayer.getCardsOnTable();

            game.startTurn();

            System.out.println(activePlayer.getName() + "'s turn");

            printBoardAndCardsOnHand();
            printMenu();

            boolean menu = true;
            while (menu) {
                menu = menuSwitch();
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

    private boolean menuSwitch() {
        int userInput;
        boolean printAll = true;

        userInput = input.validatedInput(5);

        switch (userInput) {
            case 1:
                break;
            case 2:
                printHpAndMana();
                printAll = false;
                break;
            case 3:
                playCard();
                break;
            case 4:
                attackWithCard();
                break;
            case 5:
                endPlayerTurn();
                return false;
            default:
                printMenu();
                break;
        }
        if (!game.shouldGameContinue()) {
            running = false;
        }
        if (printAll) {
            printBoardAndCardsOnHand();
        }
        printMenu();
        return true;
    }

    private void playCard() {
        int chosenCard;
        int chosenDefendingCard;

        System.out.println("Which card do you want to play?");
        printCards(cardsOnHand);

        chosenCard = input.validateChosenCard(cardsOnHand.size());

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

                        chosenDefendingCard = input.validateChosenCard(enemyCardsOnTable.size());
                        unitCard = (UnitCard) enemyCardsOnTable.toArray()[chosenDefendingCard - 1];

                        game.useEffectCard(effectCard, unitCard);
                    } else {
                        printCards(cardsOnTable);
                        System.out.println("Which card do you want to buff?");

                        chosenDefendingCard = input.validateChosenCard(cardsOnTable.size());
                        unitCard = (UnitCard) cardsOnTable.toArray()[chosenDefendingCard - 1];

                        game.useEffectCard(effectCard, unitCard);
                    }

                    break;
                case UNIT_CARD:
                    System.out.println("Played card " + card.getName());
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
    }

    private void printBoardAndCardsOnHand() {
        System.out.println("\nDefending cards on table:");
        printCards(enemyCardsOnTable);
        System.out.println("\nYour cards on table: ");
        printCards(cardsOnTable);
        System.out.println("\nCards on hand:");
        printCards(cardsOnHand);
    }

    private void printHpAndMana() {
        System.out.println("Your hp: " + activePlayer.getHealth());
        System.out.println("Your mana: " + activePlayer.getCurrentMana());
        System.out.println("Enemy hp: " + defendingPlayer.getHealth());
        System.out.println("Enemy mana: " + defendingPlayer.getCurrentMana());
    }

    private void attackWithCard() {
        int chosenCard;
        int chosenDefendingCard;
        if (cardsOnTable.size() >= 1) {
            System.out.println("Choose card: ");
            printCards(cardsOnTable);
            chosenCard = input.validateChosenCard(cardsOnTable.size());
            var attackingCard = (UnitCard) cardsOnTable.toArray()[chosenCard - 1];
            if (attackingCard.getFatigue()) {
                System.out.println("\nCard is fatigue, wait one turn to attack!\n");
                printMenu();
            } else {
                System.out.println("Attack card or player (0 for player): ");
                printCards(enemyCardsOnTable);
                chosenDefendingCard = input.validateActionOnPlayerOrCard(enemyCardsOnTable.size());

                if (chosenDefendingCard == 0) {
                    game.attackPlayer(attackingCard);
                } else if (chosenDefendingCard <= enemyCardsOnTable.toArray().length) {
                    var defendingCard = (UnitCard) enemyCardsOnTable.toArray()[chosenDefendingCard - 1];
                    game.attackCard(attackingCard, defendingCard);
                }
            }
        } else {
            System.out.println("\nNo cards on table. Choose another option\n");
            printMenu();
        }
    }

    private void endPlayerTurn() {
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
            if (card instanceof UnitCard && ((UnitCard) card).getFatigue()) {
                outputNumber.append(String.format("%-30s", "Card #: " + ref.index +  RED + " (fatigued)" + RESET));
            } else {
                outputNumber.append(String.format("%-30s", "Card #: " + ref.index));
            }
            outputCost.append(String.format("%-30s", "Cost: " + card.getCost()));
            if (card instanceof UnitCard) {
                UnitCard unitCard = (UnitCard) card;
                String hpString = "Hp: " + unitCard.getCurrentHealth() + " max:(" + unitCard.getMaxHealth() + ")";
                String hpColor = unitCard.getCurrentHealth() < unitCard.getMaxHealth() ?
                        String.format("%-39s", "\u001B[31m" + hpString + "\u001B[0m") : String.format("%-38s", "\u001B[0m" + hpString + "\u001B[0m");
                outputHp.append(hpColor);
                outputName.append(String.format("%-41s", colorizeName(card.getName(), ((UnitCard) card).getRarity())));
                outputAtk.append(String.format("%-30s", "Atk: " + unitCard.getAttack()));
                outputType.append(String.format("%-30s", "Type: Unit card"));
            } else if (card instanceof SpellCard) {
                SpellCard spellCard = (SpellCard) card;
                String type = spellCard.getType().equals("Attacker") ? "Dmg: " : "heal: ";
                outputName.append(String.format("%-41s", CYAN_BRIGHT + card.getName() + RESET));
                outputHp.append(String.format("%-30s", type + spellCard.getValue()));
                outputAtk.append(String.format("%-30s", "Aoe: " + spellCard.isMany()));
                outputType.append(String.format("%-30s", "Type: Spell card"));
            } else if (card instanceof EffectCard) {
                EffectCard effectCard = (EffectCard) card;
                String target = effectCard.getEffectValue() < 0 ? "Debuff card" : "Buff card";
                String type = effectCard.getType().equals("Hp") ? "max hp" : "atk";
                String increase = effectCard.getEffectValue() < 0 ? "Decrease " : "Increase ";
                outputName.append(String.format("%-41s", RED + card.getName() + RESET));
                outputHp.append(String.format("%-30s", "Effect: " + increase + type));
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
        if (spellCard.isMany()) {
            game.useSpellOnCard(spellCard);
            return;
        }
        int chosenDefendingCard = input.validateActionOnPlayerOrCard(cards.size());
        if (chosenDefendingCard == 0) {
            game.useSpellOnPlayer(spellCard);
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

    private String colorizeName(String name, Rarity rarity) {
        String colorName = "";
        switch (rarity) {
            case COMMON:
                colorName = GREEN + name + RESET;
                break;
            case RARE:
                colorName = BLUE_BRIGHT + name + RESET;
                break;
            case EPIC:
                colorName = MAGENTA_BRIGHT + name + RESET;
                break;
            case LEGENDARY:
                colorName = YELLOW_BRIGHT + name + RESET;
                break;
        }
        return colorName;

    }
}
