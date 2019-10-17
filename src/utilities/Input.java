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
        while(true){
            try{
            input= Integer.parseInt(scan.nextLine());
            break;}
            catch(NumberFormatException e){
                System.out.println("Not a valid number");
            }
        }
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
        if (input < max && input >= 0) {
            return true;
        }
        System.out.println("Input out of range");
        return false;
    }

}
