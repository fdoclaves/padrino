package gt;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import gm.GameCharacter;
import gm.GameTable;
import gm.PlayManager;
import gm.TableSeat;
import gm.cards.GunCard;
import gm.cards.KnifeCard;
import gm.cards.MoveCard;
import gm.cards.SleepCard;
import gm.exceptions.GameException;
import gm.exceptions.GameWarning;
import gm.info.GameMessages;
import gm.pojos.Position;
import gt.extras.Converter;

public class AccionesDobleTest {

	private static final String J1 = "1";

	private static final String J2 = "2";

	private PlayManager donePlays;

	// |0 |01 |02 |03 |04 |05 |06 |07 |08|
	private String[][] TABLE_VALUES = { { "PG", "_G", "kG", "1$", "__", "__", "P_", "__", "P_" },
			{ "2$", "**", "**", "**", "**", "**", "**", "**", "GP" },
			{ "kG", "__", "__", "GP", "__", "GP", "3$", "__", "k_" } };

	// |0 |01 |02 |03 |04 |05 |06 |07 |08|
	private String[][] playerChairs = { { "1_", "1P", "2_", "1k", "1k", "1P", "1Z", "1P", "V" },
			{ "1P", "**", "**", "**", "**", "**", "**", "**", "2_" },
			{ "2_", "1_", "2_", "2_", "V", "2_", "2_", "2_", "2Z" } };

	private Converter converter;

	private GameTable gameTable;

	@Before
	public void setUp() throws Exception {
		converter = new Converter(9, 3);
		TableSeat[][] tableSeats = converter.to(TABLE_VALUES);
		gameTable = new GameTable(tableSeats);
		donePlays = new PlayManager(converter.toCharacterArray(playerChairs), gameTable);

	}

	@Test
	public void moverseYAccion() throws GameException, GameWarning {
		donePlays.startTurn(J1);
		donePlays.play(new MoveCard(new Position(3, 0), new Position(4, 2)));
		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert = { { "1_", "1P", "2_", "VV", "1k", "1P", "1Z", "1P", "VV" },
				{ "1P", "**", "**", "**", "**", "**", "**", "**", "2_" },
				{ "2_", "1_", "2_", "2_", "1k", "2_", "2_", "2_", "2Z" } };

		assertEquals(converter.toString(chairsExpert), converter.cToString(donePlays.getChairs()).replace("V", "VV"));

		try {
			donePlays.play(new KnifeCard(new Position(4, 2), new Position(3, 2)));
			fail();
		} catch (GameException e) {
			donePlays.resert();
			assertEquals(GameMessages.TWO_ATTACK_ACTIONS, e.getMessage());
			assertEquals(converter.toString(playerChairs).replace("V", "VV"),
					converter.cToString(donePlays.getChairs()).replace("V", "VV"));
			assertEquals(0, donePlays.getMoney());
		}
	}

	@Test
	public void AccionYMoverseYMoverse() throws GameException, GameWarning {
		donePlays.startTurn(J2);
		donePlays.play(new KnifeCard(new Position(2, 0), new Position(1, 0)));
		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert = { { "1_", "VV", "2_", "1k", "1k", "1P", "1Z", "1P", "VV" },
				{ "1P", "**", "**", "**", "**", "**", "**", "**", "2_" },
				{ "2_", "1_", "2_", "2_", "VV", "2_", "2_", "2_", "2Z" } };

		assertEquals(converter.toString(chairsExpert), converter.cToString(donePlays.getChairs()).replace("V", "VV"));

		donePlays.play(new MoveCard(new Position(5, 2), new Position(4, 2)));
		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert2 = { { "1_", "VV", "2_", "1k", "1k", "1P", "1Z", "1P", "VV" },
				{ "1P", "**", "**", "**", "**", "**", "**", "**", "2_" },
				{ "2_", "1_", "2_", "2_", "2_", "VV", "2_", "2_", "2_" } };

		assertEquals(converter.toString(chairsExpert2), converter.cToString(donePlays.getChairs()).replace("V", "VV"));
		assertEquals(1, donePlays.getMoney());

		try {
			donePlays.play(new MoveCard(new Position(5, 0), new Position(8, 0)));
			fail();
		} catch (GameException e) {
			assertEquals(GameMessages.THREE_ACTIONS, e.getMessage());
			assertEquals(converter.toString(chairsExpert2),
					converter.cToString(donePlays.getChairs()).replace("V", "VV"));
			assertEquals(0, donePlays.getMoney());
		}

	}

	@Test
	public void AccionYAccion() throws GameException, GameWarning {
		donePlays.startTurn(J2);
		donePlays.play(new KnifeCard(new Position(2, 0), new Position(1, 0)));

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert = { { "1_", "VV", "2_", "1k", "1k", "1P", "1Z", "1P", "VV" },
				{ "1P", "**", "**", "**", "**", "**", "**", "**", "2_" },
				{ "2_", "1_", "2_", "2_", "VV", "2_", "2_", "2_", "2Z" } };

		assertEquals(converter.toString(chairsExpert), converter.cToString(donePlays.getChairs()).replace("V", "VV"));

		try {
			donePlays.play(new KnifeCard(new Position(2, 0), new Position(3, 0)));
			fail();
		} catch (GameException e) {
			donePlays.resert();
			assertEquals(GameMessages.TWO_ATTACK_ACTIONS, e.getMessage());
			assertEquals(converter.toString(playerChairs).replace("V", "VV"),
					converter.cToString(donePlays.getChairs()).replace("V", "VV"));
			assertEquals(0, donePlays.getMoney());
		}

	}

	@Test
	public void moverseYMoverse() throws GameException, GameWarning {
		donePlays.startTurn(J1);
		donePlays.play(new MoveCard(new Position(4, 0), new Position(8, 0)));

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert = { { "1_", "1P", "2_", "1k", "VV", "1P", "1Z", "1P", "1k" },
				{ "1P", "**", "**", "**", "**", "**", "**", "**", "2_" },
				{ "2_", "1_", "2_", "2_", "VV", "2_", "2_", "2_", "2Z" } };
		assertEquals(converter.toString(chairsExpert), converter.cToString(donePlays.getChairs()).replace("V", "VV"));

		donePlays.play(new MoveCard(new Position(3, 0), new Position(4, 2)));
		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert2 = { { "1_", "1P", "2_", "VV", "VV", "1P", "1_", "1P", "1k" },
				{ "1P", "**", "**", "**", "**", "**", "**", "**", "2_" },
				{ "2_", "1_", "2_", "2_", "1k", "2_", "2_", "2_", "2Z" } };
		assertEquals(converter.toString(chairsExpert2), converter.cToString(donePlays.getChairs()).replace("V", "VV"));
		assertEquals(2, donePlays.getMoney());
	}

	@Test
	public void moverseSolaUnaVez() throws GameException, GameWarning {
		donePlays.startTurn(J1);
		donePlays.play(new MoveCard(new Position(4, 0), new Position(8, 0)));
		donePlays.finishTurn();
		GameCharacter[][] chairsResult = donePlays.getChairs();

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert = { { "1_", "1P", "2_", "1k", "VV", "1P", "1_", "1P", "1k" },
				{ "1P", "**", "**", "**", "**", "**", "**", "**", "2_" },
				{ "2_", "1_", "2_", "2_", "VV", "2_", "2_", "2_", "2Z" } };

		assertEquals(converter.toString(chairsExpert), converter.cToString(chairsResult).replace("V", "VV"));
		assertEquals(2, donePlays.getMoney());
		donePlays.startTurn(J2);
		donePlays.play(new MoveCard(new Position(2, 0), new Position(4, 2)));
		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert2 = { { "1_", "1P", "VV", "1k", "VV", "1P", "1_", "1P", "1k" },
				{ "1P", "**", "**", "**", "**", "**", "**", "**", "2_" },
				{ "2_", "1_", "2_", "2_", "2_", "2_", "2_", "2_", "2Z" } };
		chairsResult = donePlays.getChairs();
		assertEquals(converter.toString(chairsExpert2), converter.cToString(chairsResult).replace("V", "VV"));
		assertEquals(1, donePlays.getMoney());
	}

	@Test
	public void accionJ2_accionYMoverseJ1_accionJ2() throws GameException, GameWarning {
		// JUEGA JUGADOR #2
		donePlays.startTurn(J2);
		donePlays.play(new KnifeCard(new Position(2, 0), new Position(1, 0)));
		donePlays.finishTurn();

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert = { { "1_", "VV", "2_", "1k", "1k", "1P", "1Z", "1P", "VV" },
				{ "1P", "**", "**", "**", "**", "**", "**", "**", "2_" },
				{ "2_", "1_", "2_", "2_", "VV", "2_", "2_", "2_", "2_" } };

		assertEquals(converter.toString(chairsExpert), converter.cToString(donePlays.getChairs()).replace("V", "VV"));
		assertEquals(1, donePlays.getMoney());

		// JUEGA JUGADOR #1
		donePlays.startTurn(J1);
		donePlays.play(new GunCard(new Position(0, 1), new Position(8, 1)));
		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert2 = { { "1_", "VV", "2_", "1k", "1k", "1P", "1Z", "1P", "VV" },
				{ "1P", "**", "**", "**", "**", "**", "**", "**", "VV" },
				{ "2_", "1_", "2_", "2_", "VV", "2_", "2_", "2_", "2_" } };
		assertEquals(converter.toString(chairsExpert2), converter.cToString(donePlays.getChairs()).replace("V", "VV"));
		assertEquals(2, donePlays.getMoney());
		// se mueve
		donePlays.play(new MoveCard(new Position(3, 0), new Position(4, 2)));
		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert3 = { { "1_", "VV", "2_", "VV", "1k", "1P", "1_", "1P", "VV" },
				{ "1P", "**", "**", "**", "**", "**", "**", "**", "VV" },
				{ "2_", "1_", "2_", "2_", "1k", "2_", "2_", "2_", "2_" } };
		assertEquals(converter.toString(chairsExpert3), converter.cToString(donePlays.getChairs()).replace("V", "VV"));
		assertEquals(0, donePlays.getMoney());

		// JUEGA JUGADOR #2
		donePlays.startTurn(J2);
		donePlays.play(new SleepCard(new Position(0, 0)));
		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert4 = { { "1Z", "VV", "2_", "VV", "1k", "1P", "1_", "1P", "VV" },
				{ "1P", "**", "**", "**", "**", "**", "**", "**", "VV" },
				{ "2_", "1_", "2_", "2_", "1k", "2_", "2_", "2_", "2_" } };

		assertEquals(converter.toString(chairsExpert4), converter.cToString(donePlays.getChairs()).replace("V", "VV"));
		assertEquals(1, donePlays.getMoney());
	}
}
