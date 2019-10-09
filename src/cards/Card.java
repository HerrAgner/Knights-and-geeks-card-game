package cards;

public abstract class Card {
    private String name;
    private int cost;
    private String type;

    public Card(String name, int cost, String type) {
        this.name = name;
        this.cost = cost;
        this.type = type;
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
