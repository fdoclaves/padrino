package gt;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import gm.Cake;
import gm.Card;
import gm.CardManager;
import gm.CardManagerImpl;
import gm.GameCharacter;
import gm.GameTable;
import gm.Player;
import gm.PlaysManager;
import gm.TableSeat;
import gm.TurnsManager;
import gm.exceptions.GameException;
import gm.exceptions.GameWarning;
import gm.ia.IA_PlaysController;
import gm.ia.PlaysController;
import gm.info.CardType;
import gt.extras.Converter;

public class SimulationTest {
	
	private static final int TOTAL_MONEY = 100;

	private static final String J1 = "1";

	private static final String J2 = "2";
	
	private static final String J3 = "3";

	private PlaysManager donePlays;
	
	// |0 |01 |02 |03 |04 |05 |06 |07 |08|
	private String[][] TABLE_VALUES = { { "PG", "_G", "kG", "1$", "__", "__", "P_", "__", "P_" },
										{ "2$", "**", "**", "**", "**", "**", "**", "**", "GP" },
										{ "kG", "__", "__", "GP", "__", "GP", "3$", "__", "k_" } };

	// |0 |01 |02 |03 |04 |05 |06 |07 |08|
	private String[][] playerChairs = { { "1_", "1P", "2_", "1k", "1k", "1P", "1Z", "1P", "V" },
										{ "1P", "**", "**", "**", "**", "**", "**", "**", "2_" },
										{ "2_", "1_", "2_", "2_", "V", "3_", "2_", "2_", "2Z" } };
	
	private Converter converter;

	private GameTable gameTable;
	
	private GameCharacter[][] characterArray;
	
	@Before
	public void setUp() throws Exception {
		converter = new Converter(9, 3);
		TableSeat[][] tableSeats = converter.to(TABLE_VALUES);
		gameTable = new GameTable(tableSeats, TOTAL_MONEY);
		characterArray = converter.toCharacterArray(playerChairs);
	}
	
	@Test
    public void simulacion2_guns_knives_cakes_moves() throws GameException, GameWarning {
        CardManagerImpl cardManager = new CardManagerImpl(){
            @Override
            protected void fillCards(List<CardType> chooseCard) {
                for (int i = 1; i <= 9; i++) {
                    chooseCard.add(CardType.MOVE);
                }
                for (int i = 1; i <= 6; i++) {
                    chooseCard.add(CardType.SLEEP);
                }
                for (int i = 1; i <= 4; i++) {
                    chooseCard.add(CardType.BOOM);
                }
                for (int i = 1; i <= 6; i++) {
                    chooseCard.add(CardType.MOVE_CAKE);
                }
                for (int i = 1; i <= 8; i++) {
                    chooseCard.add(CardType.CAKE);
                }
                for (int i = 1; i <= 9; i++) {
                    chooseCard.add(CardType.GUN);
                }
                for (int i = 1; i <= 9; i++) {
                    chooseCard.add(CardType.KNIFE);
                }
            }
        };
        
        Player player1 = new Player(J1, getCardsToStart(cardManager));
        Player player2 = new Player(J2, getCardsToStart(cardManager));
        Player player3 = new Player(J3, getCardsToStart(cardManager));
        List<Player> players = new ArrayList<Player>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        TurnsManager playersManager = new TurnsManager(players);
        donePlays = new PlaysManager(characterArray, gameTable,cardManager, players);
        int currentGamers = players.size();
        System.out.println(converter.cToString(donePlays.getChairs()));
        for (int i = 0; i < 19 && players.size() > 1; i++) {
            List<Player> currentPlayer = playersManager.getCurrentPlayer();
            play(currentPlayer.get(0), currentPlayer.get(1), currentGamers, i);
        }
        System.out.println("fin");
    }
	
	@Test
	public void simulacion2_guns_knives_cakes() throws GameException, GameWarning {
		CardManagerImpl cardManager = new CardManagerImpl(){
			@Override
			protected void fillCards(List<CardType> chooseCard) {
				for (int i = 1; i <= 6; i++) {
					chooseCard.add(CardType.SLEEP);
				}
				for (int i = 1; i <= 4; i++) {
					chooseCard.add(CardType.BOOM);
				}
				for (int i = 1; i <= 6; i++) {
					chooseCard.add(CardType.MOVE_CAKE);
				}
				for (int i = 1; i <= 8; i++) {
					chooseCard.add(CardType.CAKE);
				}
				for (int i = 1; i <= 9; i++) {
					chooseCard.add(CardType.GUN);
				}
				for (int i = 1; i <= 9; i++) {
					chooseCard.add(CardType.KNIFE);
				}
			}
		};
		
		Player player1 = new Player(J1, getCardsToStart(cardManager));
		Player player2 = new Player(J2, getCardsToStart(cardManager));
		Player player3 = new Player(J3, getCardsToStart(cardManager));
		List<Player> players = new ArrayList<Player>();
		players.add(player1);
		players.add(player2);
		players.add(player3);
		TurnsManager playersManager = new TurnsManager(players);
		donePlays = new PlaysManager(characterArray, gameTable,cardManager, players);
		int currentGamers = players.size();
		System.out.println(converter.cToString(donePlays.getChairs()));
		for (int i = 0; i < 19 && players.size() > 1; i++) {
			List<Player> currentPlayer = playersManager.getCurrentPlayer();
			play(currentPlayer.get(0), currentPlayer.get(1), currentGamers, i);
		}
		System.out.println("fin");
	}


	@Test
	public void simulacion1_knives_guns_sleeps() throws GameException, GameWarning {
		
		CardManagerImpl cardManager = new CardManagerImpl(){
			@Override
			protected void fillCards(List<CardType> chooseCard) {
				for (int i = 1; i <= 6; i++) {
					chooseCard.add(CardType.SLEEP);
				}
				for (int i = 1; i <= 4; i++) {
					chooseCard.add(CardType.BOOM);
				}
				for (int i = 1; i <= 9; i++) {
					chooseCard.add(CardType.GUN);
				}
				for (int i = 1; i <= 9; i++) {
					chooseCard.add(CardType.KNIFE);
				}
			}
		};
		
		Player player1 = new Player(J1, getCardsToStart(cardManager));
		Player player2 = new Player(J2, getCardsToStart(cardManager));
		Player player3 = new Player(J3, getCardsToStart(cardManager));
		List<Player> players = new ArrayList<Player>();
		players.add(player1);
		players.add(player2);
		players.add(player3);
		TurnsManager playersManager = new TurnsManager(players);
		donePlays = new PlaysManager(characterArray, gameTable,cardManager, players);
		int currentGamers = players.size();
		System.out.println(converter.cToString(donePlays.getChairs()));
		for (int i = 0; i < 19 && players.size() > 1; i++) {
			List<Player> currentPlayer = playersManager.getCurrentPlayer();
			play(currentPlayer.get(0), currentPlayer.get(1), currentGamers, i);
		}
		System.out.println("fin");
	}

	private void play(Player gaming, Player next, int currentGamers, int counter)
			throws GameException, GameWarning {
		System.out.println("Team: "+gaming.getTeam());
		System.out.println("Cards:"+gaming.getCards());
		donePlays.startTurn(gaming);
		System.out.println(converter.cToString(donePlays.getChairs()));
		PlaysController ia_Manager = new IA_PlaysController(gameTable);//actualizado con Zzz y muertesXpastel
		Card card = ia_Manager.get1stCard(characterArray, gaming, next.getTeam(), currentGamers);
		System.out.println(card.getReason());
		System.out.println("Used card: " + card.getType());
		donePlays.play(card);
		donePlays.finishTurn();
		System.out.println(converter.cToString(donePlays.getChairs()));
		System.out.println("Money: $"+ gaming.getMoney());
		for (Cake cake : gameTable.getCakeList()) {
			System.out.println("cake:" + cake.getPosition()+", team:"+cake.getTeam());
		}
		System.out.println("--------------------------------------------------------"+counter);
	}

	private List<CardType> getCardsToStart(CardManager cardManager) {
		List<CardType> cards = new ArrayList<CardType>();
		for (int i = 0; i < 5; i++) {
			cards.add(cardManager.getCard());
		}
		return cards;
	}

}
