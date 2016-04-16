package gm;

import java.util.ArrayList;
import java.util.List;

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

	private String team;

	private List<Cake> startCakeList;

	public PlayManager(GameCharacter[][] characters, GameTable gameTable) {
		this.characters = characters;
		this.gameTable = gameTable;
		this.beforeCharacters = new GameCharacter[gameTable.getMaxY()][gameTable.getMaxX()];
	}

	public void startTurn(String team) throws GameException {
		if (startTurn) {
			finishTurn();
		}
		this.team = team;
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
		this.money = getMoney(team);
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

	public void play(Card card) throws GameException, GameWarning {
		if (!startTurn) {
			throw new GameException(NO_STARTED);
		}
		validatePlays(card.getType(), playedCardsCounter);
		card.inicialize(gameTable);
		card.validateAction(characters, team);
		card.doAction(characters);
		if (playedCardsCounter == 2) {
			finishTurn();
		} else {
			playedCardsCounter++;
		}
	}

	public void resert() {
		characters = beforeCharacters;
		money = 0;
		gameTable.setCakeList(startCakeList);
	}

	private void validatePlays(CardType currentPlay, int playedCardsCounter) throws GameException {
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
