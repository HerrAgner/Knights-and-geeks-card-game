package cards;

import java.util.UUID;

public abstract class Card {
    private String name;
    private int cost;
    private UUID id;

    public Card() {

    }

    public Card(String name, int cost) {

        if (name.length() <= 0) {
            this.name = null;
            this.cost = 0;
            this.id = UUID.randomUUID();
        } else {
            this.name = name;
            this.cost = cost;
            this.id = UUID.randomUUID();

        }
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }


    public UUID getId() {
        return id;
    }

    public UUID getId(){
        return  id;
    }
}
