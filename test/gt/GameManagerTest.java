package gt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import gm.Cake;
import gm.CardManager;
import gm.CardManagerImpl;
import gm.ExternalDataGetter;
import gm.GameCharacter;
import gm.GameManager;
import gm.GameTable;
import gm.Player;
import gm.TableSeat;
import gm.Writter;
import gm.cards.CakeUtils;
import gm.info.CardType;
import gm.info.Side;
import gm.pojos.Position;
import gt.extras.Converter;

public class GameManagerTest {
	
	private static final int TOTAL_MONEY = 100;

	private Player J1;

	private Player J2;

	private Player J3;

	private Converter converter;

	private List<Player> teams;
	
	private Scanner scanner;
	
	private ExternalDataGetter externalDataGetter;
	
	private Writter writter;

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
		this.writter = new Writter() {
			
			@Override
			public void log(String text) {
				System.out.println(text);
			}
		};
		this.scanner = new Scanner(System.in);
		this.externalDataGetter = new ExternalDataGetterConsole();
	}
	
	@After
	public void tearDown() throws Exception {
		scanner.close();
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
		GameManager gameManager = new GameManager(teams, cardManager, characterArray, tableSeats, 
				TOTAL_MONEY, externalDataGetter, writter);
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
		GameManager gameManager = new GameManager(teams, cardManager, characterArray, 
				tableSeats, 30, externalDataGetter, writter);
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
		GameManager gameManager = new GameManager(teams, cardManager, characterArray, 
				tableSeats, 27, externalDataGetter, writter);
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
		GameManager gameManager = new GameManager(teams, cardManager, characterArray, 
				tableSeats, TOTAL_MONEY, externalDataGetter, writter);
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
		GameManager gameManager = new GameManager(teams, characterArray, tableSeats, 
				TOTAL_MONEY, externalDataGetter, writter);
		gameManager.start();
	}
	
	@Ignore
	@Test
	public void actualizandoDados() {		
		// ...........................|0 ...|01 ...|02 .|03 ...|04 ..|05 .|06 ..|07 ..|08|
		String[][] TABLE_VALUES = { { "PG", "_G", "kG", "1$", "__", "__", "P_", "__", "P_" },
									{ "2$", "**", "**", "**", "**", "**", "**", "**", "GP" },
									{ "kG", "__", "__", "GP", "__", "GP", "3$", "__", "k_" } };

		// ............................|0 ..|01 ...|02 ..|03 ..|04 ..|05 ..|06 ..|07 .|08|
		String[][] playerChairs = { { "VV", "VV", "V", "1kP", "VV", "V", "VV", "VV", "VV" },
									{ "2_", "**", "**", "**", "**", "**", "**", "**", "VV" },
									{ "V", "VV", "VV", "V", "VV", "VV", "1_", "1Pk", "VV" } };
		
		TableSeat[][] tableSeats = converter.to(TABLE_VALUES);
		GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
		J1.setHuman(true);
		J1.addCard(CardType.CAKE);
		J2.addCard(CardType.MOVE);
		J2.addCard(CardType.MOVE);
		J2.addCard(CardType.MOVE_CAKE);
		List<Player> teams = new ArrayList<Player>();
		teams.add(J1);
		teams.add(J2);
		GameManager gameManager = new GameManager(teams, characterArray, tableSeats, 
				TOTAL_MONEY, externalDataGetter, writter);
		GameTable gameTable = gameManager.getGameTable();
		gameTable.add(new Cake(new Position(3, 0), "2", gameTable));
		gameManager.start();
	}
	
	@Test
    public void moverFatalCakeEntocesYaNoHuir() {       
        // ...........................|0 ...|01 ...|02 .|03 ...|04 ..|05 .|06 ..|07 ..|08|
        String[][] TABLE_VALUES = { { "PG", "_G", "kG", "_k", "__", "__", "P_", "__", "P_" },
                                    { "2$", "**", "**", "**", "**", "**", "**", "**", "GP" },
                                    { "kG", "__", "__", "GP", "__", "GP", "3$", "__", "k_" } };

        // ............................|0 ..|01 ...|02 ..|03 ..|04 ..|05 ..|06 ..|07 .|08|
        String[][] playerChairs = { { "1k_", "VV", "V", "2_", "1", "V", "VV", "VV", "VV" },
                                    { "VV", "**", "**", "**", "**", "**", "**", "**", "1_" },
                                    { "VVs", "VV", "VV", "V", "VV", "VV", "V", "1Pk", "VV" } };
        
        TableSeat[][] tableSeats = converter.to(TABLE_VALUES);
        GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
        J1.addCard(CardType.CAKE);
        J2.addCard(CardType.MOVE);
        J2.addCard(CardType.MOVE_CAKE);
        List<Player> teams = new ArrayList<Player>();
        teams.add(J2);
        teams.add(J1);
        CardManager cardManager = new CardManagerImpl(){
            
            @Override
            protected void fillCards(List<CardType> chooseCard) {
                for (int i = 1; i <= 9; i++) {
                    chooseCard.add(CardType.KNIFE);
                }
            }
            
        };
        GameManager gameManager = new GameManager(teams, cardManager, characterArray, 
        		tableSeats, TOTAL_MONEY, externalDataGetter, writter){
            
            @Override
            protected boolean canKeepPlaying(List<Player> players, int totalMoney) {
                return players.size() > 1 && totalMoney > 0 && getCycles() < 1;
            }
            
        };
        GameTable gameTable = gameManager.getGameTable();
        gameTable.add(new Cake(new Position(3, 0), "3", gameTable));
        gameManager.start();
        assertTrue(J2.hasCard(CardType.MOVE));
    }
	
	@Test
    public void moverCakeEntocesYaNoHuir() {       
        // ...........................|0 ...|01 ...|02 .|03 ...|04 ..|05 .|06 ..|07 ..|08|
        String[][] TABLE_VALUES = { { "PG", "_G", "kG", "1$", "__", "__", "P_", "__", "P_" },
                                    { "2$", "**", "**", "**", "**", "**", "**", "**", "GP" },
                                    { "kG", "__", "__", "GP", "__", "GP", "3$", "__", "k_" } };

        // ............................|0 ..|01 ...|02 ..|03 ..|04 ..|05 ..|06 ..|07 .|08|
        String[][] playerChairs = { { "1kP_", "VV", "V", "2_", "V", "V", "VV", "VV", "VV" },
                                    { "V", "**", "**", "**", "**", "**", "**", "**", "1_" },
                                    { "V", "VV", "VV", "V", "VV", "VV", "V", "1Pk", "VV" } };
        
        TableSeat[][] tableSeats = converter.to(TABLE_VALUES);
        GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
        J1.addCard(CardType.CAKE);
        J2.addCard(CardType.MOVE);
        J2.addCard(CardType.MOVE_CAKE);
        List<Player> teams = new ArrayList<Player>();
        teams.add(J2);
        teams.add(J1);
        CardManager cardManager = new CardManagerImpl(){
            
            @Override
            protected void fillCards(List<CardType> chooseCard) {
                for (int i = 1; i <= 9; i++) {
                    chooseCard.add(CardType.KNIFE);
                }
            }
            
        };
        GameManager gameManager = new GameManager(teams, cardManager, characterArray, 
        		tableSeats, TOTAL_MONEY, externalDataGetter, writter){
            
            @Override
            protected boolean canKeepPlaying(List<Player> players, int totalMoney) {
                return players.size() > 1 && totalMoney > 0 && getCycles() < 1;
            }
            
        };
        GameTable gameTable = gameManager.getGameTable();
        gameTable.add(new Cake(new Position(3, 0), "3", gameTable));
        gameManager.start();
        assertTrue(J2.hasCard(CardType.MOVE));
    }
	
	@Test
    public void noMoverMismoPersonajesDosVeces() {       
        // ...........................|0 ...|01 ...|02 .|03 ...|04 ..|05 .|06 ..|07 ..|08|
        String[][] TABLE_VALUES = { { "PG", "_G", "kG", "1$", "__", "__", "P_", "__", "P_" },
                                    { "2$", "**", "**", "**", "**", "**", "**", "**", "GP" },
                                    { "kG", "__", "__", "GP", "__", "GP", "3$", "__", "k_" } };

        // ...........................|0 ..|01 ...|02 ..|03 ..|04 ..|05 ..|06 ..|07 .|08|
        String[][] playerChairs = { { "1k", "VV", "VV", "2_", "VV", "VV", "VV", "VV", "VV" },
                                    { "2_", "**", "**", "**", "**", "**", "**", "**", "1_" },
                                    { "VV", "VV", "VV", "VV", "VV", "2_", "2_", "VV", "VV" } };
        
        TableSeat[][] tableSeats = converter.to(TABLE_VALUES);
        GameCharacter[][] characterArray = converter.toCharacterArray(playerChairs);
        J1.addCard(CardType.MOVE);
        J1.addCard(CardType.MOVE);
        J1.addCard(CardType.MOVE_CAKE);
        J1.addCard(CardType.MOVE_CAKE);
        J1.addCard(CardType.MOVE_CAKE);
        J2.addCard(CardType.MOVE);
        J2.addCard(CardType.MOVE_CAKE);
        List<Player> teams = new ArrayList<Player>();
        teams.add(J1);
        teams.add(J2);
        CardManager cardManager = new CardManagerImpl(){
            
            @Override
            protected void fillCards(List<CardType> chooseCard) {
                for (int i = 1; i <= 9; i++) {
                    chooseCard.add(CardType.KNIFE);
                }
            }
            
        };
        GameManager gameManager = new GameManager(teams, cardManager, characterArray, 
        		tableSeats, TOTAL_MONEY, externalDataGetter, writter){
            
            @Override
            protected boolean canKeepPlaying(List<Player> players, int totalMoney) {
                return players.size() > 1 && totalMoney > 0 && getCycles() < 1;
            }
            
        };
        System.out.println(converter.cToString(characterArray));
        gameManager.start();
        assertTrue(J1.hasCard(CardType.MOVE));
    }
	
	private class ExternalDataGetterConsole implements ExternalDataGetter{
		@Override
		public CardType getCardToPlay(Player player) {
			System.out.println("Choose one card: " + player.getCards());
			int numberCard = Integer.parseInt(scanner.nextLine());
			if(numberCard > 5){
				return null;
			}
			return player.getCards().get(numberCard - 1);
		}

		@Override
		public boolean getcontinue() {
			System.out.println("Use 'Move Card':");
			String yesOrNo = scanner.nextLine();
			return yesOrNo.equalsIgnoreCase("Y");
		}
		
		@Override
		public Position getPositionToMove(List<Position> emptySeats) {
			System.out.print("CakePositions: " + emptySeats);
			Integer numberSeat = Integer.parseInt(scanner.nextLine());
			return emptySeats.get(numberSeat - 1);
		}

		private Position getPosition() {
			System.out.println("Write position (X,Y)");
			String coordinates = scanner.nextLine();
			int x = Integer.valueOf(coordinates.substring(0, 1));
			int y = Integer.valueOf(coordinates.substring(2));
			return new Position(x - 1, y - 1);
		}

		@Override
		public Position getNewPositionCake() {
			System.out.println("Cake position: ");
			return getPosition();
		}

		@Override
		public Side getWhereMoveCake() {
			System.out.println("New position: (R/L)");
			String result = scanner.nextLine();
			Side side;
			if(result.equalsIgnoreCase("R")){
				side = Side.CLOCK_DIRECTION;
			}else{
				side = Side.OPOSITIVE_CLOCK_DIRECTION;
			}
			return side;
		}

		@Override
		public List<Position> getSleepPositions(List<Position> list) {
				System.out.println("Sleep position: " + list);
				System.out.println("Cuantos: ");
				Integer counter = Integer.parseInt(scanner.nextLine());
				List<Position> sleepPositions = new ArrayList<Position>();
				for (int i = 0; i < counter; i++) {
					System.out.println("chose: ");
					Integer indexPosition = Integer.parseInt(scanner.nextLine());
					sleepPositions.add(list.get(indexPosition - 1));
				}
				return sleepPositions;
		}

		@Override
		public Position getPositionCake(List<Cake> cakes) {
			List<Position> cakePositions = CakeUtils.getCharacterByTeam(cakes);
			System.out.print("CakePositions: " + cakePositions);
			Integer numberCard = Integer.parseInt(scanner.nextLine());
			return cakePositions.get(numberCard - 1);
		}

		@Override
		public CardType getChangedCard(Player player) {
			System.out.println("Chose one card to change:");
			return getCardToPlay(player);
		}

		@Override
		public Position getChosePosition(List<Position> validPositions) {
			System.out.print("AttackerPositions: " + validPositions);
			Integer index = Integer.parseInt(scanner.nextLine());
			return validPositions.get(index - 1);
		}
	}

}
