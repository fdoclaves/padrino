package gt;

import static org.junit.Assert.*;

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
import gm.PlayManager;
import gm.Player;
import gm.TableSeat;
import gm.exceptions.GameException;
import gm.exceptions.GameWarning;
import gt.extras.Converter;
import gm.ia.IA_Manager;
import gm.ia.pojos.InfoAction;
import gm.info.CardType;

public class SimulationTest {
	
	private static final String J1 = "1";

	private static final String J2 = "2";
	
	private static final String J3 = "3";

	private PlayManager donePlays;
	
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
		gameTable = new GameTable(tableSeats);
		characterArray = converter.toCharacterArray(playerChairs);
		
	}
	
	@Test
    public void moneyLimitado(){
        fail("money limitado");
    }
	
	@Test
    public void QuienPierde(){
        fail("QuienPierde");
        fail("fin juego por $");
        fail("fin juego un solo jugador");
    }
	
	@Test
	public void simulacion2_guns_knives_cakes() throws GameException, GameWarning {
		IA_Manager ia_Manager = new IA_Manager(gameTable);
		CardManagerImpl cardManager = new CardManagerImpl(){
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
		donePlays = new PlayManager(characterArray, gameTable,cardManager, players);
		int currentGamers = players.size();
		int counterGamers = 0;
		System.out.println(converter.cToString(donePlays.getChairs()));
		for (int i = 0; i < 19; i++) {
			int nextPlayerIndex = getNextPlayerIndex(counterGamers);
			play(ia_Manager, players.get(counterGamers), players.get(nextPlayerIndex), currentGamers, i);
			assertEquals(5, players.get(counterGamers).getCards().size());
			assertEquals(27, cardManager.getTotalCard());
			counterGamers = nextPlayerIndex;
		}
		System.out.println("fin");
	}

	@Test
	public void simulacion1_knives_guns_sleeps() throws GameException, GameWarning {
		IA_Manager ia_Manager = new IA_Manager(gameTable);
		CardManagerImpl cardManager = new CardManagerImpl(){
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
		donePlays = new PlayManager(characterArray, gameTable,cardManager, players);
		int currentGamers = players.size();
		int counterGamers = 0;
		System.out.println(converter.cToString(donePlays.getChairs()));
		for (int i = 0; i < 19; i++) {
			int nextPlayerIndex = getNextPlayerIndex(counterGamers);
			play(ia_Manager, players.get(counterGamers), players.get(nextPlayerIndex), currentGamers, i);
			assertEquals(5, players.get(counterGamers).getCards().size());
			assertEquals(13, cardManager.getTotalCard());
			counterGamers = nextPlayerIndex;
		}
		System.out.println("fin");
	}

	private int getNextPlayerIndex(int counterGamers) {
		if(counterGamers==2){
			return 0;
		}
		return counterGamers+1;
	}

	private void play(IA_Manager ia_Manager, Player gaming, Player next, int currentGamers, int counter)
			throws GameException, GameWarning {
		System.out.println("Team: "+gaming.getTeam());
		System.out.println("Cards:"+gaming.getCards());
		donePlays.startTurn(gaming);
		InfoAction whoKill = ia_Manager.whoKill(characterArray, gaming, next.getTeam(), currentGamers);//falta
		System.out.println(whoKill.getReason());
		Card usedcard = whoKill.getCards().get(0);
		System.out.println("Used card: "+usedcard.getType());
		donePlays.play(usedcard);
		donePlays.finishTurn();
		System.out.println(converter.cToString(donePlays.getChairs()));
		System.out.println("Money: $"+ gaming.getMoney());
		for (Cake cake : gameTable.getCakeList()) {
			System.out.println("cake:" + cake.getPosition());
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
