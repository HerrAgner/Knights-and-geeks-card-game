package utilities;

import java.io.InputStream;
import java.util.Scanner;

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
        System.out.println("Input out of range");
        return false;
    }

}
