package gt;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import gm.GameTable;
import gm.PlayManager;
import gm.Player;
import gm.TableSeat;
import gm.cards.GunCard;
import gm.cards.KnifeCard;
import gm.cards.MoveCard;
import gm.cards.SleepCard;
import gm.exceptions.GameException;
import gm.exceptions.GameWarning;
import gm.info.CardType;
import gm.info.GameMessages;
import gm.pojos.Position;
import gt.extras.Converter;

public class PoisonTest {

	private static Player J1;

	private static Player J2;

	// |0 |01 |02 |03 |04 |05 |06 |07 |08|
	private String[][] TABLE_VALUES = { { "PG", "_G", "kG", "1$", "__", "__", "P_", "__", "P_" },
			{ "2$", "**", "**", "**", "**", "**", "**", "**", "GP" },
			{ "kG", "__", "__", "GP", "__", "GP", "3$", "__", "k_" } };

	// |0 |01 |02 |03 |04 |05 |06 |07 |08|
	private String[][] playerChairs = { { "1_", "1P", "2_", "1k", "1k", "1P", "1_", "1P", "1_" },
			{ "1P", "**", "**", "**", "**", "**", "**", "**", "2_" },
			{ "2_", "1_", "2_", "2_", "V", "2_", "2_", "2_", "2_" } };

	private Converter converter;

	private PlayManager donePlays;

	private GameTable gameTable;

	@Before
	public void setUp() throws Exception {
	    List<CardType> cards = new ArrayList<CardType>();
        cards.add(CardType.KNIFE);
        cards.add(CardType.GUN);
        cards.add(CardType.MOVE);
        cards.add(CardType.CAKE);
        cards.add(CardType.SLEEP);
        J1 = new Player("1", cards);
        J2 = new Player("2", cards);
		converter = new Converter(9, 3);
		TableSeat[][] tableSeats = converter.to(TABLE_VALUES);
		gameTable = new GameTable(tableSeats);
		donePlays = new PlayManager(converter.toCharacterArray(playerChairs), gameTable);
	}

	@Test
	public void test() throws GameException, GameWarning {
		// DESPIERTAN AL TERMINAR EL TURNO C/JUGADOR
		// duerme con vaso maximo 3
		donePlays.startTurn(J1);
		donePlays.play(new SleepCard(new Position(0, 2), new Position(3, 2), new Position(8, 1)));

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert = { { "1_", "1P", "2_", "1k", "1k", "1P", "1_", "1P", "1_" },
				{ "1P", "**", "**", "**", "**", "**", "**", "**", "2Z" },
				{ "2Z", "1_", "2_", "2Z", "VV", "2_", "2_", "2_", "2_" } };

		assertEquals(converter.toString(chairsExpert), converter.cToString(donePlays.getChairs()).replace("V", "VV"));
		assertEquals(2, donePlays.getMoney());

		// duerme con vaso 2 personas (2 jugadores, 2 Turnos)
		donePlays = new PlayManager(converter.toCharacterArray(playerChairs), gameTable);
		donePlays.startTurn(J1);
		donePlays.play(new SleepCard(new Position(0, 2), new Position(8, 1)));

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert2 = { { "1_", "1P", "2_", "1k", "1k", "1P", "1_", "1P", "1_" },
				{ "1P", "**", "**", "**", "**", "**", "**", "**", "2Z" },
				{ "2Z", "1_", "2_", "2_", "VV", "2_", "2_", "2_", "2_" } };

		assertEquals(converter.toString(chairsExpert2), converter.cToString(donePlays.getChairs()).replace("V", "VV"));
		assertEquals(2, donePlays.getMoney());
		donePlays.startTurn(J2);
		donePlays.play(new GunCard(new Position(5, 2), new Position(5, 0)));
		donePlays.finishTurn();
		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert2B = { { "1_", "1P", "2_", "1k", "1k", "VV", "1_", "1P", "1_" },
				{ "1P", "**", "**", "**", "**", "**", "**", "**", "2_" },
				{ "2_", "1_", "2_", "2_", "VV", "2_", "2_", "2_", "2_" } };

		assertEquals(converter.toString(chairsExpert2B), converter.cToString(donePlays.getChairs()).replace("V", "VV"));
		assertEquals(1, donePlays.getMoney());

		// duerme con vaso 1 personas
		donePlays = new PlayManager(converter.toCharacterArray(playerChairs), gameTable);
		donePlays.startTurn(J2);
		donePlays.play(new SleepCard(new Position(1, 0)));

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert3 = { { "1_", "1PZ", "2_", "1k", "1k", "1P", "1_", "1P", "1_" },
				{ "1P", "**", "**", "**", "**", "**", "**", "**", "2_" },
				{ "2_", "1_", "2_", "2_", "VV", "2_", "2_", "2_", "2_" } };

		assertEquals(converter.toString(chairsExpert3), converter.cToString(donePlays.getChairs()).replace("V", "VV"));
		assertEquals(1, donePlays.getMoney());
	}

	@Test
	public void erroresAlDormir() throws GameException, GameWarning {
		// duerme sin vaso
		donePlays.startTurn(J1);
		try {
			donePlays.play(new SleepCard(new Position(3, 0), new Position(4, 0), new Position(5, 0)));
			fail();
		} catch (GameException e) {
			assertEquals(GameMessages.THERE_ISNT_GLASS, e.getMessage());
			assertEquals(converter.toString(playerChairs).replace("V", "VV"),
					converter.cToString(donePlays.getChairs()).replace("V", "VV"));
		}

		// duerme con vaso y sin vaso
		donePlays = new PlayManager(converter.toCharacterArray(playerChairs), gameTable);
		donePlays.startTurn(J1);
		try {
			donePlays.play(new SleepCard(new Position(0, 2), new Position(1, 2)));
			fail();
		} catch (GameException e) {
			assertEquals(GameMessages.THERE_ISNT_GLASS, e.getMessage());
			assertEquals(converter.toString(playerChairs).replace("V", "VV"),
					converter.cToString(donePlays.getChairs()).replace("V", "VV"));
		}

		// misma coordenas Zzz mismo turno
		donePlays = new PlayManager(converter.toCharacterArray(playerChairs), gameTable);
		donePlays.startTurn(J1);
		try {
			donePlays.play(new SleepCard(new Position(0, 2), new Position(0, 2), new Position(5, 2)));
			fail();
		} catch (GameException e) {
			assertEquals(GameMessages.SAME_POSITIONS, e.getMessage());
			assertEquals(converter.toString(playerChairs).replace("V", "VV"),
					converter.cToString(donePlays.getChairs()).replace("V", "VV"));
		}

		// misma coordenas Zzz mismo turno
		donePlays = new PlayManager(converter.toCharacterArray(playerChairs), gameTable);
		donePlays.startTurn(J1);
		try {
			donePlays.play(new SleepCard(new Position(5, 2), new Position(0, 2), new Position(0, 2)));
			fail();
		} catch (GameException e) {
			assertEquals(converter.toString(playerChairs).replace("V", "VV"),
					converter.cToString(donePlays.getChairs()).replace("V", "VV"));
			assertEquals(GameMessages.SAME_POSITIONS, e.getMessage());
		}
	}

	@Test
	public void accionesDormidas() throws GameException, GameWarning {
		// disparar
		donePlays.startTurn(J1);
		donePlays.play(new SleepCard(new Position(3, 2)));
		donePlays.startTurn(J2);
		try {
			donePlays.play(new GunCard(new Position(3, 2), new Position(3, 0)));
			fail();
		} catch (GameException e) {
			assertEquals(GameMessages.ESTA_DORMIDO, e.getMessage());
		}

		// acuchillar
		donePlays = new PlayManager(converter.toCharacterArray(playerChairs), gameTable);
		donePlays.startTurn(J1);
		donePlays.play(new SleepCard(new Position(2, 0)));
		donePlays.finishTurn();
		donePlays.startTurn(J1);
		try {
			donePlays.play(new KnifeCard(new Position(2, 0), new Position(3, 0)));
			fail();
		} catch (GameException e) {
			assertEquals(GameMessages.ESTA_DORMIDO, e.getMessage());
		}

		// mover
		donePlays = new PlayManager(converter.toCharacterArray(playerChairs), gameTable);
		donePlays.startTurn(J1);
		donePlays.play(new SleepCard(new Position(2, 0)));
		try {
			donePlays.play(new MoveCard(new Position(2, 0), new Position(3, 0)));
			fail();
		} catch (GameException e) {
			assertEquals(GameMessages.ESTA_DORMIDO, e.getMessage());
		}
	}

	@Test
	public void matarConZzzUnJugador() throws GameException, GameWarning {
		// Turno JUGADOR #1 (mata con 1 jugadores) (3 jugadores, 2 Turnos)
		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] playerChairs = { { "1_", "2P", "3_", "1C", "2C", "3P", "1_", "2P", "3_" },
				{ "1P", "**", "**", "**", "**", "**", "**", "**", "2_" },
				{ "3_", "1_", "2_", "3_", "V", "2_", "3_", "1_", "2_" } };

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		// {{"PG","_G","CG","$$","__","__","P_","__","P_"},
		// {"$$","**","**","**","**","**","**","**","GP"},
		// {"CG","__","__","GP","G_","__","$$","__","C_"}};

		donePlays = new PlayManager(converter.toCharacterArray(playerChairs), gameTable);
		donePlays.startTurn(J1);
		donePlays.play(new SleepCard(new Position(2, 0), new Position(3, 2), new Position(1, 0)));
		// |0 |01 |02 |03 |04 |05 |06 |07 |08 |
		String[][] chairsExpert = { { "1_", "2PZ", "3Z", "1C", "2C", "3P", "1_", "2P", "3_" },
				{ "1P", "**", "**", "**", "**", "**", "**", "**", "2_" },
				{ "3_", "1_", "2_", "3Z", "VV", "2_", "3_", "1_", "2_" } };

		assertEquals(converter.toString(chairsExpert), converter.cToString(donePlays.getChairs()).replace("V", "VV"));
		donePlays.startTurn(J2);
		donePlays.play(new SleepCard(new Position(2, 0), new Position(3, 2), new Position(0, 0)));
		donePlays.finishTurn();
		// |0 |01 |02 |03 |04 |05 |06 |07 |08 |
		String[][] chairsExpert2 = { { "1Z", "2P_", "VV", "1C", "2C", "3P", "1_", "2P", "3_" },
				{ "1P", "**", "**", "**", "**", "**", "**", "**", "2_" },
				{ "3_", "1_", "2_", "VV", "VV", "2_", "3_", "1_", "2_" } };

		assertEquals(converter.toString(chairsExpert2), converter.cToString(donePlays.getChairs()).replace("V", "VV"));

	}

}
