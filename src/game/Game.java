package game;

import cards.Card;
import cards.EffectCard;
import cards.SpellCard;
import cards.UnitCard;
import com.google.gson.reflect.TypeToken;
import enums.*;
import utilities.CardGenerator;
import utilities.HttpGet;
import utilities.Input;

import java.lang.reflect.Type;
import java.util.*;

public class Game {

    private ArrayList<Card> cardPile;
    private ArrayList<Card> trashPile = new ArrayList<>();
    private Player[] players;
    private int hpBeforeHeal;
    private int activePlayer;
    private int round;
    private Input input = new Input();


    public Game(String player1, String player2, int cardAmount) {
        if (player1 == null || player2 == null) {
            return;
        }
        if (player1.isEmpty() || player2.isEmpty()) {
            return;
        }
        if (player1.equals(player2)) {
            return;
        }
        initGame(player1, player2, cardAmount);
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

    public UUID drawCard() {
        if (getCurrentPlayer().getCardsOnHand().size() >= 7) {
            return null;
        }
        Card c = cardPile.remove(0);
        getCurrentPlayer().addCardToHand(c);
        if (cardPile.size() == 0) {
            shuffleTrashPile();
        }
        return c.getId();
    }

    public Response[] playCard(UUID id) {
        Response[] res = {null, null};
        Card c = getCurrentPlayer().getCardFromHand(id);
        if (getCurrentPlayer().getCurrentMana() < c.getCost()) {
            res[0] = Response.ERROR;
            res[1] = Response.COST;
        } else if (c instanceof UnitCard && getCurrentPlayer().getCardsOnTable().size() > 6) {
            res[0] = Response.ERROR;
            res[1] = Response.TABLE_FULL;
        } else if (input.validateEmptyTable(id, getCurrentPlayer(), getDefendingPlayer())) {
            res[0] = Response.ERROR;
            res[1] = Response.TABLE_EMPTY;
        } else {
            res[0] = Response.OK;
            getCurrentPlayer().changeMana(-c.getCost());
            if (c instanceof UnitCard) {
                res[1] = Response.UNIT_CARD;
                ((UnitCard) c).setFatigue(true);
                getCurrentPlayer().removeCardFromHand(id);
                getCurrentPlayer().changeMana(-c.getCost());
                getCurrentPlayer().addCardToTable(c);
            } else if (c instanceof EffectCard) {
                res[1] = Response.EFFECT_CARD;
            } else if (c instanceof SpellCard) {
                res[1] = Response.SPELL_CARD;
            } else {
                res[0] = Response.ERROR;
            }

        }
        return res;
    }

    public boolean useEffectCard(EffectCard card, UnitCard receivingCard) {
        if (card.getType().equals("Atk") || card.getType().equals("Hp")) {
            if (card.getType().equals("Atk")) {
                receivingCard.changeAttack(card.getEffectValue());
                getCurrentPlayer().changeMana(-card.getCost());
                getCurrentPlayer().removeCardFromHand(card.getId());
            }
            if (card.getType().equals("Hp")) {
                receivingCard.changeMaxHealth(card.getEffectValue());
                getCurrentPlayer().changeMana(-card.getCost());
                getCurrentPlayer().removeCardFromHand(card.getId());
            }
            return true;
        }
        return false;
    }

    public boolean attackCard(UnitCard attackingCard, UnitCard defendingCard) {

        if (attackingCard == defendingCard) return false;
        if (attackingCard.getCurrentHealth() < 1 || defendingCard.getCurrentHealth() < 1) return false;
        if (attackingCard.getFatigue()) return false;

        defendingCard.changeCurrentHealth(-attackingCard.getAttack());
        attackingCard.changeCurrentHealth(-defendingCard.getAttack());
        attackingCard.setFatigue(true);

        if (defendingCard.getCurrentHealth() < 1) {
            getDefendingPlayer().removeCardFromTable(defendingCard.getId());
            trashPile.add(defendingCard);
        }
        if (attackingCard.getCurrentHealth() < 1) {
            getCurrentPlayer().removeCardFromTable(attackingCard.getId());
            trashPile.add(attackingCard);
        }
        return true;
    }

    public boolean useSpellSingleCard(SpellCard usedCard, UnitCard receivingCard) {
        if (usedCard.getType().equals("Healer")) {
            receivingCard.changeCurrentHealth(usedCard.getValue());
            getCurrentPlayer().changeMana(-usedCard.getCost());
        } else if (usedCard.getType().equals("Attacker")) {
            receivingCard.changeCurrentHealth(-usedCard.getValue());
            getCurrentPlayer().changeMana(-usedCard.getCost());
            if (receivingCard.getCurrentHealth() <= 0) {
                trashPile.add(getDefendingPlayer().removeCardFromTable(receivingCard.getId()));
            }
        }
        trashPile.add(getCurrentPlayer().removeCardFromHand(usedCard.getId()));
        return true;
    }

    public boolean useSpellMultiCard(SpellCard usedCard) {
        if (usedCard.getType().equals("Healer")) {
            for (Card card : getCurrentPlayer().getCardsOnTable()) {
                var unitCard = (UnitCard) card;
                unitCard.changeCurrentHealth(usedCard.getValue());
            }
        } else if (usedCard.getType().equals("Attacker")) {
            ArrayList<UUID> deadId = new ArrayList<>();
            for (Card card : getDefendingPlayer().getCardsOnTable()) {
                var unitCard = (UnitCard) card;
                unitCard.changeCurrentHealth(-usedCard.getValue());
                if (unitCard.getCurrentHealth() <= 0) {
                    deadId.add(unitCard.getId());
                }
            }
            deadId.forEach(id -> trashPile.add(getDefendingPlayer().removeCardFromTable(id)));
        }
        getCurrentPlayer().removeCardFromHand(usedCard.getId());
        getCurrentPlayer().changeMana(-usedCard.getCost());
        trashPile.add(usedCard);
        return true;
    }

    public boolean useSpellOnCard(SpellCard usedCard) {
        if (usedCard.isMany()) {
            useSpellMultiCard(usedCard);
            return true;
        }
        return false;
    }

    public boolean useSpellOnCard(SpellCard usedCard, UnitCard receivingCard) {
        if (!usedCard.isMany()) {
            useSpellSingleCard(usedCard, receivingCard);
        } else useSpellMultiCard(usedCard);
        return true;
    }

    public boolean useSpellOnPlayer(SpellCard usedCard) {
        if (usedCard.isMany()) return false;
        if (usedCard.getType().equals("Healer")) {
            hpBeforeHeal = getCurrentPlayer().getHealth();
            getCurrentPlayer().changeHealth(usedCard.getValue());
        } else if (usedCard.getType().equals("Attacker")) {
            getDefendingPlayer().changeHealth(-usedCard.getValue());
        }
        trashPile.add(getCurrentPlayer().removeCardFromHand(usedCard.getId()));
        return true;
    }

    public boolean attackPlayer(UnitCard card) {
        if (card.getFatigue()) return false;
        getDefendingPlayer().changeHealth(-card.getAttack());
        card.setFatigue(true);
        return true;
    }

    public boolean finishTurn() {
        this.setActivePlayer(getActivePlayer() == 0 ? 1 : 0);
        this.round++;
        return true;
    }

    public boolean shouldGameContinue() {
        if (getDefendingPlayer().getHealth() > 0) {
            return true;
        } else {
            String winner = players[activePlayer].getName();
            int round = getRound();
            HttpGet httpGet = new HttpGet(getCurrentPlayer().getName(), round);
            httpGet.sendGet();
            return false;
        }
    }

    public boolean initGame(String p1, String p2, int cardAmount) {
        try {
            this.players = new Player[]{new Player(p1), new Player(p2)};
            this.round = 1;
            this.activePlayer = 0;
            createCardPile(cardAmount);
            for (int i = 0; i < 2; i++) {
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
        if (amountOfCards < 45 || amountOfCards > 100) return false;

        cardPile = new ArrayList<>();

        CardGenerator cg = new CardGenerator();
        Type collectionType = new TypeToken<List<UnitCard>>() {
        }.getType();
        List<Card> unitCards = cg.generateFromJson("src/json/cards.json", collectionType);

        Type collectionType2 = new TypeToken<List<EffectCard>>() {
        }.getType();
        List<Card> effectCards = cg.generateFromJson("src/json/effectcard.json", collectionType2);

        Type collectionType3 = new TypeToken<List<SpellCard>>() {
        }.getType();
        List<Card> spellCards = cg.generateFromJson("src/json/spellcard.json", collectionType3);

        Type collectionType4 = new TypeToken<List<UnitCard>>() {
        }.getType();
        List<Card> cheapUnitCards = cg.generateFromJson("src/json/cheapcards.json", collectionType4);

        double amountOfUnitCards = amountOfCards * 0.7;
        double amountOfCheapCards = amountOfCards * 0.1;
        double amountOfEffectCards = Math.floor(amountOfCards * 0.1);
        double amountOfSpellCards = Math.floor(amountOfCards * 0.1);

        for (int i = 0; i < amountOfUnitCards; i++) {
            cardPile.add(unitCards.get(i));
        }

        for (int i = 0; i < amountOfCheapCards; i++) {
            cardPile.add(cheapUnitCards.get(i));
        }

        for (int i = 0; i < amountOfEffectCards; i++) {
            cardPile.add(effectCards.get(i));
        }
        for (int i = 0; i < amountOfSpellCards; i++) {
            cardPile.add(spellCards.get(i));
        }

        if (amountOfCards > cardPile.size()) {
            cardPile.add(unitCards.get(0));
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

    public int getHpBeforeHeal() {
        return hpBeforeHeal;
    }

    public boolean startTurn() {
        getCurrentPlayer().changeMaxMana();
        drawCard();
        getCurrentPlayer().getCardsOnTable().forEach(card -> {
            UnitCard tempCard = (UnitCard) card;
            tempCard.setFatigue(false);
        });
        return true;
    }

}
