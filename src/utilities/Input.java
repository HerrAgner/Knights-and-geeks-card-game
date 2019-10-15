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
        while (!scan.hasNextInt()){
            System.out.println("Not a valid number, try again!");
            scan.next();
        }

        input = scan.nextInt();
        if (inputValidation(max, input)) {
            return input;
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
