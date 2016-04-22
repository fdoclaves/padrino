package gt;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import gm.GameCharacter;
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

public class CharactersValuesTest {

	private PlayManager donePlays;

	private static Player J1;

	private static Player B;

	private static Player J2;

	// |0 |01 |02 |03 |04 |05 |06 |07 |08|
	private String[][] TABLE_VALUES = { { "P_", "_", "k_", "1$", "__", "__", "P_", "__", "P_" },
			{ "2$", "**", "**", "**", "**", "**", "**", "**", "P_" },
			{ "k_", "__", "__", "P_", "__", "__", "3$", "__", "k_" } };

	// |0 |01 |02 |03 |04|05 |06 |07 |08|
	private String[][] playerChairs = { { "1_", "1P", "2_", "1k", "1k", "1P", "1_", "1P", "1_" },
			{ "1P", "**", "**", "**", "**", "**", "**", "**", "2_" },
			{ "2_", "1_", "2_", "2_", "2_", "2_", "2_", "2_", "2_" } };

	private Converter converter;

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
        B = new Player("B", cards);
		converter = new Converter(9, 3);
		TableSeat[][] tableSeats = converter.to(TABLE_VALUES);
		gameTable = new GameTable(tableSeats);
		donePlays = new PlayManager(converter.toCharacterArray(playerChairs), gameTable);
	}

	@Test
	public void test() throws GameException, GameWarning {

		// Turno JUGADOR #1 (ataca con cuchillo)
		donePlays.startTurn(J1);
		donePlays.play(new KnifeCard(new Position(3, 0), new Position(2, 0)));
		donePlays.finishTurn();
		GameCharacter[][] chairsResult = donePlays.getChairs();

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert = { { "1_", "1P", "VV", "1k", "1k", "1P", "1_", "1P", "1_" },
				{ "1P", "**", "**", "**", "**", "**", "**", "**", "2_" },
				{ "2_", "1_", "2_", "2_", "2_", "2_", "2_", "2_", "2_" } };

		assertEquals(converter.toString(chairsExpert), converter.cToString(chairsResult).replace("V", "VV"));
		assertEquals(2, donePlays.getMoney());

		// Turno JUGADOR #1 (ataca con PISTOLA)
		donePlays.startTurn(J1);
		donePlays.play(new GunCard(new Position(0, 1), new Position(8, 1)));
		donePlays.finishTurn();
		chairsResult = donePlays.getChairs();

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert2 = { { "1_", "1P", "VV", "1k", "1k", "1P", "1_", "1P", "1_" },
				{ "1P", "**", "**", "**", "**", "**", "**", "**", "VV" },
				{ "2_", "1_", "2_", "2_", "2_", "2_", "2_", "2_", "2_" } };

		assertEquals(converter.toString(chairsExpert2), converter.cToString(chairsResult).replace("V", "VV"));
		assertEquals(2, donePlays.getMoney());
	}

	@Test
	public void test2() throws GameException, GameWarning {

		// Turno JUGADOR #2 (ataca con PISTOLA)
		donePlays.startTurn(J2);
		donePlays.play(new GunCard(new Position(8, 1), new Position(0, 1)));
		donePlays.finishTurn();

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert2 = { { "1_", "1P", "2_", "1k", "1k", "1P", "1_", "1P", "1_" },
				{ "VV", "**", "**", "**", "**", "**", "**", "**", "2_" },
				{ "2_", "1_", "2_", "2_", "2_", "2_", "2_", "2_", "2_" } };

		assertEquals(converter.toString(chairsExpert2), converter.cToString(donePlays.getChairs()).replace("V", "VV"));
		assertEquals(1, donePlays.getMoney());
	}

	@Test
	public void dispararDesdeVacio() throws GameException, GameWarning {

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] playerChairs = { { "R", "R", "B", "R", "R", "R", "R", "R", "R" },
				{ "R", "*", "*", "*", "*", "*", "*", "*", "V" }, { "B", "R", "B", "B", "B", "B", "B", "B", "B" } };
		donePlays = new PlayManager(converter.toCharacterArray(playerChairs), gameTable);
		donePlays.startTurn(B);
		try {
			donePlays.play(new GunCard(new Position(8, 1), new Position(0, 1)));
			fail();
		} catch (GameException e) {
			assertEquals(GameMessages.SEAT_EMPTY, e.getMessage());
		}
	}

	@Test
	public void acuchillarDesdeVacio() throws GameException, GameWarning {

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] playerChairs = { { "R", "R", "V", "R", "R", "R", "R", "R", "R" },
				{ "R", "*", "*", "*", "*", "*", "*", "*", "B" }, { "B", "R", "B", "B", "B", "B", "B", "B", "B" } };
		donePlays = new PlayManager(converter.toCharacterArray(playerChairs), gameTable);
		donePlays.startTurn(B);
		try {
			donePlays.play(new KnifeCard(new Position(2, 0), new Position(3, 0)));
			fail();
		} catch (GameException e) {
			assertEquals(GameMessages.SEAT_EMPTY, e.getMessage());
		}
	}

	@Test
	public void dispararDesdeEnemigo() throws GameException, GameWarning {

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] playerChairs = { { "R", "R", "B", "A", "R", "R", "R", "R", "R" },
				{ "R", "*", "*", "*", "*", "*", "*", "*", "V" }, { "B", "R", "B", "R", "B", "B", "B", "B", "B" } };
		donePlays = new PlayManager(converter.toCharacterArray(playerChairs), gameTable);
		donePlays.startTurn(B);
		try {
			donePlays.play(new GunCard(new Position(3, 2), new Position(3, 0)));
			fail();
		} catch (GameException e) {
			assertEquals(GameMessages.NOT_SAME_TEAM, e.getMessage());
		}
		try {
			donePlays.play(new MoveCard(new Position(3, 0), new Position(8, 0)));
			fail();
		} catch (GameException e) {
			assertEquals(GameMessages.NOT_SAME_TEAM, e.getMessage());
		}
	}
}
