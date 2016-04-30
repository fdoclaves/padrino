package gt.ia.getters;

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
import gm.ia.getters.BoomGetter;
import gm.ia.getters.DataCakeSetter;
import gm.ia.getters.IaComponentsSetter;
import gm.info.CardType;
import gm.pojos.Position;
import gt.extras.Converter;

public class BoomGetterTest {

private Converter converter;
	
	private GameTable gameTable;
	
	private Player playerB;
	
	private Player playerR;

	@Before
	public void setUp() throws Exception {
		List<CardType> cardsB = new ArrayList<CardType>();
		cardsB.add(CardType.BOOM);
		playerB = new Player("B", cardsB);
		List<CardType> cardsA = new ArrayList<CardType>();
		cardsA.add(CardType.BOOM);
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
	public void putBoomIfCanMoveCake() {

		// ..........................|.0 ..|01.. |02.. |03.. |04 ..|05.. |06.. |07.. |08|
		String[][] playerChairs = { { "V", "R_", "VV", "R_", "R_", "VV", "VV", "B_", "VV" },
									{ "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
									{ "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
									{ "V", "**", "**", "**", "**", "**", "**", "**", "VV" },
									{ "V", "VV", "R_", "R_", "N_", "VV", "VV", "VV", "VV" } };
		GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
		List<Cake> cakes = new ArrayList<Cake>();
		cakes.add(new Cake(new Position(3, 4), "N", gameTable));
		cakes.add(new Cake(new Position(4, 0), "N", gameTable));
		gameTable.setCakeList(cakes);
		new IaComponentsSetter(gameTable, characterArray, playerB, 3);
		new DataCakeSetter(characterArray, gameTable, playerB, "R");
		CakeUtils cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
		BoomGetter boomGetter = new BoomGetter();
		Cake bestBoom = boomGetter.getBestBoom(characterArray, cakes, cakeUtils, "R", "B");
		assertTrue(""+bestBoom.getPosition(), bestBoom.getPosition().isEquals(new Position(4, 0)));
	}

}
