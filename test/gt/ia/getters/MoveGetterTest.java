package gt.ia.getters;

import static org.junit.Assert.*;
import gm.Cake;
import gm.GameCharacter;
import gm.GameTable;
import gm.Player;
import gm.TableSeat;
import gm.cards.CakeUtils;
import gm.cards.MoveCard;
import gm.ia.MoverCakeGetter;
import gm.ia.getters.CakeGetter;
import gm.ia.getters.DataCakeGetter;
import gm.ia.getters.IaComponentsSetter;
import gm.info.CardType;
import gm.pojos.Position;
import gt.extras.Converter;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;


public class MoveGetterTest {
    
    private MoveGetter moveGetter;

    private Converter converter;
    
    private GameTable gameTable;
    
    private Player playerB;
    
    private Player playerR;

    @Before
    public void setUp() throws Exception {
        List<CardType> cardsB = new ArrayList<CardType>();
        cardsB.add(CardType.CAKE);
        cardsB.add(CardType.GUN);
        playerB = new Player("B", cardsB);
        List<CardType> cardsA = new ArrayList<CardType>();
        cardsA.add(CardType.CAKE);
        playerR = new Player("R", cardsA);
        converter = new Converter(9, 5);
        //........................... |0... |01.. |02.. |03.. |04 ..|05. |06 ...|07... |08|
        String[][] TABLE_VALUES = { { "__", "__", "__", "__", "__", "__", "__", "__", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
                                    { "__", "__", "__", "__", "__", "__", "__", "__", "__" } };
        TableSeat[][] tableSeats = converter.to(TABLE_VALUES);
        gameTable = new GameTable(tableSeats);
    }


    @Test
    public void whoMove() {
        gameTable.add(new Cake(new Position(3, 4), "N", gameTable));
        // ..........................|.0 ..|01.. |02.. |03.. |04 ..|05.. |06.. |07.. |08|
        String[][] playerChairs = { { "V", "V_", "VV", "R_", "VV", "VV", "VV", "VV", "VV" },
                                    { "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
                                    { "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
                                    { "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
                                    { "V", "V_", "VV", "B_", "VV", "VV", "BK", "VV", "VV" } };
        GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
        IaComponentsSetter iaComponentsSetter = new IaComponentsSetter(gameTable, characterArray, playerB, 3);
        DataCakeGetter dataCakeGetter = new DataCakeGetter(characterArray, gameTable, playerB, "R");
        CakeUtils cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
        new MoverCakeGetter(cakeUtils);
        MoveGetter moveGetter = new MoveGetter();
        Position position = moveGetter.whoMove(characterArray, playerB.getTeam(), cakeUtils,iaComponentsSetter,gameTable);
        assertTrue(""+position, position.isEquals(new Position(3, 4)));
    }
    
    @Test
    public void whoMoveFistKing() {
        gameTable.add(new Cake(new Position(3, 4), "N", gameTable));
        // ..........................|.0 ..|01.. |02.. |03.. |04 ..|05.. |06.. |07.. |08|
        String[][] playerChairs = { { "V", "V_", "VV", "R_", "VV", "VV", "VV", "VV", "VV" },
                                    { "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
                                    { "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
                                    { "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
                                    { "V", "V_", "VV", "B_", "BK", "VV", "B_", "VV", "VV" } };
        GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
        IaComponentsSetter iaComponentsSetter = new IaComponentsSetter(gameTable, characterArray, playerB, 3);
        DataCakeGetter dataCakeGetter = new DataCakeGetter(characterArray, gameTable, playerB, "R");
        CakeUtils cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
        new MoverCakeGetter(cakeUtils);
        MoveGetter moveGetter = new MoveGetter();
        Position position = moveGetter.whoMove(characterArray, playerB.getTeam(), cakeUtils,iaComponentsSetter,gameTable);
        assertTrue(""+position, position.isEquals(new Position(4, 4)));
    }
    
    @Test
    public void whoMoveFistKing2() {
        gameTable.add(new Cake(new Position(3, 4), "N", gameTable));
        // ..........................|.0 ..|01.. |02.. |03.. |04 ..|05.. |06.. |07.. |08|
        String[][] playerChairs = { { "V", "V_", "VV", "R_", "VV", "VV", "VV", "VV", "VV" },
                                    { "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
                                    { "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
                                    { "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
                                    { "V", "V_", "VV", "BK", "B_", "VV", "B_", "VV", "VV" } };
        GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
        IaComponentsSetter iaComponentsSetter = new IaComponentsSetter(gameTable, characterArray, playerB, 3);
        new DataCakeGetter(characterArray, gameTable, playerB, "R");
        CakeUtils cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
        new MoverCakeGetter(cakeUtils);
        MoveGetter moveGetter = new MoveGetter();
        Position position = moveGetter.whoMove(characterArray, playerB.getTeam(), cakeUtils,iaComponentsSetter,gameTable);
        assertTrue(""+position, position.isEquals(new Position(3, 4)));
    }
    
    @Test
    public void whoMoveFistKing3() {
        gameTable.add(new Cake(new Position(3, 4), "N", gameTable));
        // ..........................|.0 ..|01.. |02.. |03.. |04 ..|05.. |06.. |07.. |08|
        String[][] playerChairs = { { "V", "V_", "VV", "R_", "VV", "VV", "VV", "VV", "VV" },
                                    { "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
                                    { "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
                                    { "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
                                    { "V", "V_", "VV", "BK", "BP", "VV", "B_", "VV", "VV" } };
        GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
        IaComponentsSetter iaComponentsSetter = new IaComponentsSetter(gameTable, characterArray, playerB, 3);
        new DataCakeGetter(characterArray, gameTable, playerB, "R");
        CakeUtils cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
        new MoverCakeGetter(cakeUtils);
        MoveGetter moveGetter = new MoveGetter();
        Position position = moveGetter.whoMove(characterArray, playerB.getTeam(), cakeUtils,iaComponentsSetter,gameTable);
        assertTrue(""+position, position.isEquals(new Position(3, 4)));
    }
    
    @Test
    public void whoMoveFistWeapon() {
        gameTable.add(new Cake(new Position(3, 4), "N", gameTable));
        // ..........................|.0 ..|01.. |02.. |03.. |04 ..|05.. |06.. |07.. |08|
        String[][] playerChairs = { { "V", "V_", "VV", "R_", "VV", "VV", "VV", "VV", "VV" },
                                    { "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
                                    { "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
                                    { "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
                                    { "V", "V_", "VV", "BP", "B_", "VV", "B_", "VV", "VV" } };
        GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
        IaComponentsSetter iaComponentsSetter = new IaComponentsSetter(gameTable, characterArray, playerB, 3);
        new DataCakeGetter(characterArray, gameTable, playerB, "R");
        CakeUtils cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
        new MoverCakeGetter(cakeUtils);
        MoveGetter moveGetter = new MoveGetter();
        Position position = moveGetter.whoMove(characterArray, playerB.getTeam(), cakeUtils,iaComponentsSetter,gameTable);
        assertTrue(""+position, position.isEquals(new Position(3, 4)));
    }
    
    @Test
    public void whoMoveFistWeapon2() {
        gameTable.add(new Cake(new Position(3, 4), "N", gameTable));
        // ..........................|.0 ..|01.. |02.. |03.. |04 ..|05.. |06.. |07.. |08|
        String[][] playerChairs = { { "V", "V_", "VV", "R_", "VV", "VV", "VV", "VV", "VV" },
                                    { "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
                                    { "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
                                    { "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
                                    { "V", "V_", "VV", "B_", "BP", "VV", "B_", "VV", "VV" } };
        GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
        IaComponentsSetter iaComponentsSetter = new IaComponentsSetter(gameTable, characterArray, playerB, 3);
        new DataCakeGetter(characterArray, gameTable, playerB, "R");
        CakeUtils cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
        new MoverCakeGetter(cakeUtils);
        MoveGetter moveGetter = new MoveGetter();
        Position position = moveGetter.whoMove(characterArray, playerB.getTeam(), cakeUtils,iaComponentsSetter,gameTable);
        assertTrue(""+position, position.isEquals(new Position(4, 4)));
    }
    
    @Test
    public void whoMoveThinkingMoveCake() {
        gameTable.add(new Cake(new Position(3, 4), "N", gameTable));
        // ..........................|.0 ..|01.. |02.. |03.. |04 ..|05.. |06.. |07.. |08|
        String[][] playerChairs = { { "V", "V_", "VV", "R_", "VV", "VV", "BK", "VV", "VV" },
                                    { "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
                                    { "V", "**", "**", "**", "**", "**", "**", "**", "BP" },
                                    { "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
                                    { "V", "V_", "VV", "N_", "VV", "VV", "B_", "VV", "VV" } };
        GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
        IaComponentsSetter iaComponentsSetter = new IaComponentsSetter(gameTable, characterArray, playerB, 3);
        new DataCakeGetter(characterArray, gameTable, playerB, "R");
        CakeUtils cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
        new MoverCakeGetter(cakeUtils);
        MoveGetter moveGetter = new MoveGetter();
        Position position = moveGetter.whoMove(characterArray, playerB.getTeam(), cakeUtils,iaComponentsSetter,gameTable);
        assertTrue(""+position, position.isEquals(new Position(6, 4)));
    }
    
    @Test
    public void whoMoveThinkingAttackMe() {
        // ..........................|.0 ..|01.. |02.. |03.. |04 ..|05.. |06.. |07.. |08|
        String[][] playerChairs = { { "V", "V_", "VV", "R_", "VV", "Rk", "BK", "VV", "VV" },
                                    { "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
                                    { "R", "**", "**", "**", "**", "**", "**", "**", "B" },
                                    { "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
                                    { "V", "V_", "VV", "N_", "VV", "VV", "B_", "VV", "VV" } };
        GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
        IaComponentsSetter iaComponentsSetter = new IaComponentsSetter(gameTable, characterArray, playerB, 3);
        new DataCakeGetter(characterArray, gameTable, playerB, "R");
        CakeUtils cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
        new MoverCakeGetter(cakeUtils);
        MoveGetter moveGetter = new MoveGetter();
        Position position = moveGetter.whoMove(characterArray, playerB.getTeam(), cakeUtils,iaComponentsSetter,gameTable);
        assertTrue(""+position, position.isEquals(new Position(6, 0)));
    }
    
    @Test
    public void whoMoveThinkingAttackMeWithGlas() {
      //........................... |0... |01.. |02.. |03.. |04 ..|05. |06 ...|07... |08|
        String[][] TABLE_VALUES2 = { { "__", "__", "__", "__", "G_", "__", "__", "__", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
                                    { "__", "__", "__", "__", "__", "__", "__", "__", "__" } };
        TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
        gameTable = new GameTable(tableSeats);
        
        // ..........................|.0 ..|01.. |02.. |03.. |04 ..|05.. |06.. |07.. |08|
        String[][] playerChairs = { { "V", "V_", "VV", "R_", "B_", "Rk", "BK", "VV", "VV" },
                                    { "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
                                    { "R", "**", "**", "**", "**", "**", "**", "**", "B" },
                                    { "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
                                    { "V", "V_", "VV", "N_", "VV", "VV", "B_", "VV", "VV" } };
        GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
        IaComponentsSetter iaComponentsSetter = new IaComponentsSetter(gameTable, characterArray, playerB, 3);
        new DataCakeGetter(characterArray, gameTable, playerB, "R");
        CakeUtils cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
        new MoverCakeGetter(cakeUtils);
        MoveGetter moveGetter = new MoveGetter();
        Position position = moveGetter.whoMove(characterArray, playerB.getTeam(), cakeUtils,iaComponentsSetter,gameTable);
        assertTrue(""+position, position.isEquals(new Position(4, 0)));
    }
    
    @Test
    public void whoMoveThinkingKeepBusiness() {
      //........................... |0... |01.. |02.. |03.. |04 ..|05. |06 ...|07... |08|
        String[][] TABLE_VALUES2 = { { "__", "__", "__", "__", "__", "__", "1$", "__", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
                                    { "__", "__", "__", "__", "__", "__", "__", "__", "__" } };
        TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
        gameTable = new GameTable(tableSeats);
        
        // ..........................|.0 ..|01.. |02.. |03.. |04 ..|05.. |06.. |07.. |08|
        String[][] playerChairs = { { "V", "V_", "VV", "R_", "B_", "VV", "B_", "VV", "VV" },
                                    { "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
                                    { "R", "**", "**", "**", "**", "**", "**", "**", "VV" },
                                    { "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
                                    { "V", "V_", "VV", "N_", "VV", "VV", "VV", "VV", "VV" } };
        GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
        IaComponentsSetter iaComponentsSetter = new IaComponentsSetter(gameTable, characterArray, playerB, 3);
        new DataCakeGetter(characterArray, gameTable, playerB, "R");
        CakeUtils cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
        new MoverCakeGetter(cakeUtils);
        MoveGetter moveGetter = new MoveGetter();
        Position position = moveGetter.whoMove(characterArray, playerB.getTeam(), cakeUtils,iaComponentsSetter,gameTable);
        assertTrue(""+position, position.isEquals(new Position(4, 0)));
    }
    
    @Test
    public void whoMoveThinkingKeepBusiness2() {
      //........................... |0... |01.. |02.. |03.. |04 ..|05. |06 ...|07... |08|
        String[][] TABLE_VALUES2 = { { "__", "__", "__", "__", "3$", "__", "__", "__", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
                                    { "__", "__", "__", "__", "__", "__", "__", "__", "__" } };
        TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
        gameTable = new GameTable(tableSeats);
        
        // ..........................|.0 ..|01.. |02.. |03.. |04 ..|05.. |06.. |07.. |08|
        String[][] playerChairs = { { "V", "V_", "VV", "R_", "B_", "VV", "B_", "VV", "VV" },
                                    { "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
                                    { "R", "**", "**", "**", "**", "**", "**", "**", "VV" },
                                    { "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
                                    { "V", "V_", "VV", "N_", "VV", "VV", "VV", "VV", "VV" } };
        GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
        IaComponentsSetter iaComponentsSetter = new IaComponentsSetter(gameTable, characterArray, playerB, 3);
        new DataCakeGetter(characterArray, gameTable, playerB, "R");
        CakeUtils cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
        new MoverCakeGetter(cakeUtils);
        MoveGetter moveGetter = new MoveGetter();
        Position position = moveGetter.whoMove(characterArray, playerB.getTeam(), cakeUtils,iaComponentsSetter,gameTable);
        assertTrue(""+position, position.isEquals(new Position(6, 0)));
    }
    
    @Test
    public void whoMoveThinkingWithoutHandWeapon() {
      //........................... |0... |01.. |02.. |03.. |04 ..|05. |06 ...|07... |08|
        String[][] TABLE_VALUES2 = { { "__", "__", "__", "__", "__", "__", "_P", "__", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
                                    { "__", "**", "**", "**", "**", "**", "**", "**", "__" },
                                    { "__", "__", "__", "__", "__", "__", "__", "__", "__" } };
        TableSeat[][] tableSeats = converter.to(TABLE_VALUES2);
        gameTable = new GameTable(tableSeats);
        
        // ..........................|.0 ..|01.. |02.. |03.. |04 ..|05.. |06.. |07.. |08|
        String[][] playerChairs = { { "V", "V_", "VV", "R_", "B_", "VV", "B_", "VV", "VV" },
                                    { "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
                                    { "R", "**", "**", "**", "**", "**", "**", "**", "VV" },
                                    { "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
                                    { "V", "V_", "VV", "N_", "R_", "VV", "R_", "VV", "VV" } };
        GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
        IaComponentsSetter iaComponentsSetter = new IaComponentsSetter(gameTable, characterArray, playerB, 3);
        new DataCakeGetter(characterArray, gameTable, playerB, "R");
        CakeUtils cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
        new MoverCakeGetter(cakeUtils);
        MoveGetter moveGetter = new MoveGetter();
        Position position = moveGetter.whoMove(characterArray, playerB.getTeam(), cakeUtils,iaComponentsSetter,gameTable);
        assertTrue(""+position, position.isEquals(new Position(4, 0)));
    }
    
    @Test
    public void primeroFatal() {
        gameTable.add(new Cake(new Position(3, 4), "N", gameTable));
        gameTable.add(new Cake(new Position(6, 4), "R", gameTable));
        // ..........................|.0 ..|01.. |02.. |03.. |04 ..|05.. |06.. |07.. |08|
        String[][] playerChairs = { { "V", "V_", "VV", "R_", "VV", "VV", "VV", "VV", "VV" },
                                    { "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
                                    { "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
                                    { "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
                                    { "V", "V_", "VV", "B_", "VV", "VV", "BK", "VV", "VV" } };
        GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
        IaComponentsSetter iaComponentsSetter = new IaComponentsSetter(gameTable, characterArray, playerB, 3);
        new DataCakeGetter(characterArray, gameTable, playerB, "R");
        CakeUtils cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
        new MoverCakeGetter(cakeUtils);
        MoveGetter moveGetter = new MoveGetter();
        Position position = moveGetter.whoMove(characterArray, playerB.getTeam(), cakeUtils,iaComponentsSetter,gameTable);
        assertTrue(""+position, position.isEquals(new Position(6, 4)));
    }
    
    @Test
    public void primeroFatal2() {
        gameTable.add(new Cake(new Position(6, 4), "N", gameTable));
        gameTable.add(new Cake(new Position(3, 4), "R", gameTable));
        // ..........................|.0 ..|01.. |02.. |03.. |04 ..|05.. |06.. |07.. |08|
        String[][] playerChairs = { { "V", "V_", "VV", "R_", "VV", "VV", "VV", "VV", "VV" },
                                    { "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
                                    { "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
                                    { "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
                                    { "V", "V_", "VV", "B_", "VV", "VV", "BK", "VV", "VV" } };
        GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
        IaComponentsSetter iaComponentsSetter = new IaComponentsSetter(gameTable, characterArray, playerB, 3);
        new DataCakeGetter(characterArray, gameTable, playerB, "R");
        CakeUtils cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
        new MoverCakeGetter(cakeUtils);
        MoveGetter moveGetter = new MoveGetter();
        Position position = moveGetter.whoMove(characterArray, playerB.getTeam(), cakeUtils,iaComponentsSetter,gameTable);
        assertTrue(""+position, position.isEquals(new Position(3, 4)));
    }
}
