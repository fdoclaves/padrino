package gt.ia.getters;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import gm.GameTable;
import gm.Player;
import gm.TableSeat;
import gm.exceptions.GameException;
import gm.exceptions.GameWarning;
import gm.ia.getters.CharateresToAttackByKnifeGetter;
import gm.info.CardType;
import gm.pojos.Position;
import gt.extras.Converter;

public class CharateresToAttackByKnifeGetterTest {

	private static final int TOTAL_MONEY = 100;

	// |0 |01 |02 |03 |04 |05 |06 |07 |08|
	private String[][] playerChairs = { { "Ak", "Rk", "B", "R", "R", "R", "R", "R", "Bk" },
			{ "R", "*", "*", "*", "*", "*", "*", "*", "B" }, { "Bk", "*", "*", "*", "*", "*", "*", "*", "R" },
			{ "R", "*", "*", "*", "*", "*", "*", "*", "B" }, { "B", "R", "B", "B", "B", "B", "B", "B", "R" } };

	// |0 |01 |02 |03 |04 |05 |06 |07 |08|
	private String[][] TABLE_VALUES = { { "P_", "3$", "k_", "1$", "__", "2$", "P_", "__", "P_" },
			{ "__", "**", "**", "**", "**", "**", "**", "**", "Pk" },
			{ "M_", "**", "**", "**", "**", "**", "**", "**", "P_" },
			{ "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
			{ "k_", "__", "2$", "P_", "3$", "__", "1$", "_k", "k_" } };

	private CharateresToAttackByKnifeGetter getter;

	private Converter converter;
	
	private Player playerB,playerR,playerA;

	@Before
	public void setUp() throws Exception {
		converter = new Converter(9, 5);
		TableSeat[][] tableSeats = converter.to(TABLE_VALUES);
		GameTable gameTable = new GameTable(tableSeats, TOTAL_MONEY);
		getter = new CharateresToAttackByKnifeGetter(gameTable);
		playerB = new Player("B", new ArrayList<CardType>());
		playerR = new Player("R", new ArrayList<CardType>());
		playerA = new Player("A", new ArrayList<CardType>());
	}

	@Test
	public void abajo() throws GameException, GameWarning {
		List<Position> positionsList = getter.getMyAttackPositions(converter.toCharacterArray(playerChairs),
				new Position(7, 4), playerB);
		assertEquals(1, positionsList.size());
		assertEquals(new Position(8, 4).toString(), positionsList.get(0).toString());
	}

	@Test
	public void abajo2() throws GameException, GameWarning {
		List<Position> positionsList = getter.getMyAttackPositions(converter.toCharacterArray(playerChairs),
				new Position(1, 4), playerR);
		assertEquals(0, positionsList.size());
	}

	@Test
	public void arriba() throws GameException, GameWarning {
		List<Position> positionsList = getter.getMyAttackPositions(converter.toCharacterArray(playerChairs),
				new Position(1, 0), playerR);
		assertEquals(2, positionsList.size());
		assertEquals(new Position(2, 0).toString(), positionsList.get(0).toString());
		assertEquals(new Position(0, 0).toString(), positionsList.get(1).toString());
	}

	@Test
	public void ladoDerecho() throws GameException, GameWarning {
		List<Position> positionsList = getter.getMyAttackPositions(converter.toCharacterArray(playerChairs),
				new Position(8, 1), playerB);
		assertEquals(1, positionsList.size());
		assertEquals(new Position(8, 2).toString(), positionsList.get(0).toString());
	}

	@Test
	public void ladoIzq() throws GameException, GameWarning {
		List<Position> positionsList = getter.getMyAttackPositions(converter.toCharacterArray(playerChairs),
				new Position(0, 2), playerB);
		assertEquals(2, positionsList.size());
		assertEquals(new Position(0, 3).toString(), positionsList.get(0).toString());
		assertEquals(new Position(0, 1).toString(), positionsList.get(1).toString());
	}

	@Test
	public void esquinaInferiorIzq() throws GameException, GameWarning {
		List<Position> positionsList = getter.getMyAttackPositions(converter.toCharacterArray(playerChairs),
				new Position(0, 4), playerB);
		assertEquals(2, positionsList.size());
		assertEquals(new Position(1, 4).toString(), positionsList.get(0).toString());
		assertEquals(new Position(0, 3).toString(), positionsList.get(1).toString());
	}

	@Test
	public void esquinaInferiorDerecha() throws GameException, GameWarning {
		List<Position> positionsList = getter.getMyAttackPositions(converter.toCharacterArray(playerChairs),
				new Position(8, 4), playerR);
		assertEquals(2, positionsList.size());
		assertEquals(new Position(7, 4).toString(), positionsList.get(0).toString());
		assertEquals(new Position(8, 3).toString(), positionsList.get(1).toString());
	}

	@Test
	public void esquinaSuperiorDerecha() throws GameException, GameWarning {
		List<Position> positionsList = getter.getMyAttackPositions(converter.toCharacterArray(playerChairs),
				new Position(8, 0), playerB);
		assertEquals(1, positionsList.size());
		assertEquals(new Position(7, 0).toString(), positionsList.get(0).toString());
	}

	@Test
	public void esquinaSuperiorIzquierda() throws GameException, GameWarning {
		List<Position> positionsList = getter.getMyAttackPositions(converter.toCharacterArray(playerChairs),
				new Position(0, 0), playerA);
		assertEquals(2, positionsList.size());
		assertEquals(new Position(1, 0).toString(), positionsList.get(0).toString());
		assertEquals(new Position(0, 1).toString(), positionsList.get(1).toString());
	}

	@Test
	public void dormidosNo() {
		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] playerChairs2 = { { "AZk", "Rk", "B", "R", "R", "R", "R", "R", "Bk" },
				{ "R", "*", "*", "*", "*", "*", "*", "*", "B" }, { "Bk", "*", "*", "*", "*", "*", "*", "*", "R" },
				{ "R", "*", "*", "*", "*", "*", "*", "*", "B" }, { "B", "R", "B", "B", "B", "B", "B", "B", "R" } };

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] TABLE_VALUES2 = { { "P_", "3$", "k_", "1$", "__", "2$", "P_", "__", "P_" },
				{ "__", "**", "**", "**", "**", "**", "**", "**", "Pk" },
				{ "M_", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "k_", "__", "2$", "P_", "3$", "__", "1$", "_k", "k_" } };

		TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
		GameTable gameTable = new GameTable(tableSeats, TOTAL_MONEY);
		getter = new CharateresToAttackByKnifeGetter(gameTable);
		List<Position> positionsList = getter.getMyAttackPositions(converter.toCharacterArray(playerChairs2),
				new Position(0, 0), playerA);
		assertEquals(0, positionsList.size());
	}

}
