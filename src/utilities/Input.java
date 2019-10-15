package utilities;

import java.io.InputStream;
import java.util.Scanner;

public class Input {

    Scanner scan;

    public Input(){
        scan = new Scanner(System.in);
    }

    public Input(Scanner scan){
        this.scan = scan;
    }

    public int validatedInput(int max){
        return 0;
    }

    public boolean inputValidation(int max, int input){
        return false;
    }

}
