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

            print(cli.printBoardAndCardsOnHand());
            print(cli.printHpAndMana());
            print(MENU);

            boolean menu = true;
            while (menu) {
                menu = menuSwitch();
            }
        }
    }

    void setRunning(boolean running) {
        this.running = running;
    }

    private boolean menuSwitch() {
        int userInput;
        boolean printAll = true;

        userInput = input.validatedInput(6);

        switch (userInput) {
            case 1:
                print(cli.printBoardAndCardsOnHand());
                break;
            case 2:
                cli.printHpAndMana();
                printAll = false;
                break;
            case 3:
                playCard();
                break;
            case 4:
                attackWithCard();
                break;
            case 5:
                print(END_TURN);
                game.finishTurn();
                return false;
            case 6:
                endGame();
                break;
            default:
                print(MENU);
                break;
        }
        if (!game.shouldGameContinue()) {
            running = false;
        }
        if (printAll) {
            cli.printBoardAndCardsOnHand();
        }
        print(cli.printHpAndMana());
        print(MENU);
        return true;
    }

    private void playCard() {
        int chosenCard;
        int chosenDefendingCard;

        print(ENT_PLAY_CARD);
        print(cli.printCards(cardsOnHand));
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
                            print(cli.printCards(cardsOnTable));
                            if (!spellCard.isMany()) {
                                print(ENT_HEAL_CARD);
                            }
                            useSpell(spellCard, cardsOnTable);
                        } else if (spellCard.getType().equals("Attacker")) {
                            print(cli.printCards(enemyCardsOnTable));
                            if (!spellCard.isMany()) {
                                print(ENT_DEF_CARD);
                            }
                            useSpell(spellCard, enemyCardsOnTable);
                        }
                        break;
                    case EFFECT_CARD:
                        EffectCard effectCard = (EffectCard) card;
                        if (effectCard.getEffectValue() < 0) {
                            print(cli.printCards(enemyCardsOnTable));
                            print(ENT_DEBUFF_CARD);
                            chosenDefendingCard = input.validateChosenCard(enemyCardsOnTable.size());
                            if (chosenDefendingCard == 0) {
                                break;
                            }
                            unitCard = (UnitCard) enemyCardsOnTable.toArray()[chosenDefendingCard - 1];
                            game.useEffectCard(effectCard, unitCard);
                            cli.printEffectCardInfo(effectCard, unitCard);

                        } else {
                            print(cli.printCards(cardsOnTable));
                            print(ENT_BUFF_CARD);
                            chosenDefendingCard = input.validateChosenCard(cardsOnTable.size());
                            if (chosenDefendingCard == 0) {
                                break;
                            }
                            unitCard = (UnitCard) cardsOnTable.toArray()[chosenDefendingCard - 1];
                            game.useEffectCard(effectCard, unitCard);
                            cli.printEffectCardInfo(effectCard, unitCard);
                        }
                        sleep(4000);
                        break;
                    case UNIT_CARD:
                        print(UNIT_USED + card.getName());
                        print(cli.printBoardAndCardsOnHand());
                        break;
                    default:
                        // Crazy place! How did you get here?
                        break;
                }
            } else if (response[0] == Response.ERROR) {
                switch (response[1]) {
                    case TABLE_FULL:
                        print(FULL_CARDS);
                        break;
                    case TABLE_EMPTY:
                        print(NO_CARDS);
                        break;
                    case COST:
                        print(NO_MANA);
                        break;
                }
            }
        } else {
            print(MENU);
        }
    }

    private void attackWithCard() {
        int chosenCard;
        int chosenDefendingCard;
        if (cardsOnTable.size() >= 1) {
            print(ENT_ATK_CARD);
            print(cli.printCards(cardsOnTable));
            chosenCard = input.validateChosenCard(cardsOnTable.size());
            if (chosenCard != 0) {
                var attackingCard = (UnitCard) cardsOnTable.toArray()[chosenCard - 1];
                if (attackingCard.getFatigue()) {
                    print(INV_FATIGUE);
                    print(MENU);
                } else {
                    print(ENT_DEF_CARD);
                    print(cli.printCards(enemyCardsOnTable));
                    chosenDefendingCard = input.validateActionOnPlayerOrCard(enemyCardsOnTable.size());
                    if (chosenDefendingCard != 0) {
                        if (chosenDefendingCard == 9) {
                            game.attackPlayer(attackingCard);
                            print(cli.attackPlayerInfo(attackingCard));
                            cli.hpBarAnimation(attackingCard);
                            sleep(3000);
                        } else if (chosenDefendingCard <= enemyCardsOnTable.toArray().length) {
                            var defendingCard = (UnitCard) enemyCardsOnTable.toArray()[chosenDefendingCard - 1];
                            game.attackCard(attackingCard, defendingCard);
                            print(cli.printAttackInfo(attackingCard, defendingCard));
                            sleep(3000);
                        }
                    }
                }
            }
        } else {
            print("", INV_NO_CARDS, "", MENU);
        }
    }
    
    private void useSpell(SpellCard spellCard, Collection<Card> cards) {
        if (spellCard.isMany()) {
            game.useSpellOnCard(spellCard);
            print(cli.printAoeSpellInfo(spellCard));
            sleep(3000);
            return;
        }
        int chosenDefendingCard = input.validateActionOnPlayerOrCard(cards.size());
        if (chosenDefendingCard != 0) {
            if (chosenDefendingCard == 9) {
                game.useSpellOnPlayer(spellCard);
                print(cli.printSpellOnPlayerInfo(spellCard));
                cli.hpBarAnimation(spellCard);
            } else {
                UnitCard unitCard = (UnitCard) cards.toArray()[chosenDefendingCard - 1];
                game.useSpellOnCard(spellCard, unitCard);
                print(cli.printSpellOnCardInfo(spellCard, unitCard));
            }
            sleep(3000);
        } else {
            print(MENU);
        }
    }

    private void endGame() {
        System.exit(0);
    }

    void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
