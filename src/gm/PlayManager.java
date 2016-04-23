package gm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gm.exceptions.GameException;
import gm.exceptions.GameWarning;
import gm.info.CardType;
import gm.info.GameMessages;
import gm.info.TableObjects;

public class PlayManager {

	private static final TableObjects CASINOS = TableObjects.CASINOS;

	private static final TableObjects BARS = TableObjects.BAR;

	private static final TableObjects RESTAUNTS = TableObjects.RESTAURANTS;

	private static final TableObjects MACHINE = TableObjects.MACHINE;

	public final GameTable gameTable;

	private final String NO_STARTED = "NO HA EMPEZADO";

	private GameCharacter[][] characters;

	private int money;

	private int playedCardsCounter;

	private GameCharacter[][] beforeCharacters;

	private boolean startTurn;

	private List<Cake> startCakeList;
	
	private CardManager cardManager;

	private Map<String, Player> players;
	
	private List<CardType> beforeCardType;
	
	private String team;

	public PlayManager(GameCharacter[][] characters, GameTable gameTable, CardManager cardManager, List<Player> players) {
		this(characters, gameTable,players);
		this.cardManager = cardManager;
	}
	
	public PlayManager(GameCharacter[][] characters, GameTable gameTable, List<Player> players) {
		this.cardManager = new CardManagerImpl();
		this.characters = characters;
		this.gameTable = gameTable;
		this.beforeCharacters = new GameCharacter[gameTable.getMaxY()][gameTable.getMaxX()];
		this.players = new HashMap<String, Player>();
		for(Player player : players){
			this.players.put(player.getTeam(), player);
		}
	}

	public void startTurn(final Player player) throws GameException {
		this.beforeCardType = copy(player.getCards());
		this.team = player.getTeam();
		this.playedCardsCounter = 1;
		this.startTurn = true;
		for (int i = 0; i < gameTable.getMaxY(); i++) {
			for (int j = 0; j < gameTable.getMaxX(); j++) {
				if (characters[i][j] == null) {
					beforeCharacters[i][j] = null;
				} else {
					beforeCharacters[i][j] = characters[i][j].cloneCharacters();
				}
			}
		}
		boomCake();
		startCakeList = new ArrayList<Cake>();
		for (Cake cake : gameTable.getCakeList()) {
			this.startCakeList.add(cake);
		}
		this.money = getMoney(player.getTeam());
	}

	private List<CardType> copy(List<CardType> cards) {
		List<CardType> respaldo = new ArrayList<CardType>();
		for (CardType cardType : cards) {
			respaldo.add(cardType);
		}
		return respaldo;
	}

	private void boomCake() throws GameException {
		List<Cake> boomCakes = new ArrayList<Cake>();
		for (Cake cake : gameTable.getCakeList()) {
			if (team.equals(cake.getTeam())) {
				boomCakes.add(cake);
			}
		}
		for (Cake cake : boomCakes) {
			cake.boom(characters);
		}
	}

	public void play(final Card card) throws GameException, GameWarning {
		CardType cardType = card.getType();
		if (!startTurn) {
			throw new GameException(NO_STARTED);
		}
		validatePlays(cardType, playedCardsCounter);
		card.inicialize(gameTable);
		card.validateAction(characters, team);
		card.doAction(characters);
		players.get(team).removeCard(cardType);
		cardManager.setCard(cardType);
		if (playedCardsCounter == 2) {
			players.get(team).addCard(cardManager.getCard());
			finishTurn();
		} else {
			playedCardsCounter++;
		}
	}

	public void resert() {
		characters = beforeCharacters;
		money = 0;
		gameTable.setCakeList(startCakeList);
		players.get(team).setCardList(beforeCardType);
		cardManager.removeLastCard();
	}

	private void validatePlays(CardType currentPlay, int playedCardsCounter) throws GameException {
	    if(!players.get(team).hasCard(currentPlay)){
	        throw new GameException(GameMessages.NO_TIENES_CARD);
	    }
		if (playedCardsCounter == 2) {
			if (currentPlay != CardType.MOVE) {
				throw new GameException(GameMessages.TWO_ATTACK_ACTIONS);
			}
		}
		if (playedCardsCounter > 2) {
			throw new GameException(GameMessages.THREE_ACTIONS);
		}
	}

	public GameCharacter[][] getChairs() {
		return characters;
	}

	private int getMoney(String team) {
		int restaunts = 0;
		int bars = 0;
		int casinos = 0;
		int king = 0;
		boolean hasMachine = false;
		for (int x = 0; x < gameTable.getMaxX(); x++) {
			for (int y = 0; y < gameTable.getMaxY(); y++) {
				GameCharacter character = characters[y][x];
				TableSeat valueTable = gameTable.getTableSeats()[y][x];
				if (character != null) {
					if (character.isTeam(team)) {
						if (valueTable.has(RESTAUNTS)) {
							restaunts++;
						}
						if (valueTable.has(BARS)) {
							bars++;
						}
						if (valueTable.has(CASINOS)) {
							casinos++;
						}
						if (character.isKing()) {
							king++;
						}
						if (valueTable.has(MACHINE)) {
							hasMachine = true;
						}
					}

				}
			}
		}
		int money = restaunts * restaunts + bars * bars + casinos * casinos + king;
		if (hasMachine) {
			money = money * 2;
		}
		return money;
	}

	public int getMoney() {
		int wonMoney = money;
		money = 0;
		return wonMoney;
	}

	public void finishTurn() {
		removeZzz(team);
		playedCardsCounter++;
		Player player = players.get(team);
		player.addCard(cardManager.getCard());
	}

	private void removeZzz(String team) {
		for (GameCharacter[] characterArray : characters) {
			for (GameCharacter character : characterArray) {
				if (character != null && character.isTeam(team)) {
					character.wakeUp();
				}
			}
		}
	}
}
