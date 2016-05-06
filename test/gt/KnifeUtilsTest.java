package gt;

import static org.junit.Assert.*;

import java.util.List;

import gm.GameCharacter;
import gm.KnifeUtils;
import gm.TableSeat;
import gm.pojos.Position;
import gt.extras.Converter;

import org.junit.Before;
import org.junit.Test;


public class KnifeUtilsTest {
    
    private KnifeUtils knifeUtils;
    
    private Converter converter;

    @Before
    public void setUp() throws Exception {
        knifeUtils = new KnifeUtils();
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
                                    { "2_", "VV", "1", "2P", "1_", "VV", "VV", "VV", "1k" } };
        
        TableSeat[][] tableSeats = converter.to(TABLE_VALUES);
        GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
        
        List<Position> list = knifeUtils.getCharacterByTeam(tableSeats, characterArray, "1");
        assertEquals(3, list.size());
        assertTrue(list.get(0).isEquals(new Position(0, 0)));
        assertTrue(""+list.get(1),list.get(1).isEquals(new Position(2, 2)));
        assertTrue(""+list.get(2),list.get(2).isEquals(new Position(8, 2)));
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
}
