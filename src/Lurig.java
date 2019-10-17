import secret.Game;


/**
 * Project structure - Module - Dependencies - add library from java.
 * Find "Secret.jar" in src/utilites
 */
public class Lurig {
    public Lurig() {
        Game g = new Game();
        try {
            g.run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
