package utilities;

import java.io.InputStream;
import java.util.Scanner;

public class Input {

    Scanner scan;

    public Input() {
        scan = new Scanner(System.in);
    }
    private String pattern = ("\\S+");

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

 /*   public boolean inputSpaceValidation(int input){
        String s = (""+input);
        if(s.matches(pattern)){
            System.out.println("input contains space");
            return false;
        }
        s.replaceAll(pattern, "");
        System.out.println(s);
        return true;
    } */

    public boolean inputValidation(int max, int input) {
        if (input < max) {
            return true;
        }
        System.out.println("Input too high");
        return false;
    }

}
