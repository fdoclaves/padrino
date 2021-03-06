package gt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import gm.CardManager;
import gm.CardManagerImpl;
import gm.GameCharacter;
import gm.GameTable;
import gm.Player;
import gm.PlaysManager;
import gm.TableSeat;
import gm.cards.ChangeCard;
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

public class AccionesDobleTest {

	private static final int TOTAL_MONEY = 100;

	private static Player J1;

	private static Player J2;

	private PlaysManager donePlays;

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
	
	private List<Player> players;

	@Before
	public void setUp() throws Exception {
		CardManager cardManager = new CardManagerImpl(){
			@Override
			protected void fillCards(List<CardType> chooseCard) {
				for (int i = 1; i <= 6; i++) {
					chooseCard.add(CardType.SLEEP);
				}
			}
		};
	    List<CardType> cards1 = new ArrayList<CardType>();
	    cards1.add(CardType.KNIFE);
	    cards1.add(CardType.GUN);
	    cards1.add(CardType.MOVE);
	    cards1.add(CardType.MOVE);
	    cards1.add(CardType.KNIFE);
	    J1 = new Player("1", cards1);
	    List<CardType> cards2 = new ArrayList<CardType>();
	    cards2.add(CardType.KNIFE);
	    cards2.add(CardType.GUN);
	    cards2.add(CardType.MOVE);
	    cards2.add(CardType.MOVE);
	    cards2.add(CardType.KNIFE);
	    J2 = new Player("2", cards2);
	    players = new ArrayList<Player>();
	    players.add(J1);
	    players.add(J2);
		converter = new Converter(9, 3);
		TableSeat[][] tableSeats = converter.to(TABLE_VALUES);
		gameTable = new GameTable(tableSeats, TOTAL_MONEY);
		donePlays = new PlaysManager(converter.toCharacterArray(playerChairs), gameTable, cardManager,players);

	}

	@Test
	public void moverseYAccion() throws GameException, GameWarning {
		CardManagerImpl cardManager = new CardManagerImpl(){
			@Override
			protected void fillCards(List<CardType> chooseCard) {
				for (int i = 1; i <= 6; i++) {
					chooseCard.add(CardType.MOVE_CAKE);
				}
			}
		};
		donePlays = new PlaysManager(converter.toCharacterArray(playerChairs), gameTable, cardManager,players);
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
			assertEquals(0, J1.getMoney());
			assertEquals(5,J1.getCards().size());
			assertTrue(J1.hasCard(CardType.KNIFE));
			assertFalse(J1.hasCard(CardType.MOVE_CAKE));
			assertEquals(6, cardManager.getTotalCard());
		}
	}
	
	@Test
	public void changeYAccion() throws GameException, GameWarning {
		CardManagerImpl cardManager = new CardManagerImpl(){
			@Override
			protected void fillCards(List<CardType> chooseCard) {
				for (int i = 1; i <= 6; i++) {
					chooseCard.add(CardType.MOVE_CAKE);
				}
			}
		};
		donePlays = new PlaysManager(converter.toCharacterArray(playerChairs), gameTable, cardManager,players);
		donePlays.startTurn(J2);
		donePlays.play(new ChangeCard(CardType.GUN));
		assertEquals(converter.toString(playerChairs), converter.cToString(donePlays.getChairs()));

		try {
			donePlays.play(new MoveCard(new Position(5, 2), new Position(4, 2)));
			fail();
		} catch (GameException e) {
			donePlays.resert();
			assertEquals(GameMessages.CHANGE_CARD_BEFORE, e.getMessage());
			assertEquals(converter.toString(playerChairs), converter.cToString(donePlays.getChairs()));
			assertEquals(0, J2.getMoney());
			assertEquals(5,J2.getCards().size());
			assertTrue(J2.hasCard(CardType.GUN));
			assertFalse(J2.hasCard(CardType.MOVE_CAKE));
			assertEquals(6, cardManager.getTotalCard());
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
		assertEquals(1, J2.getMoney());

		try {
			donePlays.play(new MoveCard(new Position(5, 0), new Position(8, 0)));
			fail();
		} catch (GameException e) {
			assertEquals(GameMessages.THREE_ACTIONS, e.getMessage());
			assertEquals(converter.toString(chairsExpert2),
					converter.cToString(donePlays.getChairs()).replace("V", "VV"));
			assertEquals(1, J2.getMoney());
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
			assertEquals(0, J2.getMoney());
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
		assertEquals(2, J1.getMoney());
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
		assertEquals(2, J1.getMoney());
		donePlays.startTurn(J2);
		donePlays.play(new MoveCard(new Position(2, 0), new Position(4, 2)));
		donePlays.finishTurn();
		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert2 = { { "1_", "1P", "VV", "1k", "VV", "1P", "1_", "1P", "1k" },
				{ "1P", "**", "**", "**", "**", "**", "**", "**", "2_" },
				{ "2_", "1_", "2_", "2_", "2_", "2_", "2_", "2_", "2_" } };
		chairsResult = donePlays.getChairs();
		assertEquals(converter.toString(chairsExpert2), converter.cToString(chairsResult).replace("V", "VV"));
		assertEquals(1, J2.getMoney());
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
		assertEquals(1, J2.getMoney());

		// JUEGA JUGADOR #1
		donePlays.startTurn(J1);
		donePlays.play(new GunCard(new Position(0, 1), new Position(8, 1)));
		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert2 = { { "1_", "VV", "2_", "1k", "1k", "1P", "1Z", "1P", "VV" },
				{ "1P", "**", "**", "**", "**", "**", "**", "**", "VV" },
				{ "2_", "1_", "2_", "2_", "VV", "2_", "2_", "2_", "2_" } };
		assertEquals(converter.toString(chairsExpert2), converter.cToString(donePlays.getChairs()).replace("V", "VV"));
		// se mueve
		donePlays.play(new MoveCard(new Position(3, 0), new Position(4, 2)));
		donePlays.finishTurn();
		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert3 = { { "1_", "VV", "2_", "VV", "1k", "1P", "1_", "1P", "VV" },
				{ "1P", "**", "**", "**", "**", "**", "**", "**", "VV" },
				{ "2_", "1_", "2_", "2_", "1k", "2_", "2_", "2_", "2_" } };
		assertEquals(converter.toString(chairsExpert3), converter.cToString(donePlays.getChairs()).replace("V", "VV"));
		assertEquals(2, J1.getMoney());

		// JUEGA JUGADOR #2
		donePlays.startTurn(J2);
		donePlays.play(new SleepCard(new Position(0, 0)));
		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert4 = { { "1Z", "VV", "2_", "VV", "1k", "1P", "1_", "1P", "VV" },
				{ "1P", "**", "**", "**", "**", "**", "**", "**", "VV" },
				{ "2_", "1_", "2_", "2_", "1k", "2_", "2_", "2_", "2_" } };

		assertEquals(converter.toString(chairsExpert4), converter.cToString(donePlays.getChairs()).replace("V", "VV"));
		assertEquals(1, J2.getMoney());
	}
}
