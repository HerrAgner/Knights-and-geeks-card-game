import cards.Card;
import cards.UnitCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
	Player player;

	@BeforeEach
	void init() {
		player = new Player("TestName");
	}

	@Test
	void constructor(){
		assertNotNull(player);
		assertNull(new Player().getName());
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
	void setHealth() {
		final int hpCurrent = player.getHealth();
		final int hpChange = 5;
		player.changeHealth(hpChange);
		assertEquals(hpCurrent + hpChange, player.getHealth());
	}

	@Test
	void getMana() {
		assertTrue(player.getMana() > -1);
	}

	@Test
	void changeMana() {
		final int manaCurrent = player.getMana();
		final int manaChange = 1;
		player.changeMana(manaChange);
		assertEquals(manaCurrent + manaChange, player.getMana());
	}

	@Test
	void getCardsOnHand() {
		assertNotNull(player.getCardsOnHand());
	}

	@Test
	void addCardToHand() {
		final Card testCard = new UnitCard("name", 1, "",1, 1);
		player.addCardToHand(testCard);
		assertEquals(testCard, player.getCardsOnHand().get(testCard.getId()));
	}

	@Test
	void getCardsOnTable() {
		assertNotNull(player.getCardsOnTable());
	}

	@Test
	void addCardToTable() {
		final Card testCard = new UnitCard("name", 1, "",1, 1);
		player.addCardToTable(testCard);
		assertEquals(testCard, player.getCardsOnTable().get(testCard.getId()));
	}
}