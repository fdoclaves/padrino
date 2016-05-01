package gt.ia;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import gm.Cake;
import gm.Card;
import gm.GameTable;
import gm.Player;
import gm.TableSeat;
import gm.cards.MoveCard;
import gm.ia.IaCardsManager;
import gm.info.CardType;
import gm.pojos.Position;
import gt.extras.Converter;

public class IA_ManagerMoveTest {

	private static final int TOTAL_MONEY = 100;

	private static final String B = "B";

	private static final String R = "R";
	
	private Converter converter;
	
	private Player playerB;

	@Before
	public void setUp() throws Exception {
		converter = new Converter(9, 5);
		List<CardType> list = new ArrayList<CardType>();
		list.add(CardType.MOVE);
		list.add(CardType.MOVE);
		list.add(CardType.SLEEP);
		list.add(CardType.SLEEP);
		list.add(CardType.SLEEP);
		playerB = new Player(B, list);
	}

	@Test
	public void moveIfHasToCardMoveAndCake() {

		// ...........................|0 ..|01 ..|02 .|03 .|04 .|05 .|06 .|07 .|08|
		String[][] playerChairs2 = { { "V", "R", "R", "V", "V", "V", "V", "V", "V" },
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" }, 
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" },
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" }, 
									 { "V", "V", "V", "B", "B", "V", "V", "V", "V" } };

		// ...........................|0 ....|01 .|02 ...|03 ..|04 ..|05 ..|06 ..|07 ...|08|
		String[][] TABLE_VALUES2 = { { "__", "3$", "3$", "k_", "__", "__", "__", "__", "__" },
									 { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
									 { "M_", "**", "**", "**", "**", "**", "**", "**", "__" },
									 { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
									 { "__", "__", "2$", "__", "__", "__", "__", "__", "__" } };

		TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
		GameTable gameTable = new GameTable(tableSeats, TOTAL_MONEY);
		gameTable.add(new Cake(new Position(4, 4), R, gameTable));
		IaCardsManager manager = new IaCardsManager(gameTable);
		Card usedCard = manager.get1stCard(converter.toCharacterArray(playerChairs2), playerB, R, 3);
		assertEquals("MOVE, CAKE:2",usedCard.getReason());
		assertEquals(CardType.MOVE, usedCard.getType());
		MoveCard cakeCard = (MoveCard) usedCard;
		assertTrue(""+cakeCard.getWhoToMove(),new Position(4, 4).isEquals(cakeCard.getWhoToMove()));
	}
	
	@Test
	public void nadaMasQueHacerSeTien2Move() {

		// ...........................|0 ..|01 ..|02 .|03 .|04 .|05 .|06 .|07 .|08|
		String[][] playerChairs2 = { { "V", "R", "R", "V", "V", "V", "V", "V", "V" },
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" }, 
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" },
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" }, 
									 { "V", "V", "Rk", "B", "B", "V", "V", "V", "V" } };

		// ...........................|0 ....|01 .|02 ...|03 ..|04 ..|05 ..|06 ..|07 ...|08|
		String[][] TABLE_VALUES2 = { { "__", "3$", "3$", "k_", "__", "__", "__", "__", "__" },
									 { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
									 { "M_", "**", "**", "**", "**", "**", "**", "**", "__" },
									 { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
									 { "__", "__", "2$", "__", "__", "__", "__", "__", "__" } };

		TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
		GameTable gameTable = new GameTable(tableSeats, TOTAL_MONEY);
		IaCardsManager manager = new IaCardsManager(gameTable);
		Card usedCard = manager.get1stCard(converter.toCharacterArray(playerChairs2), playerB, R, 3);
		assertEquals("NADA MAS QUE HACER, MOVE",usedCard.getReason());
		assertEquals(CardType.MOVE, usedCard.getType());
		MoveCard cakeCard = (MoveCard) usedCard;
		assertTrue(""+cakeCard.getWhoToMove(),new Position(3, 4).isEquals(cakeCard.getWhoToMove()));
	}
	
}
