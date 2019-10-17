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



    public boolean inputValidation(int max, int input) {
        if (input <= max) {
            return true;
        }
        System.out.println("Input too high");
        return false;
    }

}
