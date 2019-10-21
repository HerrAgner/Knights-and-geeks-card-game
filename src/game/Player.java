package game;

import cards.Card;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Player {
    private final String name;
    private int health;
    private int currentMana;
    private int mana;
    private Map<UUID, Card> cardsOnHand;
    private Map<UUID, Card> cardsOnTable;

    public Player() {
        this("");
    }

    public Player(String name) {
        if (name.length() > 0) {
            this.name = name;
            this.health = 30;
            this.currentMana = 0;
            this.mana =0;
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
        if (this.mana <10){
            this.mana++;
        }
        this.currentMana =this.mana;
    }

    public int getMana(){
        return this.mana;
    }

    public int getCurrentMana() {
        return currentMana;
    }

    public void changeMana(int mana) {
        this.currentMana = this.currentMana + mana;
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
