import cards.Card;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
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
            this.mana = 1;
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
    }

    public int getMana() {
        return mana;
    }

    public void changeMana(int mana) {
        this.mana = this.mana + mana;
    }

    public Map<UUID, Card> getCardsOnHand() {
        return cardsOnHand;
    }

    public void addCardToHand(Card card){
        cardsOnHand.put(card.getId(), card);
    }

    public Card getCardFromHand(UUID id){
        return cardsOnHand.get(id);
    }

    public Map<UUID, Card> getCardsOnTable() {
        return cardsOnTable;
    }

    public void addCardToTable(Card card) {
        cardsOnTable.put(card.getId(), card);
    }



}
