package gt;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import gm.Card;
import gm.Player;
import gm.cards.GunCard;
import gm.cards.KnifeCard;
import gm.info.CardType;

public class PlayerTest {
	
	private Player player;
	
	private List<CardType> cards;

	@Before
	public void setUp() throws Exception {
		cards = new ArrayList<CardType>();
		player = new Player("N", cards);
	}

	@Test
	public void getMoreCard() {
		cards.add(CardType.GUN);
		cards.add(CardType.GUN);
		cards.add(CardType.GUN);
		cards.add(CardType.KNIFE);
		cards.add(CardType.MOVE);
		Card choice = player.getCard(new GunCard(null, null), new KnifeCard(null, null));
		assertEquals(CardType.GUN, choice.getType());
	}
	
	@Test
	public void getMoreCard2() {
		cards.add(CardType.GUN);
		cards.add(CardType.KNIFE);
		cards.add(CardType.KNIFE);
		cards.add(CardType.KNIFE);
		cards.add(CardType.MOVE);
		Card choice = player.getCard(new GunCard(null, null), new KnifeCard(null, null));
		assertEquals(CardType.KNIFE, choice.getType());
	}
	
	@Test
	public void getFist() {
		cards.add(CardType.GUN);
		cards.add(CardType.KNIFE);
		cards.add(CardType.CAKE);
		cards.add(CardType.MOVE);
		cards.add(CardType.MOVE);
		Card choice = player.getCard(new GunCard(null, null), new KnifeCard(null, null));
		assertEquals(CardType.GUN, choice.getType());
	}

}
