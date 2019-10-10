package cards;

import java.util.UUID;

public abstract class Card {
    private String name;
    private int cost;
    private String type;
    private UUID id;

    public Card() {

    }

    public Card(String name, int cost) {

            this.name = name;
            this.cost = cost;
            this.type = type;
            this.id = UUID.randomUUID();

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

    public UUID getId() {
        return id;
    }
}
