package gt.ia.getters;

import static org.junit.Assert.assertNull;
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
import gm.ia.getters.MoverCakeGetter;
import gm.ia.getters.WhereMoveGetter;
import gm.ia.setters.DataCakeSetter;
import gm.ia.setters.IaComponentsSetter;
import gm.info.CardType;
import gm.pojos.Position;
import gt.extras.Converter;


public class WhereMoveGetterTest {

private Converter converter;
    
    private GameTable gameTable;
    
    private Player playerB;
    
    private static final int TOTAL_MONEY = 100;
    
    private Position moveData = new Position(3, 4);

    @Before
    public void setUp() throws Exception {
        List<CardType> cardsB = new ArrayList<CardType>();
        cardsB.add(CardType.CAKE);
        cardsB.add(CardType.GUN);
        playerB = new Player("B", cardsB);
        List<CardType> cardsA = new ArrayList<CardType>();
        cardsA.add(CardType.CAKE);
        converter = new Converter(9, 5);
        //........................... |0... |01.. |02.. |03.. |04 ..|05. |06 ...|07... |08|
        String[][] TABLE_VALUES = { { "__", "__", "__", "__", "__", "__", "__", "__", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
                                    { "__", "__", "__", "__", "__", "__", "__", "__", "__" } };
        TableSeat[][] tableSeats = converter.to(TABLE_VALUES);
        gameTable = new GameTable(tableSeats, TOTAL_MONEY);
    }


    @Test
    public void whereMove() {
        // ..........................|.0 ..|01.. |02.. |03.. |04 ..|05.. |06.. |07.. |08|
        String[][] playerChairs = { { "N", "N_", "R_", "VV", "R_", "N_", "N_", "B_", "B_" },
                                    { "N", "**", "**", "**", "**", "**", "**", "**", "B_" },
                                    { "N", "**", "**", "**", "**", "**", "**", "**", "B_" },
                                    { "N", "**", "**", "**", "**", "**", "**", "**", "B_" },
                                    { "N", "N_", "R_", "B_", "R_", "N_", "BK", "B_", "B_" } };
        GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
        IaComponentsSetter iaComponentsSetter = new IaComponentsSetter(gameTable, characterArray, playerB, 3);
        new DataCakeSetter(characterArray, gameTable, playerB, "R");
        CakeUtils cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
        new MoverCakeGetter(cakeUtils);
        WhereMoveGetter whereMoveGetter = new WhereMoveGetter(gameTable);
        Position position = whereMoveGetter.whereMove(characterArray, iaComponentsSetter,playerB.getTeam(),moveData);
        assertTrue(""+position, position.isEquals(new Position(3, 0)));
    }
    
    @Test
    public void notWhereMoveWithFatalCake() {
        gameTable.add(new Cake(new Position(3, 0), "R", gameTable));
        // ..........................|.0 ..|01.. |02.. |03.. |04 ..|05.. |06.. |07.. |08|
        String[][] playerChairs = { { "N", "N_", "R_", "VV", "R_", "N_", "B_", "B_", "B_" },
                                    { "N", "**", "**", "**", "**", "**", "**", "**", "B_" },
                                    { "N", "**", "**", "**", "**", "**", "**", "**", "B_" },
                                    { "N", "**", "**", "**", "**", "**", "**", "**", "B_" },
                                    { "N", "N_", "R_", "B_", "R_", "N_", "BK", "B_", "B_" } };
        GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
        IaComponentsSetter iaComponentsSetter = new IaComponentsSetter(gameTable, characterArray, playerB, 3);
        new DataCakeSetter(characterArray, gameTable, playerB, "R");
        CakeUtils cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
        new MoverCakeGetter(cakeUtils);
        WhereMoveGetter whereMoveGetter = new WhereMoveGetter(gameTable);
        Position position = whereMoveGetter.whereMove(characterArray, iaComponentsSetter,playerB.getTeam(),moveData);
        assertNull(position);
    }
    
    @Test
    public void bestWhereMoveWithFatalCake() {
        gameTable.add(new Cake(new Position(3, 0), "R", gameTable));
        // ..........................|.0 ..|01.. |02.. |03.. |04 ..|05.. |06.. |07.. |08|
        String[][] playerChairs = { { "N", "N_", "R_", "VV", "R_", "N_", "VV", "B_", "B_" },
                                    { "N", "**", "**", "**", "**", "**", "**", "**", "B_" },
                                    { "N", "**", "**", "**", "**", "**", "**", "**", "B_" },
                                    { "N", "**", "**", "**", "**", "**", "**", "**", "B_" },
                                    { "N", "N_", "R_", "B_", "R_", "N_", "BK", "B_", "B_" } };
        GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
        IaComponentsSetter iaComponentsSetter = new IaComponentsSetter(gameTable, characterArray, playerB, 3);
        new DataCakeSetter(characterArray, gameTable, playerB, "R");
        CakeUtils cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
        new MoverCakeGetter(cakeUtils);
        WhereMoveGetter whereMoveGetter = new WhereMoveGetter(gameTable);
        Position position = whereMoveGetter.whereMove(characterArray, iaComponentsSetter,playerB.getTeam(),moveData);
        assertTrue(""+position, position.isEquals(new Position(6, 0)));
    }
    
    @Test
    public void bestWhereMoveWithBusiness() {
      //........................... |0... |01.. |02.. |03.. |04 ..|05. |06 ...|07... |08|
        String[][] TABLE_VALUES2 ={ { "__", "__", "__", "__", "__", "__", "1$", "__", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
                                    { "__", "__", "__", "__", "__", "__", "__", "__", "__" } };
        TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
        gameTable = new GameTable(tableSeats, TOTAL_MONEY);
        
        // ..........................|.0 ..|01.. |02.. |03.. |04 ..|05.. |06.. |07.. |08|
        String[][] playerChairs = { { "N", "N_", "R_", "VV", "R_", "N_", "VV", "B_", "B_" },
                                    { "N", "**", "**", "**", "**", "**", "**", "**", "B_" },
                                    { "N", "**", "**", "**", "**", "**", "**", "**", "B_" },
                                    { "N", "**", "**", "**", "**", "**", "**", "**", "B_" },
                                    { "N", "N_", "R_", "B_", "R_", "N_", "BK", "B_", "B_" } };
        GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
        IaComponentsSetter iaComponentsSetter = new IaComponentsSetter(gameTable, characterArray, playerB, 3);
        new DataCakeSetter(characterArray, gameTable, playerB, "R");
        CakeUtils cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
        new MoverCakeGetter(cakeUtils);
        WhereMoveGetter whereMoveGetter = new WhereMoveGetter(gameTable);
        Position position = whereMoveGetter.whereMove(characterArray, iaComponentsSetter,playerB.getTeam(),moveData);
        assertTrue(""+position, position.isEquals(new Position(6, 0)));
    }
    
    @Test
    public void bestWhereMoveWithBusiness2() {
      //........................... |0... |01.. |02.. |03.. |04 ..|05. |06 ...|07... |08|
        String[][] TABLE_VALUES2 ={ { "__", "__", "__", "1$", "__", "__", "__", "__", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
                                    { "__", "__", "__", "__", "__", "__", "__", "__", "__" } };
        TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
        gameTable = new GameTable(tableSeats, TOTAL_MONEY);
        
        // ..........................|.0 ..|01.. |02.. |03.. |04 ..|05.. |06.. |07.. |08|
        String[][] playerChairs = { { "N", "N_", "R_", "VV", "R_", "N_", "VV", "B_", "B_" },
                                    { "N", "**", "**", "**", "**", "**", "**", "**", "B_" },
                                    { "N", "**", "**", "**", "**", "**", "**", "**", "B_" },
                                    { "N", "**", "**", "**", "**", "**", "**", "**", "B_" },
                                    { "N", "N_", "R_", "B_", "R_", "N_", "BK", "B_", "B_" } };
        GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
        IaComponentsSetter iaComponentsSetter = new IaComponentsSetter(gameTable, characterArray, playerB, 3);
        new DataCakeSetter(characterArray, gameTable, playerB, "R");
        CakeUtils cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
        new MoverCakeGetter(cakeUtils);
        WhereMoveGetter whereMoveGetter = new WhereMoveGetter(gameTable);
        Position position = whereMoveGetter.whereMove(characterArray, iaComponentsSetter,playerB.getTeam(),moveData);
        assertTrue(""+position, position.isEquals(new Position(3, 0)));
    }
    
    @Test
    public void bestWhereMoveWithCompleteMonopoly() {
      //........................... |0... |01.. |02.. |03.. |04 ..|05. |06 ...|07... |08|
        String[][] TABLE_VALUES2 ={ { "__", "__", "__", "1$", "__", "__", "2$", "__", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "1$" },
                                    { "__", "__", "__", "__", "__", "__", "__", "__", "__" } };
        TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
        gameTable = new GameTable(tableSeats, TOTAL_MONEY);
        
        // ..........................|.0 ..|01.. |02.. |03.. |04 ..|05.. |06.. |07.. |08|
        String[][] playerChairs = { { "N", "N_", "R_", "VV", "R_", "N_", "VV", "B_", "B_" },
                                    { "N", "**", "**", "**", "**", "**", "**", "**", "B_" },
                                    { "N", "**", "**", "**", "**", "**", "**", "**", "B_" },
                                    { "N", "**", "**", "**", "**", "**", "**", "**", "B_" },
                                    { "N", "N_", "R_", "B_", "R_", "N_", "BK", "B_", "B_" } };
        GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
        IaComponentsSetter iaComponentsSetter = new IaComponentsSetter(gameTable, characterArray, playerB, 3);
        new DataCakeSetter(characterArray, gameTable, playerB, "R");
        CakeUtils cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
        new MoverCakeGetter(cakeUtils);
        WhereMoveGetter whereMoveGetter = new WhereMoveGetter(gameTable);
        Position position = whereMoveGetter.whereMove(characterArray, iaComponentsSetter,playerB.getTeam(),moveData);
        assertTrue(""+position, position.isEquals(new Position(3, 0)));
    }
    
    @Test
    public void bestWhereMoveWithGlass() {
      //........................... |0... |01.. |02.. |03.. |04 ..|05. |06 ...|07... |08|
        String[][] TABLE_VALUES2 ={ { "__", "__", "__", "_G", "__", "__", "__", "__", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "1$" },
                                    { "__", "__", "__", "__", "__", "__", "__", "__", "__" } };
        TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
        gameTable = new GameTable(tableSeats, TOTAL_MONEY);
        
        // ..........................|.0 ..|01.. |02.. |03.. |04 ..|05.. |06.. |07.. |08|
        String[][] playerChairs = { { "N", "N_", "R_", "VV", "R_", "N_", "VV", "B_", "B_" },
                                    { "N", "**", "**", "**", "**", "**", "**", "**", "B_" },
                                    { "N", "**", "**", "**", "**", "**", "**", "**", "B_" },
                                    { "N", "**", "**", "**", "**", "**", "**", "**", "B_" },
                                    { "N", "N_", "R_", "B_", "R_", "N_", "BK", "B_", "B_" } };
        GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
        IaComponentsSetter iaComponentsSetter = new IaComponentsSetter(gameTable, characterArray, playerB, 3);
        new DataCakeSetter(characterArray, gameTable, playerB, "R");
        CakeUtils cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
        new MoverCakeGetter(cakeUtils);
        WhereMoveGetter whereMoveGetter = new WhereMoveGetter(gameTable);
        Position position = whereMoveGetter.whereMove(characterArray, iaComponentsSetter,playerB.getTeam(),moveData);
        assertTrue(""+position, position.isEquals(new Position(6, 0)));
    }
    
    @Test
    public void bestWhereMoveWithGlass2() {
      //........................... |0... |01.. |02.. |03.. |04 ..|05. |06 ...|07... |08|
        String[][] TABLE_VALUES2 ={ { "__", "__", "__", "__", "__", "__", "_G", "__", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "1$" },
                                    { "__", "__", "__", "__", "__", "__", "__", "__", "__" } };
        TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
        gameTable = new GameTable(tableSeats, TOTAL_MONEY);
        
        // ..........................|.0 ..|01.. |02.. |03.. |04 ..|05.. |06.. |07.. |08|
        String[][] playerChairs = { { "N", "N_", "R_", "VV", "R_", "N_", "VV", "B_", "B_" },
                                    { "N", "**", "**", "**", "**", "**", "**", "**", "B_" },
                                    { "N", "**", "**", "**", "**", "**", "**", "**", "B_" },
                                    { "N", "**", "**", "**", "**", "**", "**", "**", "B_" },
                                    { "N", "N_", "R_", "B_", "R_", "N_", "BK", "B_", "B_" } };
        GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
        IaComponentsSetter iaComponentsSetter = new IaComponentsSetter(gameTable, characterArray, playerB, 3);
        new DataCakeSetter(characterArray, gameTable, playerB, "R");
        CakeUtils cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
        new MoverCakeGetter(cakeUtils);
        WhereMoveGetter whereMoveGetter = new WhereMoveGetter(gameTable);
        Position position = whereMoveGetter.whereMove(characterArray, iaComponentsSetter,playerB.getTeam(),moveData);
        assertTrue(""+position, position.isEquals(new Position(3, 0)));
    }
    
    @Test
    public void bestWhereMoveWithGun() {
      //........................... |0... |01.. |02.. |03.. |04 ..|05. |06 ...|07... |08|
        String[][] TABLE_VALUES2 ={ { "__", "__", "__", "__", "__", "__", "_P", "__", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "1$" },
                                    { "__", "__", "__", "__", "__", "__", "__", "__", "__" } };
        TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
        gameTable = new GameTable(tableSeats, TOTAL_MONEY);
        
        // ..........................|.0 ..|01.. |02.. |03.. |04 ..|05.. |06.. |07.. |08|
        String[][] playerChairs = { { "N", "N_", "R_", "VV", "R_", "N_", "VV", "B_", "B_" },
                                    { "N", "**", "**", "**", "**", "**", "**", "**", "B_" },
                                    { "N", "**", "**", "**", "**", "**", "**", "**", "B_" },
                                    { "N", "**", "**", "**", "**", "**", "**", "**", "B_" },
                                    { "N", "N_", "R_", "B_", "R_", "N_", "BK", "B_", "B_" } };
        GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
        IaComponentsSetter iaComponentsSetter = new IaComponentsSetter(gameTable, characterArray, playerB, 3);
        new DataCakeSetter(characterArray, gameTable, playerB, "R");
        CakeUtils cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
        new MoverCakeGetter(cakeUtils);
        WhereMoveGetter whereMoveGetter = new WhereMoveGetter(gameTable);
        Position position = whereMoveGetter.whereMove(characterArray, iaComponentsSetter,playerB.getTeam(),moveData);
        assertTrue(""+position, position.isEquals(new Position(6, 0)));
    }
    
    @Test
    public void bestWhereMoveWithKnife() {
      //........................... |0... |01.. |02.. |03.. |04 ..|05. |06 ...|07... |08|
        String[][] TABLE_VALUES2 ={ { "__", "__", "__", "_k", "__", "__", "__", "__", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "1$" },
                                    { "__", "__", "__", "__", "__", "__", "__", "__", "__" } };
        TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
        gameTable = new GameTable(tableSeats, TOTAL_MONEY);
        
        // ..........................|.0 ..|01.. |02.. |03.. |04 ..|05.. |06.. |07.. |08|
        String[][] playerChairs = { { "N", "N_", "R_", "VV", "R_", "N_", "VV", "B_", "B_" },
                                    { "N", "**", "**", "**", "**", "**", "**", "**", "B_" },
                                    { "N", "**", "**", "**", "**", "**", "**", "**", "B_" },
                                    { "N", "**", "**", "**", "**", "**", "**", "**", "B_" },
                                    { "N", "N_", "R_", "B_", "R_", "N_", "BK", "B_", "B_" } };
        GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
        IaComponentsSetter iaComponentsSetter = new IaComponentsSetter(gameTable, characterArray, playerB, 3);
        new DataCakeSetter(characterArray, gameTable, playerB, "R");
        CakeUtils cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
        new MoverCakeGetter(cakeUtils);
        WhereMoveGetter whereMoveGetter = new WhereMoveGetter(gameTable);
        Position position = whereMoveGetter.whereMove(characterArray, iaComponentsSetter,playerB.getTeam(),moveData);
        assertTrue(""+position, position.isEquals(new Position(3, 0)));
    }
    
    @Test
    public void bestWhereMoveWithKnifeVsGun_yaTnPistola() {
      //........................... |0... |01.. |02.. |03.. |04 ..|05. |06 ...|07... |08|
        String[][] TABLE_VALUES2 ={ { "__", "__", "__", "_k", "__", "__", "_P", "__", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "1$" },
                                    { "__", "__", "__", "__", "__", "__", "__", "__", "__" } };
        TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
        gameTable = new GameTable(tableSeats, TOTAL_MONEY);
        
        // ..........................|.0 ..|01.. |02.. |03.. |04 ..|05.. |06.. |07.. |08|
        String[][] playerChairs = { { "N", "N_", "R_", "VV", "R_", "N_", "VV", "B_", "B_" },
                                    { "N", "**", "**", "**", "**", "**", "**", "**", "B_" },
                                    { "N", "**", "**", "**", "**", "**", "**", "**", "B_" },
                                    { "N", "**", "**", "**", "**", "**", "**", "**", "B_" },
                                    { "N", "N_", "R_", "BP", "R_", "N_", "BK", "B_", "B_" } };
        GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
        IaComponentsSetter iaComponentsSetter = new IaComponentsSetter(gameTable, characterArray, playerB, 3);
        new DataCakeSetter(characterArray, gameTable, playerB, "R");
        CakeUtils cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
        new MoverCakeGetter(cakeUtils);
        WhereMoveGetter whereMoveGetter = new WhereMoveGetter(gameTable);
        Position position = whereMoveGetter.whereMove(characterArray, iaComponentsSetter,playerB.getTeam(),moveData);
        assertTrue(""+position, position.isEquals(new Position(3, 0)));
    }
    
    @Test
    public void bestWhereMoveWithKnifeVsGun2_elYaTnCullicho() {
      //........................... |0... |01.. |02.. |03.. |04 ..|05. |06 ...|07... |08|
        String[][] TABLE_VALUES2 ={ { "__", "__", "__", "_k", "__", "__", "_P", "__", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "1$" },
                                    { "__", "__", "__", "__", "__", "__", "__", "__", "__" } };
        TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
        gameTable = new GameTable(tableSeats, TOTAL_MONEY);
        
        // ..........................|.0 ..|01.. |02.. |03.. |04 ..|05.. |06.. |07.. |08|
        String[][] playerChairs = { { "N", "N_", "B_", "VV", "B_", "N_", "VV", "B_", "B_" },
                                    { "N", "**", "**", "**", "**", "**", "**", "**", "B_" },
                                    { "N", "**", "**", "**", "**", "**", "**", "**", "B_" },
                                    { "N", "**", "**", "**", "**", "**", "**", "**", "B_" },
                                    { "N", "N_", "R_", "Bk", "R_", "N_", "BK", "B_", "B_" } };
        GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
        IaComponentsSetter iaComponentsSetter = new IaComponentsSetter(gameTable, characterArray, playerB, 3);
        new DataCakeSetter(characterArray, gameTable, playerB, "R");
        CakeUtils cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
        new MoverCakeGetter(cakeUtils);
        WhereMoveGetter whereMoveGetter = new WhereMoveGetter(gameTable);
        Position position = whereMoveGetter.whereMove(characterArray, iaComponentsSetter,playerB.getTeam(),moveData);
        assertTrue(""+position, position.isEquals(new Position(6, 0)));
    }
    
    @Test
    public void bestWhereNotAttack() {
      //........................... |0... |01.. |02.. |03.. |04 ..|05. |06 ...|07... |08|
        String[][] TABLE_VALUES2 ={ { "__", "__", "__", "__", "__", "__", "__", "__", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "1$" },
                                    { "__", "__", "__", "__", "__", "__", "__", "__", "__" } };
        TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
        gameTable = new GameTable(tableSeats, TOTAL_MONEY);
        
        // ..........................|.0 ..|01.. |02.. |03.. |04 ..|05.. |06.. |07.. |08|
        String[][] playerChairs = { { "N", "N_", "VV", "R_", "R_", "Nk", "VV", "B_", "B_" },
                                    { "N", "**", "**", "**", "**", "**", "**", "**", "B_" },
                                    { "N", "**", "**", "**", "**", "**", "**", "**", "B_" },
                                    { "N", "**", "**", "**", "**", "**", "**", "**", "B_" },
                                    { "N", "N_", "R_", "Bk", "R_", "N_", "BK", "B_", "B_" } };
        GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
        IaComponentsSetter iaComponentsSetter = new IaComponentsSetter(gameTable, characterArray, playerB, 3);
        new DataCakeSetter(characterArray, gameTable, playerB, "R");
        CakeUtils cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
        new MoverCakeGetter(cakeUtils);
        WhereMoveGetter whereMoveGetter = new WhereMoveGetter(gameTable);
        Position position = whereMoveGetter.whereMove(characterArray, iaComponentsSetter,playerB.getTeam(),moveData);
        assertTrue(""+position, position.isEquals(new Position(2, 0)));
    }
    
    @Test
    public void isHasGunBestNotSide() {
      //........................... |0... |01.. |02.. |03.. |04 ..|05. |06 ...|07... |08|
        String[][] TABLE_VALUES2 ={ { "__", "__", "__", "__", "__", "__", "__", "__", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "1$" },
                                    { "__", "__", "__", "__", "__", "__", "__", "__", "__" } };
        TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
        gameTable = new GameTable(tableSeats, TOTAL_MONEY);
        
        // ..........................|.0 ..|01.. |02.. |03.. |04 ..|05.. |06.. |07.. |08|
        String[][] playerChairs = { { "N", "N_", "VV", "R_", "R_", "Nk", "VV", "B_", "B_" },
                                    { "N", "**", "**", "**", "**", "**", "**", "**", "B_" },
                                    { "N", "**", "**", "**", "**", "**", "**", "**", "B_" },
                                    { "N", "**", "**", "**", "**", "**", "**", "**", "B_" },
                                    { "N", "N_", "R_", "BP", "R_", "N_", "BK", "B_", "VV" } };
        GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
        IaComponentsSetter iaComponentsSetter = new IaComponentsSetter(gameTable, characterArray, playerB, 3);
        new DataCakeSetter(characterArray, gameTable, playerB, "R");
        CakeUtils cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
        new MoverCakeGetter(cakeUtils);
        WhereMoveGetter whereMoveGetter = new WhereMoveGetter(gameTable);
        Position position = whereMoveGetter.whereMove(characterArray, iaComponentsSetter,playerB.getTeam(),moveData);
        assertTrue(""+position, position.isEquals(new Position(2, 0)));
    }
    
    @Test
    public void isHasKnifeBestSide() {
      //........................... |0... |01.. |02.. |03.. |04 ..|05. |06 ...|07... |08|
        String[][] TABLE_VALUES2 ={ { "__", "__", "__", "__", "__", "__", "__", "__", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "1$" },
                                    { "__", "__", "__", "__", "__", "__", "__", "__", "__" } };
        TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
        gameTable = new GameTable(tableSeats, TOTAL_MONEY);
        
        // ..........................|.0 ..|01.. |02.. |03.. |04 ..|05.. |06.. |07.. |08|
        String[][] playerChairs = { { "N", "N_", "VV", "R_", "R_", "N_", "VV", "B_", "B_" },
                                    { "N", "**", "**", "**", "**", "**", "**", "**", "B_" },
                                    { "N", "**", "**", "**", "**", "**", "**", "**", "B_" },
                                    { "N", "**", "**", "**", "**", "**", "**", "**", "B_" },
                                    { "N", "N_", "R_", "B_", "R_", "N_", "BK", "B_", "VV" } };
        GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
        IaComponentsSetter iaComponentsSetter = new IaComponentsSetter(gameTable, characterArray, playerB, 3);
        new DataCakeSetter(characterArray, gameTable, playerB, "R");
        CakeUtils cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
        new MoverCakeGetter(cakeUtils);
        WhereMoveGetter whereMoveGetter = new WhereMoveGetter(gameTable);
        Position position = whereMoveGetter.whereMove(characterArray, iaComponentsSetter,playerB.getTeam(),moveData);
        assertTrue(""+position, position.isEquals(new Position(8, 4)));
    }
    
    @Test
    public void moveWhereICanAttackAndNotBusiness() {
      //........................... |0... |01.. |02.. |03.. |04 ..|05. |06 ...|07... |08|
        String[][] TABLE_VALUES2 ={ { "__", "__", "__", "__", "__", "__", "__", "__", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "1$" },
                                    { "__", "__", "__", "__", "__", "__", "__", "__", "__" } };
        TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
        gameTable = new GameTable(tableSeats, TOTAL_MONEY);
        
        // ..........................|.0 ..|01.. |02.. |03.. |04 ..|05.. |06.. |07.. |08|
        String[][] playerChairs = { { "N", "VV", "VV", "VV", "R_", "N_", "VV", "B_", "B_" },
                                    { "N", "**", "**", "**", "**", "**", "**", "**", "B_" },
                                    { "N", "**", "**", "**", "**", "**", "**", "**", "B_" },
                                    { "N", "**", "**", "**", "**", "**", "**", "**", "B_" },
                                    { "N", "N_", "R_", "Bk", "R_", "N_", "BK", "B_", "VV" } };
        GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
        IaComponentsSetter iaComponentsSetter = new IaComponentsSetter(gameTable, characterArray, playerB, 3);
        new DataCakeSetter(characterArray, gameTable, playerB, "R");
        CakeUtils cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
        new MoverCakeGetter(cakeUtils);
        WhereMoveGetter whereMoveGetter = new WhereMoveGetter(gameTable);
        Position position = whereMoveGetter.whereMove(characterArray, iaComponentsSetter,playerB.getTeam(),moveData);
        assertTrue(""+position, position.isEquals(new Position(6, 0)));
    }
    
    @Test
    public void moveWhereICanAttackAndBusiness_eligeBusiness() {
      //........................... |0... |01.. |02.. |03.. |04 ..|05. |06 ...|07... |08|
        String[][] TABLE_VALUES2 ={ { "__", "__", "__", "__", "__", "__", "__", "__", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "1$" },
                                    { "__", "__", "__", "__", "__", "__", "__", "__", "1$" } };
        TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
        gameTable = new GameTable(tableSeats, TOTAL_MONEY);
        
        // ..........................|.0 ..|01.. |02.. |03.. |04 ..|05.. |06.. |07.. |08|
        String[][] playerChairs = { { "N", "B_", "VV", "B_", "R_", "N_", "VV", "B_", "B_" },
                                    { "N", "**", "**", "**", "**", "**", "**", "**", "B_" },
                                    { "N", "**", "**", "**", "**", "**", "**", "**", "B_" },
                                    { "N", "**", "**", "**", "**", "**", "**", "**", "B_" },
                                    { "N", "N_", "R_", "Bk", "R_", "N_", "BK", "B_", "VV" } };
        GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
        IaComponentsSetter iaComponentsSetter = new IaComponentsSetter(gameTable, characterArray, playerB, 3);
        new DataCakeSetter(characterArray, gameTable, playerB, "R");
        CakeUtils cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
        new MoverCakeGetter(cakeUtils);
        WhereMoveGetter whereMoveGetter = new WhereMoveGetter(gameTable);
        Position position = whereMoveGetter.whereMove(characterArray, iaComponentsSetter,playerB.getTeam(),moveData);
        assertTrue(""+position, position.isEquals(new Position(8, 4)));
    }

}
