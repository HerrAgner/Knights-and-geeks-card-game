import cards.Card;

import java.util.ArrayList;

public class Player {
    private final String name;
    private int health;
    private int mana;
    private ArrayList<Card> cardsOnHand;
    private ArrayList<Card> cardsOnTable;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public ArrayList<Card> getCardsOnHand() {
        return cardsOnHand;
    }

    public void setCardsOnHand(ArrayList<Card> cardsOnHand) {
        this.cardsOnHand = cardsOnHand;
    }

    public ArrayList<Card> getCardsOnTable() {
        return cardsOnTable;
    }

    public void setCardsOnTable(ArrayList<Card> cardsOnTable) {
        this.cardsOnTable = cardsOnTable;
    }
}
