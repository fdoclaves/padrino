package gt;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import gm.GameCharacter;
import gm.GameTable;
import gm.PlaysManager;
import gm.Player;
import gm.TableSeat;
import gm.cards.KnifeCard;
import gm.exceptions.GameException;
import gm.exceptions.GameWarning;
import gm.info.CardType;
import gm.pojos.Position;
import gt.extras.Converter;

public class MoneyTest {

	private static Player J1;

	private static Player J2;

	// |0 |01 |02 |03 |04 |05 |06 |07 |08|
	private String[][] TABLE_VALUES = { { "P", "3$", "k", "1$", "_", "2$", "P", "_", "P" },
			{ "M", "*", "*", "*", "*", "*", "*", "*", "P" }, { "k", "_", "2$", "P", "3$", "_", "1$", "_", "k" } };

	private Converter converter;

	private GameTable gameTable;
	
	private List<Player> players;

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
        players = new ArrayList<Player>();
	    players.add(J1);
	    players.add(J2);
		converter = new Converter(9, 3);
		TableSeat[][] tableSeats = converter.to(TABLE_VALUES);
		gameTable = new GameTable(tableSeats);
	}

	@Test
	public void kingMoney() throws GameException, GameWarning {
		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] playerChairs = { { "1_", "1P", "2_", "1k", "1k", "1P", "1_", "1P", "1_" },
				{ "1PK", "**", "**", "**", "**", "**", "**", "**", "2_" },
				{ "2_", "1_", "2_", "2_", "2_", "2_", "2_", "2_", "2_" } };
		PlaysManager donePlays = new PlaysManager(converter.toCharacterArray(playerChairs), gameTable, players);
		donePlays.startTurn(J1);
		donePlays.play(new KnifeCard(new Position(3, 0), new Position(2, 0)));
		donePlays.finishTurn();
		GameCharacter[][] chairsResult = donePlays.getChairs();

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] chairsExpert = { { "1_", "1P", "VV", "1k", "1k", "1P", "1_", "1P", "1_" },
				{ "1PK", "**", "**", "**", "**", "**", "**", "**", "2_" },
				{ "2_", "1_", "2_", "2_", "2_", "2_", "2_", "2_", "2_" } };

		assertEquals(converter.toString(chairsExpert), converter.cToString(chairsResult).replace("V", "VV"));
		assertEquals(8, J1.getMoney());

		donePlays.startTurn(J2);
		donePlays.finishTurn();
		assertEquals(3, J2.getMoney());
	}

	@Test
	public void monopolyMoney() throws GameException {
		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] playerChairs = { { "1_", "1P", "2_", "2k", "1k", "1P", "1_", "1P", "1_" },
				{ "1PK", "**", "**", "**", "**", "**", "**", "**", "2_" },
				{ "2_", "1_", "2_", "2_", "2_", "2_", "2_", "2_", "2_" } };
		PlaysManager donePlays = new PlaysManager(converter.toCharacterArray(playerChairs), gameTable, players);
		donePlays.startTurn(J1);
		donePlays.finishTurn();
		assertEquals(6, J1.getMoney());

		donePlays.startTurn(J2);
		donePlays.finishTurn();
		assertEquals(6, J2.getMoney());
	}

}
