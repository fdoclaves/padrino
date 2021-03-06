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
import gm.cards.ChangeCard;
import gm.cards.GunCard;
import gm.cards.KnifeCard;
import gm.cards.MoveCard;
import gm.exceptions.GameException;
import gm.exceptions.GameWarning;
import gm.info.CardType;
import gm.info.GameMessages;
import gm.pojos.Position;
import gt.extras.Converter;

public class PlaysManagerTest {

	private PlaysManager playsManager;

	private static final String B = "B";
	
	private static final String R = "R";

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

	private Player player1;
	
	private Player player2;
	
	private List<Player> players;

	@Before
	public void setUp() throws Exception {
		List<CardType> cards1 = new ArrayList<CardType>();
		cards1.add(CardType.BOOM);
		cards1.add(CardType.BOOM);
		cards1.add(CardType.BOOM);
		cards1.add(CardType.MOVE);
		cards1.add(CardType.KNIFE);
		player1 = new Player(B, cards1);
		List<CardType> cards2 = new ArrayList<CardType>();
		cards2.add(CardType.BOOM);
		cards2.add(CardType.BOOM);
		cards2.add(CardType.BOOM);
		cards2.add(CardType.MOVE);
		cards2.add(CardType.KNIFE);
		player2 = new Player(R, cards2);
		players = new ArrayList<Player>();
		players.add(player1);
		players.add(player2);
		converter = new Converter(9, 5);
		TableSeat[][] tableSeats = converter.to(TABLE_VALUES);
		gameTable = new GameTable(tableSeats, 20);
	}

	@Test
	public void deleteOneCard() throws GameException, GameWarning {
		playsManager = new PlaysManager(converter.toCharacterArray(playerChairs), gameTable, players);
		playsManager.startTurn(player1);
		playsManager.play(new KnifeCard(new Position(0, 4), new Position(1, 4)));
		assertEquals(4, player1.getCards().size());
		assertEquals(0, player1.getNumberCard(CardType.KNIFE));
		playsManager.finishTurn();
		assertEquals(5, player1.getCards().size());
	}

	@Test
	public void deleteTwoCard() throws GameException, GameWarning {
		playsManager = new PlaysManager(converter.toCharacterArray(playerChairs), gameTable, players);
		playsManager.startTurn(player1);
		playsManager.play(new KnifeCard(new Position(0, 4), new Position(1, 4)));
		assertEquals(4, player1.getCards().size());
		assertEquals(0, player1.getNumberCard(CardType.KNIFE));
		playsManager.play(new MoveCard(new Position(0, 4), new Position(1, 4)));
		assertEquals(5, player1.getCards().size());
	}

	@Test
	public void noTieneCarta() throws GameException, GameWarning {
		playsManager = new PlaysManager(converter.toCharacterArray(playerChairs), gameTable, players);
		List<CardType> cards = new ArrayList<CardType>();
		cards.add(CardType.BOOM);
		cards.add(CardType.BOOM);
		cards.add(CardType.BOOM);
		cards.add(CardType.BOOM);
		cards.add(CardType.BOOM);
		Player player = new Player(B, cards);
		playsManager.startTurn(player);
		try {
			playsManager.play(new GunCard(new Position(8, 2), new Position(0, 2)));
			fail();
		} catch (GameException e) {
			assertEquals(GameMessages.NO_TIENES_CARD, e.getMessage());
		}
		assertEquals(5, player.getCards().size());
	}
	
	
	@Test
	public void contarDinero() throws GameException, GameWarning {
		
		//............................ |0 |...01 |..02 |..03 |..04 |..05 |..06 ...|07 .|08|
		String[][] TABLE_VALUES2 = { { "__", "__", "__", "1$", "__", "__", "__", "__", "__" },
									 { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
									 { "__", "**", "**", "**", "**", "**", "**", "**", "M_" },
									 { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
									 { "__", "__", "__", "__", "__", "3$", "3$", "__", "__" } };

		//............................|0....01....02...03|..04|..05|..06..|07..|08|
		String[][] playerChairs2 = { { "V", "V", "V", "B", "V", "V", "V", "V", "V" },
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" }, 
									 { "V", "*", "*", "*", "*", "*", "*", "*", "B" },
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" }, 
									 { "V", "V", "V", "V", "V", "R", "R", "V", "V" } };
		
		TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
		gameTable = new GameTable(tableSeats, 20);
		playsManager = new PlaysManager(converter.toCharacterArray(playerChairs2), gameTable, players);
		
		playsManager.startTurn(player1);
		playsManager.play(new ChangeCard(CardType.BOOM));
		playsManager.finishTurn();
		assertEquals(2, player1.getMoney());
		
		playsManager.startTurn(player2);
		playsManager.play(new ChangeCard(CardType.BOOM));
		playsManager.finishTurn();
		assertEquals(4, player2.getMoney());
		
		playsManager.startTurn(player1);
		playsManager.play(new ChangeCard(CardType.BOOM));
		playsManager.finishTurn();
		assertEquals(4, player1.getMoney());
		
		playsManager.startTurn(player2);
		playsManager.play(new ChangeCard(CardType.BOOM));
		playsManager.finishTurn();
		assertEquals(8, player2.getMoney());
	}
	
	@Test
	public void siMuerenTodosSusPersonajesPierde() throws GameException, GameWarning {

		//............................|0....01....02...03|..04|..05|..06..|07..|08|
		String[][] playerChairs2 = { { "V", "V", "N_", "Bk", "V", "V", "V", "V", "V" },
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" }, 
									 { "V", "*", "*", "*", "*", "*", "*", "*", "B" },
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" }, 
									 { "V", "V", "V", "V", "V", "R", "R", "V", "V" } };
		
		List<CardType> cards = new ArrayList<CardType>();
		cards.add(CardType.BOOM);
		cards.add(CardType.BOOM);
		cards.add(CardType.BOOM);
		cards.add(CardType.GUN);
		cards.add(CardType.GUN);
		Player player3 = new Player("N", cards);
		players.add(player3);
		playsManager = new PlaysManager(converter.toCharacterArray(playerChairs2), gameTable, players);
		
		assertEquals(3, players.size());
		playsManager.startTurn(player1);
		playsManager.play(new KnifeCard(new Position(3, 0), new Position(2, 0)));
		playsManager.finishTurn();
		assertEquals(2, players.size());
		assertEquals(0, player3.getCards().size());
	}
	
	@Test
	public void dineroIlimitado() throws GameException, GameWarning {
		
		//............................ |0 |...01 |..02 |..03 |..04 |..05 |..06 ...|07 .|08|
		String[][] TABLE_VALUES2 = { { "__", "__", "__", "1$", "__", "__", "__", "__", "__" },
									 { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
									 { "__", "**", "**", "**", "**", "**", "**", "**", "M_" },
									 { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
									 { "__", "__", "__", "__", "__", "3$", "3$", "__", "__" } };

		//............................|0....01....02...03|..04|..05|..06..|07..|08|
		String[][] playerChairs2 = { { "V", "V", "V", "B", "V", "V", "V", "V", "V" },
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" }, 
									 { "V", "*", "*", "*", "*", "*", "*", "*", "B" },
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" }, 
									 { "V", "V", "V", "V", "V", "R", "R", "V", "V" } };
		
		TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
		gameTable = new GameTable(tableSeats, 12);
		playsManager = new PlaysManager(converter.toCharacterArray(playerChairs2), gameTable, players);
		
		playsManager.startTurn(player1);
		playsManager.play(new ChangeCard(CardType.BOOM));
		playsManager.finishTurn();
		assertEquals(2, player1.getMoney());
		
		playsManager.startTurn(player2);
		playsManager.play(new ChangeCard(CardType.BOOM));
		playsManager.finishTurn();
		assertEquals(4, player2.getMoney());
		
		playsManager.startTurn(player1);
		playsManager.play(new ChangeCard(CardType.BOOM));
		playsManager.finishTurn();
		assertEquals(4, player1.getMoney());
		
		playsManager.startTurn(player2);
		playsManager.play(new ChangeCard(CardType.BOOM));
		playsManager.finishTurn();
		assertEquals(8, player2.getMoney());
		
		playsManager.startTurn(player1);
		playsManager.play(new ChangeCard(CardType.BOOM));
		playsManager.finishTurn();
		assertEquals(4, player1.getMoney());
	}
	
	@Test
	public void dineroIlimitado2() throws GameException, GameWarning {
		
		//............................ |0 |...01 |..02 |..03 |..04 |..05 |..06 ...|07 .|08|
		String[][] TABLE_VALUES2 = { { "__", "__", "__", "1$", "__", "__", "__", "__", "__" },
									 { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
									 { "__", "**", "**", "**", "**", "**", "**", "**", "M_" },
									 { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
									 { "__", "__", "__", "__", "__", "3$", "3$", "__", "__" } };

		//............................|0....01....02...03|..04|..05|..06..|07..|08|
		String[][] playerChairs2 = { { "V", "V", "V", "B", "V", "V", "V", "V", "V" },
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" }, 
									 { "V", "*", "*", "*", "*", "*", "*", "*", "B" },
									 { "V", "*", "*", "*", "*", "*", "*", "*", "V" }, 
									 { "V", "V", "V", "V", "V", "R", "R", "V", "V" } };
		
		TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
		gameTable = new GameTable(tableSeats, 13);
		playsManager = new PlaysManager(converter.toCharacterArray(playerChairs2), gameTable, players);
		
		playsManager.startTurn(player1);
		playsManager.play(new ChangeCard(CardType.BOOM));
		playsManager.finishTurn();
		assertEquals(2, player1.getMoney());
		
		playsManager.startTurn(player2);
		playsManager.play(new ChangeCard(CardType.BOOM));
		playsManager.finishTurn();
		assertEquals(4, player2.getMoney());
		
		playsManager.startTurn(player1);
		playsManager.play(new ChangeCard(CardType.BOOM));
		playsManager.finishTurn();
		assertEquals(4, player1.getMoney());
		
		playsManager.startTurn(player2);
		playsManager.play(new ChangeCard(CardType.BOOM));
		playsManager.finishTurn();
		assertEquals(8, player2.getMoney());
		
		playsManager.startTurn(player1);
		playsManager.play(new ChangeCard(CardType.BOOM));
		playsManager.finishTurn();
		assertEquals(5, player1.getMoney());
	}
}
