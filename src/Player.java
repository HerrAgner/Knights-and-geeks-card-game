import cards.Card;

import java.util.ArrayList;

public class Player {
    private final String name;
    private int health;
    private int mana;
    private ArrayList<Card> cardsOnHand;
    private ArrayList<Card> cardsOnTable;

    public Player(){
        this("");
    }

    public Player(String name) {
        if (name.length() > 0) {
            this.name = name;
            this.health = 30;
            this.mana = 1;
        }
            else this.name = null;
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
    
    public void addCardToHand(Card card){
        cardsOnHand.add(card);
    }

    public ArrayList<Card> getCardsOnTable() {
        return cardsOnTable;
    }

    public void setCardsOnTable(ArrayList<Card> cardsOnTable) {
        this.cardsOnTable = cardsOnTable;
    }
}
