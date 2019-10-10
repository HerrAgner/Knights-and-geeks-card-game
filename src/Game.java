import cards.Card;
import cards.UnitCard;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.*;

public class Game {

    private ArrayList<Card> cardPile;
    private ArrayList<Card> trashPile = new ArrayList<>();
    private Player[] players;
    private int activePlayer;
    private int round;


    public Game(String player1, String player2) {
        if (player1 == null || player2 == null) {
            return;
        }
        if (player1.isEmpty() || player2.isEmpty()) {
            return;
        }
        if (player1.equals(player2)) {
            return;
        }
        this.players = new Player[]{new Player(player1), new Player(player2)};
        this.round = 1;
        this.activePlayer = 0;
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

    public ArrayList getTrashPile() {
        return trashPile;
    }

    public Player[] getPlayers() {
        return players;
    }

    public void setCardPile(ArrayList cardPile) {
        this.cardPile = cardPile;
    }

    public void setTrashPile(ArrayList trashPile) {
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

    public boolean createCardPile(int amountOfCards) {
        if (amountOfCards < 50 || amountOfCards > 100) return false;
        cardPile = new ArrayList<>();

        String path = "src/cards.json";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        Type collectionType = new TypeToken<List<UnitCard>>(){}.getType();
        List<UnitCard> cards = gson.fromJson(br, collectionType);

        Collections.shuffle(cards);

        // Two of each card
        for (int i = 0; i < amountOfCards/2; i++) {
            cardPile.add(cards.get(i));
            cardPile.add(cards.get(i));
            System.out.println(cards.get(i).getName());
        }

        Collections.shuffle(cardPile);

        return true;
    }
}
