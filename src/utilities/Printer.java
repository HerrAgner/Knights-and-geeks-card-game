package utilities;

public class Printer {

    public static void print(Object... args) {
        for (Object printObject : args) {
            if (printObject instanceof Object[]) {
                for (Object innerObject : (Object[]) printObject) {
                    System.out.println(innerObject);
                }
            } else
                System.out.println(printObject);
        }
    }

    public static void printf(String format, Object... args) {
        System.out.printf(format, args);
    }
}
