import cards.*;
import org.junit.jupiter.api.*;

import java.util.ArrayList;



import static org.junit.jupiter.api.Assertions.*;


class GameTest {

    @Test
    void constructorTest() {
        Game game = new Game("Ted", "Anton");


        assertEquals("Ted", game.getPlayers()[0].getName());
        assertEquals("Anton", game.getPlayers()[1].getName());
        assertNotEquals(game.getPlayers()[0], game.getPlayers()[1]);
        assertEquals(0, game.getTrashPile().size());

        try {
            game = new Game("", "");
            assertNull(game.getPlayers());
        } catch (Exception ignored) {
        }

        try {
            game = new Game(null, null);
            assertNull(game.getPlayers());
        } catch (Exception ignored) {
        }

        try {
            game = new Game("Ted", "Ted");
            assertNull(game.getPlayers());
        } catch (Exception ignored) {
        }

    }

    @Test
    void getRound() {
        Game game = new Game("Ted", "Anton");
        assertEquals(1, game.getRound());
    }

    @Test
    void getActivePlayer() {
        Game game = new Game("Ted", "Anton");
        assertEquals(0, game.getActivePlayer());
    }

    @Test
    void getCardPile() {
        Game game = new Game("Ted", "Anton");
        game.createCardPile(80);
        assertNotNull(game.getCardPile());
    }

    @Test
    void getTrashPile() {
        Game game = new Game("Ted", "Anton");
        assertNotNull(game.getTrashPile());
    }

    @Test
    void getPlayers() {
        Game game = new Game("Ted", "Anton");
        assertNotNull(game.getPlayers());
    }

    @Test
    void setCardPile() {
        Game game = new Game("Ted", "Anton");
        ArrayList<Card> testList = new ArrayList<>();
        game.setCardPile(testList);
        assertSame(testList, game.getCardPile());
    }

    @Test
    void setTrashPile() {
        Game game = new Game("Ted", "Anton");
        ArrayList<Card> testList = new ArrayList<>();
        game.setTrashPile(testList);
        assertSame(testList, game.getTrashPile());
    }

    @Test
    void setActivePlayer() {
        Game game = new Game("Ted", "Anton");
        game.setActivePlayer(1);
        assertEquals(1, game.getActivePlayer());
    }

    @Test
    void setRound() {
        Game game = new Game("Ted", "Anton");
        game.setRound(2);
        assertEquals(2, game.getRound());
    }

    @Test
    void drawCard() {
        Game game = new Game("Anton", "Ted");
        game.createCardPile(80);
        ArrayList cardPile = game.getCardPile();
        Player[] players = game.getPlayers();

        assertTrue(players.length == 2);
        assertEquals(cardPile.size(), 80);

        int activePlayer = game.getActivePlayer();

        game.drawCard();
        assertEquals(cardPile.size(), 79);
        assertEquals(players[activePlayer].getCardsOnHand().size(), 6);
        game.setActivePlayer(1);
        game.drawCard();
        assertEquals(cardPile.size(), 78);
        assertEquals(players[activePlayer].getCardsOnHand().size(), 6);
        game.setActivePlayer(0);
        game.drawCard();
        assertEquals(cardPile.size(), 77);
        assertEquals(players[activePlayer].getCardsOnHand().size(), 7);
        game.setActivePlayer(1);
        game.drawCard();
        assertEquals(cardPile.size(), 76);
        assertEquals(players[activePlayer].getCardsOnHand().size(), 7);

        while (cardPile.size() != 0) {
            game.drawCard();
        }
        game.drawCard();
        assertEquals(cardPile.size(), 0);
    }

    @Test
    void playCard() {
        Game game = new Game("Ted", "Anton");
        game.getCurrentPlayer().changeMana(10);
        Card[] testCards = {
                new UnitCard("UnitCard", 1, 1, 1),
                new UnitCard("UnitCard", 11, 1, 1),
                new EffectCard("EffectCard", 1, "type", 1),
                new EffectCard("EffectCard", 11, "type", 1)
        };
        Response[] res;
        game.getPlayers()[0].addCardToHand(testCards[0]);
        game.playCard(testCards[0].getId());
        assertSame(testCards[0], game.getCurrentPlayer().getCardFromTable(testCards[0].getId()));
        assertEquals(10-testCards[0].getCost(), game.getCurrentPlayer().getMana());
        System.out.println(10-testCards[0].getCost());
        game.getPlayers()[0].addCardToHand(testCards[1]);
        game.playCard(testCards[1].getId());
        assertNull(game.getCurrentPlayer().getCardFromTable(testCards[1].getId()));

        game.getPlayers()[0].addCardToHand(testCards[2]);
        res = game.playCard(testCards[2].getId());
        assertSame(res[1], Response.EFFECT_CARD);

        game.getPlayers()[0].addCardToHand(testCards[3]);
        game.playCard(testCards[3].getId());
        assertNull(game.getCurrentPlayer().getCardFromTable(testCards[3].getId()));
    }

    @Test
    void attackCard() {
        Game game = new Game("eric", "nisse");
        Player players[] = game.getPlayers();

        UnitCard attackingCard = new UnitCard("Krigaren", 3, 5, 6);
        UnitCard defendingCard = new UnitCard("Hästen", 4, 5, 4);
        UnitCard fatiugeCard = new UnitCard("Fail", 4, 5, 2);

        fatiugeCard.setFatigue(true);

        players[game.getActivePlayer()].addCardToTable(attackingCard);
        game.getDefendingPlayer().addCardToTable(defendingCard);
        // SET UP -----------------------------------------------

        assertFalse(game.attackCard(fatiugeCard, attackingCard));
        assertTrue(game.attackCard(attackingCard, defendingCard));
        assertFalse(game.attackCard(attackingCard, attackingCard));
        assertEquals(attackingCard.getCurrentHealth(), 1);
        assertEquals(defendingCard.getCurrentHealth(), -1);
        assertTrue(game.getTrashPile().size() >= 1);
        assertNull(game.getDefendingPlayer().getCardFromTable(defendingCard.getId()));
        assertNotNull(players[game.getActivePlayer()].getCardFromTable(attackingCard.getId()));
        assertTrue(game.getTrashPile().contains(defendingCard));
        assertFalse(game.getTrashPile().contains(attackingCard));
        assertTrue(attackingCard.getFatigue());
    }

    @Test
    void useSpellOnCard(){
        Game game = new Game("eric", "nisse");
        Player players[] = game.getPlayers();
        SpellCard healer = new SpellCard("Healer", false, 2, "Eric", 2);
        SpellCard attacker = new SpellCard("Attacker", false, -2, "Ted", 2);
        SpellCard healerMany = new SpellCard("Healer", true, 2, "Hasse", 2);
        UnitCard receiver = new UnitCard("Krigaren", 3, 5, 6);
        UnitCard receiver2 = new UnitCard("Asd", 3, 7, 6);
        UnitCard receiver3 = new UnitCard("dsa", 5, 4, 5);

        players[game.getActivePlayer()].addCardToHand(healer);
        players[game.getActivePlayer()].addCardToHand(attacker);

        game.getCurrentPlayer().addCardToTable(receiver);
        game.getCurrentPlayer().addCardToTable(receiver3);
        game.getCurrentPlayer().addCardToTable(receiver2);


        game.getDefendingPlayer().addCardToHand(attacker);
        //SETUP ------------------------------------

        assertTrue(game.useSpellOnCard(healer, receiver));
        assertEquals(receiver.getCurrentHealth(), 5);
        assertTrue(game.getTrashPile().contains(healer));

        game.useSpellOnCard(attacker, receiver);
        assertEquals(receiver.getCurrentHealth(), 3);
        assertTrue(game.getTrashPile().contains(attacker));
        assertNull(game.getCurrentPlayer().getCardFromHand(healer.getId()));
        assertNull(game.getCurrentPlayer().getCardFromHand(attacker.getId()));

        receiver2.setCurrentHealth(2);
        receiver3.setCurrentHealth(1);

        assertTrue(game.useSpellOnCard(healerMany, receiver));
        assertEquals(receiver.getCurrentHealth(), 5);
        assertEquals(receiver2.getCurrentHealth(), 4);
        assertEquals(receiver3.getCurrentHealth(), 3);
        assertTrue(game.getTrashPile().contains(healerMany));
    }

    @Test
    void useSpellOnPlayer(){
        Game game = new Game("eric", "nisse");
        SpellCard healer = new SpellCard("Healer", false, 2, "Eric", 2);
        SpellCard attacker = new SpellCard("Attacker", false, -2, "Ted", 2);


        assertTrue(game.useSpellOnPlayer(healer));
        assertEquals(game.getCurrentPlayer().getHealth(), 30);
        assertTrue(game.getTrashPile().contains(healer));

        assertTrue(game.useSpellOnPlayer(attacker));
        assertEquals(game.getDefendingPlayer().getHealth(), 28);
        assertTrue(game.getTrashPile().contains(attacker));



    }

    @Test
    void attackPlayer() {
        Game game = new Game("Eric", "Ted");
        UnitCard card = new UnitCard("Pelle", 5, 9, 5);
        UnitCard card2 = new UnitCard("Håkan", 5, 5, 10);
        UnitCard card3 = new UnitCard("Anton", 5, 3, 4);

        game.getPlayers()[1].changeHealth(-25);
        assertFalse(game.attackPlayer(card2));
        game.getPlayers()[1].changeHealth(10);
        assertTrue(game.attackPlayer(card3));

        game.setActivePlayer(1);
        assertFalse(card.getFatigue());
        assertTrue(game.attackPlayer(card));
        assertEquals(25, game.getPlayers()[0].getHealth());
        assertTrue(card.getFatigue());

    }

    @Test
    void finishTurn() {
        Game game = new Game("Ted", "Anton");

        assertEquals(1, game.getRound());
        assertEquals(0, game.getActivePlayer());
        assertTrue(game.finishTurn());
        assertEquals(2, game.getRound());
        assertEquals(1, game.getActivePlayer());
    }

    @Test
    void finishGame() {

    }

    @Test
    void initGame() {
        Game game = new Game("Ted", "Anton");
        assertNotNull(game.getPlayers());
        assertEquals(0, game.getActivePlayer());
        assertEquals(1, game.getRound());
        assertEquals(2, game.getPlayers().length);
        assertEquals(5, game.getPlayers()[0].getCardsOnHand().size());
        assertEquals(5, game.getPlayers()[1].getCardsOnHand().size());
    }

    @Test
    void createCardPile() {
        Game game = new Game("Ted", "Anton");
        int amountOfCards = 80;



        assertTrue(game.createCardPile(amountOfCards));
        assertEquals(8, game.getCardPile().stream().filter(card -> card instanceof SpellCard).count());



        assertNotNull(game.getCardPile());
        assertEquals(amountOfCards, game.getCardPile().size());

        assertTrue(game.createCardPile(77));
        assertEquals(77, game.getCardPile().size());

        assertFalse(game.createCardPile(101));
        assertFalse(game.createCardPile(49));
        assertTrue(game.createCardPile(50));
        assertTrue(game.createCardPile(100));


    }

    @Test
    void shuffleTrashPile() {
        Game game = new Game("Anton", "Ted");
        game.createCardPile(80);

        assertEquals(0, game.getTrashPile().size());
        game.setTrashPile(new ArrayList<>(game.getCardPile()));
        game.getCardPile().clear();

        assertEquals(0, game.getCardPile().size());
        assertEquals(80, game.getTrashPile().size());

        assertTrue(game.shuffleTrashPile());
        assertEquals(80, game.getCardPile().size());
        assertEquals(0, game.getTrashPile().size());

    }

    @Test
    void useEffectCard() {
        Game game = new Game("Alle", "Ralle");
        EffectCard increaseAttack = new EffectCard("card", 2, "Atk", 2);
        EffectCard decreaseAttack = new EffectCard("card", 2, "Atk", -2);
        EffectCard decreaseAttackBy4 = new EffectCard("card", 2, "Atk", -4);
        EffectCard decreaseHealth = new EffectCard("card", 2, "Hp", -2);
        EffectCard increaseHealth = new EffectCard("card", 2, "Hp", 3);
        EffectCard invalidCard = new EffectCard("cardio", 2, "LAJS", 3);
        UnitCard unitCard = new UnitCard("Anton", 0, 2, 3);
        UnitCard unitCard2 = new UnitCard("Kalle", 0, 4, 5);
        unitCard2.setCurrentHealth(4);

        assertTrue(game.useEffectCard(decreaseAttack, unitCard));
        assertTrue(unitCard.getAttack()==1);
        game.useEffectCard(increaseAttack, unitCard);
        assertTrue(unitCard.getAttack()==3);
        game.useEffectCard(decreaseAttackBy4, unitCard);
        System.out.println(unitCard.getAttack());
        assertTrue(unitCard.getAttack()==1);

        assertFalse(unitCard2.getAttack()==7);
        game.useEffectCard(increaseAttack, unitCard2);
        assertTrue(unitCard2.getAttack()==7);
        System.out.println(unitCard2.getCurrentHealth());
        game.useEffectCard(decreaseHealth, unitCard2);
        assertTrue(unitCard2.getMaxHealth()==2);
        assertTrue(unitCard2.getCurrentHealth()==2);
        game.useEffectCard(decreaseHealth, unitCard2);
        assertTrue(unitCard2.getMaxHealth()>0);
        assertTrue(unitCard2.getCurrentHealth()==1);
        assertTrue(unitCard2.getCurrentHealth()<=unitCard2.getMaxHealth());
    }

    @Test
    void startTurn() {
        Game game = new Game("Ted", "Anton");
        UnitCard unitCard = new UnitCard("Krigaren", 1, 5, 6);
        game.getCurrentPlayer().addCardToHand(unitCard);

        game.startTurn();
        assertEquals(1, game.getCurrentPlayer().getMana());
        assertEquals(7, game.getCurrentPlayer().getCardsOnHand().size());

        game.playCard(unitCard.getId());

        assertEquals(1, game.getCurrentPlayer().getCardsOnTable().size());
        unitCard.setFatigue(true);
        assertEquals(6, game.getCurrentPlayer().getCardsOnHand().size());
        var tableCard = (UnitCard) game.getCurrentPlayer().getCardsOnTable().toArray()[0];
        assertTrue(tableCard.getFatigue());

        game.startTurn();
        assertEquals(1, game.getCurrentPlayer().getMana());
        assertEquals(7, game.getCurrentPlayer().getCardsOnHand().size());
        tableCard = (UnitCard) game.getCurrentPlayer().getCardsOnTable().toArray()[0];
        assertFalse(tableCard.getFatigue());

        game.startTurn();
        assertEquals(2, game.getCurrentPlayer().getMana());
        assertEquals(8, game.getCurrentPlayer().getCardsOnHand().size());

    }
}