package gt.ia.getters;

import static org.junit.Assert.assertTrue;

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
import gm.ia.setters.DataCakeSetter;
import gm.ia.setters.IaComponentsSetter;
import gm.info.CardType;
import gm.pojos.Position;
import gt.extras.Converter;

public class CakeGetterTest {
	
	private static final int TOTAL_MONEY = 100;

	private Converter converter;
	
	private GameTable gameTable;
	
	private Player playerB;
	
	private Player playerR;

	@Before
	public void setUp() throws Exception {
		List<CardType> cardsB = new ArrayList<CardType>();
		cardsB.add(CardType.CAKE);
		playerB = new Player("B", cardsB);
		List<CardType> cardsA = new ArrayList<CardType>();
		cardsA.add(CardType.CAKE);
		playerR = new Player("R", cardsA);
		converter = new Converter(9, 5);
		//........................... |0... |01.. |02.. |03.. |04 ..|05. |06 ...|07... |08|
		String[][] TABLE_VALUES = { { "k_", "__", "__", "__", "__", "__", "P_", "__", "k_" },
									{ "k_", "**", "**", "**", "**", "**", "**", "**", "k_" },
									{ "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
									{ "k_", "**", "**", "**", "**", "**", "**", "**", "k_" },
									{ "k_", "__", "__", "P_", "__", "_k", "__", "__", "k_" } };
		TableSeat[][] tableSeats = converter.to(TABLE_VALUES);
		gameTable = new GameTable(tableSeats, TOTAL_MONEY);
		
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
		new DataCakeSetter(characterArray, gameTable, playerB, "R");
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
		new DataCakeSetter(characterArray, gameTable, playerB, "R");
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
		new DataCakeSetter(characterArray, gameTable, playerB, "R");
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
		new DataCakeSetter(characterArray, gameTable, playerB, "R");
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
		new DataCakeSetter(characterArray, gameTable, playerB, "R");
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
		new DataCakeSetter(characterArray, gameTable, playerB, "R");
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
		new DataCakeSetter(characterArray, gameTable, playerB, "R");
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
		new DataCakeSetter(characterArray, gameTable, playerB, "R");
		CakeUtils cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
		CakeGetter cakeGetter= new CakeGetter(cakeUtils, characterArray, playerB.getTeam());
		Position position = cakeGetter.getBestPosition().getExplotedPosition();
		assertTrue(""+position, position.isEquals(new Position(8, 2)));
	}
	
	   @Test
	    public void calcularNextMoveCake() {

	        // ..........................|.0 ..|01.. |02.. |03.. |04 ..|05.. |06.. |07.. |08|
	        String[][] playerChairs = { { "V", "B", "R_", "VV", "VV", "VV", "VV", "B_", "VV" },
	                                    { "V", "**", "**", "**", "**", "**", "**", "**", "RK" },
	                                    { "B", "**", "**", "**", "**", "**", "**", "**", "R_" },
	                                    { "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
	                                    { "R", "VV", "VV", "B_", "VV", "R_", "VV", "VV", "VV" } };
	        GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
	        new IaComponentsSetter(gameTable, characterArray, playerB, 3);
	        new DataCakeSetter(characterArray, gameTable, playerB, "R");
	        CakeUtils cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
	        CakeGetter cakeGetter= new CakeGetter(cakeUtils, characterArray, playerB.getTeam());
	        Position position = cakeGetter.getBestPosition().getExplotedPosition();
	        assertTrue(""+position, position.isEquals(new Position(8, 2)));
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
		new DataCakeSetter(characterArray, gameTable, playerB, "R");
		CakeUtils cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
		CakeGetter cakeGetter= new CakeGetter(cakeUtils, characterArray, playerB.getTeam());
		Position position = cakeGetter.getBestPosition().getExplotedPosition();
		assertTrue(""+position, position.isEquals(new Position(1, 4)));
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
		new DataCakeSetter(characterArray, gameTable, playerB, "R");
		CakeUtils cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
		CakeGetter cakeGetter= new CakeGetter(cakeUtils, characterArray, playerB.getTeam());
		Position position = cakeGetter.getBestPosition().getExplotedPosition();
		assertTrue(""+position, position.isEquals(new Position(3, 4)));
	}
	
	@Test
	public void testSimulador() {
		Converter converter = new Converter(9, 3);
		//........................... |0... |01.. |02.. |03.. |04 ..|05. |06 ...|07... |08|
		String[][] TABLE_VALUES2 = { { "PG", "_G", "kG", "1$", "__", "__", "P_", "__", "P_" },
									{ "2$", "**", "**", "**", "**", "**", "**", "**", "GP" },
									{ "kG", "__", "__", "GP", "__", "GP", "3$", "__", "k_" } };
		
		// ..........................|.0 ..|01.. |02.. |03.. |04 ..|05.. |06.. |07.. |08|
		String[][] playerChairs = { { "B", "VV", "R_", "VV", "VV", "VV", "B_", "BP", "VV" },
									{ "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
									{ "R", "B_", "VV", "VV", "VV", "N_", "R_", "R_", "R_" } };
		
		TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
		gameTable = new GameTable(tableSeats, TOTAL_MONEY);
		GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
		new IaComponentsSetter(gameTable, characterArray, playerR, 3);
		new DataCakeSetter(characterArray, gameTable, playerR, "N");
		CakeUtils cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
		CakeGetter cakeGetter= new CakeGetter(cakeUtils, characterArray, playerR.getTeam());
		Position position = cakeGetter.getBestPosition().getExplotedPosition();
		assertTrue(""+position, position.isEquals(new Position(6, 0)));
	}
	
	
	@Test
	public void moreEnemies() {
		//........................... |0... |01.. |02.. |03.. |04 ..|05. |06 ...|07... |08|
		String[][] TABLE_VALUES2 = { { "PG", "_G", "kG", "1$", "__", "__", "P_", "__", "P_" },
									{ "2$", "**", "**", "**", "**", "**", "**", "**", "GP" },
									{ "2$", "**", "**", "**", "**", "**", "**", "**", "GP" },
									{ "2$", "**", "**", "**", "**", "**", "**", "**", "GP" },
									{ "kG", "__", "__", "GP", "__", "GP", "3$", "__", "k_" } };

		// ..........................|.0 ..|01.. |02.. |03.. |04 ..|05.. |06.. |07.. |08|
		String[][] playerChairs = { { "VV", "VV", "VV", "Bk", "Bk", "BP", "VV", "VV", "VV" },
									{ "V", "**", "**", "**", "**", "**", "**", "**", "R_" },
									{ "V", "**", "**", "**", "**", "**", "**", "**", "R_" },
									{ "V", "**", "**", "**", "**", "**", "**", "**", "R_" },
									{ "R_", "B_", "VV", "VV", "VV", "N_", "R_", "R", "R_" } };
		
		TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
		gameTable = new GameTable(tableSeats, TOTAL_MONEY);
		GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
		new IaComponentsSetter(gameTable, characterArray, playerR, 3);
		new DataCakeSetter(characterArray, gameTable, playerR, "N");
		CakeUtils cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
		CakeGetter cakeGetter= new CakeGetter(cakeUtils, characterArray, playerR.getTeam());
		Position position = cakeGetter.getBestPosition().getExplotedPosition();
		assertTrue(""+position, position.isEquals(new Position(4, 0)));
	}
	
	@Test
    public void moreEnemies2() {
        //........................... |0... |01.. |02.. |03.. |04 ..|05. |06 ...|07... |08|
        String[][] TABLE_VALUES2 = { { "PG", "_G", "kG", "1$", "__", "__", "P_", "__", "P_" },
                                    { "2$", "**", "**", "**", "**", "**", "**", "**", "GP" },
                                    { "2$", "**", "**", "**", "**", "**", "**", "**", "GP" },
                                    { "2$", "**", "**", "**", "**", "**", "**", "**", "GP" },
                                    { "kG", "__", "__", "GP", "__", "GP", "3$", "__", "k_" } };

        // ..........................|.0 ..|01.. |02.. |03.. |04 ..|05.. |06.. |07.. |08|
        String[][] playerChairs = { { "VV", "VV", "VV", "BP", "VV", "VV", "VV", "VV", "VV" },
                                    { "V", "**", "**", "**", "**", "**", "**", "**", "R_" },
                                    { "V", "**", "**", "**", "**", "**", "**", "**", "R_" },
                                    { "B", "**", "**", "**", "**", "**", "**", "**", "R_" },
                                    { "B", "B_", "VV", "R_", "VV", "N_", "VV", "VV", "R_" } };
        
        TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
        gameTable = new GameTable(tableSeats, TOTAL_MONEY);
        GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
        new IaComponentsSetter(gameTable, characterArray, playerR, 3);
        new DataCakeSetter(characterArray, gameTable, playerR, "N");
        CakeUtils cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
        CakeGetter cakeGetter= new CakeGetter(cakeUtils, characterArray, playerR.getTeam());
        Position position = cakeGetter.getBestPosition().getExplotedPosition();
        assertTrue(""+position, position.isEquals(new Position(0, 4)));
    }
	

}
