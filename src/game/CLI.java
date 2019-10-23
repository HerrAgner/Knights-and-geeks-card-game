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
import static utilities.Printer.print;
import static utilities.Printer.printf;

public class CLI {
    private int maxNameLength;
    private Scanner scan;
    private Program program;
    private Input input;
    private Player activePlayer;
    private Player defendingPlayer;
    private Collection<Card> cardsOnHand;
    private Collection<Card> cardsOnTable;
    private Collection<Card> enemyCardsOnTable;
    private Game game;
    Object[] menu = {
            "\nMake a move!",
            "1. Print cards from hand and table",
            "2. Print hp and mana",
            "3. Play card",
            "4. Attack with card",
            "5. End turn",
            "6. End game"};
    Object[] endTurn = {
            "Ending turn.\n",
            String.format("%-200s", BLACK_BACKGROUND_BRIGHT
                    + "\n" + YELLOW_BACKGROUND
                    + "\n" + BLACK_BACKGROUND_BRIGHT),
            RESET};


    public CLI(Program program) {
        scan = new Scanner(System.in);
        input = new Input();
        program.setRunning(true);
        this.program = program;
    }

    void setGame(Game game){
        this.game = game;
    }

    void createPlayers() {
        String playerOneName;
        String playerTwoName;
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
        print(activePlayer.getName() + "'s turn");

    }

    public void setVariables() {
        activePlayer = game.getCurrentPlayer();
        defendingPlayer = game.getDefendingPlayer();
        cardsOnHand = activePlayer.getCardsOnHand();
        cardsOnTable = activePlayer.getCardsOnTable();
        enemyCardsOnTable = defendingPlayer.getCardsOnTable();
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

    Object[] printBoardAndCardsOnHand() {
        return new Object[]{
                "\nDefending cards on table:",
                printCards(enemyCardsOnTable),
                "\nYour cards on table: ",
                printCards(cardsOnTable),
                "\nCards on hand:",
                printCards(cardsOnHand)
        };
    }

                            }
    Object printHpAndMana() {
        String active = BLACK_BOLD + GREEN_BACKGROUND + " "
                + String.format("%-" + (maxNameLength + 1) + "s", activePlayer.getName()) + " HP: "
                + String.format("%-20s", activePlayer.getHealth() + "/30  |  Mana: "
                + (activePlayer.getCurrentMana() < activePlayer.getMana() ? MAGENTA_BOLD + GREEN_BACKGROUND : "")
                + activePlayer.getCurrentMana() + "/" + activePlayer.getMana() + " ");
        String defending = BLACK_BOLD + RED_BACKGROUND + " "
                + String.format("%-" + (maxNameLength + 1) + "s", defendingPlayer.getName()) + " HP: "
                + String.format("%-20s", defendingPlayer.getHealth() + "/30");

        return new Object[]{"\n", active + RESET, defending + RESET};
    }

    Object printAttackInfo(UnitCard attackingCard, UnitCard defendingCard) {
        if (defendingCard.getCurrentHealth() <= 0 && attackingCard.getCurrentHealth() <= 0) {
            return "Both " + attackingCard.getName() + " and " + defendingCard.getName() + " died fighting. ";
        } else if (attackingCard.getAttack() >= defendingCard.getCurrentHealth()) {
            return new Object[]{
                    attackingCard.getName() + " killed " + defendingCard.getName() + " with a lethal attack.",
                    attackingCard.getName() + "s' health is now: " + attackingCard.getCurrentHealth()};
        } else if (attackingCard.getCurrentHealth() <= defendingCard.getAttack()) {
            return new Object[]{
                    attackingCard.getName() + " died while attacking " + defendingCard.getName() + ".",
                    defendingCard.getName() + " lives with " + defendingCard.getCurrentHealth() + " hp."};
        } else return new Object[]{
                    attackingCard.getName() + " attacked " + defendingCard.getName() + ".",
                    attackingCard.getName() + " new health: " + attackingCard.getCurrentHealth(),
                    defendingCard.getName() + " new health: " + defendingCard.getCurrentHealth()};
    }

    void hpBarAnimation(Card card) {
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
                counter = activePlayer.getHealth() - spellCard.getValue();
                counter2 = counter;
            } else {
                healer = false;
                counter = defendingPlayer.getHealth() + spellCard.getValue();
                counter2 = counter;
            }
        }

        if (healer) {
            //TODO fix max hp
            for (int i = counter2; i <= activePlayer.getHealth(); i++) {
                hp = "";
                for (int j = 0; j < counter2; j++) {
                    if (j == counter2 / 2) {
                        hp = hp.concat(String.valueOf(counter2));
                    } else {
                        hp = hp.concat(oneHpPlus);
                    }
                }
                System.out.print("\r" + hp + RESET);
                program.sleep(500);
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
                program.sleep(500);
                counter2--;
            }
        }

    }

    Object attackPlayerInfo(UnitCard attackingCard) {
        if (defendingPlayer.getHealth() <= 0) {
            return attackingCard.getName() + " killed " + defendingPlayer.getName() + " with a deadly blow!";
        } else {
            return defendingPlayer.getName() + " took " + attackingCard.getAttack() + " damage ";
        }
    }

    Object printSpellOnPlayerInfo(SpellCard spellCard) {
        if (spellCard.getType().equals("Attacker")) {
            if (defendingPlayer.getHealth() <= 0) {
                return spellCard.getName() + " killed " + defendingPlayer.getName() + " with dark magic!";
            } else {
                return new Object[]{
                        spellCard.getName() + " inflicted " + spellCard.getValue() + " to " + defendingPlayer.getName(),
                        "New health: " + defendingPlayer.getHealth()};
            }
        } else {
            return spellCard.getName() + " healed " + activePlayer.getName() + " with " + spellCard.getValue() + " hp.";
        }
    }

    Object printAoeSpellInfo(SpellCard spell) {
        if (spell.getType().equals("Attacker")) {
            return spell.getName() + " inflicted " + spell.getValue() + " dmg to all enemy cards.";
        } else {
            return spell.getName() + " healed " + spell.getValue() + " hp to all your wounded cards.";
        }
    }

    Object printSpellOnCardInfo(SpellCard spell, UnitCard card) {
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
    }

    void printEffectCardInfo(EffectCard effectCard, UnitCard unitCard) {
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
    }

    Object[] printCards(Collection<Card> cards) {
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
    }

    String colorizeUnitCardName(String name, Rarity rarity) {
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