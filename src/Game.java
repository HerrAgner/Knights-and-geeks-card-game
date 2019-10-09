import cards.Card;

import java.util.ArrayList;

public class Game {

    private ArrayList<Card> cardPile;
    private ArrayList<Card> trashPile;
    private final Player players[];
    private int activePlayer;
    private int round;


    public Game(Player[] players) {
        this.players = players;
    }

    public int getRound() {
        return round;
    }

    public int getActivePlayer() {
        return activePlayer;
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

    public void setCardPile(ArrayList<Card> cardPile) {
        this.cardPile = cardPile;
    }

    public void setTrashPile(ArrayList<Card> trashPile) {
        this.trashPile = trashPile;
    }

    public void setActivePlayer(int activePlayer) {
        this.activePlayer = activePlayer;
    }

    public void setRound(int round) {
        this.round = round;
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

    public boolean createCardPile() {

        return true;
    }
}
