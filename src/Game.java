import cards.Card;
import cards.SpellCard;
import cards.UnitCard;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.URL;
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
        initGame(player1, player2, 50);
    }

    public int getRound() {
        return round;
    }

    public int getActivePlayer() {
        return activePlayer;
    }

    public Player getCurrentPlayer() {
        return players[activePlayer];
    }

    public Player getDefendingPlayer() {
        return players[activePlayer == 0 ? 1 : 0];
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
        if (cardPile.size() == 0) {

            return false;
        }
        Card card = cardPile.remove(0);
        players[activePlayer].addCardToHand(card);
        return true;
    }

    public boolean playCard(UUID id) {
        if (getCurrentPlayer().getMana() >= getCurrentPlayer().getCardFromHand(id).getCost() &&
                getCurrentPlayer().getCardsOnTable().size() < 7) {
            getCurrentPlayer().addCardToTable(getCurrentPlayer().removeCardFromHand(id));
        }
        return getCurrentPlayer().getCardFromTable(id) != null;
    }

    public boolean attackCard(UnitCard attackingCard, UnitCard defendingCard) {

        if (attackingCard == defendingCard) return false;
        if (attackingCard.getHp() < 1 || defendingCard.getHp() < 1) return false;
        if (attackingCard.getFatigue() || defendingCard.getFatigue()) return false;

        defendingCard.setHp(defendingCard.getHp() - attackingCard.getAttack());
        attackingCard.setHp(attackingCard.getHp() - defendingCard.getAttack());
        defendingCard.setFatigue(true);
        attackingCard.setFatigue(true);

        if (defendingCard.getHp() < 1) {
            getDefendingPlayer().removeCardFromTable(defendingCard.getId());
            trashPile.add(defendingCard);
        }
        if (attackingCard.getHp() < 1) {
            players[activePlayer].removeCardFromHand(attackingCard.getId());
            trashPile.add(attackingCard);

        }
        return true;
    }

    public boolean useSpellOnCard(SpellCard usedCard, UnitCard receivingCard) {

        if(usedCard.getType().equals("Healer")){
            receivingCard.setHp(usedCard.getValue() + receivingCard.getHp());
            trashPile.add(usedCard);
        } else if (usedCard.getType().equals("Attacker")){
            receivingCard.setHp(receivingCard.getHp() + usedCard.getValue());
            trashPile.add(usedCard);

        }

        return true;
    }

    public boolean attackPlayer(UnitCard card) {
        int defendingPlayer = getActivePlayer() == 0 ? 1 : 0;

        getPlayers()[defendingPlayer].changeHealth(-card.getAttack());
        card.setFatigue(true);
        if (getPlayers()[defendingPlayer].getHealth() > 0) return true;

        return false;
    }

    public boolean finishTurn() {
        this.setActivePlayer(getActivePlayer() == 0 ? 1 : 0);
        this.round++;
        return true;
    }

    public boolean finishGame() {
        String winner = players[activePlayer].getName();
        int round = getRound();
        HttpGet httpGet = new HttpGet(winner, round);
        httpGet.sendGet();


        return true;
    }

    public boolean initGame(String p1, String p2, int cardAmount) {
        try {
            this.players = new Player[]{new Player(p1), new Player(p2)};
            this.round = 1;
            this.activePlayer = 0;
            createCardPile(cardAmount);
            Random rnd = new Random();
            while (players[0].getCardsOnHand().size() < 5 && players[1].getCardsOnHand().size() < 5) {
                players[0].addCardToHand(cardPile.remove(rnd.nextInt(cardPile.size())));
                players[1].addCardToHand(cardPile.remove(rnd.nextInt(cardPile.size())));
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean createCardPile(int amountOfCards) {
        if (amountOfCards < 50 || amountOfCards > 100) return false;

        cardPile = new ArrayList<>();

        CardGenerator cg = new CardGenerator();
        Type collectionType = new TypeToken<List<UnitCard>>() {
        }.getType();
        List<Card> cards = cg.generateFromJson("src/cards.json", collectionType);

        // Two of each card
        for (int i = 0; i < amountOfCards / 2; i++) {
            cardPile.add(cards.get(i));
            cardPile.add(cards.get(i));
        }

        if (amountOfCards % 2 == 1) {
            cardPile.add(cards.get(0));
        }

        Collections.shuffle(cardPile);

        return true;
    }

    public boolean shuffleTrashPile() {
        this.cardPile = new ArrayList<>(trashPile);
        trashPile.clear();
        Collections.shuffle(this.cardPile);

        return true;
    }
}
