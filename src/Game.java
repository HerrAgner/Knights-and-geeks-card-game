import cards.Card;
import cards.EffectCard;
import cards.SpellCard;
import cards.UnitCard;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
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

    public Response[] playCard(UUID id) {
        Response[] res = {null,null};
        if (getCurrentPlayer().getMana() < getCurrentPlayer().getCardFromHand(id).getCost()) {
            res[0] = Response.ERROR;
            res[1] = Response.COST;
        } else if (getCurrentPlayer().getCardsOnTable().size() > 6) {
            res[0] = Response.ERROR;
            res[1] = Response.TABLE_FULL;
        } else {
            res[0] = Response.OK;
            Card c = getCurrentPlayer().removeCardFromHand(id);
            if(c instanceof UnitCard){
                res[1] = Response.UNIT_CARD;
                getCurrentPlayer().addCardToTable(c);
            } else if(c instanceof EffectCard){
                res[1] = Response.EFFECT_CARD;
            }
            else if(c instanceof SpellCard) {
                res[1] = Response.SPELL_CARD;
            }
            else {
                res[0] = Response.ERROR;
            }
        }
        return res;
    }

    public void useEffectCard(UUID id){
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
        if (!usedCard.isMany()) {
            if (usedCard.getType().equals("Healer")) {
                receivingCard.setHp(usedCard.getValue() + receivingCard.getHp());
                trashPile.add(usedCard);
            } else if (usedCard.getType().equals("Attacker")) {
                receivingCard.setHp(receivingCard.getHp() + usedCard.getValue());
                trashPile.add(usedCard);
            }
            getCurrentPlayer().removeCardFromHand(usedCard.getId());
        } else if (usedCard.isMany()) {
            if (usedCard.getType().equals("Healer")) {
                for (Card card : getCurrentPlayer().getCardsOnTable()) {
                    var unitCard = (UnitCard) card;
                    unitCard.setHp(usedCard.getValue() + unitCard.getHp());
                }
            } else if (usedCard.getType().equals("Attacker")) {
                for (Card card : getDefendingPlayer().getCardsOnTable()) {
                    var unitCard = (UnitCard) card;
                    unitCard.setHp(unitCard.getHp() + usedCard.getValue());
                }
            }
            getCurrentPlayer().removeCardFromHand(usedCard.getId());
            trashPile.add(usedCard);
        }
        return true;
    }

    public boolean useSpellOnPlayer(SpellCard usedCard) {
        if (usedCard.getType().equals("Healer")) {
            getCurrentPlayer().changeHealth(usedCard.getValue());
        } else if (usedCard.getType().equals("Attacker")) {
            getDefendingPlayer().changeHealth(usedCard.getValue());
        }
        trashPile.add(usedCard);

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
            for (int i = 0; i < 5; i++) {
                players[0].addCardToHand(cardPile.remove(0));
                players[1].addCardToHand(cardPile.remove(0));
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

    public boolean startTurn() {
        getCurrentPlayer().changeMana(1);
        System.out.println(getCurrentPlayer().getCardsOnHand().size());
        drawCard();
        getCurrentPlayer().getCardsOnTable().forEach(card -> {
            UnitCard tempCard = (UnitCard) card;
            tempCard.setFatigue(false);
        });
        return true;
    }
}
