package gt.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import gm.GameCharacter;
import gm.Player;
import gm.utils.CharacterUtils;
import gt.extras.Converter;

public class CharacterUtilsTest {

	private Converter converter;

    @Before
    public void setUp() throws Exception {
        converter = new Converter(9, 3);
    }

	@Test
	public void hasAwakeCharacter_true() {
		
        // ............................|0 ..|01 ...|02 ..|03 ..|04 ..|05 ..|06 ..|07 .|08|
        String[][] playerChairs = { { "VV", "VV", "3P", "1PZ", "VV", "3kZ", "VV", "3k", "VV" },
                                    { "1kP", "**", "**", "**", "**", "**", "**", "**", "2_" },
                                    { "2_", "VV", "1", "2P", "VV", "VV", "VV", "1k", "VV" } };
        GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
        Player player = new Player("1", false);
        player.plusCounterCharacterOne();
        player.plusCounterCharacterOne();
        player.plusCounterCharacterOne();
        player.plusCounterCharacterOne();
		assertFalse(CharacterUtils.hasAsleepAllCharacter(player, characterArray));
	}
	
	@Test
	public void hasAwakeCharacter_false() {
		
        // ............................|0 ..|01 ...|02 ..|03 ..|04 ..|05 ..|06 ..|07 .|08|
        String[][] playerChairs = { { "VV", "VV", "3P", "1PZ", "VV", "3kZ", "VV", "3k", "VV" },
                                    { "1kPZ", "**", "**", "**", "**", "**", "**", "**", "2_" },
                                    { "2_", "VV", "1Z", "2P", "VV", "VV", "VV", "1kZ", "VV" } };
        GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
        Player player = new Player("1", false);
        player.plusCounterCharacterOne();
        player.plusCounterCharacterOne();
        player.plusCounterCharacterOne();
        player.plusCounterCharacterOne();
		assertTrue(CharacterUtils.hasAsleepAllCharacter(player, characterArray));
	}

}
