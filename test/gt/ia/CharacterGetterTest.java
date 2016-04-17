package gt.ia;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import gm.GameTable;
import gm.TableSeat;
import gm.ia.getters.CharacterGetter;
import gm.ia.pojos.IA_Character;
import gm.info.MoneyValues;
import gt.extras.Converter;

public class CharacterGetterTest {
	// |0 |01 |02 |03 |04 |05 |06 |07 |08|
	private String[][] playerChairs = { { "RK", "R", "B", "R", "R", "R", "R", "R", "B" },
			{ "R", "*", "*", "*", "*", "*", "*", "*", "B" }, { "B", "*", "*", "*", "*", "*", "*", "*", "R" },
			{ "R", "*", "*", "*", "*", "*", "*", "*", "B" }, { "B", "R", "B", "B", "B", "V", "B", "B", "R" } };

	// |0 |01 |02 |03 |04 |05 |06 |07 |08|
	private String[][] TABLE_VALUES = { { "P_", "3$", "k_", "1$", "__", "2$", "P_", "__", "P_" },
			{ "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
			{ "M_", "**", "**", "**", "**", "**", "**", "**", "P_" },
			{ "__", "**", "**", "**", "**", "**", "**", "**", "P_" },
			{ "k_", "__", "2$", "P_", "3$", "__", "1$", "__", "k_" } };

	private Converter converter;

	private CharacterGetter characterGetter;

	@Before
	public void setUp() throws Exception {
		converter = new Converter(9, 5);
		TableSeat[][] tableSeats = converter.to(TABLE_VALUES);
		GameTable gameTable = new GameTable(tableSeats);
		characterGetter = new CharacterGetter(converter.toCharacterArray(playerChairs), gameTable);
	}

	@Test
	public void obtenecionCorrectaDeEnemigos() {
		List<IA_Character> redList = characterGetter.getCharactersByTeam("R");
		assertEquals(12, redList.size());
		assertEquals(true, redList.get(0).isKing());
		assertEquals(MoneyValues.CASINO, redList.get(1).getMoney());
		assertEquals(MoneyValues.RESTAURANT, redList.get(2).getMoney());
		assertEquals(MoneyValues.NOTTHING, redList.get(3).getMoney());
		assertEquals(false, redList.get(3).isKing());
		assertEquals(MoneyValues.BAR, redList.get(4).getMoney());

		List<IA_Character> blueList = characterGetter.getCharactersByTeam("B");
		assertEquals(11, blueList.size());
		assertEquals(MoneyValues.MACHINE, blueList.get(3).getMoney());
	}

}
