import cards.Card;
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
	}

	@Test
	void setCardsOnHand() {

	}

	@Test
	void getCardsOnTable() {
	}

	@Test
	void setCardsOnTable() {
	}
}