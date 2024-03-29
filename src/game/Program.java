package game;

import cards.Card;
import cards.EffectCard;
import cards.SpellCard;
import cards.UnitCard;
import enums.Response;
import utilities.Input;

import java.util.Collection;

import static utilities.Messages.*;
import static utilities.Printer.print;

public class Program {
    private CLI cli;
    protected Game game;
    private boolean running;
    private Input input;
    private Player activePlayer;
    private Player defendingPlayer;
    private Collection<Card> cardsOnHand;
    private Collection<Card> cardsOnTable;
    private Collection<Card> enemyCardsOnTable;

    public Program() {
        input = new Input();
        cli = new CLI(this);
        cli.createPlayers();
    }

    void startGame(String playerOneName, String playerTwoName, int cardPileSize) {
        game = new Game(playerOneName, playerTwoName, cardPileSize);
        cli.setGame(game);
        gameLoop();
    }

    private void gameLoop() {
        while (running) {
            activePlayer = game.getCurrentPlayer();
            defendingPlayer = game.getDefendingPlayer();
            cardsOnHand = activePlayer.getCardsOnHand();
            cardsOnTable = activePlayer.getCardsOnTable();
            enemyCardsOnTable = defendingPlayer.getCardsOnTable();

            cli.setVariables();
            game.startTurn();

            System.out.println(activePlayer.getName() + TURN);

            boolean menu = true;
            while (menu) {
                print(cli.infoBoardAndCardsOnHand());
                print(cli.infoHpAndMana());
                print(MENU);
                menu = menuSwitch();
            }
        }
    }

    void setRunning(boolean running) {
        this.running = running;
    }

    private boolean menuSwitch() {
        int userInput;

        userInput = input.validatedInput(5);

        switch (userInput) {
            case 1:
                print(cli.infoBoardAndCardsOnHand());
                break;
            case 2:
                playCard();
                break;
            case 3:
                attackWithCard();
                break;
            case 4:
                print(END_TURN);
                game.finishTurn();
                return false;
            case 5:
                endGame();
                break;
            default:
                print(MENU);
                break;
        }
        if (!game.shouldGameContinue()) {
            running = false;
            return false;
        }
        return true;
    }

    private void playCard() {
        int chosenCard;
        int chosenDefendingCard;

        print(ENT_PLAY_CARD);
        print(cli.infoCardRow(cardsOnHand));
        chosenCard = input.validateChosenCard(cardsOnHand.size());

        if (chosenCard != 0) {
            Card card = (Card) cardsOnHand.toArray()[(chosenCard - 1)];
            Response[] response = game.playCard(card.getId());
            if (response[0] == Response.OK) {
                UnitCard unitCard;
                switch (response[1]) {
                    case SPELL_CARD:
                        SpellCard spellCard = (SpellCard) card;
                        if (spellCard.getType().equals("Healer")) {
                            print(cli.infoCardRow(cardsOnTable));
                            if (!spellCard.isMany()) {
                                print(ENT_HEAL_CARD);
                            }
                            useSpell(spellCard, cardsOnTable);
                        } else if (spellCard.getType().equals("Attacker")) {
                            print(cli.infoCardRow(enemyCardsOnTable));
                            if (!spellCard.isMany()) {
                                print(ENT_DEF_CARD);
                            }
                            useSpell(spellCard, enemyCardsOnTable);
                        }
                        break;
                    case EFFECT_CARD:
                        EffectCard effectCard = (EffectCard) card;
                        if (effectCard.getEffectValue() < 0) {
                            print(cli.infoCardRow(enemyCardsOnTable));
                            print(ENT_DEBUFF_CARD);
                            chosenDefendingCard = input.validateChosenCard(enemyCardsOnTable.size());
                            if (chosenDefendingCard == 0) {
                                break;
                            }
                            unitCard = (UnitCard) enemyCardsOnTable.toArray()[chosenDefendingCard - 1];
                            game.useEffectCard(effectCard, unitCard);
                            cli.printEffectCardInfo(effectCard, unitCard);

                        } else {
                            print(cli.infoCardRow(cardsOnTable));
                            print(ENT_BUFF_CARD);
                            chosenDefendingCard = input.validateChosenCard(cardsOnTable.size());
                            if (chosenDefendingCard == 0) {
                                break;
                            }
                            unitCard = (UnitCard) cardsOnTable.toArray()[chosenDefendingCard - 1];
                            game.useEffectCard(effectCard, unitCard);
                            cli.printEffectCardInfo(effectCard, unitCard);
                        }
                        sleep(2000);
                        break;
                    case UNIT_CARD:
                        print(UNIT_USED + card.getName());
                        break;
                    default:
                        // Crazy place! How did you get here?
                        break;
                }
            } else if (response[0] == Response.ERROR) {
                switch (response[1]) {
                    case TABLE_FULL:
                        print(FULL_CARDS);
                        sleep(2000);
                        break;
                    case TABLE_EMPTY:
                        print(NO_CARDS);
                        sleep(2000);
                        break;
                    case COST:
                        print(NO_MANA);
                        sleep(2000);
                        break;
                }
            }
        }
    }

    private void attackWithCard() {
        int chosenCard;
        int chosenDefendingCard;
        if (cardsOnTable.size() >= 1) {
            print(ENT_ATK_CARD);
            print(cli.infoCardRow(cardsOnTable));
            chosenCard = input.validateChosenCard(cardsOnTable.size());
            if (chosenCard != 0) {
                var attackingCard = (UnitCard) cardsOnTable.toArray()[chosenCard - 1];
                if (attackingCard.getFatigue()) {
                    print(INV_FATIGUE);
                    print(MENU);
                } else {
                    print(ENT_DEF_CARD);
                    print(cli.infoCardRow(enemyCardsOnTable));
                    chosenDefendingCard = input.validateActionOnPlayerOrCard(enemyCardsOnTable.size());
                    if (chosenDefendingCard != 0) {
                        if (chosenDefendingCard == 9) {
                            game.attackPlayer(attackingCard);
                            print(cli.infoAttackPlayer(attackingCard));
                            cli.hpBarAnimation(attackingCard);
                        } else if (chosenDefendingCard <= enemyCardsOnTable.toArray().length) {
                            var defendingCard = (UnitCard) enemyCardsOnTable.toArray()[chosenDefendingCard - 1];
                            game.attackCard(attackingCard, defendingCard);
                            print(cli.infoAttack(attackingCard, defendingCard));
                        }
                        sleep(2000);
                    }
                }
            }
        } else {
            print("", INV_NO_CARDS, "");
            sleep(2000);
        }
    }

    private void useSpell(SpellCard spellCard, Collection<Card> cards) {
        if (spellCard.isMany()) {
            game.useSpellOnCard(spellCard);
            print(cli.infoAoeSpell(spellCard));
            sleep(3000);
            return;
        }
        int chosenDefendingCard = input.validateActionOnPlayerOrCard(cards.size());
        if (chosenDefendingCard != 0) {
            if (chosenDefendingCard == 9) {
                game.useSpellOnPlayer(spellCard);
                print(cli.infoSpellOnPlayer(spellCard));
                cli.hpBarAnimation(spellCard);
            } else {
                UnitCard unitCard = (UnitCard) cards.toArray()[chosenDefendingCard - 1];
                game.useSpellOnCard(spellCard, unitCard);
                print(cli.infoSpellOnCard(spellCard, unitCard));
            }
            sleep(3000);
        } else {
            print(MENU);
        }
    }

    private void endGame() {
        System.out.println("Are you sure you want to end the game? (1 for yes, any other number for no)");
        if (input.validatedInput(1) == 1) {
            System.exit(0);
        }
    }

    void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
