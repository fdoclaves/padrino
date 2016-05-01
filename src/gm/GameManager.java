package gm;

import java.util.ArrayList;
import java.util.List;

import gm.cards.ChangeCard;
import gm.exceptions.GameException;
import gm.exceptions.GameWarning;
import gm.ia.IA_PlaysController;
import gm.ia.PlaysController;
import gm.info.CardType;
import gt.extras.Converter;

public class GameManager {

	private List<Player> players;

	private TurnsManager turnsManager;

	private PlaysManager playsManager;

	private GameTable gameTable;

	private GameCharacter[][] characterArray;

	public GameManager(List<Player> players, CardManager cardManager, GameCharacter[][] characterArray,
			TableSeat[][] tableSeats, Integer totalMoney) {
		this.players = players;
		this.characterArray = characterArray;
		for (Player team : players) {
			addCardsToPlayerToStart(team, cardManager);
		}
		turnsManager = new TurnsManager(players);
		gameTable = new GameTable(tableSeats, totalMoney);
		playsManager = new PlaysManager(characterArray, gameTable, cardManager, players);
	}
	
	public GameManager(List<Player> players, GameCharacter[][] characterArray,
			TableSeat[][] tableSeats, Integer totalMoney) {
		this(players, new CardManagerImpl(), characterArray, tableSeats, totalMoney);
	}

	private void addCardsToPlayerToStart(Player team, CardManager cardManager) {
		while(team.getCards().size() < 5){
			team.addCard(cardManager.getCard());
		}
	}

	public void start() {
		while (players.size() > 1 && gameTable.getTotalMoney() > 0) {
			List<Player> currentPlayer = turnsManager.getCurrentPlayer();
			Player nowPlayer = currentPlayer.get(0);
			String nextTeam = currentPlayer.get(1).getTeam();
			System.out.println("-------------" + nowPlayer.getTeam() + "-----------------");
			playsManager.startTurn(nowPlayer);
			if(nowPlayer.isHuman()){
				humanPlays(nowPlayer, nextTeam);
			}else{
				iaPlays(nowPlayer, nextTeam);
			}
			playsManager.finishTurn();
			System.out.println(new Converter(8, 3).cToString(playsManager.getChairs()));
			for (Cake cake : gameTable.getCakeList()) {
				System.out.print("cake:" + cake.getPosition()+", team:"+cake.getTeam() + " ");
			}
		}
	}
	
	private void humanPlays(Player iaPlayer, String nextTeam) {
		PlaysController iaCardManager = new Human_PlaysController(gameTable);
		Card firstCard = iaCardManager.get1stCard(characterArray, iaPlayer, nextTeam, players.size());
		Card secondCard = null;
		try {
			if(firstCard != null){
				playsManager.play(firstCard);
			}
			secondCard = iaCardManager.get2ndCard(characterArray, iaPlayer, nextTeam, players.size(),
					firstCard);
			if (secondCard != null) {
				playsManager.play(secondCard);
			}
		} catch (GameException e) {
			e.printStackTrace();
			playsManager.resert();
			humanPlays(iaPlayer, nextTeam);
		} catch (GameWarning e) {
			System.out.println("Warning!");
			e.printStackTrace();
		}
	}

	private void iaPlays(Player iaPlayer, String nextTeam) {
		PlaysController iaCardManager = new IA_PlaysController(gameTable);
		Card firstCard = iaCardManager.get1stCard(characterArray, iaPlayer, nextTeam, players.size());
		try {
			if (!waitForBestAction(firstCard)) {
				playsManager.play(firstCard);
			}
			Card secondCard = iaCardManager.get2ndCard(characterArray, iaPlayer, nextTeam, players.size(),
					firstCard);
			if (secondCard == null) {
				if (waitForBestAction(firstCard)) {
					playsManager.play(firstCard);
				}
			} else {
				playsManager.play(secondCard);
			}
			System.out.println("1st: "  + firstCard + ", 2nd: " + secondCard);
		} catch (GameException e) {
			e.printStackTrace();
			managerException(firstCard.getType());
		} catch (GameWarning e) {
			System.out.println("Warning!");
			e.printStackTrace();
		}
	}

	private boolean waitForBestAction(Card firstCard) {
		return firstCard instanceof ChangeCard;
	}

	private void managerException(CardType card) {
		try {
			playsManager.play(new ChangeCard(card));
		} catch (GameException e1) {
			System.out.println("Se salvo!");
			e1.printStackTrace();
		} catch (GameWarning e1) {

		}
	}

	public List<Player> whoWin() {
		List<Player> wonPlayer = new ArrayList<Player>();
		if(players.size() > 1){
			int moreMoney = getMoreMoney();
			for (Player player : players) {
				if(player.getMoney() == moreMoney){
					wonPlayer.add(player);
				}
			}
			
		}else{
			wonPlayer.add(players.get(0));
		}
			return wonPlayer;
	}

	private int getMoreMoney() {
		int moreMoney = -100;
		for (Player player : players) {
			if(player.getMoney() > moreMoney){
				moreMoney = player.getMoney();
			}
		}
		return moreMoney;
	}

}
