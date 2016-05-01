package gm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gm.cards.ChangeCard;
import gm.exceptions.GameException;
import gm.exceptions.GameWarning;
import gm.ia.CharacterUtils;
import gm.info.CardType;
import gm.info.GameMessages;
import gm.info.TableObjects;

public class PlaysManager {

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

	private Map<String, Player> playersMap;
	
	private List<Player> playersList;
	
	private List<CardType> beforeCardType;
	
	private String team;
	
	private boolean useChangeCard;

	public PlaysManager(GameCharacter[][] characters, GameTable gameTable, CardManager cardManager, List<Player> players) {
		this(characters, gameTable,players);
		this.cardManager = cardManager;
	}
	
	public PlaysManager(GameCharacter[][] characters, GameTable gameTable, List<Player> players) {
		this.cardManager = new CardManagerImpl();
		this.characters = characters;
		this.gameTable = gameTable;
		this.beforeCharacters = new GameCharacter[gameTable.getMaxY()][gameTable.getMaxX()];
		this.playersList = players;
		this.playersMap = new HashMap<String, Player>();
		for(Player player : players){
			this.playersMap.put(player.getTeam(), player);
		}
		fillCounterCharacters(characters);
	}

	private void fillCounterCharacters(GameCharacter[][] charactersArray) {
		for (GameCharacter[] gameCharacters : charactersArray) {
			for (GameCharacter gameCharacter : gameCharacters) {
				if(CharacterUtils.isValid(gameCharacter)){
					playersMap.get(gameCharacter.getTeam()).plusCounterCharacterOne();
				}
			}
		}
	}

	public void startTurn(final Player player) {
		this.useChangeCard = false;
		this.beforeCardType = copy(player.getCards());
		this.team = player.getTeam();
		this.playedCardsCounter = 1;
		this.startTurn = true;
		for (int i = 0; i < gameTable.getMaxY(); i++) {
			for (int j = 0; j < gameTable.getMaxX(); j++) {
					beforeCharacters[i][j] = characters[i][j].cloneCharacters();

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

	private void boomCake() {
		List<Cake> boomCakes = new ArrayList<Cake>();
		for (Cake cake : gameTable.getCakeList()) {
			if (team.equals(cake.getTeam())) {
				boomCakes.add(cake);
			}
		}
		for (Cake cake : boomCakes) {
			cake.boom(characters, playersMap);
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
		card.doAction(characters, playersMap);
		if(card instanceof ChangeCard){
			useChangeCard = true;
		}
		playersMap.get(team).removeCard(cardType);
		cardManager.setCard(cardType);
		if (playedCardsCounter == 2) {
			playersMap.get(team).addCard(cardManager.getCard());
			finishTurn();
		} else {
			playedCardsCounter++;
		}
	}

	public void resert() {
		characters = beforeCharacters;
		money = 0;
		gameTable.setCakeList(startCakeList);
		playersMap.get(team).setCardList(beforeCardType);
		cardManager.removeLastCard();
	}

	private void validatePlays(CardType currentPlay, int playedCardsCounter) throws GameException {
	    if(!playersMap.get(team).hasCard(currentPlay)){
	        throw new GameException(GameMessages.NO_TIENES_CARD);
	    }
		if (playedCardsCounter == 2) {
			if (useChangeCard) {
				throw new GameException(GameMessages.CHANGE_CARD_BEFORE);
			}
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
				if (!character.isEmpty()) {
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

	public void finishTurn() {
		removeZzz(team);
		playedCardsCounter++;
		Player currentPlayer = playersMap.get(team);
		currentPlayer.addCard(cardManager.getCard());
		currentPlayer.addMoney(getMoney());
		removeFromListLosers();
	}

	private void removeFromListLosers() {
		List<Player> playersToDelete = new ArrayList<Player>();
		for (Player player : playersList) {
			if(player.getCounterCharacters() <= 0){
				playersToDelete.add(player);
			}
		}
		for (Player player : playersToDelete) {
			cardManager.setCards(player.getCards());
			player.setCardList(new ArrayList<CardType>());
			playersList.remove(player);
		}
	}

	private int getMoney() {
		int wonMoney = money;
		money = 0;
		return wonMoney;
	}

	private void removeZzz(String team) {
		for (GameCharacter[] characterArray : characters) {
			for (GameCharacter character : characterArray) {
				if (!character.isEmpty() && character.isTeam(team)) {
					character.wakeUp();
				}
			}
		}
	}
}
