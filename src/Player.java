import cards.Card;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Player {
    private final String name;
    private int health;
    private int mana;
    private int maxMana;
    private Map<UUID, Card> cardsOnHand;
    private Map<UUID, Card> cardsOnTable;

    public Player() {
        this("");
    }

    public Player(String name) {
        if (name.length() > 0) {
            this.name = name;
            this.health = 30;
            this.mana = 0;
            this.maxMana=0;
            this.cardsOnHand = new ConcurrentHashMap<>();
            this.cardsOnTable = new ConcurrentHashMap<>();
        } else this.name = null;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public void changeHealth(int hpChange) {
        this.health = health + hpChange;
        if (this.health > 30) {
            this.health = 30;
        }
    }

    public void changeMaxMana(){
        if (this.maxMana<10){
            this.maxMana++;
        }
        this.mana=this.maxMana;
    }

    public int getMaxMana(){
        return this.maxMana;
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

    public boolean addCardToHand(Card card) {
        if (card != null) {
            cardsOnHand.put(card.getId(), card);
            return true;
        }
        return false;
    }

    public Card getCardFromHand(UUID id) {
        return id != null ? cardsOnHand.get(id) : null;
    }

    public Card removeCardFromHand(UUID id) {
        return id != null ? cardsOnHand.remove(id) : null;
    }

    public Collection<Card> getCardsOnTable() {
        return cardsOnTable.values();
    }

    public boolean addCardToTable(Card card) {
        if (card != null) {
            cardsOnTable.put(card.getId(), card);
            return true;
        }
        return false;
    }

    public Card getCardFromTable(UUID id) {
        return id != null ? cardsOnTable.remove(id) : null;
    }

    public Card removeCardFromTable(UUID id) {
        return id != null ? cardsOnTable.remove(id) : null;
    }
}
