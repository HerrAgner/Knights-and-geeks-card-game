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
		player.setHealth(hpChange);
		assertEquals(hpCurrent + hpChange, player.getHealth());
	}

	@Test
	void getMana() {
	}

	@Test
	void setMana() {
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