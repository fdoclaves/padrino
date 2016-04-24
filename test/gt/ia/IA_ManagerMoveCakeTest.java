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
import gm.cards.MoveCakeCard;
import gm.ia.IA_Manager;
import gm.ia.pojos.InfoAction;
import gm.info.CardType;
import gm.pojos.Position;
import gt.extras.Converter;

public class IA_ManagerMoveCakeTest {

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
		GameTable gameTable = new GameTable(tableSeats);
		gameTable.add(new Cake(new Position(2, 0), "N", gameTable));
		IA_Manager manager = new IA_Manager(gameTable);
		InfoAction attackedPositionIA = manager.whoKill(converter.toCharacterArray(playerChairs2), playerB, R, 3);
		assertEquals("ATTACK CAKE ME, MOVE CAKE",attackedPositionIA.getReason());
		assertEquals(1, attackedPositionIA.getCards().size());
		assertEquals(CardType.MOVE_CAKE, attackedPositionIA.getCards().get(0).getType());
		MoveCakeCard card = (MoveCakeCard) attackedPositionIA.getCards().get(0);
		assertTrue(new Position(5, 0).isEquals(card.getNewPosition()));
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
		GameTable gameTable = new GameTable(tableSeats);
		gameTable.add(new Cake(new Position(2, 0), "N", gameTable));
		IA_Manager manager = new IA_Manager(gameTable);
		InfoAction attackedPositionIA = manager.whoKill(converter.toCharacterArray(playerChairs2), playerB, R, 3);
		assertEquals("ATTACK CAKE ME, MOVE CAKE",attackedPositionIA.getReason());
		assertEquals(1, attackedPositionIA.getCards().size());
		assertEquals(CardType.MOVE_CAKE, attackedPositionIA.getCards().get(0).getType());
		MoveCakeCard card = (MoveCakeCard) attackedPositionIA.getCards().get(0);
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
		GameTable gameTable = new GameTable(tableSeats);
		gameTable.add(new Cake(new Position(2, 0), "N", gameTable));
		IA_Manager manager = new IA_Manager(gameTable);
		InfoAction attackedPositionIA = manager.whoKill(converter.toCharacterArray(playerChairs2), playerB, R, 3);
		assertEquals("NO PUEDO ATACAR//I DONT HAVE SLEEPCARD//CHANGE CARD",attackedPositionIA.getReason());
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
		GameTable gameTable = new GameTable(tableSeats);
		gameTable.add(new Cake(new Position(2, 0), "N", gameTable));
		gameTable.add(new Cake(new Position(5, 0), "R", gameTable));
		IA_Manager manager = new IA_Manager(gameTable);
		InfoAction attackedPositionIA = manager.whoKill(converter.toCharacterArray(playerChairs2), playerB, R, 3);
		assertEquals("ATTACK CAKE ME, MOVE CAKE",attackedPositionIA.getReason());
		assertEquals(1, attackedPositionIA.getCards().size());
		assertEquals(CardType.MOVE_CAKE, attackedPositionIA.getCards().get(0).getType());
		MoveCakeCard card = (MoveCakeCard) attackedPositionIA.getCards().get(0);
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
		GameTable gameTable = new GameTable(tableSeats);
		gameTable.add(new Cake(new Position(2, 0), "N", gameTable));
		IA_Manager manager = new IA_Manager(gameTable);
		InfoAction attackedPositionIA = manager.whoKill(converter.toCharacterArray(playerChairs2), playerB, R, 3);
		assertEquals("MOVE CAKE BY BUSINESS: -100.0",attackedPositionIA.getReason());
		assertEquals(1, attackedPositionIA.getCards().size());
		assertEquals(CardType.MOVE_CAKE, attackedPositionIA.getCards().get(0).getType());
		MoveCakeCard card = (MoveCakeCard) attackedPositionIA.getCards().get(0);
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
		GameTable gameTable = new GameTable(tableSeats);
		gameTable.add(new Cake(new Position(2, 0), "N", gameTable));
		IA_Manager manager = new IA_Manager(gameTable);
		InfoAction attackedPositionIA = manager.whoKill(converter.toCharacterArray(playerChairs2), playerB, R, 3);
		assertEquals("MORE ENEMIES, MOVE CAKE",attackedPositionIA.getReason());
		assertEquals(1, attackedPositionIA.getCards().size());
		assertEquals(CardType.MOVE_CAKE, attackedPositionIA.getCards().get(0).getType());
		MoveCakeCard card = (MoveCakeCard) attackedPositionIA.getCards().get(0);
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
		GameTable gameTable = new GameTable(tableSeats);
		gameTable.add(new Cake(new Position(2, 0), "N", gameTable));
		IA_Manager manager = new IA_Manager(gameTable);
		InfoAction attackedPositionIA = manager.whoKill(converter.toCharacterArray(playerChairs2), playerB, R, 3);
		assertEquals("MORE ENEMIES, MOVE CAKE",attackedPositionIA.getReason());
		assertEquals(1, attackedPositionIA.getCards().size());
		assertEquals(CardType.MOVE_CAKE, attackedPositionIA.getCards().get(0).getType());
		MoveCakeCard card = (MoveCakeCard) attackedPositionIA.getCards().get(0);
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
		GameTable gameTable = new GameTable(tableSeats);
		gameTable.add(new Cake(new Position(2, 0), "N", gameTable));
		gameTable.add(new Cake(new Position(6, 0), "N", gameTable));
		IA_Manager manager = new IA_Manager(gameTable);
		InfoAction attackedPositionIA = manager.whoKill(converter.toCharacterArray(playerChairs2), playerB, R, 3);
		assertEquals("ATTACK CAKE ME, MOVE CAKE",attackedPositionIA.getReason());
		assertEquals(1, attackedPositionIA.getCards().size());
		assertEquals(CardType.MOVE_CAKE, attackedPositionIA.getCards().get(0).getType());
		MoveCakeCard card = (MoveCakeCard) attackedPositionIA.getCards().get(0);
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
		GameTable gameTable = new GameTable(tableSeats);
		gameTable.add(new Cake(new Position(6, 0), "N", gameTable));
		gameTable.add(new Cake(new Position(2, 0), "N", gameTable));
		IA_Manager manager = new IA_Manager(gameTable);
		InfoAction attackedPositionIA = manager.whoKill(converter.toCharacterArray(playerChairs2), playerB, R, 3);
		assertEquals("ATTACK CAKE ME, MOVE CAKE",attackedPositionIA.getReason());
		assertEquals(1, attackedPositionIA.getCards().size());
		assertEquals(CardType.MOVE_CAKE, attackedPositionIA.getCards().get(0).getType());
		MoveCakeCard card = (MoveCakeCard) attackedPositionIA.getCards().get(0);
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
		GameTable gameTable = new GameTable(tableSeats);
		gameTable.add(new Cake(new Position(6, 0), "N", gameTable));
		gameTable.add(new Cake(new Position(2, 0), "N", gameTable));
		IA_Manager manager = new IA_Manager(gameTable);
		InfoAction attackedPositionIA = manager.whoKill(converter.toCharacterArray(playerChairs2), playerB, R, 3);
		assertEquals("ATTACK CAKE ME, MOVE CAKE",attackedPositionIA.getReason());
		assertEquals(1, attackedPositionIA.getCards().size());
		assertEquals(CardType.MOVE_CAKE, attackedPositionIA.getCards().get(0).getType());
		MoveCakeCard card = (MoveCakeCard) attackedPositionIA.getCards().get(0);
		assertTrue(new Position(5, 4).isEquals(card.getNewPosition()));
	}

}
