package game;

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
    private int maxNameLength;
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
        maxNameLength = playerOneName.length() >= playerTwoName.length() ?
                playerTwoName.length() : playerTwoName.length();
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
            printHpAndMana();
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
        printHpAndMana();
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
                        if (!spellCard.isMany()) {
                            System.out.println("Which card do you want to heal? (0 to heal you)");
                        }
                        useSpell(spellCard, cardsOnHand);
                    } else if (spellCard.getType().equals("Attacker")) {
                        printCards(enemyCardsOnTable);
                        if (!spellCard.isMany()) {
                            System.out.println("Which card do you want to attack? (0 to attack player)");
                        }
                        useSpell(spellCard, enemyCardsOnTable);
                    }
                    break;
                case EFFECT_CARD:
                    EffectCard effectCard = (EffectCard) card;
                    if (effectCard.getEffectValue() < 0) {
                        printCards(enemyCardsOnTable);
                        System.out.println("Which card do you want to debuff? [0] to return");

                        chosenDefendingCard = input.validateChosenCard(enemyCardsOnTable.size());
                        unitCard = (UnitCard) enemyCardsOnTable.toArray()[chosenDefendingCard - 1];
                        game.useEffectCard(effectCard, unitCard);
                        printEffectCardInfo(effectCard, unitCard);

                    } else {
                        printCards(cardsOnTable);
                        System.out.println("Which card do you want to buff? [0] to return");
                        chosenDefendingCard = input.validateChosenCard(cardsOnTable.size());
                        unitCard = (UnitCard) cardsOnTable.toArray()[chosenDefendingCard - 1];
                        game.useEffectCard(effectCard, unitCard);
                        printEffectCardInfo(effectCard, unitCard);
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
                    System.out.println("To many cards on the table. Max 7.");
                    break;
                case TABLE_EMPTY:
                    System.out.println("No cards on table");
                    break;
                case COST:
                    System.out.println("Not enough mana.");
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

    private String fixedNameString(String name, String extra, String colorDefault, String nameStyle) {
        int length = 1 + maxNameLength;
        if (extra != null) length += extra.length();
        if (colorDefault != null) length += colorDefault.length();
        if (nameStyle != null) length += nameStyle.length();
        String returnString = (nameStyle != null ? nameStyle : (colorDefault != null ? colorDefault : ""))
                + name + (nameStyle != null ? colorDefault : "") + (extra != null ? extra : "");
        return String.format("%-" + length + "s", returnString);
    }

    private void printHpAndMana() {
        String active = fixedNameString(" " + activePlayer.getName(), "'s health: "
                , BLACK + GREEN_BACKGROUND, BLACK_BOLD + GREEN_BACKGROUND)
                + String.format("%-20s", activePlayer.getHealth() + "/30  |  Mana: "
                + (activePlayer.getCurrentMana() < activePlayer.getMana() ? MAGENTA + GREEN_BACKGROUND : "")
                + activePlayer.getCurrentMana() + "/" + activePlayer.getMana() + " ");

        String defending = fixedNameString(" " + defendingPlayer.getName(), "'s health: "
                , BLACK + RED_BACKGROUND, BLACK_BOLD + RED_BACKGROUND)
                + String.format("%-20s", defendingPlayer.getHealth() + "/30 ");


        System.out.println(active + RESET);
        System.out.println(defending + RESET);

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
                    attackPlayerInfo(attackingCard);
                } else if (chosenDefendingCard <= enemyCardsOnTable.toArray().length) {
                    var defendingCard = (UnitCard) enemyCardsOnTable.toArray()[chosenDefendingCard - 1];
                    game.attackCard(attackingCard, defendingCard);
                    printAttackInfo(attackingCard, defendingCard);
                }
            }
//            printHpAndMana();
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

    private void printAttackInfo(UnitCard attackingCard, UnitCard defendingCard) {
        if (defendingCard.getCurrentHealth() <= 0 && attackingCard.getCurrentHealth() <= 0) {
            System.out.println("Both " + attackingCard.getName() + " and " + defendingCard.getName() + " died fighting. ");
        } else if (attackingCard.getAttack() >= defendingCard.getCurrentHealth()) {
            System.out.println(attackingCard.getName() + " killed " + defendingCard.getName() + " with a lethal attack.");
            System.out.println(attackingCard.getName() + "s' health is now: " + attackingCard.getCurrentHealth());
        } else if (attackingCard.getCurrentHealth() <= defendingCard.getAttack()) {
            System.out.println(attackingCard.getName() + " died while attacking " + defendingCard.getName() + ".");
            System.out.println(defendingCard.getName() + " lives with " + defendingCard.getCurrentHealth() + " hp.");
        } else {
            System.out.println(attackingCard.getName() + " attacked " + defendingCard.getName() + ".");
            System.out.println(attackingCard.getName() + " new health: " + attackingCard.getCurrentHealth());
            System.out.println(defendingCard.getName() + " new health: " + defendingCard.getCurrentHealth());
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
        }
    }

    private void hpBarAnimation(Card card) {
        String oneHp = String.format("%s", BLACK + RED_BACKGROUND + " ");
        String oneHpPlus = String.format("%s", BLACK + GREEN_BACKGROUND + " ");
        String hp = "";
        int counter = 0;
        int counter2 = 0;
        boolean healer = false;


        if (card instanceof UnitCard) {
            UnitCard unitCard = (UnitCard) card;
            counter = defendingPlayer.getHealth() + unitCard.getAttack();
            counter2 = counter;

        } else if (card instanceof SpellCard) {
            SpellCard spellCard = (SpellCard) card;
            if (spellCard.getType().equals("Healer")) {
                healer = true;
                counter = defendingPlayer.getHealth() - spellCard.getValue();
                counter2 = counter;
            } else {
                healer = false;
                counter = defendingPlayer.getHealth() + spellCard.getValue();
                counter2 = counter;
            }
        }

        if (healer) {
            for (int i = counter2; i <= defendingPlayer.getHealth(); i++) {
                hp = "";
                for (int j = 0; j < counter2; j++) {
                    if (j == counter2 / 2) {
                        hp = hp.concat(String.valueOf(counter2));
                    } else {
                        hp = hp.concat(oneHpPlus);
                    }
                }
                System.out.print("\r" + hp + RESET);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                counter2++;
            }
        } else {
            for (int i = defendingPlayer.getHealth(); i <= counter; i++) {
                hp = "";
                for (int j = 0; j < counter2; j++) {
                    if (j == counter2 / 2) {
                        hp = hp.concat(String.valueOf(counter2));
                    } else {
                        hp = hp.concat(oneHp);
                    }
                }
                System.out.print("\r" + hp + RESET);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                counter2--;
            }
        }

    }

    private void attackPlayerInfo(UnitCard attackingCard) {
        if (defendingPlayer.getHealth() <= 0) {
            System.out.println(attackingCard.getName() + " killed " + defendingPlayer.getName() + " with a deadly blow!");
        } else {
            System.out.println(defendingPlayer.getName() + " took " + attackingCard.getAttack() + " damage ");
        }
        hpBarAnimation(attackingCard);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {

        }
    }

    private void printSpellOnPlayerInfo(SpellCard spellCard) {
        if (spellCard.getType().equals("Attacker")) {
            if (defendingPlayer.getHealth() <= 0) {
                System.out.println(spellCard.getName() + " killed " + defendingPlayer.getName() + " with dark magic!");
            } else {
                System.out.println(spellCard.getName() + " inflicted " + spellCard.getValue() + " to " + defendingPlayer.getName());
                System.out.println("New health: " + defendingPlayer.getHealth());
            }
        } else {
            System.out.println(spellCard.getName() + " healed " + activePlayer.getName() + " with " + spellCard.getValue() + " hp.");
        }
        hpBarAnimation(spellCard);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {

        }
    }

    private void printAoeSpellInfo(SpellCard spell) {
        if (spell.getType().equals("Attacker")) {
            System.out.println(spell.getName() + " inflicted " + spell.getValue() + " dmg to all enemy cards.");
        } else {
            System.out.println(spell.getName() + " healed " + spell.getValue() + " hp to all your wounded cards.");
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {

        }
    }

    private void printSpellOnCardInfo(SpellCard spell, UnitCard card) {
        if (spell.getType().equals("Attacker")) {
            if (card.getCurrentHealth() <= 0) {
                System.out.println(spell.getName() + " killed " + card.getName() + " with dark magic.");
            } else {
                System.out.println(spell.getName() + " inflicted " + spell.getValue() + " dmg with dark magic");
                System.out.println(card.getName() + "'s new hp: " + card.getCurrentHealth());
            }
        } else {
            if (card.getCurrentHealth() == card.getMaxHealth()) {
                System.out.println("You healed for nothing, you fool!");
            } else {
                System.out.println(spell.getName() + " healed " + spell.getValue() + " hp with light magic");
                System.out.println(card.getName() + "'s new hp: " + card.getCurrentHealth());
            }
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {

        }
    }

    private void printEffectCardInfo(EffectCard effectCard, UnitCard unitCard) {
        if (effectCard.getEffectValue() > 0) {
            if (effectCard.getType().equals("Hp")) {
                System.out.printf("You buffed the card %50s with %s hp", unitCard.getName(), effectCard.getEffectValue());
            } else {
                System.out.printf("You buffed the card %50s with %s attack", unitCard.getName(), effectCard.getEffectValue());
            }
        } else {
            if (effectCard.getType().equals("Hp")) {
                System.out.printf("You debuffed the card %50s with %s hp", unitCard.getName(), effectCard.getEffectValue());
            } else {
                System.out.printf("You debuffed the card %50s with %s attack", unitCard.getName(), effectCard.getEffectValue());
            }
        }
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {

        }
    }

    private void printCards(Collection<Card> cards) {
        StringBuilder top = new StringBuilder();
        StringBuilder bottom = new StringBuilder();
        StringBuilder outputNumber = new StringBuilder();
        StringBuilder outputName = new StringBuilder();
        StringBuilder outputHp = new StringBuilder();
        StringBuilder outputAtk = new StringBuilder();
        StringBuilder outputMiddle = new StringBuilder();
        StringBuilder outputCost = new StringBuilder();
        StringBuilder outputType = new StringBuilder();
        var ref = new Object() {
            int index = 1;
        };

        cards.forEach(card -> {
            top.append("┌").append(String.format("%31s", "").replace(" ", "─")).append("┐");
            bottom.append("└").append(String.format("%31s", " ").replace(" ", "─")).append("┘");
            if (card instanceof UnitCard && ((UnitCard) card).getFatigue()) {
                outputNumber.append(String.format("%-41s", "Card #: " + ref.index + RED + " (fatigued)" + RESET));
            } else {
                outputNumber.append(String.format("%-30s", "Card #: " + ref.index));
            }
            outputCost.append(String.format("%-30s", "│ Cost: " + card.getCost()));
            outputMiddle.append(String.format("%-30s", "│"));
            if (card instanceof UnitCard) {
                UnitCard unitCard = (UnitCard) card;
                String hpString = "│ Hp: " + unitCard.getCurrentHealth() + " max:(" + unitCard.getMaxHealth() + ")";
                String hpColor = unitCard.getCurrentHealth() < unitCard.getMaxHealth() ?
                        String.format("%-39s", "\u001B[31m" + hpString + "\u001B[0m") : String.format("%-38s", "\u001B[0m" + hpString + "\u001B[0m");
                outputHp.append(hpColor);
                outputName.append(String.format("%-41s", "│ " + colorizeUnitCardName(card.getName(), ((UnitCard) card).getRarity())));
                outputAtk.append(String.format("%-30s", "│ Atk: " + unitCard.getAttack()));
                outputType.append(String.format("%-30s", "│ Type: Unit card"));
            } else if (card instanceof SpellCard) {
                SpellCard spellCard = (SpellCard) card;
                String type = spellCard.getType().equals("Attacker") ? "│ Dmg: " : "│ Heal: ";
                outputName.append(String.format("%-41s", "│ " + CYAN_BRIGHT + card.getName() + RESET));
                outputHp.append(String.format("%-30s", type + spellCard.getValue()));
                outputAtk.append(String.format("%-30s", "│ Aoe: " + spellCard.isMany()));
                outputType.append(String.format("%-30s", "│ Type: Spell card"));
            } else if (card instanceof EffectCard) {
                EffectCard effectCard = (EffectCard) card;
                String target = effectCard.getEffectValue() < 0 ? "Debuff card" : "Buff card";
                String type = effectCard.getType().equals("Hp") ? "max hp" : "atk";
                String increase = effectCard.getEffectValue() < 0 ? "Decrease " : "Increase ";
                outputName.append(String.format("%-41s", "│ " + RED + card.getName() + RESET));
                outputHp.append(String.format("%-30s", "│ Effect: " + increase + type));
                outputAtk.append(String.format("%-30s", "│ Amount: " + effectCard.getEffectValue()));
                outputType.append(String.format("%-30s", "│ Type: " + target));
            }
            top.append("   ");
            bottom.append("   ");
            outputNumber.append("      ");
            outputName.append("  │   ");
            outputHp.append("  │   ");
            outputAtk.append("  │   ");
            outputMiddle.append("  │   ");
            outputCost.append("  │   ");
            outputType.append("  │   ");
            ref.index++;
        });
        System.out.println(outputNumber);
        System.out.println(top);
        System.out.println(outputName);
        System.out.println(outputHp);
        System.out.println(outputAtk);
        System.out.println(outputMiddle);
        System.out.println(outputCost);
        System.out.println(outputType);
        System.out.println(bottom);
    }

    private void useSpell(SpellCard spellCard, Collection<Card> cards) {
        if (spellCard.isMany()) {
            game.useSpellOnCard(spellCard);
            printAoeSpellInfo(spellCard);
            return;
        }
        int chosenDefendingCard = input.validateActionOnPlayerOrCard(cards.size());
        if (chosenDefendingCard == 0) {
            game.useSpellOnPlayer(spellCard);
            printSpellOnPlayerInfo(spellCard);
        } else {
            UnitCard unitCard = (UnitCard) cards.toArray()[chosenDefendingCard - 1];
            game.useSpellOnCard(spellCard, unitCard);
            printSpellOnCardInfo(spellCard, unitCard);
        }
    }

    public String getPlayerOneName() {
        return playerOneName;
    }

    public String getPlayerTwoName() {
        return playerTwoName;
    }

    private String colorizeUnitCardName(String name, Rarity rarity) {
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
