package gt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import gm.CardManagerImpl;
import gm.info.CardType;

public class CardManagerTest {
	
	private CardManagerImpl cardManager;
	
	@Before
	public void setUp() throws Exception {
		cardManager = new CardManagerImpl();
	}

	@Test
	public void test() {
		for (int i = 1; i <= 58; i++) {
			CardType cardType = cardManager.getCard();
			cardManager.setCard(cardType);
			assertNotNull(cardType);
		}
		for (int i = 1; i <= 58; i++) {
			CardType cardType = cardManager.getCard();
			assertNotNull(cardType);
		}
		assertEquals(0, cardManager.getTotalCard());
		cardManager.setCard(CardType.MOVE_CAKE);
		CardType cardType = cardManager.getCard(); 
		assertEquals(CardType.MOVE_CAKE, cardType);
	}

}
