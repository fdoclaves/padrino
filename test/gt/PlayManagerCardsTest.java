package gt;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

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

import org.junit.Before;
import org.junit.Test;

public class PlayManagerCardsTest {

	private PlayManager donePlays;

	private static final String R = "R";

	private static final String B = "B";

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

	private Player player;

	@Before
	public void setUp() throws Exception {
		List<CardType> cards = new ArrayList<CardType>();
		cards.add(CardType.BOOM);
		cards.add(CardType.BOOM);
		cards.add(CardType.BOOM);
		cards.add(CardType.MOVE);
		cards.add(CardType.KNIFE);
		player = new Player(B, cards);
		converter = new Converter(9, 5);
		TableSeat[][] tableSeats = converter.to(TABLE_VALUES);
		gameTable = new GameTable(tableSeats);
		List<Player> players = new ArrayList<Player>();
		players.add(player);
		donePlays = new PlayManager(converter.toCharacterArray(playerChairs), gameTable, players);
	}

	@Test
	public void deleteOneCard() throws GameException, GameWarning {
		donePlays.startTurn(player);
		donePlays.play(new KnifeCard(new Position(0, 4), new Position(1, 4)));
		assertEquals(4, player.getCards().size());
		assertEquals(0, player.getNumberCard(CardType.KNIFE));
		donePlays.finishTurn();
		assertEquals(5, player.getCards().size());
	}

	@Test
	public void deleteTwoCard() throws GameException, GameWarning {
		donePlays.startTurn(player);
		donePlays.play(new KnifeCard(new Position(0, 4), new Position(1, 4)));
		assertEquals(4, player.getCards().size());
		assertEquals(0, player.getNumberCard(CardType.KNIFE));
		donePlays.play(new MoveCard(new Position(0, 4), new Position(1, 4)));
		assertEquals(5, player.getCards().size());
	}

	@Test
	public void noTieneCarta() throws GameException, GameWarning {
		List<CardType> cards = new ArrayList<CardType>();
		cards.add(CardType.BOOM);
		cards.add(CardType.BOOM);
		cards.add(CardType.BOOM);
		cards.add(CardType.BOOM);
		cards.add(CardType.BOOM);
		Player player = new Player(B, cards);
		donePlays.startTurn(player);
		try {
			donePlays.play(new GunCard(new Position(8, 2), new Position(0, 2)));
			fail();
		} catch (GameException e) {
			assertEquals(GameMessages.NO_TIENES_CARD, e.getMessage());
		}
		assertEquals(5, player.getCards().size());
	}
}
