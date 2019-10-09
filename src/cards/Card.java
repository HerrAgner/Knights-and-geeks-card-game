package cards;

public class Card {
    private String name;
    private int cost;
    private String type;

    public Card() {

    }

    public Card(String name, int cost, String type) {
        if (name.length() <= 0 || type.length() <= 0) {
            this.name = null;
            this.cost = 0;
            this.type = null;

        } else {
            this.name = name;
            this.cost = cost;
            this.type = type;
        }
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public String getType() {
        return type;
    }
}
