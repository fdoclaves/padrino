package gt.ia;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import gm.Cake;
import gm.GameTable;
import gm.Player;
import gm.TableSeat;
import gm.cards.CakeCard;
import gm.cards.SleepCard;
import gm.ia.IA_Manager;
import gm.ia.pojos.InfoAction;
import gm.info.CardType;
import gm.pojos.Position;
import gt.extras.Converter;

public class IA_ManagerPutCakeTest {

	private static final String B = "B";

	private static final String R = "R";

	private Converter converter;
	
	private Player playerB;

	@Before
	public void setUp() throws Exception {
		converter = new Converter(9, 5);
		List<CardType> list = new ArrayList<CardType>();
		list.add(CardType.MOVE_CAKE);
		list.add(CardType.KNIFE);
		list.add(CardType.GUN);
		list.add(CardType.CAKE);
		list.add(CardType.SLEEP);
		playerB = new Player(B, list);
	}

	@Test
	public void putCake_deadTwo() {

		// ...........................|0 ..|01 ..|02 .|03 .|04 .|05 .|06 .|07 .|08|
		String[][] playerChairs2 = { { "V", "R", "R", "V", "V", "V", "V", "V", "V" },
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" }, 
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" },
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" }, 
									 { "V", "V", "V", "V", "B", "V", "V", "V", "V" } };

		// ...........................|0 ....|01 .|02 ...|03 ..|04 ..|05 ..|06 ..|07 ...|08|
		String[][] TABLE_VALUES2 = { { "__", "3$", "3$", "k_", "__", "__", "__", "__", "__" },
									 { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
									 { "M_", "**", "**", "**", "**", "**", "**", "**", "__" },
									 { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
									 { "__", "__", "2$", "__", "__", "__", "__", "__", "__" } };

		TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
		GameTable gameTable = new GameTable(tableSeats);
		IA_Manager manager = new IA_Manager(gameTable);
		InfoAction infoAction = manager.whoKill(converter.toCharacterArray(playerChairs2), playerB, R, 3);
		assertEquals("PUT CAKE:ENEMIES:2",infoAction.getReason());
		assertEquals(1, infoAction.getCards().size());
		assertEquals(CardType.CAKE, infoAction.getCards().get(0).getType());
		CakeCard cakeCard = (CakeCard) infoAction.getCards().get(0);
		assertTrue(""+cakeCard.getCakePosition(),new Position(1, 0).isEquals(cakeCard.getCakePosition()));
	}
	
	   @Test
	    public void putCake_butFistSleep() {

	        // ...........................|0 ..|01 ..|02 .|03 .|04 .|05 .|06 .|07 .|08|
	        String[][] playerChairs2 = { { "V", "RZ", "RZ", "V", "V", "V", "RZ", "V", "V" },
	                                     { "V", "*", "*", "*", "*", "*", "*", "*", "V" }, 
	                                     { "V", "*", "*", "*", "*", "*", "*", "*", "V" },
	                                     { "V", "*", "*", "*", "*", "*", "*", "*", "V" }, 
	                                     { "V", "V", "V", "V", "B", "V", "V", "V", "V" } };

	        // ...........................|0 ....|01 .|02 ...|03 ..|04 ..|05 ..|06 ..|07 ...|08|
	        String[][] TABLE_VALUES2 = { { "__", "3$G", "3$G", "k_", "__", "__", "G_", "__", "__" },
	                                     { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
	                                     { "M_", "**", "**", "**", "**", "**", "**", "**", "__" },
	                                     { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
	                                     { "__", "__", "2$", "__", "__", "__", "__", "__", "__" } };

	        TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
	        GameTable gameTable = new GameTable(tableSeats);
	        IA_Manager manager = new IA_Manager(gameTable);
	        InfoAction infoAction = manager.whoKill(converter.toCharacterArray(playerChairs2), playerB, R, 3);
	        assertEquals("HAY MAS DE 3 DORMIDOS",infoAction.getReason());
	        assertEquals(1, infoAction.getCards().size());
	        assertEquals(CardType.SLEEP, infoAction.getCards().get(0).getType());
	        SleepCard sleepCard = (SleepCard) infoAction.getCards().get(0);
	        assertEquals(3, sleepCard.getPositionList().size());
	    }
	
	@Test
	public void putCake_deadOne() {

		// ...........................|0 ..|01 ..|02 .|03 .|04 .|05 .|06 .|07 .|08|
		String[][] playerChairs2 = { { "V", "V", "V", "V", "V", "V", "V", "V", "V" },
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" }, 
									 { "V", "*", "*", "*", "*", "*", "*", "*", "RK" },
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" }, 
									 { "V", "V", "V", "V", "B", "V", "V", "V", "V" } };

		// ...........................|0 ....|01 .|02 ...|03 ..|04 ..|05 ..|06 ..|07 ...|08|
		String[][] TABLE_VALUES2 = { { "__", "3$", "3$", "k_", "__", "__", "__", "__", "__" },
									 { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
									 { "M_", "**", "**", "**", "**", "**", "**", "**", "__" },
									 { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
									 { "__", "__", "2$", "__", "__", "__", "__", "__", "__" } };

		TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
		GameTable gameTable = new GameTable(tableSeats);
		IA_Manager manager = new IA_Manager(gameTable);
		InfoAction infoAction = manager.whoKill(converter.toCharacterArray(playerChairs2), playerB, R, 3);
		assertEquals("PUT CAKE:ENEMIES:1",infoAction.getReason());
		assertEquals(1, infoAction.getCards().size());
		assertEquals(CardType.CAKE, infoAction.getCards().get(0).getType());
		CakeCard cakeCard = (CakeCard) infoAction.getCards().get(0);
		assertTrue(""+cakeCard.getCakePosition(), new Position(8, 1).isEquals(cakeCard.getCakePosition()));
	}
	
	@Test
	public void notToPutCakeIfThereisACakeHere() {

		// ...........................|0 ..|01 ..|02 .|03 .|04 .|05 .|06 .|07 .|08|
		String[][] playerChairs2 = { { "V", "V", "V", "V", "V", "V", "V", "V", "V" },
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" }, 
									 { "V", "*", "*", "*", "*", "*", "*", "*", "RK" },
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" }, 
									 { "V", "V", "V", "V", "B", "V", "V", "V", "V" } };

		// ...........................|0 ....|01 .|02 ...|03 ..|04 ..|05 ..|06 ..|07 ...|08|
		String[][] TABLE_VALUES2 = { { "__", "3$", "3$", "k_", "__", "__", "__", "__", "__" },
									 { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
									 { "M_", "**", "**", "**", "**", "**", "**", "**", "__" },
									 { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
									 { "__", "__", "2$", "__", "__", "__", "__", "__", "__" } };

		TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
		GameTable gameTable = new GameTable(tableSeats);
		gameTable.add(new Cake(new Position(8, 3), "N", gameTable));
		IA_Manager manager = new IA_Manager(gameTable);
		InfoAction infoAction = manager.whoKill(converter.toCharacterArray(playerChairs2), playerB, R, 3);
		assertEquals("PEOR ES NADA, PUT CAKE",infoAction.getReason());
		assertEquals(1, infoAction.getCards().size());
		assertEquals(CardType.CAKE, infoAction.getCards().get(0).getType());
		CakeCard cakeCard = (CakeCard) infoAction.getCards().get(0);
		assertTrue(""+cakeCard.getCakePosition(),new Position(8, 1).isEquals(cakeCard.getCakePosition()));
	}

}
