package gt;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import gm.CardManager;
import gm.info.CardType;

public class CardManagerTest {
	
	private CardManager cardManager;
	
	@Before
	public void setUp() throws Exception {
		cardManager = new CardManager();
	}

	@Test
	public void test() {
		for (int i = 1; i <= 60; i++) {
			CardType cardType = cardManager.getCard();
			cardManager.setCard(cardType);
			assertNotNull(cardType);
		}
		for (int i = 1; i <= 60; i++) {
			CardType cardType = cardManager.getCard();
			System.out.println(cardType);
			assertNotNull(cardType);
		}
		cardManager.setCard(CardType.POLICE);
		CardType cardType = cardManager.getCard(); 
		assertEquals(CardType.POLICE, cardType);
	}

}
