package utilities;

public class Printer {

    public static void print(Object ... args) {
        for(Object text : args){
            if(text instanceof Object[]){
                for(Object innerText : (Object[]) text) {
                    System.out.println(innerText);
                }
            } else
                System.out.println(text);
        }
    }

    public static void printf(String format, Object ... args){
        System.out.printf(format, args);
    }
}
