package gt.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import gm.GameCharacter;
import gm.GameTable;
import gm.TableSeat;
import gm.pojos.Position;
import gm.utils.GunUtils;
import gt.extras.Converter;


public class GunUtilsTest {
    
    
    private Converter converter;

    @Before
    public void setUp() throws Exception {
        converter = new Converter(9, 3);
    }

    @Test
    public void test() {
        
     // ...........................|0 ...|01 ...|02 .|03 ...|04 ..|05 .|06 ..|07 ..|08|
        String[][] TABLE_VALUES = { { "PG", "_G", "kG", "1$", "__", "__", "P_", "__", "P_" },
                                    { "2$", "**", "**", "**", "**", "**", "**", "**", "GP" },
                                    { "kG", "__", "__", "GP", "__", "GP", "3$", "_P", "P_" } };

        // ............................|0 ..|01 ...|02 ..|03 ..|04 ..|05 ..|06 ..|07 .|08|
        String[][] playerChairs = { { "VV", "VV", "3P", "1PZ", "VV", "3kZ", "VV", "3k", "VV" },
                                    { "1kP", "**", "**", "**", "**", "**", "**", "**", "2_" },
                                    { "2_", "VV", "1", "2P", "VV", "VV", "VV", "1k", "VV" } };
        
        TableSeat[][] tableSeats = converter.to(TABLE_VALUES);
        GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
        GameTable gameTable = new GameTable(tableSeats, 100);
        List<Position> list = GunUtils.getCharacterByTeam(gameTable, characterArray, "1");
        assertEquals(2, list.size());
        assertTrue(list.get(0).isEquals(new Position(0, 1)));
        assertTrue(""+list.get(1),list.get(1).isEquals(new Position(7, 2)));
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
}
