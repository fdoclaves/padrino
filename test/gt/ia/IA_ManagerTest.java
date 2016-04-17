package gt.ia;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import gm.Cake;
import gm.Card;
import gm.GameTable;
import gm.Player;
import gm.TableSeat;
import gm.cards.ChangeCard;
import gm.cards.GunCard;
import gm.cards.KnifeCard;
import gm.cards.SleepCard;
import gm.ia.IA_Manager;
import gm.ia.pojos.InfoAction;
import gm.info.CardType;
import gm.pojos.Position;
import gt.extras.Converter;

public class IA_ManagerTest {

	private static final String B = "B";

	private static final String R = "R";

	// |0 |01 |02 |03 |04 |05 |06 |07 |08|
	private String[][] playerChairs = { { "A", "R", "B", "RP", "R", "R", "R", "R", "B" },
			{ "R", "*", "*", "*", "*", "*", "*", "*", "B" }, { "B", "*", "*", "*", "*", "*", "*", "*", "R" },
			{ "R", "*", "*", "*", "*", "*", "*", "*", "B" }, { "B", "R", "B", "B", "B", "B", "B", "B", "V" } };

	// |0 |01 |02 |03 |04 |05 |06 |07 |08|
	private String[][] TABLE_VALUES = { { "P_", "3$", "k_", "1$", "__", "2$", "P_", "Pk", "P_" },
			{ "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
			{ "M_", "**", "**", "**", "**", "**", "**", "**", "P_" },
			{ "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
			{ "k_", "__", "2$", "P_", "3$", "__", "1$", "P_", "k_" } };

	private Converter converter;
	
	private Player playerB;

	@Before
	public void setUp() throws Exception {
		converter = new Converter(9, 5);
		List<CardType> list = new ArrayList<CardType>();
		list.add(CardType.SLEEP);
		list.add(CardType.KNIFE);
		list.add(CardType.GUN);
		list.add(CardType.BOOM);
		list.add(CardType.MOVE);
		playerB = new Player(B, list);
	}

	@Test
	public void getAttactedPositionsTest() {
		TableSeat[][] tableSeats = converter.to(TABLE_VALUES);
		GameTable gameTable = new GameTable(tableSeats);
		IA_Manager manager = new IA_Manager(gameTable);
		List<InfoAction> attactedPositions = manager.getAttactedPositions(playerB, "",
				converter.toCharacterArray(playerChairs));
		assertEquals(5, attactedPositions.size());
	}

	@Test
	public void AtacarPorAlDespierto() {

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] playerChairs2 = { { "A", "R", "B", "RP", "R", "R", "R", "RZ", "B" },
				{ "R", "*", "*", "*", "*", "*", "*", "*", "B" }, { "B", "*", "*", "*", "*", "*", "*", "*", "R" },
				{ "R", "*", "*", "*", "*", "*", "*", "*", "B" }, { "B", "R", "B", "B", "B", "B", "B", "B", "V" } };

		TableSeat[][] tableSeats = converter.to(TABLE_VALUES);
		GameTable gameTable = new GameTable(tableSeats);
		IA_Manager manager = new IA_Manager(gameTable);
		InfoAction attackedPositionIA = manager.whoKill(converter.toCharacterArray(playerChairs2), playerB, R, 2);
		assertEquals("PUEDO ATACARLO//PUEDE ATACARME//VER FREE LETAL PASTEL//VER FREE PASTEL//VER DESPIERTOS//GANO DESPIERTOS: 1.0",
				attackedPositionIA.getReason());
		assertEquals(2, attackedPositionIA.getCards().size());
		assertTrue(attackedPositionIA.getCards().get(1) instanceof GunCard);
		GunCard gunCard = (GunCard) attackedPositionIA.getCards().get(1);
		assertTrue(new Position(3, 0).isEquals(gunCard.getAttackedPosition()));
		assertTrue(new Position(3, 4).isEquals(gunCard.getAttackerPosition()));
		assertTrue(new Position(3, 0).isEquals(attackedPositionIA.getAttackedPosition()));
	}
	
	@Test
	public void noPuedeAtacarmeTnCake() {
		
		//........................... |0 ...|01 ..|02 ..|03 ..|04 ..|05 |.06 |...07 ..|08|
		String[][] TABLE_VALUES2 = { { "P", "3$", "k_", "__", "__", "2$", "P_", "Pk", "P_" },
									 { "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
									 { "P_", "**", "**", "**", "**", "**", "**", "**", "__" },
									 { "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
									 { "k_", "__", "2$", "P_", "3$", "__", "1$", "P_", "k_" } };

									// |0 ..|01 |.02 |.03 ..|04 |05 |..06 ..|07 .|08|
		String[][] playerChairs2 = { { "V", "V", "V", "R", "V", "V", "V", "V", "V" },
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" }, 
									 { "B", "*", "*", "*", "*", "*", "*", "*", "R" },
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" }, 
									 { "V", "V", "V", "B", "V", "V", "V", "V", "V" } };

		TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
		GameTable gameTable = new GameTable(tableSeats);
		Cake cakeA = new Cake(new Position(8, 2), "A", gameTable);
		gameTable.add(cakeA);
		IA_Manager manager = new IA_Manager(gameTable);
		List<CardType> cards = new ArrayList<CardType>();
		cards.add(CardType.KNIFE);
		cards.add(CardType.GUN);
		cards.add(CardType.MOVE);
		cards.add(CardType.SLEEP);
		cards.add(CardType.MOVE);
		Player playerBWithoutGun = new Player(B, cards);
		InfoAction attackedPositionIA = manager.whoKill(converter.toCharacterArray(playerChairs2), playerBWithoutGun, R, 3);
		assertEquals("PUEDO ATACARLO//NO ME ATACA//VER FREE LETAL PASTEL//VER FREE PASTEL//GANO FREE CAKE: 1.0",attackedPositionIA.getReason());
		assertEquals(1, attackedPositionIA.getCards().size());
		Card card = attackedPositionIA.getCards().get(0);
		assertTrue(card instanceof GunCard);
		GunCard gunCard = (GunCard) card;
		assertTrue(new Position(3, 0).isEquals(gunCard.getAttackedPosition()));
	}
	
	@Test
	public void noPuedeAtacarmeTnFatalCake() {
		
		//........................... |0 ...|01 ..|02 ..|03 ..|04 ..|05 |.06 |...07 ..|08|
		String[][] TABLE_VALUES2 = { { "P", "3$", "k_", "__", "__", "2$", "P_", "Pk", "P_" },
									 { "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
									 { "P_", "**", "**", "**", "**", "**", "**", "**", "__" },
									 { "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
									 { "k_", "__", "2$", "P_", "3$", "__", "1$", "P_", "k_" } };

									// |0 ..|01 |.02 |.03 ..|04 |05 |..06 ..|07 .|08|
		String[][] playerChairs2 = { { "V", "V", "V", "R", "V", "V", "V", "V", "V" },
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" }, 
									 { "B", "*", "*", "*", "*", "*", "*", "*", "R" },
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" }, 
									 { "V", "V", "V", "B", "V", "V", "V", "V", "V" } };

		TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
		GameTable gameTable = new GameTable(tableSeats);
		Cake cakeA = new Cake(new Position(8, 2), "A", gameTable);
		gameTable.add(cakeA);
		Cake cakeR = new Cake(new Position(3, 0), "R", gameTable);
		gameTable.add(cakeR);
		IA_Manager manager = new IA_Manager(gameTable);
		List<CardType> cards = new ArrayList<CardType>();
		cards.add(CardType.KNIFE);
		cards.add(CardType.GUN);
		cards.add(CardType.MOVE);
		cards.add(CardType.SLEEP);
		cards.add(CardType.MOVE);
		Player playerBWithoutGun = new Player(B, cards);
		InfoAction attackedPositionIA = manager.whoKill(converter.toCharacterArray(playerChairs2), playerBWithoutGun, R, 3);
		assertEquals("PUEDO ATACARLO//NO ME ATACA//VER FREE LETAL PASTEL//GANO FREE FATAL CAKE: 1.0",attackedPositionIA.getReason());
		assertEquals(1, attackedPositionIA.getCards().size());
		Card card = attackedPositionIA.getCards().get(0);
		assertTrue(card instanceof GunCard);
		GunCard gunCard = (GunCard) card;
		assertTrue(new Position(8, 2).isEquals(gunCard.getAttackedPosition()));
	}
	
	@Test
	public void _2pastelUnoApuntoDeMatar() {
		
		//........................... |0 ...|01 ..|02 ..|03 ..|04 ..|05 |.06 |...07 ..|08|
		String[][] TABLE_VALUES2 = { { "P", "3$", "k_", "P_", "__", "2$", "P_", "Pk", "P_" },
									 { "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
									 { "P_", "**", "**", "**", "**", "**", "**", "**", "P_" },
									 { "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
									 { "k_", "__", "2$", "P_", "3$", "__", "1$", "P_", "k_" } };

									// |0 ..|01 |.02 |.03 ..|04 |05 |..06 ..|07 .|08|
		String[][] playerChairs2 = { { "V", "V", "V", "R", "V", "V", "V", "V", "V" },
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" }, 
									 { "B", "*", "*", "*", "*", "*", "*", "*", "R" },
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" }, 
									 { "V", "V", "V", "B", "V", "V", "V", "V", "V" } };

		TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
		GameTable gameTable = new GameTable(tableSeats);
		Cake cakeA = new Cake(new Position(8, 2), "A", gameTable);
		gameTable.add(cakeA);
		Cake cakeR = new Cake(new Position(3, 0), "R", gameTable);
		gameTable.add(cakeR);
		IA_Manager manager = new IA_Manager(gameTable);
		List<CardType> cards = new ArrayList<CardType>();
		cards.add(CardType.KNIFE);
		cards.add(CardType.GUN);
		cards.add(CardType.MOVE);
		cards.add(CardType.SLEEP);
		cards.add(CardType.MOVE);
		Player playerBWithoutGun = new Player(B, cards);
		InfoAction attackedPositionIA = manager.whoKill(converter.toCharacterArray(playerChairs2), playerBWithoutGun, R, 3);
		assertEquals("PUEDO ATACARLO//PUEDE ATACARME//VER FREE LETAL PASTEL//GANO FREE FATAL CAKE: 1.0",attackedPositionIA.getReason());
		assertEquals(1, attackedPositionIA.getCards().size());
		Card card = attackedPositionIA.getCards().get(0);
		assertTrue(card instanceof GunCard);
		GunCard gunCard = (GunCard) card;
		assertTrue(new Position(8, 2).isEquals(gunCard.getAttackedPosition()));
	}
	
	@Test
	public void atacarEvaluarBoomPastel() {
		
		//........................... |0 ...|01 ..|02 ..|03 ..|04 ..|05 |.06 |...07 ..|08|
		String[][] TABLE_VALUES2 = { { "P", "3$", "k_", "P_", "__", "2$", "P_", "Pk", "P_" },
									 { "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
									 { "P_", "**", "**", "**", "**", "**", "**", "**", "P_" },
									 { "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
									 { "k_", "__", "2$", "P_", "3$", "__", "1$", "P_", "k_" } };

									// |0 ..|01 |.02 |.03 ..|04 |05 |..06 ..|07 .|08|
		String[][] playerChairs2 = { { "V", "V", "V", "R", "V", "V", "V", "V", "V" },
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" }, 
									 { "B", "*", "*", "*", "*", "*", "*", "*", "R" },
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" }, 
									 { "V", "V", "V", "B", "V", "V", "V", "V", "V" } };

		TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
		GameTable gameTable = new GameTable(tableSeats);
		Cake cake = new Cake(new Position(8, 2), "A", gameTable);
		gameTable.add(cake);
		IA_Manager manager = new IA_Manager(gameTable);
		List<CardType> cards = new ArrayList<CardType>();
		cards.add(CardType.KNIFE);
		cards.add(CardType.GUN);
		cards.add(CardType.MOVE);
		cards.add(CardType.SLEEP);
		cards.add(CardType.MOVE);
		Player playerBWithoutGun = new Player(B, cards);
		InfoAction attackedPositionIA = manager.whoKill(converter.toCharacterArray(playerChairs2), playerBWithoutGun, R, 2);
		assertEquals("PUEDO ATACARLO//PUEDE ATACARME//VER FREE LETAL PASTEL//VER FREE PASTEL//GANO FREE CAKE: 1.0",attackedPositionIA.getReason());
		assertEquals(1, attackedPositionIA.getCards().size());
		Card card = attackedPositionIA.getCards().get(0);
		assertTrue(card instanceof GunCard);
		GunCard gunCard = (GunCard) card;
		assertTrue(new Position(3, 0).isEquals(gunCard.getAttackedPosition()));
	}
	
	@Test
	public void sinCardKnifeOrGunAndAnybodyToSleep() {
		
		//........................... |0 ...|01 ..|02 ..|03 ..|04 ..|05 |.06 |...07 ..|08|
		String[][] TABLE_VALUES2 = { { "P", "3$", "k_", "1$", "__", "2$", "P_", "Pk", "P_" },
									 { "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
									 { "M_", "**", "**", "**", "**", "**", "**", "**", "P_" },
									 { "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
									 { "k_", "__", "2$", "P_", "3$", "__", "1$", "P_", "k_" } };

									// |0 ..|01 |.02 |.03 ..|04 |05 |..06 ..|07 .|08|
		String[][] playerChairs2 = { { "A", "R", "B", "RP", "R", "R", "R", "RZ", "B" },
									 { "R", "*", "*", "*", "*", "*", "*", "*", "B" }, 
									 { "B", "*", "*", "*", "*", "*", "*", "*", "R" },
									 { "R", "*", "*", "*", "*", "*", "*", "*", "B" }, 
									 { "B", "R", "B", "B", "B", "B", "B", "B", "V" } };

		TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
		GameTable gameTable = new GameTable(tableSeats);
		IA_Manager manager = new IA_Manager(gameTable);
		List<CardType> cards = new ArrayList<CardType>();
		cards.add(CardType.BOOM);
		cards.add(CardType.MOVE);
		cards.add(CardType.MOVE);
		cards.add(CardType.SLEEP);
		cards.add(CardType.MOVE);
		Player playerBWithoutGun = new Player(B, cards);
		InfoAction attackedPositionIA = manager.whoKill(converter.toCharacterArray(playerChairs2), playerBWithoutGun, R, 2);
		assertEquals("NO PUEDO ATACAR//I DONT HAVE SLEEPCARD",attackedPositionIA.getReason());
		assertEquals(1, attackedPositionIA.getCards().size());
		Card card = attackedPositionIA.getCards().get(0);
		assertTrue(card instanceof ChangeCard);
		assertEquals(CardType.BOOM, card.getType());
	}
	
	@Test
	public void sinCardKnifeOrGun() {
		
		//........................... |0 ...|01 ..|02 ..|03 ..|04 ..|05 |.06 |...07 ..|08|
		String[][] TABLE_VALUES2 = { { "PG", "3$", "k_", "1$", "__", "2$", "P_", "Pk", "P_" },
									 { "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
									 { "M_", "**", "**", "**", "**", "**", "**", "**", "P_" },
									 { "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
									 { "k_", "__", "2$", "P_", "3$", "__", "1$", "P_", "k_" } };

									// |0 ..|01 |.02 |.03 ..|04 |05 |..06 ..|07 .|08|
		String[][] playerChairs2 = { { "A", "R", "B", "RP", "R", "R", "R", "RZ", "B" },
									 { "R", "*", "*", "*", "*", "*", "*", "*", "B" }, 
									 { "B", "*", "*", "*", "*", "*", "*", "*", "R" },
									 { "R", "*", "*", "*", "*", "*", "*", "*", "B" }, 
									 { "B", "R", "B", "B", "B", "B", "B", "B", "V" } };

		TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
		GameTable gameTable = new GameTable(tableSeats);
		IA_Manager manager = new IA_Manager(gameTable);
		List<CardType> cards = new ArrayList<CardType>();
		cards.add(CardType.BOOM);
		cards.add(CardType.MOVE);
		cards.add(CardType.MOVE);
		cards.add(CardType.SLEEP);
		cards.add(CardType.MOVE);
		Player playerBWithoutGun = new Player(B, cards);
		InfoAction attackedPositionIA = manager.whoKill(converter.toCharacterArray(playerChairs2), playerBWithoutGun, R, 2);
		assertEquals("NO PUEDO ATACAR",attackedPositionIA.getReason());
		assertEquals(1, attackedPositionIA.getCards().size());
		assertTrue(attackedPositionIA.getCards().get(0) instanceof SleepCard);
		SleepCard card = (SleepCard) attackedPositionIA.getCards().get(0);
		assertTrue(new Position(0, 0).isEquals(card.getPositionList().get(0)));
	}
	
	@Test
	public void AtacarPorAlDormidoSoloCartaKnife() {

									// |0 ..|01 |.02 |.03 ..|04 |05 |..06 ..|07 .|08|
		String[][] playerChairs2 = { { "A", "R", "B", "RP", "R", "R", "R", "RZ", "B" },
									 { "R", "*", "*", "*", "*", "*", "*", "*", "B" }, 
									 { "B", "*", "*", "*", "*", "*", "*", "*", "R" },
									 { "R", "*", "*", "*", "*", "*", "*", "*", "B" }, 
									 { "B", "R", "B", "B", "B", "B", "B", "B", "V" } };

		TableSeat[][] tableSeats = converter.to(TABLE_VALUES);
		GameTable gameTable = new GameTable(tableSeats);
		IA_Manager manager = new IA_Manager(gameTable);
		List<CardType> cards = new ArrayList<CardType>();
		cards.add(CardType.BOOM);
		cards.add(CardType.KNIFE);
		cards.add(CardType.KNIFE);
		cards.add(CardType.SLEEP);
		cards.add(CardType.KNIFE);
		Player playerBWithoutGun = new Player(B, cards);
		InfoAction attackedPositionIA = manager.whoKill(converter.toCharacterArray(playerChairs2), playerBWithoutGun, R, 2);
		assertEquals("PUEDO ATACARLO//PUEDE ATACARME//GANO ATACARME",
				attackedPositionIA.getReason());
		assertEquals(1, attackedPositionIA.getCards().size());
		assertTrue(attackedPositionIA.getCards().get(0) instanceof KnifeCard);
		KnifeCard card = (KnifeCard) attackedPositionIA.getCards().get(0);
		assertTrue(new Position(3, 0).isEquals(card.getAttackedPosition()));
		assertTrue(new Position(2, 0).isEquals(card.getAttackerPosition()));
		assertTrue(new Position(3, 0).isEquals(attackedPositionIA.getAttackedPosition()));
	}
	

	@Test
	public void AtacadoPorMasArmas() {
		TableSeat[][] tableSeats = converter.to(TABLE_VALUES);
		GameTable gameTable = new GameTable(tableSeats);
		IA_Manager manager = new IA_Manager(gameTable);
		InfoAction attackedPositionIA = manager.whoKill(converter.toCharacterArray(playerChairs), playerB, R, 2);
		assertEquals("PUEDO ATACARLO//PUEDE ATACARME//VER FREE LETAL PASTEL//VER FREE PASTEL//VER DESPIERTOS//MAS ARMAS//GANO MAS ARMAS:2.0",
				attackedPositionIA.getReason());
		assertEquals(1, attackedPositionIA.getCards().size());
		assertTrue(attackedPositionIA.getCards().get(0) instanceof GunCard);
		GunCard gunCard = (GunCard) attackedPositionIA.getCards().get(0);
		assertTrue(new Position(7, 4).isEquals(gunCard.getAttackerPosition()));
		assertTrue(new Position(7, 0).isEquals(gunCard.getAttackedPosition()));
		assertTrue(new Position(7, 0).isEquals(attackedPositionIA.getAttackedPosition()));
	}

	@Test
	public void matarALosDormidos() {

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] TABLE_VALUES2 = { { "P_", "3$G", "k_", "1$", "__", "2$", "P_", "k_", "P_" },
				{ "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "M_", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "k_", "__", "2$", "P_", "3$", "__", "1$", "P_", "kG" } };

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] playerChairs2 = { { "A", "RZ", "B", "RP", "R", "R", "B", "R", "B" },
				{ "R", "*", "*", "*", "*", "*", "*", "*", "B" }, { "B", "*", "*", "*", "*", "*", "*", "*", "R" },
				{ "R", "*", "*", "*", "*", "*", "*", "*", "B" }, { "B", "R", "B", "B", "B", "B", "B", "B", "RZ" } };

		TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
		GameTable gameTable = new GameTable(tableSeats);
		IA_Manager manager = new IA_Manager(gameTable);
		InfoAction attackedPositionIA = manager.whoKill(converter.toCharacterArray(playerChairs2), playerB, R, 2);
		assertEquals("HAY MAS DE 2 DORMIDOS", attackedPositionIA.getReason());
		assertEquals(1, attackedPositionIA.getCards().size());
		assertTrue(attackedPositionIA.getCards().get(0) instanceof SleepCard);
		SleepCard card = (SleepCard) attackedPositionIA.getCards().get(0);
		List<Position> asleep = card.getPositionList();
		assertEquals(2, asleep.size());
		assertTrue(asleep.get(0).isEquals(new Position(1, 0)));
		assertTrue(asleep.get(1).isEquals(new Position(8, 4)));
	}

	@Test
	public void AtacadoPorMasFlancos() {

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] TABLE_VALUES2 = { { "P_", "3$", "k_", "1$", "__", "2$", "P_", "k_", "P_" },
				{ "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "M_", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "k_", "__", "2$", "P_", "3$", "__", "1$", "P_", "k_" } };

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] playerChairs2 = { { "A", "R", "B", "RP", "R", "R", "B", "R", "B" },
				{ "R", "*", "*", "*", "*", "*", "*", "*", "B" }, { "B", "*", "*", "*", "*", "*", "*", "*", "R" },
				{ "R", "*", "*", "*", "*", "*", "*", "*", "B" }, { "B", "R", "B", "B", "B", "B", "B", "B", "R" } };

		TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
		GameTable gameTable = new GameTable(tableSeats);
		IA_Manager manager = new IA_Manager(gameTable);
		InfoAction attackedPositionIA = manager.whoKill(converter.toCharacterArray(playerChairs2), playerB, R, 2);
		assertEquals("PUEDO ATACARLO//PUEDE ATACARME//VER FREE LETAL PASTEL//VER FREE PASTEL//VER DESPIERTOS//MAS ARMAS//MAS FLANCOS//GANO FLANCOS:2.0",
				attackedPositionIA.getReason());
		assertEquals(1, attackedPositionIA.getCards().size());
		assertTrue(attackedPositionIA.getCards().get(0) instanceof GunCard);
		GunCard gunCard = (GunCard) attackedPositionIA.getCards().get(0);
		assertTrue(new Position(7, 4).isEquals(gunCard.getAttackerPosition()));
		assertTrue(new Position(7, 0).isEquals(gunCard.getAttackedPosition()));
		assertTrue(new Position(7, 0).isEquals(attackedPositionIA.getAttackedPosition()));
	}

	@Test
	public void mejorArma() {

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] TABLE_VALUES2 = { { "P_", "3$", "k_", "1$", "__", "2$", "P_", "k_", "P_" },
				{ "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "M_", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "k_", "__", "2$", "P_", "3$", "__", "1$", "P_", "k_" } };

		TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
		GameTable gameTable = new GameTable(tableSeats);
		IA_Manager manager = new IA_Manager(gameTable);

		InfoAction attackedPositionIA = manager.whoKill(converter.toCharacterArray(playerChairs), playerB, R, 2);
		assertEquals(
				"PUEDO ATACARLO//PUEDE ATACARME//VER FREE LETAL PASTEL//VER FREE PASTEL//VER DESPIERTOS//MAS ARMAS//MAS FLANCOS//MEJOR ARMA//GANO MEJOR ARMA",
				attackedPositionIA.getReason());
		assertEquals(1, attackedPositionIA.getCards().size());
		assertTrue(attackedPositionIA.getCards().get(0) instanceof GunCard);
		GunCard gunCard = (GunCard) attackedPositionIA.getCards().get(0);
		assertTrue(new Position(7, 4).isEquals(gunCard.getAttackerPosition()));
		assertTrue(new Position(7, 0).isEquals(gunCard.getAttackedPosition()));
		assertTrue(new Position(7, 0).isEquals(attackedPositionIA.getAttackedPosition()));
	}

	@Test
	public void mejorArmaDifereciaVaso3Jugadores() {

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] TABLE_VALUES2 = { { "P_", "3$", "k_", "1$", "__", "2$", "P_", "PG", "P_" },
				{ "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "M_", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "k_", "__", "2$", "P_", "3$", "__", "1$", "P_", "k_" } };

		TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
		GameTable gameTable = new GameTable(tableSeats);
		IA_Manager manager = new IA_Manager(gameTable);

		InfoAction attackedPositionIA = manager.whoKill(converter.toCharacterArray(playerChairs), playerB, R, 3);
		assertEquals("PUEDO ATACARLO//PUEDE ATACARME//VER FREE LETAL PASTEL//VER FREE PASTEL//VER DESPIERTOS//MAS ARMAS//GANO MAS ARMAS:1.5",
				attackedPositionIA.getReason());
		assertEquals(2, attackedPositionIA.getCards().size());
		assertTrue(attackedPositionIA.getCards().get(0) instanceof KnifeCard);
		KnifeCard knifeCard = (KnifeCard) attackedPositionIA.getCards().get(0);
		assertTrue(new Position(2, 0).isEquals(knifeCard.getAttackerPosition()));
		assertTrue(new Position(3, 0).isEquals(knifeCard.getAttackedPosition()));
		assertTrue(new Position(3, 0).isEquals(attackedPositionIA.getAttackedPosition()));
		assertTrue(attackedPositionIA.getCards().get(1) instanceof GunCard);
		GunCard gunCard = (GunCard) attackedPositionIA.getCards().get(1);
		assertTrue(new Position(3, 4).isEquals(gunCard.getAttackerPosition()));
		assertTrue(new Position(3, 0).isEquals(gunCard.getAttackedPosition()));
		assertTrue(new Position(3, 0).isEquals(attackedPositionIA.getAttackedPosition()));
	}

	@Test
	public void MejorBusinessMIO() {

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] TABLE_VALUES2 = { { "P_", "3$", "1$k", "1$", "__", "2$", "P_", "P_", "P_" },
				{ "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "M_", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "k_", "__", "2$", "P_", "3$", "__", "1$", "P_", "k_" } };

		TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
		GameTable gameTable = new GameTable(tableSeats);
		IA_Manager manager = new IA_Manager(gameTable);

		InfoAction attackedPositionIA = manager.whoKill(converter.toCharacterArray(playerChairs), playerB, R, 2);
		assertEquals(
				"PUEDO ATACARLO//PUEDE ATACARME//VER FREE LETAL PASTEL//VER FREE PASTEL//VER DESPIERTOS//MAS ARMAS//MAS FLANCOS//MEJOR ARMA//TENGO MAS ARMAS//MI MEJOR BUSINESS//GANO MI MEJOR BUSINESS",
				attackedPositionIA.getReason());
		assertEquals(2, attackedPositionIA.getCards().size());
		assertTrue(attackedPositionIA.getCards().get(0) instanceof KnifeCard);
		KnifeCard knifeCard = (KnifeCard) attackedPositionIA.getCards().get(0);
		assertTrue(new Position(2, 0).isEquals(knifeCard.getAttackerPosition()));
		assertTrue(new Position(3, 0).isEquals(knifeCard.getAttackedPosition()));
		assertTrue(new Position(3, 0).isEquals(attackedPositionIA.getAttackedPosition()));
		assertTrue(attackedPositionIA.getCards().get(1) instanceof GunCard);
		GunCard gunCard = (GunCard) attackedPositionIA.getCards().get(1);
		assertTrue(new Position(3, 4).isEquals(gunCard.getAttackerPosition()));
		assertTrue(new Position(3, 0).isEquals(gunCard.getAttackedPosition()));
		assertTrue(new Position(3, 0).isEquals(attackedPositionIA.getAttackedPosition()));
	}

	@Test
	public void MejorBusinessSUYO() {

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] TABLE_VALUES2 = { { "P_", "3$", "k_", "1$", "__", "2$", "P_", "P_", "P_" },
				{ "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "M_", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "k_", "__", "2$", "P_", "3$", "__", "1$", "P_", "k_" } };

		TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
		GameTable gameTable = new GameTable(tableSeats);
		IA_Manager manager = new IA_Manager(gameTable);

		InfoAction attackedPositionIA = manager.whoKill(converter.toCharacterArray(playerChairs), playerB, R, 2);
		assertEquals(
				"PUEDO ATACARLO//PUEDE ATACARME//VER FREE LETAL PASTEL//VER FREE PASTEL//VER DESPIERTOS//MAS ARMAS//MAS FLANCOS//MEJOR ARMA//TENGO MAS ARMAS//MI MEJOR BUSINESS//SU MEJOR BUSINESS//GANO SU MEJOR BUSINESS",
				attackedPositionIA.getReason());
		assertEquals(2, attackedPositionIA.getCards().size());
		assertTrue(attackedPositionIA.getCards().get(0) instanceof KnifeCard);
		KnifeCard knifeCard = (KnifeCard) attackedPositionIA.getCards().get(0);
		assertTrue(new Position(2, 0).isEquals(knifeCard.getAttackerPosition()));
		assertTrue(new Position(3, 0).isEquals(knifeCard.getAttackedPosition()));
		assertTrue(new Position(3, 0).isEquals(attackedPositionIA.getAttackedPosition()));
		assertTrue(attackedPositionIA.getCards().get(1) instanceof GunCard);
		GunCard gunCard = (GunCard) attackedPositionIA.getCards().get(1);
		assertTrue(new Position(3, 4).isEquals(gunCard.getAttackerPosition()));
		assertTrue(new Position(3, 0).isEquals(gunCard.getAttackedPosition()));
		assertTrue(new Position(3, 0).isEquals(attackedPositionIA.getAttackedPosition()));
	}

	@Test
	public void AlQueSea2JugadoresMeAtacaAPoderoso() {

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] TABLE_VALUES2 = { { "P_", "3$", "k_", "__", "__", "2$", "P_", "P_", "P_" },
				{ "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "M_", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "k_", "__", "2$", "P_", "3$", "__", "1$", "Pk", "k_" } };

		TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
		GameTable gameTable = new GameTable(tableSeats);
		IA_Manager manager = new IA_Manager(gameTable);
		InfoAction attackedPositionIA = manager.whoKill(converter.toCharacterArray(playerChairs), playerB, R, 2);
		assertEquals(
				"PUEDO ATACARLO//PUEDE ATACARME//VER FREE LETAL PASTEL//VER FREE PASTEL//VER DESPIERTOS//MAS ARMAS//MAS FLANCOS//MEJOR ARMA//TENGO MAS ARMAS//GANO TENGO MAS ARMAS:2.0",
				attackedPositionIA.getReason());
		assertEquals(1, attackedPositionIA.getCards().size());
		assertTrue(attackedPositionIA.getCards().get(0) instanceof GunCard);
	}

	@Test
	public void AlQueSea2JugadoresMeAtacaAPoderoso2() {

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] playerChairs2 = { { "V", "B", "R", "BP", "V", "V", "V", "V", "V" },
				{ "V", "*", "*", "*", "*", "*", "*", "*", "V" }, { "V", "*", "*", "*", "*", "*", "*", "*", "V" },
				{ "V", "*", "*", "*", "*", "*", "*", "*", "V" }, { "V", "V", "BP", "R", "B", "V", "V", "V", "V" } };

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] TABLE_VALUES2 = { { "P_", "3$", "k_", "k_", "__", "2$", "P_", "P_", "P_" },
				{ "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "M_", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "k_", "__", "2$", "k_", "3$", "__", "1$", "Pk", "k_" } };

		TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
		GameTable gameTable = new GameTable(tableSeats);
		IA_Manager manager = new IA_Manager(gameTable);
		InfoAction attackedPositionIA = manager.whoKill(converter.toCharacterArray(playerChairs2), playerB, R, 2);
		assertEquals(
				"PUEDO ATACARLO//PUEDE ATACARME//VER FREE LETAL PASTEL//VER FREE PASTEL//VER DESPIERTOS//MAS ARMAS//MAS FLANCOS//MEJOR ARMA//TENGO MAS ARMAS//GANO TENGO MAS ARMAS:2.0",
				attackedPositionIA.getReason());
		assertEquals(2, attackedPositionIA.getCards().size());
		assertTrue(attackedPositionIA.getCards().get(0) instanceof KnifeCard);
		KnifeCard knifeCard = (KnifeCard) attackedPositionIA.getCards().get(0);
		assertTrue(new Position(2, 0).isEquals(knifeCard.getAttackedPosition()));
		assertTrue(new Position(3, 0).isEquals(knifeCard.getAttackerPosition()));
	}

	@Test
	public void AlQueSea2Jugadores() {

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] TABLE_VALUES2 = { { "P_", "3$", "k_", "__", "__", "2$", "P_", "P_", "P_" },
				{ "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "M_", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "k_", "__", "2$", "P_", "3$", "__", "1$", "P_", "k_" } };

		TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
		GameTable gameTable = new GameTable(tableSeats);
		IA_Manager manager = new IA_Manager(gameTable);
		InfoAction attackedPositionIA = manager.whoKill(converter.toCharacterArray(playerChairs), playerB, R, 2);
		assertEquals(
				"PUEDO ATACARLO//PUEDE ATACARME//VER FREE LETAL PASTEL//VER FREE PASTEL//VER DESPIERTOS//MAS ARMAS//MAS FLANCOS//MEJOR ARMA//TENGO MAS ARMAS//MI MEJOR BUSINESS//SU MEJOR BUSINESS//EL QUE SEA, < 3 JUGADORES",
				attackedPositionIA.getReason());
		assertEquals(2, attackedPositionIA.getCards().size());
		assertTrue(attackedPositionIA.getCards().get(0) instanceof KnifeCard);
		assertTrue(attackedPositionIA.getCards().get(1) instanceof GunCard);
	}

	@Test
	public void AlQueSea3Jugadores() {

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] playerChairs = { { "A", "R", "B", "AP", "R", "R", "R", "R", "B" },
				{ "R", "*", "*", "*", "*", "*", "*", "*", "B" }, { "B", "*", "*", "*", "*", "*", "*", "*", "R" },
				{ "R", "*", "*", "*", "*", "*", "*", "*", "B" }, { "B", "R", "B", "B", "B", "B", "B", "B", "R" } };

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] TABLE_VALUES2 = { { "P_", "3$", "k_", "__", "__", "2$", "P_", "P_", "P_" },
				{ "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "M_", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "k_", "__", "2$", "P_", "3$", "__", "1$", "P_", "k_" } };

		TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
		GameTable gameTable = new GameTable(tableSeats);
		IA_Manager manager = new IA_Manager(gameTable);
		InfoAction attackedPositionIA = manager.whoKill(converter.toCharacterArray(playerChairs), playerB, R, 3);
		assertEquals(
				"PUEDO ATACARLO//PUEDE ATACARME//VER FREE LETAL PASTEL//VER FREE PASTEL//VER DESPIERTOS//MAS ARMAS//MAS FLANCOS//MEJOR ARMA//TENGO MAS ARMAS//MI MEJOR BUSINESS//SU MEJOR BUSINESS//GANO MAS PERSONAJES:10",
				attackedPositionIA.getReason());
		assertEquals(1, attackedPositionIA.getCards().size());
		assertTrue(attackedPositionIA.getCards().get(0) instanceof GunCard);
		GunCard gunCard = (GunCard) attackedPositionIA.getCards().get(0);
		assertTrue(new Position(7, 4).isEquals(gunCard.getAttackerPosition()));
		assertTrue(new Position(7, 0).isEquals(gunCard.getAttackedPosition()));
	}

	@Test
	public void nadieDuermo() {

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] playerChairs2 = { { "V", "B", "R", "BP", "V", "V", "V", "V", "V" },
				{ "V", "*", "*", "*", "*", "*", "*", "*", "V" }, { "V", "*", "*", "*", "*", "*", "*", "*", "V" },
				{ "V", "*", "*", "*", "*", "*", "*", "*", "V" }, { "V", "V", "V", "R", "A", "N", "N", "N", "V" } };

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] TABLE_VALUES2 = { { "P_", "3$", "__", "k_", "__", "2$", "P_", "P_", "P_" },
				{ "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "M_", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "k_", "__", "2$", "k_", "3$", "_G", "_G", "_G", "k_" } };

		TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
		GameTable gameTable = new GameTable(tableSeats);
		IA_Manager manager = new IA_Manager(gameTable);
		List<CardType> cards = new ArrayList<CardType>();
		cards.add(CardType.SLEEP);
		cards.add(CardType.SLEEP);
		cards.add(CardType.BOOM);
		cards.add(CardType.KNIFE);
		cards.add(CardType.GUN);
		Player playerWithTwoSleepCard = new Player(B, cards);
		InfoAction attackedPositionIA = manager.whoKill(converter.toCharacterArray(playerChairs2), playerWithTwoSleepCard, R, 4);
		assertEquals("PUEDO ATACARLO//NO ME ATACA//3 o MAS JUGADORES, DORMIR", attackedPositionIA.getReason());
		assertEquals(1, attackedPositionIA.getCards().size());
		assertTrue(attackedPositionIA.getCards().get(0) instanceof SleepCard);
	}

	@Test
	public void nadieMeAtacaMasArmas() {

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] playerChairs2 = { { "V", "B", "R", "BP", "V", "V", "V", "V", "V" },
				{ "V", "*", "*", "*", "*", "*", "*", "*", "V" }, { "V", "*", "*", "*", "*", "*", "*", "*", "V" },
				{ "V", "*", "*", "*", "*", "*", "*", "*", "V" }, { "V", "V", "V", "R", "V", "V", "V", "V", "V" } };

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] TABLE_VALUES2 = { { "P_", "3$", "__", "k_", "__", "2$", "P_", "P_", "P_" },
				{ "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "M_", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "k_", "__", "2$", "k_", "3$", "__", "1$", "Pk", "k_" } };

		TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
		GameTable gameTable = new GameTable(tableSeats);
		IA_Manager manager = new IA_Manager(gameTable);
		InfoAction attackedPositionIA = manager.whoKill(converter.toCharacterArray(playerChairs2), playerB, R, 2);
		assertEquals("PUEDO ATACARLO//NO ME ATACA//VER FREE LETAL PASTEL//VER FREE PASTEL//VER DESPIERTOS//MAS ARMAS//GANO MAS ARMAS:1.0", attackedPositionIA.getReason());
		assertEquals(1, attackedPositionIA.getCards().size());
		assertTrue(attackedPositionIA.getCards().get(0) instanceof GunCard);
		GunCard gunCard = (GunCard) attackedPositionIA.getCards().get(0);
		assertTrue(new Position(3, 4).isEquals(gunCard.getAttackedPosition()));
		assertTrue(new Position(3, 0).isEquals(gunCard.getAttackerPosition()));
	}

	@Test
	public void nadieMeAtacaMejorArma() {

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] playerChairs2 = { { "V", "B", "R", "BP", "V", "A", "V", "R", "V" },
				{ "V", "*", "*", "*", "*", "*", "*", "*", "V" }, { "V", "*", "*", "*", "*", "*", "*", "*", "V" },
				{ "V", "*", "*", "*", "*", "*", "*", "*", "V" }, { "V", "V", "V", "R", "V", "V", "V", "V", "V" } };

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] TABLE_VALUES2 = { { "P_", "3$", "_P", "k_", "__", "2$", "P_", "P_", "P_" },
				{ "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "M_", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "k_", "__", "2$", "k_", "3$", "__", "1$", "Pk", "k_" } };

		TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
		GameTable gameTable = new GameTable(tableSeats);
		IA_Manager manager = new IA_Manager(gameTable);
		InfoAction attackedPositionIA = manager.whoKill(converter.toCharacterArray(playerChairs2), playerB, R, 2);
		assertEquals("PUEDO ATACARLO//NO ME ATACA//VER FREE LETAL PASTEL//VER FREE PASTEL//VER DESPIERTOS//MAS ARMAS//MEJOR ARMA//GANO MEJOR ARMA",
				attackedPositionIA.getReason());
		assertEquals(1, attackedPositionIA.getCards().size());
		assertTrue(attackedPositionIA.getCards().get(0) instanceof GunCard);
		GunCard gunCard = (GunCard) attackedPositionIA.getCards().get(0);
		assertTrue(new Position(3, 4).isEquals(gunCard.getAttackedPosition()));
		assertTrue(new Position(3, 0).isEquals(gunCard.getAttackerPosition()));
	}

	@Test
	public void nadieMeAtacaMejorBusiness() {

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] playerChairs2 = { { "V", "B", "R", "BP", "V", "A", "V", "R", "V" },
				{ "V", "*", "*", "*", "*", "*", "*", "*", "V" }, { "V", "*", "*", "*", "*", "*", "*", "*", "V" },
				{ "V", "*", "*", "*", "*", "*", "*", "*", "V" }, { "V", "V", "V", "R", "V", "V", "V", "V", "V" } };

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] TABLE_VALUES2 = { { "P_", "3$", "3$", "k_", "__", "2$", "P_", "P_", "P_" },
				{ "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "M_", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "k_", "__", "2$", "__", "__", "__", "1$", "Pk", "k_" } };

		TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
		GameTable gameTable = new GameTable(tableSeats);
		IA_Manager manager = new IA_Manager(gameTable);
		InfoAction attackedPositionIA = manager.whoKill(converter.toCharacterArray(playerChairs2), playerB, R, 2);
		assertEquals("PUEDO ATACARLO//NO ME ATACA//VER FREE LETAL PASTEL//VER FREE PASTEL//VER DESPIERTOS//MAS ARMAS//MEJOR ARMA//SU MEJOR BUSINESS//GANO SU MEJOR BUSINESS",
				attackedPositionIA.getReason());
		assertEquals(1, attackedPositionIA.getCards().size());
		assertTrue(attackedPositionIA.getCards().get(0) instanceof KnifeCard);
		KnifeCard knifeCard = (KnifeCard) attackedPositionIA.getCards().get(0);
		assertTrue(new Position(2, 0).isEquals(knifeCard.getAttackedPosition()));
		assertTrue(new Position(3, 0).isEquals(knifeCard.getAttackerPosition()));
	}

	@Test
	public void nadieMeAtaca2JugadoresElQueSea() {

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] playerChairs2 = { { "V", "B", "R", "BP", "V", "R", "R", "R", "V" },
				{ "V", "*", "*", "*", "*", "*", "*", "*", "V" }, { "V", "*", "*", "*", "*", "*", "*", "*", "V" },
				{ "V", "*", "*", "*", "*", "*", "*", "*", "V" }, { "V", "V", "V", "R", "V", "V", "V", "V", "V" } };

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] TABLE_VALUES2 = { { "P_", "3$", "__", "k_", "__", "2$", "P_", "P_", "P_" },
				{ "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "M_", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "k_", "__", "2$", "__", "__", "__", "1$", "Pk", "k_" } };

		TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
		GameTable gameTable = new GameTable(tableSeats);
		IA_Manager manager = new IA_Manager(gameTable);
		InfoAction attackedPositionIA = manager.whoKill(converter.toCharacterArray(playerChairs2), playerB, R, 2);
		assertEquals("PUEDO ATACARLO//NO ME ATACA//VER FREE LETAL PASTEL//VER FREE PASTEL//VER DESPIERTOS//MAS ARMAS//MEJOR ARMA//SU MEJOR BUSINESS//EL QUE SEA, < 3 JUGADORES",
				attackedPositionIA.getReason());
		assertEquals(1, attackedPositionIA.getCards().size());
		assertTrue(attackedPositionIA.getCards().get(0) instanceof KnifeCard);
		KnifeCard knifeCard = (KnifeCard) attackedPositionIA.getCards().get(0);
		assertTrue(new Position(2, 0).isEquals(knifeCard.getAttackedPosition()));
		assertTrue(new Position(3, 0).isEquals(knifeCard.getAttackerPosition()));
	}

	@Test
	public void nadieMeAtaca3JugadoresMasJugadores() {

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] playerChairs2 = { { "V", "B", "R", "BP", "V", "A", "A", "R", "V" },
				{ "V", "*", "*", "*", "*", "*", "*", "*", "V" }, { "V", "*", "*", "*", "*", "*", "*", "*", "V" },
				{ "V", "*", "*", "*", "*", "*", "*", "*", "V" }, { "V", "V", "V", "A", "V", "V", "V", "V", "V" } };

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] TABLE_VALUES2 = { { "P_", "3$", "__", "k_", "__", "2$", "P_", "P_", "P_" },
				{ "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "M_", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "k_", "__", "2$", "__", "__", "__", "1$", "Pk", "k_" } };

		TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
		GameTable gameTable = new GameTable(tableSeats);
		IA_Manager manager = new IA_Manager(gameTable);
		InfoAction attackedPositionIA = manager.whoKill(converter.toCharacterArray(playerChairs2), playerB, R, 3);
		assertEquals("PUEDO ATACARLO//NO ME ATACA//VER FREE LETAL PASTEL//VER FREE PASTEL//VER DESPIERTOS//MAS ARMAS//MEJOR ARMA//SU MEJOR BUSINESS//GANO MAS PERSONAJES:3",
				attackedPositionIA.getReason());
		assertEquals(1, attackedPositionIA.getCards().size());
		assertTrue(attackedPositionIA.getCards().get(0) instanceof GunCard);
		GunCard gunCard = (GunCard) attackedPositionIA.getCards().get(0);
		assertTrue(new Position(3, 4).isEquals(gunCard.getAttackedPosition()));
		assertTrue(new Position(3, 0).isEquals(gunCard.getAttackerPosition()));
	}

	@Test
	public void otros() {
		// IA
		// MOVE CAKE
		// ZZZZ
		// CAKE
		// TIENE 2 CARTAS MOVE_SEAT
		// MOVE SEAT
		// HAS TWO MOVE SEAT
		fail();
	}

}
