import cards.Card;

import java.util.ArrayList;

public class Game {
    private ArrayList<Card> cardPile;
    private ArrayList<Card> trashPile;
    private final Player players[];


    public Game(ArrayList<Card> cardPile, ArrayList<Card> trashPile, Player[] players) {
        this.cardPile = cardPile;
        this.trashPile = trashPile;
        this.players = players;
    }

    public ArrayList<Card> getCardPile() {
        return cardPile;
    }

    public ArrayList<Card> getTrashPile() {
        return trashPile;
    }

    public Player[] getPlayers() {
        return players;
    }

    public boolean drawCard() {

        return true;
    }

    public boolean playCard() {

        return true;
    }

    public boolean attackCard(Card playedCard, Card enemyCard) {

        return true;
    }

    public boolean attackPlayer(Card card) {

        return true;
    }

    public boolean finishTurn() {

        return true;
    }

    public boolean finishGame() {

        return true;
    }

    public boolean initGame(Player p1, Player p2) {

        return true;
    }
}
