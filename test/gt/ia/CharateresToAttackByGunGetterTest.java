package gt.ia;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import gm.Card;
import gm.GameTable;
import gm.Player;
import gm.TableSeat;
import gm.exceptions.GameException;
import gm.exceptions.GameWarning;
import gm.ia.getters.CharateresToAttackByGunGetter;
import gm.info.CardType;
import gm.pojos.Position;
import gt.extras.Converter;

public class CharateresToAttackByGunGetterTest {

	private String B = "B";

	private String R = "R";

	// |0 |01 |02 |03 |04 |05 |06 |07 |08|
	private String[][] playerChairs = { { "R", "R", "R", "V", "R", "RP", "R", "R", "B" },
			{ "R", "*", "*", "*", "*", "*", "*", "*", "B" }, { "BP", "*", "*", "*", "*", "*", "*", "*", "R" },
			{ "R", "*", "*", "*", "*", "*", "*", "*", "B" }, { "B", "R", "B", "B", "BP", "B", "B", "B", "R" } };

	// |0 |01 |02 |03 |04 |05 |06 |07 |08|
	private String[][] TABLE_VALUES = { { "P_", "3$", "k_", "1$", "__", "2$", "P_", "__", "P_" },
			{ "__", "**", "**", "**", "**", "**", "**", "**", "Pk" },
			{ "M_", "**", "**", "**", "**", "**", "**", "**", "P_" },
			{ "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
			{ "k_", "__", "2$", "P_", "3$", "__", "1$", "_k", "k_" } };

	private CharateresToAttackByGunGetter getter;
	private Converter converter;
	
	private Player playerB;
	
	private Player playerR;

	@Before
	public void setUp() throws Exception {
		converter = new Converter(9, 5);
		TableSeat[][] tableSeats = converter.to(TABLE_VALUES);
		GameTable gameTable = new GameTable(tableSeats);
		getter = new CharateresToAttackByGunGetter(gameTable);
		playerB = new Player(B, new ArrayList<CardType>());
		playerR = new Player(R, new ArrayList<CardType>());
	}

	@Test
	public void abajo() throws GameException, GameWarning {
		Position position = getter.getMyAttackPosition(converter.toCharacterArray(playerChairs), new Position(4, 4), playerB);
		assertEquals(new Position(4, 0).toString(), position.toString());
	}

	@Test
	public void abajo3() throws GameException, GameWarning {
		Position position = getter.getMyAttackPosition(converter.toCharacterArray(playerChairs), new Position(5, 4), playerB);
		assertNull(position);
	}

	@Test
	public void abajo2() throws GameException, GameWarning {
		Position position = getter.getMyAttackPosition(converter.toCharacterArray(playerChairs), new Position(3, 4), playerB);
		assertNull(position);
	}

	@Test
	public void arriba() throws GameException, GameWarning {
		Position position = getter.getMyAttackPosition(converter.toCharacterArray(playerChairs), new Position(5, 0), playerR);
		assertEquals(new Position(5, 4).toString(), position.toString());
	}

	@Test
	public void ladoDerecho() throws GameException, GameWarning {
		Position position = getter.getMyAttackPosition(converter.toCharacterArray(playerChairs), new Position(0, 2), playerB);
		assertEquals(new Position(8, 2).toString(), position.toString());
	}

	@Test
	public void ladoIzq() throws GameException, GameWarning {
		Position position = getter.getMyAttackPosition(converter.toCharacterArray(playerChairs), new Position(8, 2), playerR);
		assertEquals(new Position(0, 2).toString(), position.toString());
	}

	@Test
	public void dorminosNo() {
		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] playerChairs2 = { { "R", "R", "R", "V", "R", "RP", "R", "R", "B" },
				{ "R", "*", "*", "*", "*", "*", "*", "*", "B" }, { "BP", "*", "*", "*", "*", "*", "*", "*", "RZ" },
				{ "R", "*", "*", "*", "*", "*", "*", "*", "B" }, { "B", "R", "B", "B", "BP", "B", "B", "B", "R" } };

		// |0 |01 |02 |03 |04 |05 |06 |07 |08|
		String[][] TABLE_VALUES2 = { { "P_", "3$", "k_", "1$", "__", "2$", "P_", "__", "P_" },
				{ "__", "**", "**", "**", "**", "**", "**", "**", "Pk" },
				{ "M_", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
				{ "k_", "__", "2$", "P_", "3$", "__", "1$", "_k", "k_" } };

		TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
		GameTable gameTable = new GameTable(tableSeats);
		getter = new CharateresToAttackByGunGetter(gameTable);
		assertNull(getter.getMyAttackPosition(converter.toCharacterArray(playerChairs2), new Position(8, 2), playerR));
	}

	@Test
	public void esquinaInferiorIzq() throws GameException, GameWarning {
		Position position = getter.getMyAttackPosition(converter.toCharacterArray(playerChairs), new Position(0, 4), playerB);
		assertNull(position);
	}

	@Test
	public void esquinaInferiorDerecha() throws GameException, GameWarning {
		Position position = getter.getMyAttackPosition(converter.toCharacterArray(playerChairs), new Position(8, 4), playerR);
		assertNull(position);
	}

	@Test
	public void esquinaSuperiorDerecha() throws GameException, GameWarning {
		Position position = getter.getMyAttackPosition(converter.toCharacterArray(playerChairs), new Position(8, 0), playerB);
		assertNull(position);
	}

	@Test
	public void esquinaSuperiorIzquierda() throws GameException, GameWarning {
		Position position = getter.getMyAttackPosition(converter.toCharacterArray(playerChairs), new Position(0, 0), playerR);
		assertNull(position);
	}

}
