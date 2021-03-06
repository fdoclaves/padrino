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
import gm.cards.CakeCard;
import gm.cards.SleepCard;
import gm.ia.IA_PlaysController;
import gm.ia.PlaysController;
import gm.info.CardType;
import gm.pojos.Position;
import gt.extras.Converter;

public class IA_PlaysControllerPutCakeTest {

	private static final int TOTAL_MONEY = 100;

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
		GameTable gameTable = new GameTable(tableSeats, TOTAL_MONEY);
		PlaysController manager = new IA_PlaysController(gameTable);
		Card usedCard = manager.get1stCard(converter.toCharacterArray(playerChairs2), playerB, R, 3);
		assertEquals("PUT CAKE:ENEMIES:2",usedCard.getReason());
		assertEquals(CardType.CAKE, usedCard.getType());
		CakeCard cakeCard = (CakeCard) usedCard;
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
	        GameTable gameTable = new GameTable(tableSeats, TOTAL_MONEY);
	        PlaysController manager = new IA_PlaysController(gameTable);
	        Card usedCard = manager.get1stCard(converter.toCharacterArray(playerChairs2), playerB, R, 3);
	        assertEquals("HAY MAS DE 3 DORMIDOS",usedCard.getReason());
	        assertEquals(CardType.SLEEP, usedCard.getType());
	        SleepCard sleepCard = (SleepCard) usedCard;
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
		GameTable gameTable = new GameTable(tableSeats, TOTAL_MONEY);
		PlaysController manager = new IA_PlaysController(gameTable);
		Card usedCard = manager.get1stCard(converter.toCharacterArray(playerChairs2), playerB, R, 3);
		assertEquals("PUT CAKE:ENEMIES:1",usedCard.getReason());
		assertEquals(CardType.CAKE, usedCard.getType());
		CakeCard cakeCard = (CakeCard) usedCard;
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
		GameTable gameTable = new GameTable(tableSeats, TOTAL_MONEY);
		gameTable.add(new Cake(new Position(8, 3), "N", gameTable));
		PlaysController manager = new IA_PlaysController(gameTable);
		Card usedCard = manager.get1stCard(converter.toCharacterArray(playerChairs2), playerB, R, 3);
		assertEquals("PEOR ES NADA, PUT CAKE",usedCard.getReason());
		assertEquals(CardType.CAKE, usedCard.getType());
		CakeCard cakeCard = (CakeCard) usedCard;
		assertTrue(""+cakeCard.getCakePosition(),new Position(8, 1).isEquals(cakeCard.getCakePosition()));
	}
	
	@Test
	public void simulacion_correcto() {

		// ...........................|0 ...|01 ...|02 .|03 ...|04 ..|05 .|06 ..|07 ..|08|
		String[][] TABLE_VALUES2 = { { "PG", "_G", "kG", "1$", "__", "__", "P_", "__", "P_" },
									 { "2$", "**", "**", "**", "**", "**", "**", "**", "GP" },
									 { "kG", "__", "__", "GP", "__", "GP", "3$", "__", "k_" } };

		// ............................|0 ..|01 ...|02 ..|03 ..|04 ..|05 ..|06 ..|07 .|08|
		String[][] playerChairs2 = { { "VV", "VV", "3P", "1kP", "VV", "3k", "VV", "VV", "VV" },
									 { "1Pk", "**", "**", "**", "**", "**", "**", "**", "VV" },
									 { "V", "VV", "VV", "V", "VV", "VV", "2_", "VV", "VV" } };

		TableSeat[][] tableSeats = new Converter(9, 3).to(TABLE_VALUES2);
		GameTable gameTable = new GameTable(tableSeats, TOTAL_MONEY);
		PlaysController manager = new IA_PlaysController(gameTable);
		List<CardType> cards = new ArrayList<CardType>();
		cards.add(CardType.BOOM);
		cards.add(CardType.BOOM);
		cards.add(CardType.BOOM);
		cards.add(CardType.MOVE_CAKE);
		cards.add(CardType.CAKE);
		Player player1 = new Player("2", cards);
		Card usedCard = manager.get1stCard(new Converter(9, 3).toCharacterArray(playerChairs2), player1, "3", 3);
		assertEquals("PUT CAKE:ENEMIES:2",usedCard.getReason());
		assertEquals(CardType.CAKE, usedCard.getType());
		CakeCard cakeCard = (CakeCard) usedCard;
		assertTrue(""+cakeCard.getCakePosition(),new Position(2, 0).isEquals(cakeCard.getCakePosition()));
	}
	
	@Test
    public void simulacion_PuedenAtacarme() {

        // ...........................|0 ...|01 ...|02 .|03 ...|04 ..|05 .|06 ..|07 ..|08|
        String[][] TABLE_VALUES2 = { { "PG", "_G", "kG", "1$", "__", "__", "P_", "__", "P_" },
                                     { "2$", "**", "**", "**", "**", "**", "**", "**", "GP" },
                                     { "kG", "__", "__", "GP", "__", "GP", "3$", "__", "k_" } };

        // ............................|0 ..|01 ...|02 ..|03 ..|04 ..|05 ..|06 ..|07 .|08|
        String[][] playerChairs2 = { { "VV", "VV", "VV", "1kP", "1kP", "VV", "VV", "VV", "VV" },
                                     { "2_", "**", "**", "**", "**", "**", "**", "**", "VV" },
                                     { "2_", "VV", "2_", "2_", "VV", "VV", "1P", "VV", "2_" } };

        TableSeat[][] tableSeats = new Converter(9, 3).to(TABLE_VALUES2);
        GameTable gameTable = new GameTable(tableSeats, TOTAL_MONEY);
        gameTable.add(new Cake(new Position(4, 0), "3", gameTable));
        PlaysController manager = new IA_PlaysController(gameTable);
        List<CardType> cards = new ArrayList<CardType>();
        cards.add(CardType.SLEEP);
        cards.add(CardType.SLEEP);
        cards.add(CardType.SLEEP);
        cards.add(CardType.SLEEP);
        cards.add(CardType.CAKE);
        Player player1 = new Player("1", cards);
        Card usedCard = manager.get1stCard(new Converter(9, 3).toCharacterArray(playerChairs2), player1, "2", 3);
        assertEquals("PUT CAKE:ENEMIES:2",usedCard.getReason());
        assertEquals(CardType.CAKE, usedCard.getType());
        CakeCard cakeCard = (CakeCard) usedCard;
        assertTrue(""+cakeCard.getCakePosition(),new Position(2, 2).isEquals(cakeCard.getCakePosition()));
    }

}
