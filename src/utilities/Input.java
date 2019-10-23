package utilities;
import cards.EffectCard;
import game.Player;
import java.util.Scanner;
import java.util.UUID;


public class Input {
    Scanner scan;

    public Input() {
        scan = new Scanner(System.in);
    }

    public Input(Scanner scan) {
        this.scan = scan;
    }

    public int validatedInput(int max) {
        int input;
        while (true) {
            try {
                input = Integer.parseInt(scan.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.printf("Not a valid choice. Please enter a number between 1 and %s\n", max);
            }
        }
        if (inputValidation(max, input)) {
            return input;
        }
        return 0;
    }

    public boolean inputValidation(int max, int input) {
        if (input <= max && input >= 0) {
            return true;
        }
        return false;
    }

    public int validateChosenCard(int max) {
        int input;
        while (true) {
            try {
                input = Integer.parseInt(scan.nextLine());
                if (input > 0 && input <= max || input == 0) {
                    return input;
                }
                else {
                    System.out.println("Not a valid number");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input");
            }
        }
    }

    public int validateActionOnPlayerOrCard(int max) {
        int input;
        while (true) {
            try {
                input = Integer.parseInt(scan.nextLine());
                if (input == 9 || input == 0 || input <= max && input >= 1 ) {
                    return input;
                } else {
                    if (max == 0) {
                        System.out.println("You can only attack the player, option 9.");
                    } else {
                        System.out.println("Select a number between 1 and " + max + "or 9 to attack player");
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input");
            }
        }
    }

    public boolean validateEmptyTable(UUID id, Player activePlayer, Player defendingPlayer) {
        if (activePlayer.getCardFromHand(id) instanceof EffectCard) {
            if (activePlayer.getCardsOnTable().size() == 0 && ((EffectCard) activePlayer.getCardFromHand(id)).getEffectValue() > 0) {
                return true;
            } else if (defendingPlayer.getCardsOnTable().size() == 0 && ((EffectCard) activePlayer.getCardFromHand(id)).getEffectValue() < 0)
                return true;
        }
        return false;
    }
}
