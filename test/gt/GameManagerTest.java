package gt;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import gm.CardManagerImpl;
import gm.GameCharacter;
import gm.GameManager;
import gm.Player;
import gm.TableSeat;
import gm.info.CardType;
import gt.extras.Converter;

public class GameManagerTest {
	
	private static final int TOTAL_MONEY = 100;

	private Player J1;

	private Player J2;

	private Player J3;

	private Converter converter;

	private List<Player> teams;

	@Before
	public void setUp() throws Exception {
		converter = new Converter(9, 3);
		teams = new ArrayList<Player>();
		J1 = new Player("1", false);
		J2 = new Player("2", false);
		J3 = new Player("3", false);
		teams.add(J1);
		teams.add(J2);
		teams.add(J3);
	}

	@Test
	public void whoWinOneTeam() {
		CardManagerImpl cardManager = new CardManagerImpl() {
			@Override
			protected void fillCards(List<CardType> chooseCard) {
				for (int i = 1; i <= 16; i++) {
					chooseCard.add(CardType.GUN);
				}
			}
		};
		
		// ...........................|0 ...|01 ...|02 .|03 ...|04 ..|05 .|06 ..|07 ..|08|
		String[][] TABLE_VALUES = { { "PG", "_G", "kG", "1$", "__", "__", "P_", "__", "P_" },
									{ "2$", "**", "**", "**", "**", "**", "**", "**", "GP" },
									{ "kG", "__", "__", "GP", "__", "GP", "3$", "__", "k_" } };

		// ............................|0 ..|01 ...|02 ..|03 ..|04 ..|05 ..|06 ..|07 .|08|
		String[][] playerChairs = { { "VV", "VV", "VV", "1k", "VV", "VV", "VV", "VV", "VV" },
									{ "VV", "**", "**", "**", "**", "**", "**", "**", "VV" },
									{ "VV", "VV", "VV", "2P", "VV", "VV", "VV", "VV", "VV" } };
		
		TableSeat[][] tableSeats = converter.to(TABLE_VALUES);
		GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
		GameManager gameManager = new GameManager(teams, cardManager, characterArray, tableSeats, TOTAL_MONEY);
		gameManager.start();
		//System.out.println(converter.cToString(characterArray));
		List<Player> winners = gameManager.whoWin();
		assertEquals(1, gameManager.whoWin().size());
		assertEquals(J2.getTeam(), winners.get(0).getTeam());
		assertEquals(0, J1.getCards().size());
		assertEquals(0, J3.getCards().size());
	}
	
	@Test
	public void whoWinByMoney() {
		CardManagerImpl cardManager = new CardManagerImpl() {
			@Override
			protected void fillCards(List<CardType> chooseCard) {
				for (int i = 1; i <= 16; i++) {
					chooseCard.add(CardType.GUN);
				}
			}
		};
		
		// ...........................|0 ...|01 ...|02 .|03 ...|04 ..|05 .|06 ..|07 ..|08|
		String[][] TABLE_VALUES = { { "__", "__", "__", "1$", "__", "__", "3$", "__", "__" },
									{ "2$", "**", "**", "**", "**", "**", "**", "**", "M_" },
									{ "__", "__", "__", "1$", "__", "__", "3$", "__", "__" } };

		// ............................|0 ..|01 ...|02 ..|03 ..|04 ..|05 ..|06 ..|07 .|08|
		String[][] playerChairs = { { "VV", "VV", "VV", "1k", "VV", "VV", "3P", "VV", "VV" },
									{ "2k", "**", "**", "**", "**", "**", "**", "**", "3k" },
									{ "VV", "VV", "VV", "1P", "VV", "VV", "3K", "VV", "VV" } };
		
		TableSeat[][] tableSeats = converter.to(TABLE_VALUES);
		GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
		GameManager gameManager = new GameManager(teams, cardManager, characterArray, tableSeats, 30);
		gameManager.start();
		//System.out.println(converter.cToString(characterArray));
		List<Player> winners = gameManager.whoWin();
		assertEquals(1, gameManager.whoWin().size());
		assertEquals(J3.getTeam(), winners.get(0).getTeam());
		assertEquals(5, J1.getCards().size());
		assertEquals(5, J3.getCards().size());
	}
	
	@Test
	public void empateWinByMoney() {
		CardManagerImpl cardManager = new CardManagerImpl() {
			@Override
			protected void fillCards(List<CardType> chooseCard) {
				for (int i = 1; i <= 16; i++) {
					chooseCard.add(CardType.GUN);
				}
			}
		};
		
		// ...........................|0 ...|01 ...|02 .|03 ...|04 ..|05 .|06 ..|07 ..|08|
		String[][] TABLE_VALUES = { { "__", "__", "__", "1$", "__", "__", "3$", "__", "__" },
									{ "2$", "**", "**", "**", "**", "**", "**", "**", "M_" },
									{ "__", "__", "__", "1$", "__", "__", "3$", "__", "__" } };

		// ............................|0 ..|01 ...|02 ..|03 ..|04 ..|05 ..|06 ..|07 .|08|
		String[][] playerChairs = { { "VV", "VV", "VV", "1k", "VV", "VV", "3P", "VV", "VV" },
									{ "2k", "**", "**", "**", "**", "**", "**", "**", "VV" },
									{ "VV", "VV", "VV", "1P", "VV", "VV", "3K", "VV", "VV" } };
		
		TableSeat[][] tableSeats = converter.to(TABLE_VALUES);
		GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
		GameManager gameManager = new GameManager(teams, cardManager, characterArray, tableSeats, 27);
		gameManager.start();
		//System.out.println(converter.cToString(characterArray));
		List<Player> winners = gameManager.whoWin();
		assertEquals(2, gameManager.whoWin().size());
		assertEquals(J1.getTeam(), winners.get(0).getTeam());
		assertEquals(J3.getTeam(), winners.get(1).getTeam());
	}
	
	@Test
	public void _2playsWithMoveAction() {
		
		CardManagerImpl cardManager = new CardManagerImpl() {
			@Override
			protected void fillCards(List<CardType> chooseCard) {
				for (int i = 1; i <= 16; i++) {
					chooseCard.add(CardType.MOVE);
				}
				for (int i = 1; i <= 16; i++) {
					chooseCard.add(CardType.KNIFE);
				}
				for (int i = 1; i <= 16; i++) {
					chooseCard.add(CardType.GUN);
				}
			}
		};
		
		J1.addCard(CardType.KNIFE);
		J1.addCard(CardType.KNIFE);
		J1.addCard(CardType.KNIFE);
		J1.addCard(CardType.KNIFE);
		J1.addCard(CardType.KNIFE);
		
		J2.addCard(CardType.GUN);
		J2.addCard(CardType.GUN);
		J2.addCard(CardType.GUN);
		J2.addCard(CardType.MOVE);
		J2.addCard(CardType.MOVE);
		
		// ...........................|0 ...|01 ...|02 .|03 ...|04 ..|05 .|06 ..|07 ..|08|
		String[][] TABLE_VALUES = { { "__", "__", "2$", "__", "__", "__", "__", "__", "__" },
									{ "__", "**", "**", "**", "**", "**", "**", "**", "__" },
									{ "__", "__", "1$", "1$", "__", "__", "__", "__", "__" } };

		// ............................|0 ..|01 ...|02 ..|03 ..|04 ..|05 ..|06 ..|07 .|08|
		String[][] playerChairs = { { "VV", "VV", "1K", "1k", "3_", "VV", "VV", "VV", "VV" },
									{ "VV", "**", "**", "**", "**", "**", "**", "**", "VV" },
									{ "VV", "VV", "VV", "2P", "2P", "VV", "VV", "2P", "VV" } };
		
		TableSeat[][] tableSeats = converter.to(TABLE_VALUES);
		GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
		GameManager gameManager = new GameManager(teams, cardManager, characterArray, tableSeats, TOTAL_MONEY);
		gameManager.start();
		//System.out.println(converter.cToString(characterArray));
		List<Player> winners = gameManager.whoWin();
		assertEquals(1, gameManager.whoWin().size());
		assertEquals(J2.getTeam(), winners.get(0).getTeam());
	}
	
	@Ignore
	@Test
	public void simulacion() {		
		// ...........................|0 ...|01 ...|02 .|03 ...|04 ..|05 .|06 ..|07 ..|08|
		String[][] TABLE_VALUES = { { "PG", "_G", "kG", "1$", "__", "__", "P_", "__", "P_" },
									{ "2$", "**", "**", "**", "**", "**", "**", "**", "GP" },
									{ "kG", "__", "__", "GP", "__", "GP", "3$", "__", "k_" } };

		// ............................|0 ..|01 ...|02 ..|03 ..|04 ..|05 ..|06 ..|07 .|08|
		String[][] playerChairs = { { "VV", "VV", "3P", "1kP", "VV", "3k", "VV", "VV", "VV" },
									{ "VV", "**", "**", "**", "**", "**", "**", "**", "VV" },
									{ "2_", "VV", "VV", "2P", "VV", "VV", "1Pk", "VV", "VV" } };
		
		TableSeat[][] tableSeats = converter.to(TABLE_VALUES);
		GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
		J1.setHuman(true);
		GameManager gameManager = new GameManager(teams, characterArray, tableSeats, TOTAL_MONEY);
		gameManager.start();
		System.out.println(converter.cToString(characterArray));
	}

}
