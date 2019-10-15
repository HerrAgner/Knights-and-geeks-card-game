import cards.Card;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Player {
    private final String name;
    private int health;
    private int mana;
    private Map<UUID, Card> cardsOnHand;
    private Map<UUID, Card> cardsOnTable;

    public Player(){
        this("");
    }

    public Player(String name) {
        if (name.length() > 0) {
            this.name = name;
            this.health = 30;
            this.mana = 0;
            this.cardsOnHand = new ConcurrentHashMap<>();
            this.cardsOnTable = new ConcurrentHashMap<>();
        }
            else this.name = null;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public void changeHealth(int hpChange) {
        this.health = health + hpChange;
        if (this.health > 30){
            this.health = 30;
        }
    }

    public int getMana() {
        return mana;
    }

    public void changeMana(int mana) {
        this.mana = this.mana + mana;
    }



    public Collection<Card> getCardsOnHand() {
        return cardsOnHand.values();
    }

    public boolean addCardToHand(Card card){
        if(card != null) {
            cardsOnHand.put(card.getId(), card);
            return true;
        }
        return false;
    }

    public Card getCardFromHand(UUID id){
        return id != null ? cardsOnHand.get(id) : null;
    }

    public Card removeCardFromHand(UUID id){
        return cardsOnHand.remove(id);
    }

    public Collection<Card> getCardsOnTable() {
        return cardsOnTable.values();
    }

    public void addCardToTable(Card card) {
        cardsOnTable.put(card.getId(), card);
    }

        public Card getCardFromTable(UUID id) {
        return cardsOnTable.get(id);
    }

    public Card removeCardFromTable(UUID id){
        return cardsOnTable.remove(id);
    }

    public void setCardsOnTable(Map<UUID, Card> cardsOnTable) {
        this.cardsOnTable = cardsOnTable;
    }
}
