package gt.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import gm.pojos.Position;
import gm.utils.SleepUtils;
import gt.extras.Converter;

public class SleepUtilsTest {

    private Converter converter;

    @Before
    public void setUp() throws Exception {
        converter = new Converter(9, 3);
    }

    @Test
    public void test() {
        //......................... |0 .....|01.. |02 ..|03 ..|04.. |05 ..|06 ..|07 ..|08|
        String[][] TABLE_VALUES = { { "PG", "_G", "kG", "1$", "__", "__", "P_", "__", "P_" },
                                    { "2$", "**", "**", "**", "**", "**", "**", "**", "GP" },
                                    { "kG", "__", "__", "GP", "_G", "GP", "3$", "__", "k_" } };

        //.......................... |0.... |01 ...|02 ..|03 ..|04 .|05 ..|06 ..|07 ..|08|
        String[][] playerChairs = { { "1_", "1P", "2_", "1k", "1k", "1P", "1Z", "1P", "V" },
                                    { "1P", "**", "**", "**", "**", "**", "**", "**", "2_" },
                                    { "2_", "1_", "2_", "2_", "V", "3_", "2_", "2_", "2Z" } };
        
        
        List<Position> list = SleepUtils.get(converter.toCharacterArray(playerChairs), converter.to(TABLE_VALUES), "1");
        assertEquals(5, list.size());
        assertTrue("" + list.get(4), list.get(4).isEquals(new Position(8, 1)));
        assertTrue("" + list.get(3), list.get(3).isEquals(new Position(5, 2)));
        assertTrue("" + list.get(2), list.get(2).isEquals(new Position(3, 2)));
        assertTrue("" + list.get(1), list.get(1).isEquals(new Position(2, 0)));
        assertTrue("" + list.get(0), list.get(0).isEquals(new Position(0, 2)));
        
    }
}
