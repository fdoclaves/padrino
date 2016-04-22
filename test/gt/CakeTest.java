package gt;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import gm.Cake;
import gm.GameTable;
import gm.PlayManager;
import gm.Player;
import gm.TableSeat;
import gm.cards.BoomCard;
import gm.cards.CakeCard;
import gm.cards.GunCard;
import gm.cards.MoveCakeCard;
import gm.exceptions.GameException;
import gm.exceptions.GameWarning;
import gm.info.CardType;
import gm.info.GameMessages;
import gm.pojos.Position;
import gt.extras.Converter;

public class CakeTest {

	private static Player J1;

	private static Player J2;

	private static Player J3;

	// |0 |01 |02 |03 |04 |05 |06 |07 |08|
	private String[][] TABLE_VALUES = { { "P_", "3$", "k-", "1$", "__", "2$", "P_", "__", "P_" },
			{ "M_", "**", "**", "**", "**", "**", "**", "**", "P_" },
			{ "k_", "__", "2$", "P_", "3$", "__", "1$", "__", "k_" } };

	// |0 |01 |02 |03 |04 |05 |06 |07 |08|
	String[][] playerChairs = { { "1_", "1P", "2_", "1k", "1k", "1P", "1_", "1P", "1_" },
			{ "1K", "**", "**", "**", "**", "**", "**", "**", "2_" },
			{ "2_", "1_", "2_", "3_", "2_", "2_", "V", "3_", "2_" } };

	private PlayManager donePlays;

	private TableSeat[][] tableSeats;

	private Converter converter;

	private GameTable gameTable;

	@Before
	public void setUp() throws Exception {
	    List<CardType> cards = new ArrayList<CardType>();
        cards.add(CardType.KNIFE);
        cards.add(CardType.GUN);
        cards.add(CardType.BOOM);
        cards.add(CardType.CAKE);
        cards.add(CardType.MOVE_CAKE);
        J1 = new Player("1", cards);
        J2 = new Player("2", cards);
        J3 = new Player("3", cards);
		converter = new Converter(9, 3);
		tableSeats = converter.to(TABLE_VALUES);
		gameTable = new GameTable(tableSeats);
		donePlays = new PlayManager(converter.toCharacterArray(playerChairs), gameTable);
	}

	@Test
	public void ponerCake() throws GameException, GameWarning {
		donePlays.startTurn(J1);
		donePlays.play(new CakeCard(new Cake(new Position(3, 2), J1.getTeam())));
		donePlays.finishTurn();

		assertEquals(1, gameTable.getCakeList().size());
		assertEquals(8, donePlays.getMoney());

		donePlays.startTurn(J2);
		donePlays.finishTurn();
		assertEquals(2, donePlays.getMoney());
	}

	@Test
	public void finRondaCake() throws GameException, GameWarning {
		donePlays.startTurn(J1);
		donePlays.play(new CakeCard(new Cake(new Position(3, 2), J1.getTeam())));
		donePlays.finishTurn();

		assertEquals(1, gameTable.getCakeList().size());
		assertEquals(8, donePlays.getMoney());

		donePlays.startTurn(J2);
		assertEquals(1, gameTable.getCakeList().size());
		donePlays.finishTurn();
		assertEquals(1, gameTable.getCakeList().size());

		donePlays.startTurn(J1);
		assertEquals(0, gameTable.getCakeList().size());

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert = { { "1_", "1P", "2_", "1k", "1k", "1P", "1_", "1P", "1_" },
				{ "1K", "**", "**", "**", "**", "**", "**", "**", "2_" },
				{ "2_", "1_", "VV", "VV", "VV", "2_", "VV", "3_", "2_" } };

		assertEquals(converter.toString(chairsExpert), converter.cToString(donePlays.getChairs()).replace("V", "VV"));
	}

	@Test
	public void finRondaCakeVacioAlado() throws GameException, GameWarning {
		donePlays.startTurn(J1);
		donePlays.play(new CakeCard(new Cake(new Position(5, 2), J1.getTeam())));
		donePlays.finishTurn();

		assertEquals(1, gameTable.getCakeList().size());
		assertEquals(8, donePlays.getMoney());

		donePlays.startTurn(J2);
		assertEquals(1, gameTable.getCakeList().size());
		donePlays.finishTurn();
		assertEquals(1, gameTable.getCakeList().size());

		donePlays.startTurn(J1);
		assertEquals(0, gameTable.getCakeList().size());

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert = { { "1_", "1P", "2_", "1k", "1k", "1P", "1_", "1P", "1_" },
				{ "1K", "**", "**", "**", "**", "**", "**", "**", "2_" },
				{ "2_", "1_", "2_", "3_", "VV", "VV", "VV", "3_", "2_" } };

		assertEquals(converter.toString(chairsExpert), converter.cToString(donePlays.getChairs()).replace("V", "VV"));
	}

	@Test
	public void ponerPastelEnLugarVacio() throws GameException, GameWarning {
		donePlays.startTurn(J1);
		donePlays.play(new CakeCard(new Cake(new Position(6, 2), J1.getTeam())));
		donePlays.finishTurn();

		assertEquals(1, gameTable.getCakeList().size());
		assertEquals(8, donePlays.getMoney());

		donePlays.startTurn(J2);
		assertEquals(1, gameTable.getCakeList().size());
		donePlays.finishTurn();
		assertEquals(1, gameTable.getCakeList().size());

		donePlays.startTurn(J1);
		assertEquals(0, gameTable.getCakeList().size());

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert = { { "1_", "1P", "2_", "1k", "1k", "1P", "1_", "1P", "1_" },
				{ "1K", "**", "**", "**", "**", "**", "**", "**", "2_" },
				{ "2_", "1_", "2_", "3_", "2_", "VV", "VV", "VV", "2_" } };

		assertEquals(converter.toString(chairsExpert), converter.cToString(donePlays.getChairs()).replace("V", "VV"));
	}

	@Test
	public void tresJugadores() throws GameException, GameWarning {
		donePlays.startTurn(J1);
		donePlays.play(new CakeCard(new Cake(new Position(6, 2), J1.getTeam())));
		assertEquals(1, gameTable.getCakeList().size());
		donePlays.finishTurn();

		assertEquals(1, gameTable.getCakeList().size());
		donePlays.startTurn(J2);
		assertEquals(1, gameTable.getCakeList().size());
		donePlays.finishTurn();
		assertEquals(1, gameTable.getCakeList().size());

		assertEquals(1, gameTable.getCakeList().size());
		donePlays.startTurn(J3);
		assertEquals(1, gameTable.getCakeList().size());
		donePlays.finishTurn();
		assertEquals(1, gameTable.getCakeList().size());

		donePlays.startTurn(J1);
		assertEquals(0, gameTable.getCakeList().size());
	}

	@Test
	public void pastelFinRondaEsquinaSuperiorIzquierda() throws GameException, GameWarning {
		donePlays.startTurn(J1);
		donePlays.play(new CakeCard(new Cake(new Position(0, 0), J1.getTeam())));
		donePlays.finishTurn();

		assertEquals(1, gameTable.getCakeList().size());
		assertEquals(8, donePlays.getMoney());

		donePlays.startTurn(J2);
		assertEquals(1, gameTable.getCakeList().size());
		donePlays.finishTurn();
		assertEquals(1, gameTable.getCakeList().size());

		donePlays.startTurn(J1);
		assertEquals(0, gameTable.getCakeList().size());

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert = { { "VV", "VV", "2_", "1k", "1k", "1P", "1_", "1P", "1_" },
				{ "VV", "**", "**", "**", "**", "**", "**", "**", "2_" },
				{ "2_", "1_", "2_", "3_", "2_", "2_", "VV", "3_", "2_" } };

		assertEquals(converter.toString(chairsExpert), converter.cToString(donePlays.getChairs()).replace("V", "VV"));
	}

	@Test
	public void pastelFinRondaEsquinaInferiorDerecha() throws GameException, GameWarning {
		donePlays.startTurn(J1);
		donePlays.play(new CakeCard(new Cake(new Position(8, 2), J1.getTeam())));
		donePlays.finishTurn();

		assertEquals(1, gameTable.getCakeList().size());
		assertEquals(8, donePlays.getMoney());

		donePlays.startTurn(J2);
		assertEquals(1, gameTable.getCakeList().size());
		donePlays.finishTurn();
		assertEquals(1, gameTable.getCakeList().size());

		donePlays.startTurn(J1);
		assertEquals(0, gameTable.getCakeList().size());

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert = { { "1_", "1P", "2_", "1k", "1k", "1P", "1_", "1P", "1_" },
				{ "1K", "**", "**", "**", "**", "**", "**", "**", "VV" },
				{ "2_", "1_", "2_", "3_", "2_", "2_", "VV", "VV", "VV" } };

		assertEquals(converter.toString(chairsExpert), converter.cToString(donePlays.getChairs()).replace("V", "VV"));
	}

	@Test
	public void pastelFinRondaEsquinaInferiorIzquierda() throws GameException, GameWarning {
		donePlays.startTurn(J1);
		donePlays.play(new CakeCard(new Cake(new Position(0, 2), J1.getTeam())));
		donePlays.finishTurn();

		assertEquals(1, gameTable.getCakeList().size());
		assertEquals(8, donePlays.getMoney());

		donePlays.startTurn(J2);
		assertEquals(1, gameTable.getCakeList().size());
		donePlays.finishTurn();
		assertEquals(1, gameTable.getCakeList().size());

		donePlays.startTurn(J1);
		assertEquals(0, gameTable.getCakeList().size());

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert = { { "1_", "1P", "2_", "1k", "1k", "1P", "1_", "1P", "1_" },
				{ "VV", "**", "**", "**", "**", "**", "**", "**", "2_" },
				{ "VV", "VV", "2_", "3_", "2_", "2_", "VV", "3_", "2_" } };

		assertEquals(converter.toString(chairsExpert), converter.cToString(donePlays.getChairs()).replace("V", "VV"));
	}

	@Test
	public void pastelFinRondaEsquinaSuperiorDerecha() throws GameException, GameWarning {
		donePlays.startTurn(J1);
		donePlays.play(new CakeCard(new Cake(new Position(8, 0), J1.getTeam())));
		donePlays.finishTurn();

		assertEquals(1, gameTable.getCakeList().size());
		assertEquals(8, donePlays.getMoney());

		donePlays.startTurn(J2);
		assertEquals(1, gameTable.getCakeList().size());
		donePlays.finishTurn();
		assertEquals(1, gameTable.getCakeList().size());

		donePlays.startTurn(J1);
		assertEquals(0, gameTable.getCakeList().size());

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert = { { "1_", "1P", "2_", "1k", "1k", "1P", "1_", "VV", "VV" },
				{ "1K", "**", "**", "**", "**", "**", "**", "**", "VV" },
				{ "2_", "1_", "2_", "3_", "2_", "2_", "VV", "3_", "2_" } };

		assertEquals(converter.toString(chairsExpert), converter.cToString(donePlays.getChairs()).replace("V", "VV"));
	}

	@Test
	public void pastelFinRondaMedioIzquieda() throws GameException, GameWarning {
		donePlays.startTurn(J1);
		donePlays.play(new CakeCard(new Cake(new Position(0, 1), J1.getTeam())));
		donePlays.finishTurn();

		assertEquals(1, gameTable.getCakeList().size());
		assertEquals(8, donePlays.getMoney());

		donePlays.startTurn(J2);
		assertEquals(1, gameTable.getCakeList().size());
		donePlays.finishTurn();
		assertEquals(1, gameTable.getCakeList().size());

		donePlays.startTurn(J1);
		assertEquals(0, gameTable.getCakeList().size());

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert = { { "VV", "1P", "2_", "1k", "1k", "1P", "1_", "1P", "1_" },
				{ "VV", "**", "**", "**", "**", "**", "**", "**", "2_" },
				{ "VV", "1_", "2_", "3_", "2_", "2_", "VV", "3_", "2_" } };

		assertEquals(converter.toString(chairsExpert), converter.cToString(donePlays.getChairs()).replace("V", "VV"));
	}

	@Test
	public void pastelFinRondaMedioDerecha() throws GameException, GameWarning {
		donePlays.startTurn(J1);
		donePlays.play(new CakeCard(new Cake(new Position(8, 1), J1.getTeam())));
		donePlays.finishTurn();

		assertEquals(1, gameTable.getCakeList().size());
		assertEquals(8, donePlays.getMoney());

		donePlays.startTurn(J2);
		assertEquals(1, gameTable.getCakeList().size());
		donePlays.finishTurn();
		assertEquals(1, gameTable.getCakeList().size());

		donePlays.startTurn(J1);
		assertEquals(0, gameTable.getCakeList().size());

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert = { { "1_", "1P", "2_", "1k", "1k", "1P", "1_", "1P", "VV" },
				{ "1K", "**", "**", "**", "**", "**", "**", "**", "VV" },
				{ "2_", "1_", "2_", "3_", "2_", "2_", "VV", "3_", "VV" } };

		assertEquals(converter.toString(chairsExpert), converter.cToString(donePlays.getChairs()).replace("V", "VV"));
	}

	@Test
	public void boomCardTest() throws GameException, GameWarning {
		donePlays.startTurn(J1);
		Cake cake = new Cake(new Position(6, 2), J1.getTeam());
		donePlays.play(new CakeCard(cake));
		assertEquals(1, gameTable.getCakeList().size());
		donePlays.finishTurn();

		assertEquals(1, gameTable.getCakeList().size());
		donePlays.startTurn(J2);
		assertEquals(1, gameTable.getCakeList().size());
		donePlays.play(new BoomCard(cake));
		donePlays.finishTurn();
		assertEquals(0, gameTable.getCakeList().size());
	}

	@Test
	public void boom2PastelesMismaPosicion() throws GameException, GameWarning {
		donePlays.startTurn(J1);
		Cake cake = new Cake(new Position(6, 2), J1.getTeam());
		donePlays.play(new CakeCard(cake));
		assertEquals(1, gameTable.getCakeList().size());
		assertEquals(6, gameTable.getCakeList().get(0).getPosition().getX());
		assertEquals(2, gameTable.getCakeList().get(0).getPosition().getY());
		donePlays.finishTurn();

		assertEquals(1, gameTable.getCakeList().size());
		donePlays.startTurn(J2);
		assertEquals(1, gameTable.getCakeList().size());
		Cake cake2 = new Cake(new Position(6, 2), J2.getTeam());
		donePlays.play(new CakeCard(cake2));
		assertEquals(2, gameTable.getCakeList().size());
		donePlays.finishTurn();

		assertEquals(2, gameTable.getCakeList().size());
		donePlays.startTurn(J3);
		assertEquals(2, gameTable.getCakeList().size());
		donePlays.play(new BoomCard(cake2));
		donePlays.finishTurn();
		assertEquals(1, gameTable.getCakeList().size());

		donePlays.startTurn(J1);
		assertEquals(0, gameTable.getCakeList().size());
	}

	@Test
	public void boom2PastelesDiferentePosicion() throws GameException, GameWarning {
		donePlays.startTurn(J1);
		Cake cake = new Cake(new Position(6, 2), J1.getTeam());
		donePlays.play(new CakeCard(cake));
		assertEquals(1, gameTable.getCakeList().size());
		assertEquals(6, gameTable.getCakeList().get(0).getPosition().getX());
		assertEquals(2, gameTable.getCakeList().get(0).getPosition().getY());
		donePlays.finishTurn();

		assertEquals(1, gameTable.getCakeList().size());
		donePlays.startTurn(J2);
		assertEquals(1, gameTable.getCakeList().size());
		Cake cake2 = new Cake(new Position(1, 0), J2.getTeam());
		donePlays.play(new CakeCard(cake2));
		assertEquals(2, gameTable.getCakeList().size());
		assertEquals(1, gameTable.getCakeList().get(1).getPosition().getX());
		assertEquals(0, gameTable.getCakeList().get(1).getPosition().getY());
		donePlays.finishTurn();

		assertEquals(2, gameTable.getCakeList().size());
		donePlays.startTurn(J3);
		assertEquals(2, gameTable.getCakeList().size());
		donePlays.play(new BoomCard(cake2));
		donePlays.finishTurn();
		assertEquals(1, gameTable.getCakeList().size());
		assertEquals(6, gameTable.getCakeList().get(0).getPosition().getX());
		assertEquals(2, gameTable.getCakeList().get(0).getPosition().getY());

		donePlays.startTurn(J1);
		assertEquals(0, gameTable.getCakeList().size());
	}

	@Test
	public void yaNoExiten3Jugadores() throws GameException, GameWarning {

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] playerChairs = { { "1_", "1P", "2_", "1k", "1k", "1P", "1_", "1P", "1_" },
				{ "1K", "**", "**", "**", "**", "**", "**", "**", "2_" },
				{ "2_", "1_", "2_", "3_", "2_", "2_", "V", "VV", "2_" } };

		donePlays = new PlayManager(converter.toCharacterArray(playerChairs), gameTable);

		assertEquals(0, gameTable.getCakeList().size());
		donePlays.startTurn(J1);
		donePlays.play(new CakeCard(new Cake(new Position(6, 2), J1.getTeam())));
		assertEquals(1, gameTable.getCakeList().size());
		donePlays.finishTurn();
		assertEquals(1, gameTable.getCakeList().size());

		donePlays.startTurn(J2);
		assertEquals(1, gameTable.getCakeList().size());
		donePlays.finishTurn();
		assertEquals(1, gameTable.getCakeList().size());

		donePlays.startTurn(J1);
		assertEquals(0, gameTable.getCakeList().size());
		donePlays.finishTurn();
		assertEquals(0, gameTable.getCakeList().size());

		donePlays.startTurn(J2);
		assertEquals(0, gameTable.getCakeList().size());
	}

	@Test
	public void moverPastel() throws GameException, GameWarning {
		donePlays.startTurn(J1);
		Cake cake = new Cake(new Position(3, 2), J1.getTeam());
		donePlays.play(new CakeCard(cake));
		donePlays.finishTurn();

		assertEquals(1, gameTable.getCakeList().size());
		assertEquals(3, gameTable.getCakeList().get(0).getPosition().getX());
		assertEquals(2, gameTable.getCakeList().get(0).getPosition().getY());

		donePlays.startTurn(J2);
		assertEquals(1, gameTable.getCakeList().size());
		donePlays.play(new MoveCakeCard(cake, new Position(2, 2)));
		donePlays.finishTurn();
		assertEquals(1, gameTable.getCakeList().size());
		assertEquals(2, gameTable.getCakeList().get(0).getPosition().getX());
		assertEquals(2, gameTable.getCakeList().get(0).getPosition().getY());

		donePlays.startTurn(J1);
		assertEquals(0, gameTable.getCakeList().size());

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert = { { "1_", "1P", "2_", "1k", "1k", "1P", "1_", "1P", "1_" },
				{ "1K", "**", "**", "**", "**", "**", "**", "**", "2_" },
				{ "2_", "VV", "VV", "VV", "2_", "2_", "VV", "3_", "2_" } };

		assertEquals(converter.toString(chairsExpert), converter.cToString(donePlays.getChairs()).replace("V", "VV"));
	}

	@Test
	public void moverPastelAVacio() throws GameException, GameWarning {
		donePlays.startTurn(J1);
		Cake cake = new Cake(new Position(3, 2), J1.getTeam());
		donePlays.play(new CakeCard(cake));
		donePlays.finishTurn();

		assertEquals(1, gameTable.getCakeList().size());
		assertEquals(3, gameTable.getCakeList().get(0).getPosition().getX());
		assertEquals(2, gameTable.getCakeList().get(0).getPosition().getY());

		donePlays.startTurn(J2);
		assertEquals(1, gameTable.getCakeList().size());
		try {
			donePlays.play(new MoveCakeCard(cake, new Position(6, 2)));
			fail();
		} catch (GameException e) {
			assertEquals(GameMessages.SEAT_EMPTY, e.getMessage());
		}
	}

	@Test
	public void moverPastelDosEspaciosMas() throws GameException, GameWarning {
		donePlays.startTurn(J1);
		Cake cake = new Cake(new Position(3, 2), J1.getTeam());
		donePlays.play(new CakeCard(cake));
		donePlays.finishTurn();

		assertEquals(1, gameTable.getCakeList().size());
		assertEquals(3, gameTable.getCakeList().get(0).getPosition().getX());
		assertEquals(2, gameTable.getCakeList().get(0).getPosition().getY());

		donePlays.startTurn(J2);
		assertEquals(1, gameTable.getCakeList().size());
		try {
			donePlays.play(new MoveCakeCard(cake, new Position(1, 2)));
			fail();
		} catch (GameException e) {
			assertEquals(GameMessages.INVALID_CAKE_MOVE, e.getMessage());
		}
	}

	@Test
	public void moverPastelDespuesVacioIzquierda() throws GameException, GameWarning {
		donePlays.startTurn(J1);
		Cake cake = new Cake(new Position(5, 2), J1.getTeam());
		donePlays.play(new CakeCard(cake));
		donePlays.finishTurn();

		assertEquals(1, gameTable.getCakeList().size());
		assertEquals(5, gameTable.getCakeList().get(0).getPosition().getX());
		assertEquals(2, gameTable.getCakeList().get(0).getPosition().getY());

		donePlays.startTurn(J2);
		donePlays.play(new MoveCakeCard(cake, new Position(7, 2)));
		assertEquals(1, gameTable.getCakeList().size());
		assertEquals(7, gameTable.getCakeList().get(0).getPosition().getX());
		assertEquals(2, gameTable.getCakeList().get(0).getPosition().getY());

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert = { { "1_", "1P", "2_", "1k", "1k", "1P", "1_", "1P", "1_" },
				{ "1K", "**", "**", "**", "**", "**", "**", "**", "2_" },
				{ "2_", "1_", "2_", "3_", "2_", "2_", "VV", "3_", "2_" } };

		assertEquals(converter.toString(chairsExpert), converter.cToString(donePlays.getChairs()).replace("V", "VV"));
	}

	@Test
	public void moverPastelDespuesVacioDerecha() throws GameException, GameWarning {
		donePlays.startTurn(J1);
		Cake cake = new Cake(new Position(7, 2), J1.getTeam());
		donePlays.play(new CakeCard(cake));
		donePlays.finishTurn();

		assertEquals(1, gameTable.getCakeList().size());
		assertEquals(7, gameTable.getCakeList().get(0).getPosition().getX());
		assertEquals(2, gameTable.getCakeList().get(0).getPosition().getY());

		donePlays.startTurn(J2);
		donePlays.play(new MoveCakeCard(cake, new Position(5, 2)));
		assertEquals(1, gameTable.getCakeList().size());
		assertEquals(5, gameTable.getCakeList().get(0).getPosition().getX());
		assertEquals(2, gameTable.getCakeList().get(0).getPosition().getY());

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert = { { "1_", "1P", "2_", "1k", "1k", "1P", "1_", "1P", "1_" },
				{ "1K", "**", "**", "**", "**", "**", "**", "**", "2_" },
				{ "2_", "1_", "2_", "3_", "2_", "2_", "VV", "3_", "2_" } };

		assertEquals(converter.toString(chairsExpert), converter.cToString(donePlays.getChairs()).replace("V", "VV"));
	}

	@Test
	public void moverPastelDespuesVacioMal() throws GameException, GameWarning {
		donePlays.startTurn(J1);
		Cake cake = new Cake(new Position(7, 2), J1.getTeam());
		donePlays.play(new CakeCard(cake));
		donePlays.finishTurn();

		assertEquals(1, gameTable.getCakeList().size());
		assertEquals(7, gameTable.getCakeList().get(0).getPosition().getX());
		assertEquals(2, gameTable.getCakeList().get(0).getPosition().getY());

		donePlays.startTurn(J2);
		try {
			donePlays.play(new MoveCakeCard(cake, new Position(4, 2)));
			fail();
		} catch (GameException e) {
			assertEquals(GameMessages.INVALID_CAKE_MOVE, e.getMessage());
		}
	}

	@Test
	public void moverPastelAOrillasIzquieda() throws GameException, GameWarning {
		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] playerChairs = { { "1_", "1P", "2_", "1k", "1k", "1P", "1_", "1P", "1_" },
				{ "1K", "**", "**", "**", "**", "**", "**", "**", "2_" },
				{ "V", "V", "V", "V", "V", "2_", "V", "V", "V" } };

		donePlays = new PlayManager(converter.toCharacterArray(playerChairs), gameTable);
		donePlays.startTurn(J1);
		Cake cake = new Cake(new Position(5, 2), J1.getTeam());
		donePlays.play(new CakeCard(cake));
		donePlays.finishTurn();

		assertEquals(1, gameTable.getCakeList().size());
		assertEquals(5, gameTable.getCakeList().get(0).getPosition().getX());
		assertEquals(2, gameTable.getCakeList().get(0).getPosition().getY());

		donePlays.startTurn(J2);
		donePlays.play(new MoveCakeCard(cake, new Position(0, 1)));
		assertEquals(1, gameTable.getCakeList().size());
		assertEquals(0, gameTable.getCakeList().get(0).getPosition().getX());
		assertEquals(1, gameTable.getCakeList().get(0).getPosition().getY());

		donePlays.startTurn(J1);
		assertEquals(0, gameTable.getCakeList().size());
		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert = { { "VV", "1P", "2_", "1k", "1k", "1P", "1_", "1P", "1_" },
				{ "VV", "**", "**", "**", "**", "**", "**", "**", "2_" },
				{ "VV", "VV", "VV", "VV", "VV", "2_", "VV", "VV", "VV" } };

		assertEquals(converter.toString(chairsExpert), converter.cToString(donePlays.getChairs()).replace("V", "VV"));
	}

	@Test
	public void moverPastelAOrillasDerecha() throws GameException, GameWarning {
		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] playerChairs = { { "1_", "1P", "2_", "1k", "1k", "1P", "1_", "1P", "1_" },
				{ "1K", "**", "**", "**", "**", "**", "**", "**", "VV" },
				{ "V", "V", "V", "V", "V", "2_", "V", "V", "V" } };

		donePlays = new PlayManager(converter.toCharacterArray(playerChairs), gameTable);
		donePlays.startTurn(J1);
		Cake cake = new Cake(new Position(5, 2), J1.getTeam());
		donePlays.play(new CakeCard(cake));
		donePlays.finishTurn();

		assertEquals(1, gameTable.getCakeList().size());
		assertEquals(5, gameTable.getCakeList().get(0).getPosition().getX());
		assertEquals(2, gameTable.getCakeList().get(0).getPosition().getY());

		donePlays.startTurn(J2);
		donePlays.play(new MoveCakeCard(cake, new Position(8, 0)));
		assertEquals(1, gameTable.getCakeList().size());
		assertEquals(8, gameTable.getCakeList().get(0).getPosition().getX());
		assertEquals(0, gameTable.getCakeList().get(0).getPosition().getY());

		donePlays.startTurn(J1);
		assertEquals(0, gameTable.getCakeList().size());
		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert = { { "1_", "1P", "2_", "1k", "1k", "1P", "1_", "VV", "VV" },
				{ "1K", "**", "**", "**", "**", "**", "**", "**", "VV" },
				{ "VV", "VV", "VV", "VV", "VV", "2_", "VV", "VV", "VV" } };

		assertEquals(converter.toString(chairsExpert), converter.cToString(donePlays.getChairs()).replace("V", "VV"));
	}

	@Test
	public void moverPastelDeAbajoAArriba() throws GameException, GameWarning {
		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] playerChairs = { { "1_", "1P", "2_", "1k", "1k", "1P", "1_", "V", "V" },
				{ "V", "**", "**", "**", "**", "**", "**", "**", "V" },
				{ "V", "V", "V", "V", "V", "2_", "V", "V", "V" } };

		donePlays = new PlayManager(converter.toCharacterArray(playerChairs), gameTable);
		donePlays.startTurn(J1);
		Cake cake = new Cake(new Position(5, 2), J1.getTeam());
		donePlays.play(new CakeCard(cake));
		donePlays.finishTurn();

		assertEquals(1, gameTable.getCakeList().size());
		assertEquals(5, gameTable.getCakeList().get(0).getPosition().getX());
		assertEquals(2, gameTable.getCakeList().get(0).getPosition().getY());

		donePlays.startTurn(J2);
		donePlays.play(new MoveCakeCard(cake, new Position(6, 0)));
		assertEquals(1, gameTable.getCakeList().size());
		assertEquals(6, gameTable.getCakeList().get(0).getPosition().getX());
		assertEquals(0, gameTable.getCakeList().get(0).getPosition().getY());

		donePlays.startTurn(J1);
		assertEquals(0, gameTable.getCakeList().size());
		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert = { { "1_", "1P", "2_", "1k", "1k", "VV", "VV", "VV", "VV" },
				{ "VV", "**", "**", "**", "**", "**", "**", "**", "VV" },
				{ "VV", "VV", "VV", "VV", "VV", "2_", "VV", "VV", "VV" } };

		assertEquals(converter.toString(chairsExpert), converter.cToString(donePlays.getChairs()).replace("V", "VV"));
	}

	@Test
	public void moverPastelDeArribaAAbajo() throws GameException, GameWarning {
		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] playerChairs = { { "1_", "1P", "2_", "1k", "1k", "1P", "1_", "V", "V" },
				{ "V", "**", "**", "**", "**", "**", "**", "**", "V" },
				{ "V", "V", "V", "V", "V", "2_", "V", "V", "V" } };

		donePlays = new PlayManager(converter.toCharacterArray(playerChairs), gameTable);
		donePlays.startTurn(J1);
		Cake cake = new Cake(new Position(6, 0), J1.getTeam());
		donePlays.play(new CakeCard(cake));
		donePlays.finishTurn();

		assertEquals(1, gameTable.getCakeList().size());
		assertEquals(6, gameTable.getCakeList().get(0).getPosition().getX());
		assertEquals(0, gameTable.getCakeList().get(0).getPosition().getY());

		donePlays.startTurn(J2);
		donePlays.play(new MoveCakeCard(cake, new Position(5, 2)));
		assertEquals(1, gameTable.getCakeList().size());
		assertEquals(5, gameTable.getCakeList().get(0).getPosition().getX());
		assertEquals(2, gameTable.getCakeList().get(0).getPosition().getY());

		donePlays.startTurn(J1);
		assertEquals(0, gameTable.getCakeList().size());
		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert = { { "1_", "1P", "2_", "1k", "1k", "1P", "1_", "VV", "VV" },
				{ "VV", "**", "**", "**", "**", "**", "**", "**", "VV" },
				{ "VV", "VV", "VV", "VV", "VV", "VV", "VV", "VV", "VV" } };

		assertEquals(converter.toString(chairsExpert), converter.cToString(donePlays.getChairs()).replace("V", "VV"));
	}

	@Test
	public void moverPastelDesdeEsquinaDireccionAbajo() throws GameException, GameWarning {
		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] playerChairs = { { "1_", "1P", "2_", "1k", "1k", "1P", "1_", "V", "V" },
				{ "V", "**", "**", "**", "**", "**", "**", "**", "V" },
				{ "V", "V", "V", "V", "V", "2_", "V", "V", "V" } };

		donePlays = new PlayManager(converter.toCharacterArray(playerChairs), gameTable);
		donePlays.startTurn(J1);
		Cake cake = new Cake(new Position(0, 0), J1.getTeam());
		donePlays.play(new CakeCard(cake));
		donePlays.finishTurn();

		assertEquals(1, gameTable.getCakeList().size());
		assertEquals(0, gameTable.getCakeList().get(0).getPosition().getX());
		assertEquals(0, gameTable.getCakeList().get(0).getPosition().getY());

		donePlays.startTurn(J2);
		donePlays.play(new MoveCakeCard(cake, new Position(1, 0)));
		assertEquals(1, gameTable.getCakeList().size());
		assertEquals(1, gameTable.getCakeList().get(0).getPosition().getX());
		assertEquals(0, gameTable.getCakeList().get(0).getPosition().getY());

		donePlays.startTurn(J1);
		assertEquals(0, gameTable.getCakeList().size());
		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert = { { "VV", "VV", "VV", "1k", "1k", "1P", "1_", "VV", "VV" },
				{ "VV", "**", "**", "**", "**", "**", "**", "**", "VV" },
				{ "VV", "VV", "VV", "VV", "VV", "2_", "VV", "VV", "VV" } };

		assertEquals(converter.toString(chairsExpert), converter.cToString(donePlays.getChairs()).replace("V", "VV"));
	}

	@Test
	public void moverPastelDesdeEsquinaDireccionDelado() throws GameException, GameWarning {
		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] playerChairs = { { "1_", "1P", "2_", "1k", "1k", "1P", "1_", "V", "V" },
				{ "V", "**", "**", "**", "**", "**", "**", "**", "V" },
				{ "V", "V", "V", "V", "V", "2_", "V", "V", "V" } };

		donePlays = new PlayManager(converter.toCharacterArray(playerChairs), gameTable);
		donePlays.startTurn(J1);
		Cake cake = new Cake(new Position(0, 0), J1.getTeam());
		donePlays.play(new CakeCard(cake));
		donePlays.finishTurn();

		assertEquals(1, gameTable.getCakeList().size());
		assertEquals(0, gameTable.getCakeList().get(0).getPosition().getX());
		assertEquals(0, gameTable.getCakeList().get(0).getPosition().getY());

		donePlays.startTurn(J2);
		donePlays.play(new MoveCakeCard(cake, new Position(5, 2)));
		assertEquals(1, gameTable.getCakeList().size());
		assertEquals(5, gameTable.getCakeList().get(0).getPosition().getX());
		assertEquals(2, gameTable.getCakeList().get(0).getPosition().getY());

		donePlays.startTurn(J1);
		assertEquals(0, gameTable.getCakeList().size());
		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert = { { "1_", "1P", "2_", "1k", "1k", "1P", "1_", "VV", "VV" },
				{ "VV", "**", "**", "**", "**", "**", "**", "**", "VV" },
				{ "VV", "VV", "VV", "VV", "VV", "VV", "VV", "VV", "VV" } };

		assertEquals(converter.toString(chairsExpert), converter.cToString(donePlays.getChairs()).replace("V", "VV"));
	}

	@Test
	public void moverPastelDesdeEsquinaDerechaDireccionDelado() throws GameException, GameWarning {
		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] playerChairs = { { "1_", "1P", "2_", "1k", "1k", "1P", "1_", "V", "V" },
				{ "V", "**", "**", "**", "**", "**", "**", "**", "V" },
				{ "V", "V", "V", "V", "V", "2_", "V", "V", "V" } };

		donePlays = new PlayManager(converter.toCharacterArray(playerChairs), gameTable);
		donePlays.startTurn(J1);
		Cake cake = new Cake(new Position(8, 0), J1.getTeam());
		donePlays.play(new CakeCard(cake));
		donePlays.finishTurn();

		assertEquals(1, gameTable.getCakeList().size());
		assertEquals(8, gameTable.getCakeList().get(0).getPosition().getX());
		assertEquals(0, gameTable.getCakeList().get(0).getPosition().getY());

		donePlays.startTurn(J2);
		donePlays.play(new MoveCakeCard(cake, new Position(5, 2)));
		assertEquals(1, gameTable.getCakeList().size());
		assertEquals(5, gameTable.getCakeList().get(0).getPosition().getX());
		assertEquals(2, gameTable.getCakeList().get(0).getPosition().getY());

		donePlays.startTurn(J1);
		assertEquals(0, gameTable.getCakeList().size());
		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert = { { "1_", "1P", "2_", "1k", "1k", "1P", "1_", "VV", "VV" },
				{ "VV", "**", "**", "**", "**", "**", "**", "**", "VV" },
				{ "VV", "VV", "VV", "VV", "VV", "VV", "VV", "VV", "VV" } };

		assertEquals(converter.toString(chairsExpert), converter.cToString(donePlays.getChairs()).replace("V", "VV"));
	}

	@Test
	public void moverPastelDesdeEsquinaDerechaAbajoDireccionDelado() throws GameException, GameWarning {
		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] playerChairs = { { "1_", "1P", "2_", "1k", "1k", "1P", "1_", "V", "V" },
				{ "V", "**", "**", "**", "**", "**", "**", "**", "V" },
				{ "V", "V", "V", "V", "V", "2_", "V", "V", "V" } };

		donePlays = new PlayManager(converter.toCharacterArray(playerChairs), gameTable);
		donePlays.startTurn(J1);
		Cake cake = new Cake(new Position(8, 2), J1.getTeam());
		donePlays.play(new CakeCard(cake));
		donePlays.finishTurn();

		assertEquals(1, gameTable.getCakeList().size());
		assertEquals(8, gameTable.getCakeList().get(0).getPosition().getX());
		assertEquals(2, gameTable.getCakeList().get(0).getPosition().getY());

		donePlays.startTurn(J2);
		donePlays.play(new MoveCakeCard(cake, new Position(5, 2)));
		assertEquals(1, gameTable.getCakeList().size());
		assertEquals(5, gameTable.getCakeList().get(0).getPosition().getX());
		assertEquals(2, gameTable.getCakeList().get(0).getPosition().getY());

		donePlays.startTurn(J1);
		assertEquals(0, gameTable.getCakeList().size());
		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert = { { "1_", "1P", "2_", "1k", "1k", "1P", "1_", "VV", "VV" },
				{ "VV", "**", "**", "**", "**", "**", "**", "**", "VV" },
				{ "VV", "VV", "VV", "VV", "VV", "VV", "VV", "VV", "VV" } };

		assertEquals(converter.toString(chairsExpert), converter.cToString(donePlays.getChairs()).replace("V", "VV"));
	}

	@Test
	public void moverPastelDesdeEsquinaIzquierdaAbajoDireccionDelado() throws GameException, GameWarning {
		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] playerChairs = { { "1_", "1P", "2_", "1k", "1k", "1P", "1_", "V", "V" },
				{ "V", "**", "**", "**", "**", "**", "**", "**", "V" },
				{ "V", "V", "V", "V", "V", "2_", "V", "V", "V" } };

		donePlays = new PlayManager(converter.toCharacterArray(playerChairs), gameTable);
		donePlays.startTurn(J1);
		Cake cake = new Cake(new Position(0, 2), J1.getTeam());
		donePlays.play(new CakeCard(cake));
		donePlays.finishTurn();

		assertEquals(1, gameTable.getCakeList().size());
		assertEquals(0, gameTable.getCakeList().get(0).getPosition().getX());
		assertEquals(2, gameTable.getCakeList().get(0).getPosition().getY());

		donePlays.startTurn(J2);
		donePlays.play(new MoveCakeCard(cake, new Position(5, 2)));
		assertEquals(1, gameTable.getCakeList().size());
		assertEquals(5, gameTable.getCakeList().get(0).getPosition().getX());
		assertEquals(2, gameTable.getCakeList().get(0).getPosition().getY());

		donePlays.startTurn(J1);
		assertEquals(0, gameTable.getCakeList().size());
		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert = { { "1_", "1P", "2_", "1k", "1k", "1P", "1_", "VV", "VV" },
				{ "VV", "**", "**", "**", "**", "**", "**", "**", "VV" },
				{ "VV", "VV", "VV", "VV", "VV", "VV", "VV", "VV", "VV" } };

		assertEquals(converter.toString(chairsExpert), converter.cToString(donePlays.getChairs()).replace("V", "VV"));
	}

	@Test
	public void moverPastelDesdeMachine() throws GameException, GameWarning {
		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] playerChairs = { { "V", "1P", "2_", "1k", "1k", "1P", "1_", "V", "V" },
				{ "4_", "**", "**", "**", "**", "**", "**", "**", "V" },
				{ "V", "V", "V", "V", "V", "V", "V", "V", "V" } };

		donePlays = new PlayManager(converter.toCharacterArray(playerChairs), gameTable);
		donePlays.startTurn(J1);
		Cake cake = new Cake(new Position(0, 1), J1.getTeam());
		donePlays.play(new CakeCard(cake));
		donePlays.finishTurn();

		assertEquals(1, gameTable.getCakeList().size());
		assertEquals(0, gameTable.getCakeList().get(0).getPosition().getX());
		assertEquals(1, gameTable.getCakeList().get(0).getPosition().getY());

		donePlays.startTurn(J2);
		donePlays.play(new MoveCakeCard(cake, new Position(6, 0)));
		assertEquals(1, gameTable.getCakeList().size());
		assertEquals(6, gameTable.getCakeList().get(0).getPosition().getX());
		assertEquals(0, gameTable.getCakeList().get(0).getPosition().getY());

		donePlays.startTurn(J1);
		assertEquals(0, gameTable.getCakeList().size());
		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert = { { "VV", "1P", "2_", "1k", "1k", "VV", "VV", "VV", "VV" },
				{ "4_", "**", "**", "**", "**", "**", "**", "**", "VV" },
				{ "VV", "VV", "VV", "VV", "VV", "VV", "VV", "VV", "VV" } };

		assertEquals(converter.toString(chairsExpert), converter.cToString(donePlays.getChairs()).replace("V", "VV"));
	}

	@Test
	public void moverPastelDesdeNoMachine() throws GameException, GameWarning {
		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] TABLE_VALUES2 = { { "P_", "3$", "k-", "1$", "__", "2$", "P_", "__", "P_" },
				{ "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "M_", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "k_", "__", "2$", "P_", "3$", "__", "1$", "__", "k_" } };
		converter = new Converter(9, 5);
		tableSeats = converter.to(TABLE_VALUES2);
		gameTable = new GameTable(tableSeats);
		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] playerChairs2 = { { "V", "V", "V", "V", "V", "V", "V", "V", "V" },
				{ "V", "**", "**", "**", "**", "**", "**", "**", "V" },
				{ "V", "**", "**", "**", "**", "**", "**", "**", "V" },
				{ "V", "**", "**", "**", "**", "**", "**", "**", "V" },
				{ "6_", "7_", "V", "V", "V", "V", "V", "V", "V" } };

		donePlays = new PlayManager(converter.toCharacterArray(playerChairs2), gameTable);
		donePlays.startTurn(J1);
		Cake cake = new Cake(new Position(8, 1), J1.getTeam());
		donePlays.play(new CakeCard(cake));
		donePlays.finishTurn();

		assertEquals(1, gameTable.getCakeList().size());
		assertEquals(8, gameTable.getCakeList().get(0).getPosition().getX());
		assertEquals(1, gameTable.getCakeList().get(0).getPosition().getY());

		donePlays.startTurn(J2);
		donePlays.play(new MoveCakeCard(cake, new Position(0, 4)));
		assertEquals(1, gameTable.getCakeList().size());
		assertEquals(0, gameTable.getCakeList().get(0).getPosition().getX());
		assertEquals(4, gameTable.getCakeList().get(0).getPosition().getY());

		donePlays.startTurn(J1);
		assertEquals(0, gameTable.getCakeList().size());
	}

	@Test
	public void mover1Pastel2EnMesa() throws GameException, GameWarning {
		donePlays.startTurn(J1);
		donePlays.play(new CakeCard(new Cake(new Position(6, 2), J1.getTeam())));
		assertEquals(1, gameTable.getCakeList().size());
		donePlays.finishTurn();

		assertEquals(1, gameTable.getCakeList().size());
		donePlays.startTurn(J2);
		assertEquals(1, gameTable.getCakeList().size());
		Cake cake2 = new Cake(new Position(0, 0), J2.getTeam());
		donePlays.play(new CakeCard(cake2));
		donePlays.finishTurn();
		assertEquals(2, gameTable.getCakeList().size());

		assertEquals(2, gameTable.getCakeList().size());
		donePlays.startTurn(J3);
		assertEquals(2, gameTable.getCakeList().size());
		donePlays.play(new MoveCakeCard(cake2, new Position(0, 1)));
		donePlays.finishTurn();
		assertEquals(2, gameTable.getCakeList().size());
		assertEquals(6, gameTable.getCakeList().get(0).getPosition().getX());
		assertEquals(2, gameTable.getCakeList().get(0).getPosition().getY());
		assertEquals(0, gameTable.getCakeList().get(1).getPosition().getX());
		assertEquals(1, gameTable.getCakeList().get(1).getPosition().getY());

		donePlays.startTurn(J1);
		assertEquals(1, gameTable.getCakeList().size());
	}

	@Test
	public void mover1PastelMismoLugarOtroPastel() throws GameException, GameWarning {
		donePlays.startTurn(J1);
		Cake cake = new Cake(new Position(6, 2), J1.getTeam());
		donePlays.play(new CakeCard(cake));
		assertEquals(1, gameTable.getCakeList().size());
		donePlays.finishTurn();

		assertEquals(1, gameTable.getCakeList().size());
		donePlays.startTurn(J2);
		assertEquals(1, gameTable.getCakeList().size());
		donePlays.play(new CakeCard(new Cake(new Position(6, 2), J2.getTeam())));
		donePlays.finishTurn();
		assertEquals(2, gameTable.getCakeList().size());

		assertEquals(2, gameTable.getCakeList().size());
		donePlays.startTurn(J3);
		assertEquals(2, gameTable.getCakeList().size());
		donePlays.play(new MoveCakeCard(cake, new Position(7, 2)));
		donePlays.finishTurn();
		assertEquals(2, gameTable.getCakeList().size());
		assertEquals(7, gameTable.getCakeList().get(0).getPosition().getX());
		assertEquals(2, gameTable.getCakeList().get(0).getPosition().getY());
		assertEquals(6, gameTable.getCakeList().get(1).getPosition().getX());
		assertEquals(2, gameTable.getCakeList().get(1).getPosition().getY());

		donePlays.startTurn(J1);
		assertEquals(1, gameTable.getCakeList().size());
	}

	@Test
	public void dosJugadasConPastel_RegresarComoEstabaAntes() throws GameException, GameWarning {
		donePlays.startTurn(J1);
		Cake cake = new Cake(new Position(6, 2), J1.getTeam());
		donePlays.play(new CakeCard(cake));
		assertEquals(1, gameTable.getCakeList().size());
		try {
			donePlays.play(new GunCard(new Position(6, 0), new Position(6, 2)));
			fail();
		} catch (GameException e) {
			assertEquals(GameMessages.TWO_ATTACK_ACTIONS, e.getMessage());
			assertEquals(1, gameTable.getCakeList().size());
		}
		donePlays.resert();
		assertEquals(0, gameTable.getCakeList().size());
	}

}
