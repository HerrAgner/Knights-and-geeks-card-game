import cards.Card;
import cards.UnitCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
	Player player;

	@BeforeEach
	void init() {
		player = new Player("TestName");
	}

	@Test
	void constructor(){
		String name = "testName";
		player = new Player(name);
		assertNotNull(player);
		assertSame(name, player.getName());
		player = new Player("");
		assertNull(player.getName());
	}

	@Test
	void getName() {
		assertNotNull(player.getName());
	}

	@Test
	void getHealth() {
		assertTrue(player.getHealth() > -1);
	}

	@Test
	void changeHealth() {
		int hpCurrent = player.getHealth();
		int hpChange = -5;
		player.changeHealth(hpChange);
		assertEquals(hpCurrent + hpChange, player.getHealth());
		hpCurrent = player.getHealth();
		hpChange = 5;
		player.changeHealth(hpChange);
		assertEquals(hpCurrent + hpChange, player.getHealth());
	}

	@Test
	void getMana() {
		assertTrue(player.getMana() > -1);
	}

	@Test
	void changeMana() {
		int manaCurrent = player.getMana();
		int manaChange = 5;
		player.changeMana(manaChange);
		assertEquals(manaCurrent + manaChange, player.getMana());
		manaCurrent = player.getMana();
		manaChange = -5;
		player.changeMana(manaChange);
		assertEquals(manaCurrent + manaChange, player.getMana());
	}

	@Test
	void getCardsOnHand() {
		assertNotNull(player.getCardsOnHand());
	}

	@Test
	void addCardToHand() {
		Card testCard = new UnitCard("name", 1,1, 1);
		assertTrue(player.addCardToHand(testCard));
		assertEquals(testCard, player.getCardFromHand(testCard.getId()));
		testCard = null;
		assertFalse(player.addCardToHand(testCard));
	}

	@Test
	void getCardFromHand() {
		Card testCard = new UnitCard("name", 1,1, 1);
		player.addCardToHand(testCard);
		assertSame(testCard.getId(), player.getCardFromHand(testCard.getId()).getId());
		testCard = new UnitCard("name", 1,1, 1);
		assertNull(player.getCardFromHand(testCard.getId()));
		assertNull(player.getCardFromHand(null));
	}

	@Test
	void removeCardFromHand(){
		Card testCard = new UnitCard("name", 1,1, 1);
		player.addCardToHand(testCard);
		assertEquals(testCard, player.removeCardFromHand(testCard.getId()));
		assertTrue(player.getCardsOnHand().isEmpty());
		testCard = new UnitCard("name", 1,1, 1);
		assertNull(player.removeCardFromHand(testCard.getId()));
		assertNull(player.removeCardFromHand(null));
	}

	@Test
	void getCardsOnTable() {
		assertNotNull(player.getCardsOnTable());
	}

	@Test
	void addCardToTable() {
		Card testCard = new UnitCard("namn",1,1,1);
		player.addCardToTable(testCard);
		assertEquals(testCard, player.getCardFromTable(testCard.getId()));
	}

	@Test
	void removeCardFromTable(){
		Card testCard = new UnitCard("name", 1,1, 1);
		player.addCardToTable(testCard);
		assertEquals(testCard, player.removeCardFromTable(testCard.getId()));
		assertTrue(player.getCardsOnTable().isEmpty());
	}
}