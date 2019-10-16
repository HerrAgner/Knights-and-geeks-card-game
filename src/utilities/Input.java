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
        int inputNumber;

        while (!scan.hasNextInt()) {
            String input = scan.next();
            System.out.printf("\"%s\" is not a valid number.\n", input);
        }

        inputNumber = scan.nextInt();

        if (inputValidation(max, inputNumber)) {
            return inputNumber;
        }

        return 0;
    }

    public boolean inputValidation(int max, int input) {
        if (input < max) {
            return true;
        }
        System.out.println("Input too high");
        return false;
    }

}
