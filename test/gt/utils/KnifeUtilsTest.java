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
import gm.utils.KnifeUtils;
import gt.extras.Converter;


public class KnifeUtilsTest {
    
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
                                    { "kG", "__", "_k", "GP", "__", "GP", "3$", "__", "P_" } };

        // ............................|0 ..|01 ...|02 ..|03 ..|04 ..|05 ..|06 ..|07 .|08|
        String[][] playerChairs = { { "1kP", "VV", "3P", "VV", "VV", "3k", "VV", "VV", "VV" },
                                    { "VV", "**", "**", "**", "**", "**", "**", "**", "VV" },
                                    { "2_", "VV", "1", "2P", "1_", "1kZ", "VV", "VV", "1k" } };
        
        TableSeat[][] tableSeats = converter.to(TABLE_VALUES);
        GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
        GameTable gameTable = new GameTable(tableSeats, 100);
        List<Position> list = KnifeUtils.getCharacterByTeam(gameTable, characterArray, "1");
        assertEquals(1, list.size());
        assertTrue(""+list.get(0), list.get(0).isEquals(new Position(2, 2)));
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
}
