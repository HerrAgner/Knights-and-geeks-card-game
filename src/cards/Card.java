package cards;

import java.util.UUID;

public abstract class Card {
    private String name;
    private int cost;
    private String type;
    private UUID id;

    public Card() {

    }

    public Card(String name, int cost, String type) {
        if (name.length() <= 0 || type.length() <= 0) {
            this.name = null;
            this.cost = 0;
            this.type = null;
            this.id = null;

        } else {
            this.name = name;
            this.cost = cost;
            this.type = type;
            this.id = UUID.randomUUID();
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

    public UUID getId() {
        return id;
    }
}
