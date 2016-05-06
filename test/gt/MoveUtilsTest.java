package gt;

import static org.junit.Assert.*;

import java.util.List;

import gm.GameCharacter;
import gm.MoveUtils;
import gm.pojos.Position;
import gt.extras.Converter;

import org.junit.Before;
import org.junit.Test;


public class MoveUtilsTest {
    
    private MoveUtils moveUtils;
    
    private Converter converter;

    @Before
    public void setUp() throws Exception {
        moveUtils = new MoveUtils();
        converter = new Converter(9, 3);
    }

    @Test
    public void test() {

        // ...........................|0 ..|01 ...|02 ..|03 ..|04 ..|05 ..|06 ..|07 .|08|
        String[][] playerChairs = { { "1kP", "VV", "3P", "VV", "VV", "3k", "1", "VV", "VV" },
                                    { "VV", "**", "**", "**", "**", "**", "**", "**", "VV" },
                                    { "2_", "VV", "1", "2P", "1_", "VV", "VV", "VV", "1k" } };
        
        GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
        
        List<Position> list = moveUtils.getCharacterByTeam(characterArray, "1");
        assertEquals(5, list.size());
        assertTrue(list.get(0).isEquals(new Position(0, 0)));
        assertTrue(""+list.get(1),list.get(1).isEquals(new Position(2, 2)));
        assertTrue(""+list.get(2),list.get(2).isEquals(new Position(4, 2)));
        assertTrue(""+list.get(3),list.get(3).isEquals(new Position(6, 0)));
        assertTrue(""+list.get(4),list.get(4).isEquals(new Position(8, 2)));
    }
    
}
