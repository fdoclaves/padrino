package gt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import gm.GameTable;
import gm.Player;
import gm.PlaysManager;
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

public class OchoEsquinas {

	private PlaysManager donePlays;

	private static Player R;

	private static Player B;

	// |0 |01 |02 |03 |04 |05 |06 |07 |08|
	private String[][] TABLE_VALUES = { { "P_", "__", "k_", "1$", "__", "__", "P_", "__", "P_" },
			{ "k_", "**", "**", "**", "**", "**", "**", "**", "P_" },
			{ "MkP", "**", "**", "**", "**", "**", "**", "**", "P_" },
			{ "P_", "**", "**", "**", "**", "**", "**", "**", "k_" },
			{ "k_", "__", "__", "P_", "__", "__", "3$", "__", "k_" } };

	// |0 |01 |02 |03 |04 |05 |06 |07 |08|
	private String[][] playerChairs = { { "R", "R", "B", "R", "R", "R", "R", "R", "B" },
			{ "R", "*", "*", "*", "*", "*", "*", "*", "B" }, { "B", "*", "*", "*", "*", "*", "*", "*", "R" },
			{ "R", "*", "*", "*", "*", "*", "*", "*", "B" }, { "B", "R", "B", "B", "B", "B", "B", "B", "R" } };

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
        
        B = new Player("B", cards);
        R = new Player("R", cards);
        List<Player> players = new ArrayList<Player>();
	    players.add(B);
	    players.add(R);
		converter = new Converter(9, 5);
		TableSeat[][] tableSeats = converter.to(TABLE_VALUES);
		gameTable = new GameTable(tableSeats, 100);
		donePlays = new PlaysManager(converter.toCharacterArray(playerChairs), gameTable, players);
	}

	@Test
	public void happyPath5taLinea() throws GameException, GameWarning {

		// Turno Azul (ataca con cuchillo)
		donePlays.startTurn(B);
		donePlays.play(new KnifeCard(new Position(0, 4), new Position(1, 4)));
		donePlays.finishTurn();
		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert = { { "R", "R", "B", "R", "R", "R", "R", "R", "B" },
				{ "R", "*", "*", "*", "*", "*", "*", "*", "B" }, { "B", "*", "*", "*", "*", "*", "*", "*", "R" },
				{ "R", "*", "*", "*", "*", "*", "*", "*", "B" }, { "B", "V", "B", "B", "B", "B", "B", "B", "R" } };

		assertEquals(converter.toString(chairsExpert), converter.cToString(donePlays.getChairs()));

		// Turno Rojo (ataca con pistola)
		donePlays.startTurn(R);
		donePlays.play(new GunCard(new Position(6, 0), new Position(6, 4)));
		donePlays.finishTurn();
		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert2 = { { "R", "R", "B", "R", "R", "R", "R", "R", "B" },
				{ "R", "*", "*", "*", "*", "*", "*", "*", "B" }, { "B", "*", "*", "*", "*", "*", "*", "*", "R" },
				{ "R", "*", "*", "*", "*", "*", "*", "*", "B" }, { "B", "V", "B", "B", "B", "B", "V", "B", "R" } };

		assertEquals(converter.toString(chairsExpert2), converter.cToString(donePlays.getChairs()));

		// Turno Azul (se mueve al dinero)
		donePlays.startTurn(B);
		donePlays.play(new MoveCard(new Position(2, 4), new Position(6, 4)));
		donePlays.finishTurn();
		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert3 = { { "R", "R", "B", "R", "R", "R", "R", "R", "B" },
				{ "R", "*", "*", "*", "*", "*", "*", "*", "B" }, { "B", "*", "*", "*", "*", "*", "*", "*", "R" },
				{ "R", "*", "*", "*", "*", "*", "*", "*", "B" }, { "B", "V", "V", "B", "B", "B", "B", "B", "R" } };

		assertEquals(converter.toString(chairsExpert3), converter.cToString(donePlays.getChairs()));
	}

	@Test
	public void acuchillarAOtroQueNoEstaAladoYSeTieneCuchillo() throws GameException, GameWarning {
		donePlays.startTurn(B);
		try {
			donePlays.play(new KnifeCard(new Position(2, 0), new Position(4, 4)));
			fail();
		} catch (GameException e) {
			assertEquals(GameMessages.ESTA_LEJOS + ", ", e.getMessage());
		}
	}

	@Test
	public void AcuchillarAOtroQueNoEstaAladoYSeTieneCuchillo() throws GameException, GameWarning {
		donePlays.startTurn(B);
		try {
			donePlays.play(new KnifeCard(new Position(2, 0), new Position(4, 4)));
			fail();
		} catch (GameException e) {
			assertEquals(GameMessages.ESTA_LEJOS + ", ", e.getMessage());
		}

		donePlays.startTurn(B);
		try {
			donePlays.play(new KnifeCard(new Position(0, 2), new Position(0, 0)));
			fail();
		} catch (GameException e) {
			assertEquals(GameMessages.ESTA_LEJOS + ", ", e.getMessage());
		}
	}

	@Test
	public void AcuchillarAOtroQueNoEstaAladoYSeTieneCuchilloEsquinaInciales() throws GameException, GameWarning {
		donePlays.startTurn(B);
		// ESQUINA INCIALES
		try {
			donePlays.play(new KnifeCard(new Position(0, 4), new Position(0, 0)));
			fail();
		} catch (GameException e) {
			assertEquals(GameMessages.ESTA_LEJOS + ", ", e.getMessage());
		}

		try {
			donePlays.play(new KnifeCard(new Position(0, 4), new Position(0, 1)));
			fail();
		} catch (GameException e) {
			assertEquals(GameMessages.ESTA_LEJOS + ", ", e.getMessage());
		}

		try {
			donePlays.play(new KnifeCard(new Position(0, 4), new Position(0, 2)));
			fail();
		} catch (GameException e) {
			assertEquals(GameMessages.ESTA_LEJOS + ", ", e.getMessage());
		}

		donePlays.play(new KnifeCard(new Position(0, 2), new Position(0, 3)));

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert3 = { { "R", "R", "B", "R", "R", "R", "R", "R", "B" },
				{ "R", "*", "*", "*", "*", "*", "*", "*", "B" }, { "B", "*", "*", "*", "*", "*", "*", "*", "R" },
				{ "V", "*", "*", "*", "*", "*", "*", "*", "B" }, { "B", "R", "B", "B", "B", "B", "B", "B", "R" } };

		assertEquals(converter.toString(chairsExpert3), converter.cToString(donePlays.getChairs()));
	}

	@Test
	public void AcuchillarAOtroQueNoEstaAladoYSeTieneCuchilloEsquinaFinales() throws GameException, GameWarning {
		donePlays.startTurn(R);
		try {
			donePlays.play(new KnifeCard(new Position(8, 4), new Position(8, 0)));
			fail();
		} catch (GameException e) {
			assertEquals(GameMessages.ESTA_LEJOS + ", ", e.getMessage());
		}

		try {
			donePlays.play(new KnifeCard(new Position(8, 4), new Position(8, 1)));
			fail();
		} catch (GameException e) {
			assertEquals(GameMessages.ESTA_LEJOS + ", ", e.getMessage());
		}

		try {
			donePlays.play(new KnifeCard(new Position(8, 4), new Position(8, 2)));
			fail();
		} catch (GameException e) {
			assertEquals(GameMessages.ESTA_LEJOS + ", ", e.getMessage());
		}

		try {
			donePlays.play(new KnifeCard(new Position(8, 4), new Position(0, 3)));
			fail();
		} catch (GameException e) {
			assertEquals(GameMessages.ESTA_LEJOS + ", ", e.getMessage());
		}

		donePlays.play(new KnifeCard(new Position(8, 4), new Position(8, 3)));

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert3 = { { "R", "R", "B", "R", "R", "R", "R", "R", "B" },
				{ "R", "*", "*", "*", "*", "*", "*", "*", "B" }, { "B", "*", "*", "*", "*", "*", "*", "*", "R" },
				{ "R", "*", "*", "*", "*", "*", "*", "*", "V" }, { "B", "R", "B", "B", "B", "B", "B", "B", "R" } };

		assertEquals(converter.toString(chairsExpert3), converter.cToString(donePlays.getChairs()));
	}

	@Test
	public void validadoDisparos() throws GameException, GameWarning {
		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		// "R","R","B","R","R","R","R","R","B",
		// "R", "B",
		// "B", "R",
		// "R", "B",
		// "B","R","B","B","B","B","B","B","R"};

		// DISPARAR A OTRO QUE NO ESTE ENFRENTE
		donePlays.startTurn(B);
		try {
			donePlays.play(new GunCard(new Position(8, 1), new Position(0, 1)));
			fail();
		} catch (GameException e) {
			assertEquals(GameMessages.ESQUINA_NO_PUEDE_DISPARAR + ", ", e.getMessage());
		}

		// DISPARAR EN EQUINAS INCIO
		donePlays.startTurn(R);
		try {
			donePlays.play(new GunCard(new Position(0, 0), new Position(8, 0)));
			fail();
		} catch (GameException e) {
			assertEquals(GameMessages.ESQUINA_NO_PUEDE_DISPARAR + ", ", e.getMessage());
		}

		// DISPARAR EN EQUINAS FINALES
		donePlays.startTurn(R);
		try {
			donePlays.play(new GunCard(new Position(0, 3), new Position(0, 4)));
			fail();
		} catch (GameException e) {
			assertEquals(GameMessages.ESQUINA_NO_PUEDE_DISPARAR + ", ", e.getMessage());
		}
	}

}
