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
import gm.cards.MoveCakeCard;
import gm.ia.IA_PlaysController;
import gm.ia.PlaysController;
import gm.info.CardType;
import gm.pojos.Position;
import gt.extras.Converter;

public class IA_PlaysControllerMoveCakeTest {

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
		list.add(CardType.MOVE_CAKE);
		list.add(CardType.MOVE);
		playerB = new Player(B, list);
	}

	@Test
	public void saveMeOfCake() {

		// ...........................|0 ..|01 ..|02 .|03 .|04 .|05 .|06 .|07 .|08|
		String[][] playerChairs2 = { { "V", "A", "B", "V", "V", "A", "V", "R", "V" },
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" }, 
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" },
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" }, 
									 { "V", "V", "V", "B", "V", "V", "V", "V", "V" } };

		// ...........................|0 ....|01 .|02 ...|03 ..|04 ..|05 ..|06 ..|07 ...|08|
		String[][] TABLE_VALUES2 = { { "P_", "3$", "3$_", "k_", "__", "__", "P_", "P_", "P_" },
									 { "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
									 { "M_", "**", "**", "**", "**", "**", "**", "**", "P_" },
									 { "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
									 { "k_", "__", "2$", "__", "__", "__", "1$", "Pk", "k_" } };

		TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
		GameTable gameTable = new GameTable(tableSeats, TOTAL_MONEY);
		gameTable.add(new Cake(new Position(2, 0), "N", gameTable));
		PlaysController manager = new IA_PlaysController(gameTable);
		Card card = manager.get1stCard(converter.toCharacterArray(playerChairs2), playerB, R, 3);
		assertEquals("ATTACK CAKE ME, MOVE CAKE",card.getReason());
		assertEquals(CardType.MOVE_CAKE, card.getType());
		MoveCakeCard moveCakeCard = (MoveCakeCard) card;
		assertTrue(new Position(5, 0).isEquals(moveCakeCard.getNewPosition()));
	}
	
	@Test
	public void muerenMenosMios() {

		// ...........................|0 ..|01 ..|02 .|03 .|04 .|05 .|06 .|07 .|08|
		String[][] playerChairs2 = { { "V", "B", "B", "V", "V", "B", "R", "R", "V" },
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" }, 
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" },
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" }, 
									 { "V", "V", "V", "B", "V", "V", "V", "V", "V" } };

		// ...........................|0 ....|01 .|02 ...|03 ..|04 ..|05 ..|06 ..|07 ...|08|
		String[][] TABLE_VALUES2 = { { "P_", "3$", "3$_", "k_", "__", "__", "P_", "P_", "P_" },
									 { "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
									 { "M_", "**", "**", "**", "**", "**", "**", "**", "P_" },
									 { "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
									 { "k_", "__", "2$", "__", "__", "__", "1$", "Pk", "k_" } };

		TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
		GameTable gameTable = new GameTable(tableSeats, TOTAL_MONEY);
		gameTable.add(new Cake(new Position(2, 0), "N", gameTable));
		PlaysController manager = new IA_PlaysController(gameTable);
		Card usedCard = manager.get1stCard(converter.toCharacterArray(playerChairs2), playerB, R, 3);
		assertEquals("ATTACK CAKE ME, MOVE CAKE",usedCard.getReason());
		assertEquals(CardType.MOVE_CAKE, usedCard.getType());
		MoveCakeCard card = (MoveCakeCard) usedCard;
		assertTrue(new Position(5, 0).isEquals(card.getNewPosition()));
	}
	
	@Test
	public void notMoveCake() {

		// ...........................|0 ..|01 ..|02 .|03 .|04 .|05 .|06 .|07 .|08|
		String[][] playerChairs2 = { { "V", "V", "B", "V", "V", "B", "B", "R", "V" },
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" }, 
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" },
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" }, 
									 { "V", "V", "V", "B", "V", "V", "V", "V", "V" } };

		// ...........................|0 ....|01 .|02 ...|03 ..|04 ..|05 ..|06 ..|07 ...|08|
		String[][] TABLE_VALUES2 = { { "P_", "3$", "__", "k_", "__", "__", "P_", "P_", "P_" },
									 { "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
									 { "M_", "**", "**", "**", "**", "**", "**", "**", "P_" },
									 { "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
									 { "k_", "__", "2$", "__", "__", "__", "1$", "Pk", "k_" } };

		TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
		GameTable gameTable = new GameTable(tableSeats, TOTAL_MONEY);
		gameTable.add(new Cake(new Position(2, 0), "N", gameTable));
		PlaysController manager = new IA_PlaysController(gameTable);
		Card usedCard = manager.get1stCard(converter.toCharacterArray(playerChairs2), playerB, R, 3);
		assertEquals("NO PUEDO ATACAR//CHANGE CARD",usedCard.getReason());
	}
	
	@Test
	public void fatalMoveCakeFist() {

		// ...........................|0 ..|01 ..|02 .|03 .|04 .|05 .|06 .|07 .|08|
		String[][] playerChairs2 = { { "V", "V", "B", "V", "V", "B", "V", "R", "V" },
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" }, 
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" },
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" }, 
									 { "V", "V", "V", "R", "V", "V", "V", "V", "V" } };

		// ...........................|0 ....|01 .|02 ...|03 ..|04 ..|05 ..|06 ..|07 ...|08|
		String[][] TABLE_VALUES2 = { { "P_", "3$", "3$_", "k_", "__", "__", "P_", "P_", "P_" },
									 { "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
									 { "M_", "**", "**", "**", "**", "**", "**", "**", "P_" },
									 { "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
									 { "k_", "__", "2$", "__", "__", "__", "1$", "Pk", "k_" } };

		TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
		GameTable gameTable = new GameTable(tableSeats, TOTAL_MONEY);
		gameTable.add(new Cake(new Position(2, 0), "N", gameTable));
		gameTable.add(new Cake(new Position(5, 0), "R", gameTable));
		PlaysController manager = new IA_PlaysController(gameTable);
		Card usedCard = manager.get1stCard(converter.toCharacterArray(playerChairs2), playerB, R, 3);
		assertEquals("ATTACK CAKE ME, MOVE CAKE",usedCard.getReason());
		assertEquals(CardType.MOVE_CAKE, usedCard.getType());
		MoveCakeCard card = (MoveCakeCard) usedCard;
		assertTrue(new Position(7, 0).isEquals(card.getNewPosition()));
	}
	
	@Test
	public void muereMenosPoderoso() {

		// ...........................|0 ..|01 ..|02 .|03 .|04 .|05 .|06 .|07 .|08|
		String[][] playerChairs2 = { { "V", "V", "B", "V", "V", "B", "V", "R", "V" },
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" }, 
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" },
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" }, 
									 { "V", "V", "V", "BK", "V", "V", "V", "V", "V" } };

		// ...........................|0 ....|01 .|02 ...|03 ..|04 ..|05 ..|06 ..|07 ...|08|
		String[][] TABLE_VALUES2 = { { "P_", "3$", "3$_", "k_", "__", "__", "P_", "P_", "P_" },
									 { "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
									 { "M_", "**", "**", "**", "**", "**", "**", "**", "P_" },
									 { "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
									 { "k_", "__", "2$", "__", "__", "__", "1$", "Pk", "k_" } };

		TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
		GameTable gameTable = new GameTable(tableSeats, TOTAL_MONEY);
		gameTable.add(new Cake(new Position(2, 0), "N", gameTable));
		PlaysController manager = new IA_PlaysController(gameTable);
		Card usedCard = manager.get1stCard(converter.toCharacterArray(playerChairs2), playerB, R, 3);
		assertEquals("MOVE CAKE BY BUSINESS: -100.0",usedCard.getReason());
		assertEquals(CardType.MOVE_CAKE, usedCard.getType());
		MoveCakeCard card = (MoveCakeCard) usedCard;
		assertTrue(new Position(5, 0).isEquals(card.getNewPosition()));
	}
	
	@Test
	public void mueroYOPeroTambienEnemy() {

		// ...........................|0 ..|01 ..|02 .|03 .|04 .|05 .|06 .|07 .|08|
		String[][] playerChairs2 = { { "V", "V", "B", "V", "V", "B", "B", "R", "V" },
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" }, 
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" },
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" }, 
									 { "V", "V", "R", "B", "V", "V", "V", "V", "V" } };

		// ...........................|0 ....|01 .|02 ...|03 ..|04 ..|05 ..|06 ..|07 ...|08|
		String[][] TABLE_VALUES2 = { { "P_", "3$", "3$_", "k_", "__", "__", "P_", "P_", "P_" },
									 { "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
									 { "M_", "**", "**", "**", "**", "**", "**", "**", "P_" },
									 { "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
									 { "k_", "__", "2$", "__", "__", "__", "1$", "Pk", "k_" } };

		TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
		GameTable gameTable = new GameTable(tableSeats, TOTAL_MONEY);
		gameTable.add(new Cake(new Position(2, 0), "N", gameTable));
		PlaysController manager = new IA_PlaysController(gameTable);
		Card usedCard = manager.get1stCard(converter.toCharacterArray(playerChairs2), playerB, R, 3);
		assertEquals("MORE ENEMIES, MOVE CAKE",usedCard.getReason());
		assertEquals(CardType.MOVE_CAKE, usedCard.getType());
		MoveCakeCard card = (MoveCakeCard) usedCard;
		assertTrue(new Position(2, 4).isEquals(card.getNewPosition()));
	}
	
	@Test
	public void moveCakeToAttackMoreEnemies() {

		// ...........................|0 ..|01 ..|02 .|03 .|04 .|05 .|06 .|07 .|08|
		String[][] playerChairs2 = { { "V", "V", "R", "V", "V", "A", "A", "R", "V" },
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" }, 
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" },
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" }, 
									 { "V", "V", "V", "B", "V", "V", "V", "V", "V" } };

		// ...........................|0 ....|01 .|02 ...|03 ..|04 ..|05 ..|06 ..|07 ...|08|
		String[][] TABLE_VALUES2 = { { "P_", "3$", "3$_", "k_", "__", "__", "P_", "P_", "P_" },
									 { "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
									 { "M_", "**", "**", "**", "**", "**", "**", "**", "P_" },
									 { "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
									 { "k_", "__", "2$", "__", "__", "__", "1$", "Pk", "k_" } };

		TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
		GameTable gameTable = new GameTable(tableSeats, TOTAL_MONEY);
		gameTable.add(new Cake(new Position(2, 0), "N", gameTable));
		PlaysController manager = new IA_PlaysController(gameTable);
		Card usedCard = manager.get1stCard(converter.toCharacterArray(playerChairs2), playerB, R, 3);
		assertEquals("MORE ENEMIES, MOVE CAKE",usedCard.getReason());
		assertEquals(CardType.MOVE_CAKE, usedCard.getType());
		MoveCakeCard card = (MoveCakeCard) usedCard;
		assertTrue(new Position(5, 0).isEquals(card.getNewPosition()));
	}
	
	@Test
	public void _2CakeMoveBest() {

		// ...........................|0 ..|01 ..|02 .|03 .|04 .|05 .|06 .|07 .|08|
		String[][] playerChairs2 = { { "V", "V", "R", "V", "V", "V", "B", "V", "V" },
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" }, 
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" },
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" }, 
									 { "V", "R", "R", "B", "V", "R", "V", "V", "V" } };

		// ...........................|0 ....|01 .|02 ...|03 ..|04 ..|05 ..|06 ..|07 ...|08|
		String[][] TABLE_VALUES2 = { { "P_", "3$", "3$_", "k_", "__", "__", "P_", "P_", "P_" },
									 { "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
									 { "M_", "**", "**", "**", "**", "**", "**", "**", "P_" },
									 { "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
									 { "k_", "__", "2$", "__", "__", "__", "1$", "Pk", "k_" } };

		TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
		GameTable gameTable = new GameTable(tableSeats, TOTAL_MONEY);
		gameTable.add(new Cake(new Position(2, 0), "N", gameTable));
		gameTable.add(new Cake(new Position(6, 0), "N", gameTable));
		PlaysController manager = new IA_PlaysController(gameTable);
		Card usedCard = manager.get1stCard(converter.toCharacterArray(playerChairs2), playerB, R, 3);
		assertEquals("ATTACK CAKE ME, MOVE CAKE",usedCard.getReason());
		assertEquals(CardType.MOVE_CAKE, usedCard.getType());
		MoveCakeCard card = (MoveCakeCard) usedCard;
		assertTrue(new Position(5, 4).isEquals(card.getNewPosition()));
	}
	
	@Test
	public void _2CakeMoveBest2() {

		// ...........................|0 ..|01 ..|02 .|03 .|04 .|05 .|06 .|07 .|08|
		String[][] playerChairs2 = { { "V", "V", "R", "V", "V", "V", "B", "V", "V" },
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" }, 
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" },
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" }, 
									 { "V", "R", "R", "B", "V", "R", "V", "V", "V" } };

		// ...........................|0 ....|01 .|02 ...|03 ..|04 ..|05 ..|06 ..|07 ...|08|
		String[][] TABLE_VALUES2 = { { "P_", "3$", "3$_", "k_", "__", "__", "P_", "P_", "P_" },
									 { "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
									 { "M_", "**", "**", "**", "**", "**", "**", "**", "P_" },
									 { "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
									 { "k_", "__", "2$", "__", "__", "__", "1$", "Pk", "k_" } };

		TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
		GameTable gameTable = new GameTable(tableSeats, TOTAL_MONEY);
		gameTable.add(new Cake(new Position(6, 0), "N", gameTable));
		gameTable.add(new Cake(new Position(2, 0), "N", gameTable));
		PlaysController manager = new IA_PlaysController(gameTable);
		Card usedCard = manager.get1stCard(converter.toCharacterArray(playerChairs2), playerB, R, 3);
		assertEquals("ATTACK CAKE ME, MOVE CAKE",usedCard.getReason());
		assertEquals(CardType.MOVE_CAKE, usedCard.getType());
		MoveCakeCard card = (MoveCakeCard) usedCard;
		assertTrue(new Position(5, 4).isEquals(card.getNewPosition()));
	}
	
	
	@Test
	public void _2CakeMuerenMenos() {

		// ...........................|0 ..|01 ..|02 .|03 .|04 .|05 .|06 .|07 .|08|
		String[][] playerChairs2 = { { "V", "V", "B", "V", "V", "B", "B", "V", "V" },
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" }, 
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" },
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" }, 
									 { "V", "R", "R", "B", "V", "R", "V", "V", "V" } };

		// ...........................|0 ....|01 .|02 ...|03 ..|04 ..|05 ..|06 ..|07 ...|08|
		String[][] TABLE_VALUES2 = { { "P_", "3$", "3$_", "k_", "__", "__", "P_", "P_", "P_" },
									 { "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
									 { "M_", "**", "**", "**", "**", "**", "**", "**", "P_" },
									 { "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
									 { "k_", "__", "2$", "__", "__", "__", "1$", "Pk", "k_" } };

		TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
		GameTable gameTable = new GameTable(tableSeats, TOTAL_MONEY);
		gameTable.add(new Cake(new Position(6, 0), "N", gameTable));
		gameTable.add(new Cake(new Position(2, 0), "N", gameTable));
		PlaysController manager = new IA_PlaysController(gameTable);
		Card usedCard = manager.get1stCard(converter.toCharacterArray(playerChairs2), playerB, R, 3);
		assertEquals("ATTACK CAKE ME, MOVE CAKE",usedCard.getReason());
		assertEquals(CardType.MOVE_CAKE, usedCard.getType());
		MoveCakeCard card = (MoveCakeCard) usedCard;
		assertTrue(new Position(5, 4).isEquals(card.getNewPosition()));
	}
	
	@Test
	public void simulacion() {

		// ............................|0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] TABLE_VALUES2 = { { "PG", "_G", "kG", "1$", "__", "__", "P_", "__", "P_" },
									 { "2$", "**", "**", "**", "**", "**", "**", "**", "GP" },
									 { "kG", "__", "__", "GP", "__", "GP", "3$", "__", "k_" } };

		// ............................|0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] playerChairs2 = { { "1_", "1P", "2_", "1k", "1k", "1P", "1Z", "1P", "V" },
									 { "1P", "**", "**", "**", "**", "**", "**", "**", "2_" },
									 { "2_", "1_", "2_", "2_", "V", "3_", "2_", "2_", "2Z" } };

		Converter converter = new Converter(8, 3);
		TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
		GameTable gameTable = new GameTable(tableSeats, TOTAL_MONEY);
		gameTable.add(new Cake(new Position(1, 0), "2", gameTable));
		gameTable.add(new Cake(new Position(0, 2), "3", gameTable));
		List<CardType> cards = new ArrayList<CardType>();
		cards.add(CardType.SLEEP);
		cards.add(CardType.SLEEP);
		cards.add(CardType.KNIFE);
		cards.add(CardType.MOVE_CAKE);
		cards.add(CardType.KNIFE);
		Player player1 = new Player("1", cards);
		PlaysController manager = new IA_PlaysController(gameTable);
		Card usedCard = manager.get1stCard(converter.toCharacterArray(playerChairs2), player1, R, 3);
		assertEquals("ATTACK CAKE ME, MOVE CAKE",usedCard.getReason());
		assertEquals(CardType.MOVE_CAKE, usedCard.getType());
		MoveCakeCard card = (MoveCakeCard) usedCard;
		assertTrue(""+card.getCake().getPosition(), new Position(0, 2).isEquals(card.getCake().getPosition()));
		assertTrue(""+card.getNewPosition(), new Position(1, 2).isEquals(card.getNewPosition()));
	}

}
