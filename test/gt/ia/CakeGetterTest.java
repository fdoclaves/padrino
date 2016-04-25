package gt.ia;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import gm.Cake;
import gm.GameCharacter;
import gm.GameTable;
import gm.Player;
import gm.TableSeat;
import gm.cards.CakeUtils;
import gm.ia.getters.CakeGetter;
import gm.ia.getters.DataCakeGetter;
import gm.ia.getters.IaComponentsSetter;
import gm.info.CardType;
import gm.pojos.Position;
import gt.extras.Converter;

public class CakeGetterTest {
	
	private Converter converter;
	
	private GameTable gameTable;
	
	private Player playerB;
	
	private Player playerA;

	@Before
	public void setUp() throws Exception {
		List<CardType> cardsB = new ArrayList<CardType>();
		cardsB.add(CardType.CAKE);
		playerB = new Player("B", cardsB);
		List<CardType> cardsA = new ArrayList<CardType>();
		cardsA.add(CardType.CAKE);
		playerA = new Player("R", cardsA);
		converter = new Converter(9, 5);
		//........................... |0... |01.. |02.. |03.. |04 ..|05. |06 ...|07... |08|
		String[][] TABLE_VALUES = { { "k_", "__", "__", "__", "__", "__", "P_", "__", "k_" },
									{ "k_", "**", "**", "**", "**", "**", "**", "**", "k_" },
									{ "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
									{ "k_", "**", "**", "**", "**", "**", "**", "**", "k_" },
									{ "k_", "__", "__", "P_", "__", "_k", "__", "__", "k_" } };
		TableSeat[][] tableSeats = converter.to(TABLE_VALUES);
		gameTable = new GameTable(tableSeats);
		
	}

	@Test
	public void putCakeWhereThereIsEnemy() {

		// ..........................|.0 ..|01.. |02.. |03.. |04 ..|05.. |06.. |07.. |08|
		String[][] playerChairs = { { "V", "V_", "VV", "R_", "VV", "VV", "VV", "VV", "VV" },
									{ "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
									{ "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
									{ "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
									{ "V", "V_", "VV", "B_", "VV", "VV", "VV", "VV", "VV" } };
		GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
		new IaComponentsSetter(gameTable, characterArray, playerB, 3);
		new DataCakeGetter(characterArray, gameTable, playerB, "R");
		CakeUtils cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
		CakeGetter cakeGetter= new CakeGetter(cakeUtils, characterArray, playerB.getTeam());
		Position position = cakeGetter.getBestPosition().getExplotedPosition();
		assertTrue(""+position, position.isEquals(new Position(2, 0)));
	}
	
	@Test
	public void putCakeWhereThereIsEnemyYICDontStayHere() {

		// ..........................|.0 ..|01.. |02.. |03.. |04 ..|05.. |06.. |07.. |08|
		String[][] playerChairs = { { "B", "R_", "VV", "R_", "VV", "VV", "VV", "VV", "VV" },
									{ "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
									{ "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
									{ "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
									{ "V", "V_", "VV", "B_", "VV", "VV", "VV", "VV", "VV" } };
		GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
		new IaComponentsSetter(gameTable, characterArray, playerB, 3);
		new DataCakeGetter(characterArray, gameTable, playerB, "R");
		CakeUtils cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
		CakeGetter cakeGetter= new CakeGetter(cakeUtils, characterArray, playerB.getTeam());
		Position position = cakeGetter.getBestPosition().getExplotedPosition();
		assertTrue(""+position, position.isEquals(new Position(2, 0)));
	}
	
	@Test
	public void putCakeWhereThereIsMoreEnemiesyYICDontStayHere() {

		// ..........................|.0 ..|01.. |02.. |03.. |04 ..|05.. |06.. |07.. |08|
		String[][] playerChairs = { { "B", "R_", "VV", "R_", "R_", "VV", "VV", "VV", "VV" },
									{ "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
									{ "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
									{ "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
									{ "V", "V_", "VV", "B_", "VV", "VV", "VV", "VV", "VV" } };
		GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
		new IaComponentsSetter(gameTable, characterArray, playerB, 3);
		new DataCakeGetter(characterArray, gameTable, playerB, "R");
		CakeUtils cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
		CakeGetter cakeGetter= new CakeGetter(cakeUtils, characterArray, playerB.getTeam());
		Position position = cakeGetter.getBestPosition().getExplotedPosition();
		assertTrue(""+position, position.isEquals(new Position(2, 0)));
	}
	
	@Test
	public void putCakeWhereThereIsMoreEnemiesyYICDontStayHere2() {

		// ..........................|.0 ..|01.. |02.. |03.. |04 ..|05.. |06.. |07.. |08|
		String[][] playerChairs = { { "B", "R_", "R_", "VV", "R_", "VV", "VV", "VV", "VV" },
									{ "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
									{ "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
									{ "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
									{ "V", "V_", "VV", "B_", "VV", "VV", "VV", "VV", "VV" } };
		GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
		new IaComponentsSetter(gameTable, characterArray, playerB, 3);
		new DataCakeGetter(characterArray, gameTable, playerB, "R");
		CakeUtils cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
		CakeGetter cakeGetter= new CakeGetter(cakeUtils, characterArray, playerB.getTeam());
		Position position = cakeGetter.getBestPosition().getExplotedPosition();
		assertTrue(""+position, position.isEquals(new Position(2, 0)));
	}
	
	
	@Test
	public void putCakeWhereThereIsMoreEnemiesyYICDontStayHere3() {

		// ..........................|.0 ..|01.. |02.. |03.. |04 ..|05.. |06.. |07.. |08|
		String[][] playerChairs = { { "B", "R_", "R_", "VV", "R_", "VV", "VV", "VV", "VV" },
									{ "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
									{ "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
									{ "V", "**", "**", "**", "**", "**", "**", "**", "N_" },
									{ "V", "V_", "VV", "B_", "VV", "VV", "VV", "R_", "R_" } };
		GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
		new IaComponentsSetter(gameTable, characterArray, playerB, 3);
		new DataCakeGetter(characterArray, gameTable, playerB, "R");
		CakeUtils cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
		CakeGetter cakeGetter= new CakeGetter(cakeUtils, characterArray, playerB.getTeam());
		Position position = cakeGetter.getBestPosition().getExplotedPosition();
		assertTrue(""+position, position.isEquals(new Position(8, 4)));
	}
	
	@Test
	public void putCakeWhereThereIsMoreEnemiesAndPowestAndIDontStayHere() {

		// ..........................|.0 ..|01.. |02.. |03.. |04 ..|05.. |06.. |07.. |08|
		String[][] playerChairs = { { "B", "B", "R_", "VV", "R_", "VV", "VV", "VV", "VV" },
									{ "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
									{ "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
									{ "N", "**", "**", "**", "**", "**", "**", "**", "N_" },
									{ "R", "RP", "VV", "B_", "VV", "VV", "VV", "R_", "R_" } };
		GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
		new IaComponentsSetter(gameTable, characterArray, playerB, 3);
		new DataCakeGetter(characterArray, gameTable, playerB, "R");
		CakeUtils cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
		CakeGetter cakeGetter= new CakeGetter(cakeUtils, characterArray, playerB.getTeam());
		Position position = cakeGetter.getBestPosition().getExplotedPosition();
		assertTrue(""+position, position.isEquals(new Position(0, 4)));
	}
	
	@Test
	public void putCakeWhereThereIsMoreEnemiesAndPowestAndIDontStayHere2() {

		// ..........................|.0 ..|01.. |02.. |03.. |04 ..|05.. |06.. |07.. |08|
		String[][] playerChairs = { { "B", "B", "R_", "VV", "R_", "VV", "VV", "B_", "VV" },
									{ "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
									{ "V", "**", "**", "**", "**", "**", "**", "**", "B_" },
									{ "N", "**", "**", "**", "**", "**", "**", "**", "Nk" },
									{ "R", "RP", "VV", "B_", "VV", "VV", "VV", "RP", "R_" } };
		GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
		new IaComponentsSetter(gameTable, characterArray, playerB, 3);
		new DataCakeGetter(characterArray, gameTable, playerB, "R");
		CakeUtils cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
		CakeGetter cakeGetter= new CakeGetter(cakeUtils, characterArray, playerB.getTeam());
		Position position = cakeGetter.getBestPosition().getExplotedPosition();
		assertTrue(""+position, position.isEquals(new Position(8, 4)));
	}
	
	@Test
	public void putCakeWhereThereIsMoreEnemiesAndPowestAndIDontStayHere3() {

		// ..........................|.0 ..|01.. |02.. |03.. |04 ..|05.. |06.. |07.. |08|
		String[][] playerChairs = { { "V", "B", "R_", "VV", "VV", "VV", "VV", "B_", "VV" },
									{ "V", "**", "**", "**", "**", "**", "**", "**", "RK" },
									{ "B", "**", "**", "**", "**", "**", "**", "**", "VV" },
									{ "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
									{ "R", "VV", "VV", "B_", "VV", "VV", "VV", "VV", "VV" } };
		GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
		new IaComponentsSetter(gameTable, characterArray, playerB, 3);
		new DataCakeGetter(characterArray, gameTable, playerB, "R");
		CakeUtils cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
		CakeGetter cakeGetter= new CakeGetter(cakeUtils, characterArray, playerB.getTeam());
		Position position = cakeGetter.getBestPosition().getExplotedPosition();
		assertTrue(""+position, position.isEquals(new Position(8, 1)));
	}
	
	@Test
	public void notToPutIfThereIsACakeHere() {

		// ..........................|.0 ..|01.. |02.. |03.. |04 ..|05.. |06.. |07.. |08|
		String[][] playerChairs = { { "V", "B", "R_", "VV", "VV", "VV", "VV", "B_", "VV" },
									{ "V", "**", "**", "**", "**", "**", "**", "**", "RK" },
									{ "B", "**", "**", "**", "**", "**", "**", "**", "VV" },
									{ "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
									{ "R", "VV", "VV", "B_", "VV", "VV", "VV", "VV", "VV" } };
		GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
		gameTable.add(new Cake(new Position(8, 2), "N", gameTable));
		new IaComponentsSetter(gameTable, characterArray, playerB, 3);
		new DataCakeGetter(characterArray, gameTable, playerB, "R");
		CakeUtils cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
		CakeGetter cakeGetter= new CakeGetter(cakeUtils, characterArray, playerB.getTeam());
		Position position = cakeGetter.getBestPosition().getExplotedPosition();
		assertTrue(""+position, position.isEquals(new Position(0, 4)));
	}
	
	@Test
	public void cuandoPeorEsNadaEsNulo() {

		// ..........................|.0 ..|01.. |02.. |03.. |04 ..|05.. |06.. |07.. |08|
		String[][] playerChairs = { { "B", "BP", "VV", "VV", "VV", "VV", "VV", "VV", "VV" },
									{ "B", "**", "**", "**", "**", "**", "**", "**", "VV" },
									{ "B", "**", "**", "**", "**", "**", "**", "**", "VV" },
									{ "B", "**", "**", "**", "**", "**", "**", "**", "VV" },
									{ "V", "B_", "R_", "VV", "VV", "N_", "VV", "VV", "VV" } };
		GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
		gameTable.add(new Cake(new Position(8, 2), "N", gameTable));
		new IaComponentsSetter(gameTable, characterArray, playerB, 3);
		new DataCakeGetter(characterArray, gameTable, playerB, "R");
		CakeUtils cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
		CakeGetter cakeGetter= new CakeGetter(cakeUtils, characterArray, playerB.getTeam());
		Position position = cakeGetter.getBestPosition().getExplotedPosition();
		assertTrue(""+position, position.isEquals(new Position(3, 4)));
	}
	
	@Test
	public void testSimulador() {

		// ..........................|.0 ..|01.. |02.. |03.. |04 ..|05.. |06.. |07.. |08|
		String[][] playerChairs = { { "B", "VV", "R_", "VV", "VV", "VV", "B_", "BP", "VV" },
									{ "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
									{ "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
									{ "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
									{ "R", "B_", "VV", "VV", "VV", "N_", "R_", "R_", "R_" } };
		GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
		new IaComponentsSetter(gameTable, characterArray, playerA, 3);
		new DataCakeGetter(characterArray, gameTable, playerA, "N");
		CakeUtils cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
		CakeGetter cakeGetter= new CakeGetter(cakeUtils, characterArray, playerA.getTeam());
		Position position = cakeGetter.getBestPosition().getExplotedPosition();
		assertTrue(""+position, position.isEquals(new Position(6, 0)));
	}
	

}
