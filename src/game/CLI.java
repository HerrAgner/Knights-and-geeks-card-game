package game;

import cards.Card;
import cards.EffectCard;
import cards.SpellCard;
import cards.UnitCard;
import utilities.Input;
import enums.*;

import java.util.Collection;
import java.util.PrimitiveIterator;
import java.util.Scanner;

import static utilities.CLIColors.*;
import static utilities.Printer.print;
import static utilities.Printer.printf;

public class CLI {
    private String playerOneName, playerTwoName;
    private int maxNameLength;
    private Scanner scan;
    private Program program;
    private Input input;
    private Player activePlayer;
    private Player defendingPlayer;
    private Collection<Card> cardsOnHand;
    private Collection<Card> cardsOnTable;
    private Collection<Card> enemyCardsOnTable;
    private Object[] menu = {
            "\nMake a move!",
            "1. Print cards from hand and table",
            "2. Print hp and mana",
            "3. Play card",
            "4. Attack with card",
            "5. End turn"};
    private Object[] endTurn = {
            "Ending turn.",
            "---------------------------------------------------------------------------------------------",
            "",
            ""};

    public CLI(Program program) {
        scan = new Scanner(System.in);
        input = new Input();
        program.setRunning(true);
        this.program = program;
    }

    protected void createPlayers() {
        System.out.println("Enter name for player 1");
        playerOneName = scan.nextLine();
        while (playerOneName.length() == 0 || playerOneName.length() > 10) {
            print("Invalid name. Max length is 10 characters, please enter a new one.");
            playerOneName = scan.nextLine();
        }
        print("Enter name for player 2");
        playerTwoName = scan.nextLine();
        while (playerTwoName.length() == 0 || playerTwoName.length() > 10 || playerOneName.equals(playerTwoName)) {
            print("Invalid name. Max length is 10 characters and has to be different from Player One. " +
                    "\nPlease enter a new name!");
            playerTwoName = scan.nextLine();
        }
        if (playerOneName.toLowerCase().equals("cheetah") && playerTwoName.toLowerCase().equals("zebra")) {
            Lurig lurig = new Lurig();
        }
        maxNameLength = Math.max(playerOneName.length(), playerTwoName.length());

        program.startGame(playerOneName, playerTwoName, choseCardPileSize());
        System.out.println(activePlayer.getName() + "'s turn");

    }

    public int choseCardPileSize() {
        print("Enter your desired card pile size");
        int cardSize = input.validatedInput(100);
        while (cardSize < 45 || cardSize > 100) {
            print("Invalid size, chose number between 45-100");
            cardSize = input.validatedInput(100);
        }
        return cardSize;
    }

    public void setVariables() {
            activePlayer = program.game.getCurrentPlayer();
            defendingPlayer = program.game.getDefendingPlayer();
            cardsOnHand = activePlayer.getCardsOnHand();
            cardsOnTable = activePlayer.getCardsOnTable();
            enemyCardsOnTable = defendingPlayer.getCardsOnTable();
    }


            print(activePlayer.getName() + "'s turn");

            print(printBoardAndCardsOnHand());
            print(printHpAndMana());
            print(menu);

            boolean menu = true;
            while (menu) {
                menu = menuSwitch();
            }

        }
    }

//    private Object[] printMenu() {
//        return new Object[]{
//                "\nMake a move!",
//                "1. Print cards from hand and table",
//                "2. Print hp and mana",
//                "3. Play card",
//                "3. Play card",
//                "4. Attack with card",
//                "5. End turn"};
////        );
////        print("\nMake a move!");
////        print("1. Print cards from hand and table\n" +
////                "2. Print hp and mana\n" +
////                "3. Play card\n" +
////                "4. Attack with card\n" +
////                "5. End turn\n");
//    }

    private boolean menuSwitch() {
        int userInput;
        boolean printAll = true;

        userInput = input.validatedInput(6);

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
//                endPlayerTurn();
                print(endTurn);
                game.finishTurn();
                return false;
            case 6:
                endGame();
                break;
            default:
                print(menu);
                break;
        }
        if (!game.shouldGameContinue()) {
            running = false;
        }
        if (printAll) {
            print(printBoardAndCardsOnHand());
        }
        print(printHpAndMana());
        print(menu);
        return true;
    }

    private void endGame(){
        System.exit(0);
    }

    protected void playCard() {
        int chosenCard;
        int chosenDefendingCard;

        print("Which card do you want to play?");
        print(printCards(cardsOnHand));

        chosenCard = input.validateChosenCard(cardsOnHand.size());

        Card card = (Card) cardsOnHand.toArray()[(chosenCard - 1)];

        Response[] response = program.game.playCard(card.getId());

        if (response[0] == Response.OK) {
            UnitCard unitCard;
            switch (response[1]) {
                case SPELL_CARD:
                    SpellCard spellCard = (SpellCard) card;
                    if (spellCard.getType().equals("Healer")) {
                        print(printCards(cardsOnTable));
                        if (!spellCard.isMany()) {
                            print("Which card do you want to heal? (0 to heal you)");
                        }
                        useSpell(spellCard, cardsOnHand);
                    } else if (spellCard.getType().equals("Attacker")) {
                        print(printCards(enemyCardsOnTable));
                        if (!spellCard.isMany()) {
                            print("Which card do you want to attack? (0 to attack player)");
                        }
                        useSpell(spellCard, enemyCardsOnTable);
                    }
                    break;
                case EFFECT_CARD:
                    EffectCard effectCard = (EffectCard) card;
                    if (effectCard.getEffectValue() < 0) {
                        print(printCards(enemyCardsOnTable));
                        print("Which card do you want to debuff?");

                        chosenDefendingCard = input.validateChosenCard(enemyCardsOnTable.size());
                        unitCard = (UnitCard) enemyCardsOnTable.toArray()[chosenDefendingCard - 1];
                        program.game.useEffectCard(effectCard, unitCard);
                        printEffectCardInfo(effectCard, unitCard);


                    } else {
                        print(printCards(cardsOnTable));
                        print("Which card do you want to buff?");
                        chosenDefendingCard = input.validateChosenCard(cardsOnTable.size());
                        unitCard = (UnitCard) cardsOnTable.toArray()[chosenDefendingCard - 1];
                        program.game.useEffectCard(effectCard, unitCard);
                        printEffectCardInfo(effectCard, unitCard);
                    }
                    sleep(4000);
                    break;
                case UNIT_CARD:
                    print("Played card " + card.getName());
                    break;
                default:
                    // Crazy place! How did you get here?
                    break;
            }
        } else if (response[0] == Response.ERROR) {
            switch (response[1]) {
                case TABLE_FULL:
                    print("To many cards on the table. Max 7.");
                    break;
                case TABLE_EMPTY:
                    print("No cards on table");
                    break;
                case COST:
                    print("Not enough mana.");
                    break;
            }
        }
    }

    private Object[] printBoardAndCardsOnHand() {
        return new Object[]{
                "",
                "Defending cards on table:",
                printCards(enemyCardsOnTable),
                "",
                "Your cards on table: ",
                printCards(cardsOnTable),
                "",
                "Cards on hand:",
                printCards(cardsOnHand)
        };
//        print("\nDefending cards on table:");
//        print(printCards(enemyCardsOnTable));
//        print("\nYour cards on table: ");
//        print(printCards(cardsOnTable));
//        print("\nCards on hand:");
//        print(printCards(cardsOnHand));
    }

    private Object[] printHpAndMana() {
        String active = BLACK_BOLD + GREEN_BACKGROUND + " "
                + String.format("%-" + (maxNameLength+1) + "s", activePlayer.getName()) + " HP: "
                + String.format("%-20s", activePlayer.getHealth() + "/30  |  Mana: "
                + (activePlayer.getCurrentMana() < activePlayer.getMana() ? MAGENTA_BOLD + GREEN_BACKGROUND : "")
                + activePlayer.getCurrentMana() + "/" + activePlayer.getMana() + " ");
        String defending = BLACK_BOLD + RED_BACKGROUND + " "
                + String.format("%-" + (maxNameLength+1) + "s", defendingPlayer.getName()) + " HP: "
                + String.format("%-20s", activePlayer.getHealth() + "/30");
//        print("\n" + active + RESET);
//        print(defending + RESET);
        return new Object[]{
                "",
                active + RESET,
                defending + RESET
        };
    }

    protected void attackWithCard() {
        int chosenCard;
        int chosenDefendingCard;
        if (cardsOnTable.size() >= 1) {
            print("Choose card: ");
            print(printCards(cardsOnTable));
            chosenCard = input.validateChosenCard(cardsOnTable.size());
            var attackingCard = (UnitCard) cardsOnTable.toArray()[chosenCard - 1];
            if (attackingCard.getFatigue()) {
                print("\nCard is fatigue, wait one turn to attack!\n");
                print(menu);
            } else {
                print("Attack card or player (0 for player): ");
                print(printCards(enemyCardsOnTable));
                chosenDefendingCard = input.validateActionOnPlayerOrCard(enemyCardsOnTable.size());

                if (chosenDefendingCard == 0) {
                    program.game.attackPlayer(attackingCard);
                    print(attackPlayerInfo(attackingCard));
                    sleep(3000);
                    hpBarAnimation(attackingCard);
                } else if (chosenDefendingCard <= enemyCardsOnTable.toArray().length) {
                    var defendingCard = (UnitCard) enemyCardsOnTable.toArray()[chosenDefendingCard - 1];
                    program.game.attackCard(attackingCard, defendingCard);
                    print(printAttackInfo(attackingCard, defendingCard));
                    sleep(3000);
                }
            }
//            print(printHpAndMana());
        } else {
            print(
                    "",
                    "No cards on table. Choose another option",
                    "",
                    menu);
//            print(menu);
        }
    }

//    private void endPlayerTurn() {
//        print(
//                "Ending turn.",
//                "---------------------------------------------------------------------------------------------",
//                "",
//                "");
////        print("---------------------------------------------------------------------------------------------");
////        print("\n\n");
//        game.finishTurn();
//    }

    private Object[] printAttackInfo(UnitCard attackingCard, UnitCard defendingCard) {
        if (defendingCard.getCurrentHealth() <= 0 && attackingCard.getCurrentHealth() <= 0) {
            return new Object[]{"Both " + attackingCard.getName() + " and " + defendingCard.getName() + " died fighting. "};
        } else if (attackingCard.getAttack() >= defendingCard.getCurrentHealth()) {
            return new Object[]{
                    attackingCard.getName() + " killed " + defendingCard.getName() + " with a lethal attack.",
                    attackingCard.getName() + "s' health is now: " + attackingCard.getCurrentHealth()};
//            print(attackingCard.getName() + "s' health is now: " + attackingCard.getCurrentHealth());
        } else if (attackingCard.getCurrentHealth() <= defendingCard.getAttack()) {
            return new Object[]{
                attackingCard.getName() + " died while attacking " + defendingCard.getName() + ".",
                defendingCard.getName() + " lives with " + defendingCard.getCurrentHealth() + " hp."};
        } else {
            return new Object[]{
                attackingCard.getName() + " attacked " + defendingCard.getName() + ".",
                attackingCard.getName() + " new health: " + attackingCard.getCurrentHealth(),
                defendingCard.getName() + " new health: " + defendingCard.getCurrentHealth()};
        }
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//        }
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
            //TODO fix max hp
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
                sleep(500);
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
                sleep(500);
                counter2--;
            }
        }

    }

    private Object attackPlayerInfo(UnitCard attackingCard) {
        if (defendingPlayer.getHealth() <= 0) {
            return attackingCard.getName() + " killed " + defendingPlayer.getName() + " with a deadly blow!";
        } else {
            return defendingPlayer.getName() + " took " + attackingCard.getAttack() + " damage ";
        }
//        hpBarAnimation(attackingCard);
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//
//        }
    }

    private Object printSpellOnPlayerInfo(SpellCard spellCard) {
        if (spellCard.getType().equals("Attacker")) {
            if (defendingPlayer.getHealth() <= 0) {
                return spellCard.getName() + " killed " + defendingPlayer.getName() + " with dark magic!";
            } else {
                return new Object[]{
                        spellCard.getName() + " inflicted " + spellCard.getValue()+" to "+defendingPlayer.getName(),
                        "New health: " + defendingPlayer.getHealth()};
            }
        } else {
            return spellCard.getName() + " healed " + activePlayer.getName() + " with " +spellCard.getValue() + " hp.";
        }
//        hpBarAnimation(spellCard);
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//
//        }
    }

    private Object printAoeSpellInfo(SpellCard spell) {
        if (spell.getType().equals("Attacker")) {
            return spell.getName() + " inflicted " + spell.getValue() + " dmg to all enemy cards.";
        } else {
            return spell.getName() + " healed " + spell.getValue() + " hp to all your wounded cards.";
        }
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//
//        }
    }

    private Object printSpellOnCardInfo(SpellCard spell, UnitCard card) {
        if (spell.getType().equals("Attacker")) {
            if (card.getCurrentHealth() <= 0) {
                return spell.getName() + " killed " + card.getName() + " with dark magic.";
            } else {
                return new Object[]{
                        spell.getName() + " inflicted " + spell.getValue() + " dmg with dark magic",
                        card.getName() + "'s new hp: " + card.getCurrentHealth()};
            }
        } else {
            if (card.getCurrentHealth() == card.getMaxHealth()) {
                return ("You healed for nothing, you fool!");
            } else {
                return new Object[]{
                        spell.getName() + " healed " + spell.getValue() + " hp with light magic",
                        card.getName() + "'s new hp: " + card.getCurrentHealth()};
            }
        }
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//
//        }
    }

    private void printEffectCardInfo(EffectCard effectCard, UnitCard unitCard) {
        if (effectCard.getEffectValue() > 0) {
            if (effectCard.getType().equals("Hp")) {
                printf("You buffed the card %s with %s hp", unitCard.getName(), effectCard.getEffectValue());
            } else {
                printf("You buffed the card %s with %s attack", unitCard.getName(), effectCard.getEffectValue());
            }
        } else {
            if (effectCard.getType().equals("Hp")) {
                printf("You debuffed the card %s with %s hp", unitCard.getName(), effectCard.getEffectValue());
            } else {
                printf("You debuffed the card %s with %s attack", unitCard.getName(), effectCard.getEffectValue());
            }
        }
//        try {
//            Thread.sleep(4000);
//        } catch (InterruptedException e) {
//
//        }
    }

    private Object[] printCards(Collection<Card> cards) {
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
        return new Object[]{
                outputNumber,
                top,
                outputName,
                outputHp,
                outputAtk,
                outputMiddle,
                outputCost,
                outputType,
                bottom};
//        print(outputNumber);
//        print(top);
//        print(outputName);
//        print(outputHp);
//        print(outputAtk);
//        print(outputMiddle);
//        print(outputCost);
//        print(outputType);
//        print(bottom);
    }

    private void useSpell(SpellCard spellCard, Collection<Card> cards) {
        if (spellCard.isMany()) {
            program.game.useSpellOnCard(spellCard);
            print(printAoeSpellInfo(spellCard));
            sleep(3000);
            return;
        }
        int chosenDefendingCard = input.validateActionOnPlayerOrCard(cards.size());
        if (chosenDefendingCard == 0) {
            program.game.useSpellOnPlayer(spellCard);
            print(printSpellOnPlayerInfo(spellCard));
            hpBarAnimation(spellCard);
            sleep(3000);
        } else {
            UnitCard unitCard = (UnitCard) cards.toArray()[chosenDefendingCard - 1];
            program.game.useSpellOnCard(spellCard, unitCard);
            print(printSpellOnCardInfo(spellCard, unitCard));
            sleep(3000);
        }
    }

//    public String getPlayerOneName() {
//        return playerOneName;
//    }
//
//    public String getPlayerTwoName() {
//        return playerTwoName;
//    }

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

    private void sleep(int ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
