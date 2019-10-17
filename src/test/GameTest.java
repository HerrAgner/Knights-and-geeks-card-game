import cards.*;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.UUID;


import static org.junit.jupiter.api.Assertions.*;


class GameTest {

    @Test
    void constructorTest() {
        Game game = new Game("Ted", "Anton", 46);


        assertEquals("Ted", game.getPlayers()[0].getName());
        assertEquals("Anton", game.getPlayers()[1].getName());
        assertNotEquals(game.getPlayers()[0], game.getPlayers()[1]);
        assertEquals(0, game.getTrashPile().size());

        try {
            game = new Game("", "", 46);
            assertNull(game.getPlayers());
        } catch (Exception ignored) {
        }

        try {
            game = new Game(null, null, 46);
            assertNull(game.getPlayers());
        } catch (Exception ignored) {
        }

        try {
            game = new Game("Ted", "Ted", 46);
            assertNull(game.getPlayers());
        } catch (Exception ignored) {
        }

    }

    @Test
    void getRound() {
        Game game = new Game("Ted", "Anton", 46);
        assertEquals(1, game.getRound());
    }

    @Test
    void getActivePlayer() {
        Game game = new Game("Ted", "Anton", 46);
        assertEquals(0, game.getActivePlayer());
    }

    @Test
    void getCardPile() {
        Game game = new Game("Ted", "Anton", 46);
        game.createCardPile(80);
        assertNotNull(game.getCardPile());
    }

    @Test
    void getTrashPile() {
        Game game = new Game("Ted", "Anton", 46);
        assertNotNull(game.getTrashPile());
    }

    @Test
    void getPlayers() {
        Game game = new Game("Ted", "Anton", 46);
        assertNotNull(game.getPlayers());
    }

    @Test
    void setCardPile() {
        Game game = new Game("Ted", "Anton", 46);
        ArrayList<Card> testList = new ArrayList<>();
        game.setCardPile(testList);
        assertSame(testList, game.getCardPile());
    }

    @Test
    void setTrashPile() {
        Game game = new Game("Ted", "Anton", 46);
        ArrayList<Card> testList = new ArrayList<>();
        game.setTrashPile(testList);
        assertSame(testList, game.getTrashPile());
    }

    @Test
    void setActivePlayer() {
        Game game = new Game("Ted", "Anton", 46);
        game.setActivePlayer(1);
        assertEquals(1, game.getActivePlayer());
    }

    @Test
    void setRound() {
        Game game = new Game("Ted", "Anton", 46);
        game.setRound(2);
        assertEquals(2, game.getRound());
    }

    @Test
    void drawCard() {
        Game game = new Game("Anton", "Ted", 46);

        assertEquals(40, game.getCardPile().size());

        game.drawCard();
        assertEquals(game.getCardPile().size(), 39);
        assertEquals(game.getCurrentPlayer().getCardsOnHand().size(), 4);
        game.setActivePlayer(1);
        game.drawCard();
        assertEquals(game.getCardPile().size(), 38);
        assertEquals(game.getCurrentPlayer().getCardsOnHand().size(), 4);
        game.setActivePlayer(0);
        game.drawCard();
        assertEquals(game.getCardPile().size(), 37);
        assertEquals(game.getCurrentPlayer().getCardsOnHand().size(), 5);
        game.setActivePlayer(1);
        game.drawCard();
        assertEquals(game.getCardPile().size(), 36);
        assertEquals(game.getCurrentPlayer().getCardsOnHand().size(), 5);

        while (game.getCardPile().size() > 1) {
            UUID id = game.drawCard();
            if(id == null) {
                System.out.println("HAND FULL");
                break;
            }
            game.getTrashPile().add(game.getCurrentPlayer().removeCardFromHand(id));
        }
        game.drawCard();
        assertTrue(game.getCardPile().size() > 1);
    }

    @Test
    void playCard() {
        Game game = new Game("Ted", "Anton", 46);
        game.getCurrentPlayer().changeMana(10);
        Card[] testCards = {
                new UnitCard("UnitCard", 1, 1, 1,"COMMON"),
                new UnitCard("UnitCard", 11, 1, 1, "COMMON"),
                new EffectCard("EffectCard", 1, "type", 1),
                new EffectCard("EffectCard", 11, "type", 1)
        };
        Response[] res;
        game.getPlayers()[0].addCardToHand(testCards[0]);
        game.playCard(testCards[0].getId());
        assertSame(testCards[0], game.getCurrentPlayer().getCardFromTable(testCards[0].getId()));
        assertEquals(10-testCards[0].getCost(), game.getCurrentPlayer().getCurrentMana());
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
        Game game = new Game("eric", "nisse", 46);
        Player players[] = game.getPlayers();

        UnitCard attackingCard = new UnitCard("Krigaren", 3, 5, 6, "COMMON");
        UnitCard defendingCard = new UnitCard("Hästen", 4, 5, 4, "COMMON");
        UnitCard fatiugeCard = new UnitCard("Fail", 4, 5, 2, "COMMON");

        fatiugeCard.setFatigue(true);

        players[game.getActivePlayer()].addCardToTable(attackingCard);
        game.getDefendingPlayer().addCardToTable(defendingCard);
        // SET UP -----------------------------------------------

        assertFalse(game.attackCard(fatiugeCard, attackingCard));
        assertTrue(game.attackCard(attackingCard, defendingCard));
        assertFalse(game.attackCard(attackingCard, attackingCard));
        assertEquals(1, attackingCard.getCurrentHealth());
        assertEquals(0, defendingCard.getCurrentHealth());
        assertTrue(game.getTrashPile().size() >= 1);
        assertNull(game.getDefendingPlayer().getCardFromTable(defendingCard.getId()));
        assertNotNull(players[game.getActivePlayer()].getCardFromTable(attackingCard.getId()));
        assertTrue(game.getTrashPile().contains(defendingCard));
        assertFalse(game.getTrashPile().contains(attackingCard));
        assertTrue(attackingCard.getFatigue());
    }

    @Test
    void useSpellOnCard(){
        Game game = new Game("eric", "nisse", 46);
        Player players[] = game.getPlayers();
        SpellCard healer = new SpellCard("Healer", false, 2, "Eric", 2);
        SpellCard attacker = new SpellCard("Attacker", false, 2, "Ted", 2);
        SpellCard healerMany = new SpellCard("Healer", true, 2, "Hasse", 2);
        SpellCard attackerMany = new SpellCard("Attacker", true, 2, "Frasse", 2);

        UnitCard receiver = new UnitCard("receiver", 3, 5, 6, "COMMON");
        UnitCard receiver2 = new UnitCard("receiver2", 3, 7, 6, "COMMON");
        UnitCard receiver3 = new UnitCard("receiver3", 5, 4, 5, "COMMON");

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

        assertTrue(game.useSpellOnCard(healerMany, receiver));
        assertEquals(receiver.getCurrentHealth(), 5);
        assertEquals(receiver2.getCurrentHealth(), 7);
        assertEquals(4, receiver3.getCurrentHealth());
        assertTrue(game.getTrashPile().contains(healerMany));

        game.getDefendingPlayer().addCardToTable(receiver);
        game.getDefendingPlayer().addCardToTable(receiver2);
        game.getDefendingPlayer().addCardToTable(receiver3);
        assertTrue(game.useSpellOnCard(attackerMany, receiver));
        assertEquals(3, receiver.getCurrentHealth());
        assertEquals(5, receiver2.getCurrentHealth());
        assertEquals(2, receiver3.getCurrentHealth());
        assertTrue(game.getTrashPile().contains(healerMany));

        game.useSpellOnCard(attackerMany, receiver);
        assertNull(game.getDefendingPlayer().getCardFromTable(receiver3.getId()));

        game.useSpellOnCard(attacker, receiver);
        assertNull(game.getDefendingPlayer().getCardFromTable(receiver.getId()));

        SpellCard attackManyExtra = new SpellCard("Attacker", true, 2, "Frasse", 2);
        game.useSpellOnCard(attackManyExtra, null);
        assertEquals(1, receiver2.getCurrentHealth());
    }

    @Test
    void useSpellOnPlayer(){
        Game game = new Game("eric", "nisse", 46);
        SpellCard healer = new SpellCard("Healer", false, 2, "Eric", 2);
        SpellCard attacker = new SpellCard("Attacker", false, 2, "Ted", 2);
        SpellCard attackerMany = new SpellCard("Attacker", true, 2, "Flipp", 2);
        SpellCard attackerKill = new SpellCard("Attacker", true, 30, "Flopp", 2);


        assertTrue(game.useSpellOnPlayer(healer));
        assertEquals(game.getCurrentPlayer().getHealth(), 30);
        assertTrue(game.getTrashPile().contains(healer));

        assertTrue(game.useSpellOnPlayer(attacker));
        assertEquals(game.getDefendingPlayer().getHealth(), 28);
        assertTrue(game.getTrashPile().contains(attacker));

        assertFalse(game.useSpellOnPlayer(attackerKill));
    }

    @Test
    void attackPlayer() {
        Game game = new Game("Eric", "Ted", 46);
        Game game = new Game("Eric", "Ted");
        UnitCard card = new UnitCard("Pelle", 5, 9, 5, "COMMON");
        UnitCard card2 = new UnitCard("Håkan", 5, 5, 10, "COMMON");
        UnitCard card3 = new UnitCard("Anton", 5, 3, 4, "COMMON");
        card.setFatigue(false);
        card2.setFatigue(false);
        card3.setFatigue(false);
        game.getPlayers()[1].changeHealth(-25);
        game.attackPlayer(card);
        assertFalse(game.shouldGameContinue());
        game.getPlayers()[1].changeHealth(10);
        assertTrue(game.attackPlayer(card3));

        game.setActivePlayer(1);
        assertFalse(game.attackPlayer(card));
        card.setFatigue(false);
        assertTrue(game.attackPlayer(card));
        assertEquals(25, game.getPlayers()[0].getHealth());
        assertTrue(card.getFatigue());
        assertTrue(game.shouldGameContinue());
    }

    @Test
    void finishTurn() {
        Game game = new Game("Ted", "Anton", 46);

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
        Game game = new Game("Ted", "Anton", 46);
        assertNotNull(game.getPlayers());
        assertEquals(0, game.getActivePlayer());
        assertEquals(1, game.getRound());
        assertEquals(2, game.getPlayers().length);
        assertEquals(3, game.getPlayers()[0].getCardsOnHand().size());
        assertEquals(3, game.getPlayers()[1].getCardsOnHand().size());
    }

    @Test
    void createCardPile() {
        Game game = new Game("Ted", "Anton", 46);
        int amountOfCards = 80;



        assertTrue(game.createCardPile(amountOfCards));
        assertEquals(8, game.getCardPile().stream().filter(card -> card instanceof SpellCard).count());



        assertNotNull(game.getCardPile());
        assertEquals(amountOfCards, game.getCardPile().size());

        assertTrue(game.createCardPile(77));
        assertEquals(77, game.getCardPile().size());

        assertFalse(game.createCardPile(101));
        assertFalse(game.createCardPile(44));
        assertTrue(game.createCardPile(50));
        assertTrue(game.createCardPile(100));


    }

    @Test
    void shuffleTrashPile() {
        Game game = new Game("Anton", "Ted", 46);
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
        Game game = new Game("Alle", "Ralle", 46);
        EffectCard increaseAttack = new EffectCard("card", 2, "Atk", 2);
        EffectCard decreaseAttack = new EffectCard("card", 2, "Atk", -2);
        EffectCard decreaseAttackBy4 = new EffectCard("card", 2, "Atk", -4);
        EffectCard decreaseHealth = new EffectCard("card", 2, "Hp", -2);
        UnitCard unitCard = new UnitCard("Anton", 0, 2, 3, "COMMON");
        UnitCard unitCard2 = new UnitCard("Kalle", 0, 4, 5, "COMMON");
        unitCard2.changeCurrentHealth(4);

        assertTrue(game.useEffectCard(decreaseAttack, unitCard));
        assertEquals(1, unitCard.getAttack());

        game.useEffectCard(increaseAttack, unitCard);
        assertEquals(3, unitCard.getAttack());

        game.useEffectCard(decreaseAttackBy4, unitCard);
        assertEquals(1, unitCard.getAttack());
        assertNotEquals(7, unitCard2.getAttack());

        game.useEffectCard(increaseAttack, unitCard2);
        assertEquals(7, unitCard2.getAttack());

        game.useEffectCard(decreaseHealth, unitCard2);
        assertEquals(2, unitCard2.getMaxHealth());
        assertEquals(2, unitCard2.getCurrentHealth());

        game.useEffectCard(decreaseHealth, unitCard2);
        assertTrue(unitCard2.getMaxHealth()>0);
        assertEquals(1, unitCard2.getCurrentHealth());
        assertTrue(unitCard2.getCurrentHealth()<=unitCard2.getMaxHealth());
    }

    @Test
    void startTurn() {
        Game game = new Game("Ted", "Anton", 46);
        UnitCard unitCard = new UnitCard("Krigaren", 1, 5, 6, "COMMON");
        game.getCurrentPlayer().addCardToHand(unitCard);

        game.startTurn();
        assertEquals(1, game.getCurrentPlayer().getCurrentMana());
        assertEquals(5, game.getCurrentPlayer().getCardsOnHand().size());

        game.playCard(unitCard.getId());

        assertEquals(1, game.getCurrentPlayer().getCardsOnTable().size());
        unitCard.setFatigue(true);
        assertEquals(4, game.getCurrentPlayer().getCardsOnHand().size());
        var tableCard = (UnitCard) game.getCurrentPlayer().getCardsOnTable().toArray()[0];
        assertTrue(tableCard.getFatigue());
        assertEquals(0, game.getCurrentPlayer().getCurrentMana());

        game.startTurn();
        assertEquals(2, game.getCurrentPlayer().getCurrentMana());
        assertEquals(5, game.getCurrentPlayer().getCardsOnHand().size());
        tableCard = (UnitCard) game.getCurrentPlayer().getCardsOnTable().toArray()[0];
        assertFalse(tableCard.getFatigue());

        game.startTurn();
        assertEquals(3, game.getCurrentPlayer().getCurrentMana());
        assertEquals(6, game.getCurrentPlayer().getCardsOnHand().size());

        for(int i=0; i<15; i++){
            game.startTurn();
        }
        assertEquals(10, game.getCurrentPlayer().getMana());

    }
}