package gt;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import gm.CardManagerImpl;
import gm.GameCharacter;
import gm.GameManager;
import gm.TableSeat;
import gm.info.CardType;
import gt.extras.Converter;

public class GameManagerTest {

	private static final String J1 = "1";

	private static final String J2 = "2";

	private static final String J3 = "3";

	private Converter converter;

	private List<String> nameTeams;

	private CardManagerImpl cardManager;

	@Before
	public void setUp() throws Exception {
		converter = new Converter(9, 3);
		nameTeams = new ArrayList<String>();
		nameTeams.add(J1);
		nameTeams.add(J2);
		nameTeams.add(J3);
		cardManager = new CardManagerImpl() {
			@Override
			protected void fillCards(List<CardType> chooseCard) {
				for (int i = 1; i <= 6; i++) {
					chooseCard.add(CardType.SLEEP);
				}
				for (int i = 1; i <= 9; i++) {
					chooseCard.add(CardType.GUN);
				}
				for (int i = 1; i <= 9; i++) {
					chooseCard.add(CardType.KNIFE);
				}
			}
		};
	}

	@Test
	public void test() {
		
		// ...........................|0 ...|01 ...|02 .|03 ...|04 ..|05 .|06 ..|07 ..|08|
		String[][] TABLE_VALUES = { { "PG", "_G", "kG", "1$", "__", "__", "P_", "__", "P_" },
									{ "2$", "**", "**", "**", "**", "**", "**", "**", "GP" },
									{ "kG", "__", "__", "GP", "__", "GP", "3$", "__", "k_" } };

		// ............................|0 ..|01 ...|02 ..|03 ..|04 ..|05 ..|06 ..|07 .|08|
		String[][] playerChairs = { { "VV", "VV", "VV", "1k", "3_", "VV", "VV", "VV", "VV" },
									{ "VV", "**", "**", "**", "**", "**", "**", "**", "VV" },
									{ "VV", "VV", "VV", "2P", "VV", "VV", "VV", "VV", "VV" } };
		
		TableSeat[][] tableSeats = converter.to(TABLE_VALUES);
		GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
		GameManager gameManager = new GameManager(nameTeams, cardManager, characterArray, tableSeats);
		gameManager.start();
		//System.out.println(converter.cToString(characterArray));
		assertEquals(J2, gameManager.whoWin());
		
	}

}
