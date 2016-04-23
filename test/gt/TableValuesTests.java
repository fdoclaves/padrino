package gt;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import gm.GameTable;
import gm.PlayManager;
import gm.Player;
import gm.TableSeat;
import gm.cards.GunCard;
import gm.cards.KnifeCard;
import gm.cards.MoveCard;
import gm.exceptions.GameException;
import gm.exceptions.GameWarning;
import gm.info.CardType;
import gm.info.GameMessages;
import gm.pojos.Position;
import gt.extras.Converter;

public class TableValuesTests {

	private PlayManager donePlays;

	private static Player R;

	private static Player B;

	// |0 |01 |02 |03 |04 |05 |06 |07 |08|
	private String[][] TABLE_VALUES = { { "P_", "__", "k_", "1$", "__", "__", "P_", "__", "P_" },
			{ "2$", "**", "**", "**", "**", "**", "**", "**", "P_" },
			{ "k_", "__", "__", "P_", "__", "__", "3$", "__", "k_" } };

	// |0 |01 |02 |03 |04 |05 |06 |07 |08|
	private String[][] playerChairs = { { "R", "R", "B", "R", "R", "R", "R", "R", "R" },
			{ "R", "*", "*", "*", "*", "*", "*", "*", "B" }, { "B", "R", "B", "B", "B", "B", "B", "B", "B" } };
	private Converter converter;

	private GameTable gameTable;

	@Before
	public void setUp() throws Exception {
		List<CardType> cards = new ArrayList<CardType>();
		cards.add(CardType.KNIFE);
		cards.add(CardType.GUN);
		cards.add(CardType.MOVE);
		cards.add(CardType.KNIFE);
		cards.add(CardType.SLEEP);
		B = new Player("B", cards);
		R = new Player("R", cards);
		List<Player> players = new ArrayList<Player>();
		players.add(B);
		players.add(R);
		converter = new Converter(9, 3);
		TableSeat[][] tableSeats = converter.to(TABLE_VALUES);
		gameTable = new GameTable(tableSeats);
		donePlays = new PlayManager(converter.toCharacterArray(playerChairs), gameTable, players);
	}

	@Test
	public void test() throws GameException, GameWarning {

		// Turno Azul (ataca con cuchillo)
		donePlays.startTurn(B);
		donePlays.play(new KnifeCard(new Position(0, 2), new Position(1, 2)));
		donePlays.finishTurn();
		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert = { { "R", "R", "B", "R", "R", "R", "R", "R", "R" },
				{ "R", "*", "*", "*", "*", "*", "*", "*", "B" }, { "B", "V", "B", "B", "B", "B", "B", "B", "B" } };

		assertEquals(converter.toString(chairsExpert), converter.cToString(donePlays.getChairs()));
		assertEquals(1, donePlays.getMoney());

		// Turno Rojo (ataca con pistola)
		donePlays.startTurn(R);
		donePlays.play(new GunCard(new Position(6, 0), new Position(6, 2)));
		donePlays.finishTurn();
		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert2 = { { "R", "R", "B", "R", "R", "R", "R", "R", "R" },
				{ "R", "*", "*", "*", "*", "*", "*", "*", "B" }, { "B", "V", "B", "B", "B", "B", "V", "B", "B" } };

		assertEquals(converter.toString(chairsExpert2), converter.cToString(donePlays.getChairs()));
		assertEquals(2, donePlays.getMoney());

		// Turno Azul (se mueve al dinero)
		donePlays.startTurn(B);
		donePlays.play(new MoveCard(new Position(2, 2), new Position(6, 2)));
		donePlays.finishTurn();
		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert3 = { { "R", "R", "B", "R", "R", "R", "R", "R", "R" },
				{ "R", "*", "*", "*", "*", "*", "*", "*", "B" }, { "B", "V", "V", "B", "B", "B", "B", "B", "B" } };

		assertEquals(converter.toString(chairsExpert3), converter.cToString(donePlays.getChairs()));
		assertEquals(0, donePlays.getMoney());

		// Turno Rojo (ataca con cuchillo a vacio)
		donePlays.startTurn(B);
		try {
			donePlays.play(new KnifeCard(new Position(0, 2), new Position(1, 2)));
		} catch (GameWarning e) {
			assertEquals(GameMessages.ATTACKER_SEAT_IS_EMPTY, e.getMessage());
		}

	}

	@Ignore
	@Test
	public void testtest() {
		int[][] ints = { { 0, 1, 2 }, { 3, 4, 5 } };
		System.out.println(ints[1][1]);
		System.out.println(ints[0][1]);
	}

	@Test
	public void validadoJugadas() throws GameException, GameWarning {
		// |0 |01 |02 |03 |04|05 |06 |07 |08|
		// "R","R","B","R","R","R","R","R","R",
		// "R", "B",
		// "B","R","B","B","B","B","B","B","B"};

		// MOVERSE A UNA SILLA OCUPADA
		donePlays.startTurn(B);
		try {
			donePlays.play(new MoveCard(new Position(2, 0), new Position(3, 0)));
			fail();
		} catch (GameException e) {
			assertEquals(GameMessages.OCCUPIED, e.getMessage());
		}

		// acuchillar a otro que no esta alado y se tiene el cuchillo
		try {
			donePlays.play(new KnifeCard(new Position(2, 0), new Position(0, 0)));
			fail();
		} catch (GameException e) {
			assertEquals(GameMessages.ESTA_LEJOS, e.getMessage());
		}

		// acuchillar a otro que no esta alado y se tiene el cuchillo
		try {
			donePlays.play(new KnifeCard(new Position(2, 0), new Position(4, 0)));
			fail();
		} catch (GameException e) {
			assertEquals(GameMessages.ESTA_LEJOS, e.getMessage());
		}

		// acuchillar a otro que no esta alado y se tiene el cuchillo //ESQUINA
		// INCIALES
		try {
			donePlays.play(new KnifeCard(new Position(0, 2), new Position(0, 0)));
			fail();
		} catch (GameException e) {
			assertEquals(GameMessages.ESTA_LEJOS, e.getMessage());
		}

		// acuchillar a otro que no esta alado y se tiene el cuchillo //ESQUINA
		// FINALES
		try {
			donePlays.play(new KnifeCard(new Position(8, 2), new Position(8, 0)));
			fail();
		} catch (GameException e) {
			assertEquals(GameMessages.ESTA_LEJOS, e.getMessage());
		}

		// |0 |01 |02 |03 |04|05 |06 |07 |08|
		// "R","R","B","R","R","R","R","R","R",
		// "R", "B",
		// "B","R","B","B","B","B","B","B","B"};

		// disparar sin arma
		donePlays.startTurn(R);
		try {
			donePlays.play(new GunCard(new Position(5, 0), new Position(5, 2)));
			fail("DISPARAR SIN ARMA");
		} catch (GameException e) {
			assertEquals(GameMessages.YOU_HAS_NOT_GUN, e.getMessage());
		}

		// DISPARAR A OTRO QUE NO ESTE ENFRENTE
		try {
			donePlays.play(new GunCard(new Position(6, 0), new Position(5, 2)));
			fail();
		} catch (GameException e) {
			assertEquals(GameMessages.NO_ESTA_ENFRENTE, e.getMessage());
		}

		// DISPARAR EN EQUINAS INCIO
		try {
			donePlays.play(new GunCard(new Position(0, 0), new Position(0, 2)));
			fail();
		} catch (GameException e) {
			assertEquals(GameMessages.ESQUINA_NO_PUEDE_DISPARAR, e.getMessage());
		}

		// DISPARAR EN EQUINAS FINALES
		try {
			donePlays.play(new GunCard(new Position(8, 0), new Position(8, 2)));
			fail();
		} catch (GameException e) {
			assertEquals(GameMessages.ESQUINA_NO_PUEDE_DISPARAR, e.getMessage());
		}

		// acuchillar sin arma
		try {
			donePlays.play(new KnifeCard(new Position(1, 0), new Position(2, 0)));
			fail();
		} catch (GameException e) {
			assertEquals(GameMessages.YOU_HAS_NOT_KNIFE, e.getMessage());
		}
	}

}
