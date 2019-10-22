package game;

import utilities.Input;
import static utilities.Printer.print;

public class Program {
    private CLI cli;
    protected Game game;
    private boolean running;
    private Input input;

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
            Player activePlayer = game.getCurrentPlayer();

            cli.setVariables();
            game.startTurn();

            System.out.println(activePlayer.getName() + "'s turn");

            print(cli.printBoardAndCardsOnHand());
            print(cli.printHpAndMana());
            print(cli.menu);

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
                break;
            case 2:
                cli.printHpAndMana();
                printAll = false;
                break;
            case 3:
                cli.playCard();
                break;
            case 4:
                cli.attackWithCard();
                break;
            case 5:
                print(cli.endTurn);
                game.finishTurn();
                return false;
            case 6:
                cli.endGame();
                break;
            default:
                print(cli.menu);
                break;
        }
        if (!game.shouldGameContinue()) {
            running = false;
        }
        if (printAll) {
            cli.printBoardAndCardsOnHand();
        }
        cli.printHpAndMana();
        print(cli.menu);
        return true;
    }
}
