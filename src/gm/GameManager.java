package gm;

import java.util.List;

import gm.cards.ChangeCard;
import gm.cards.MoveCard;
import gm.exceptions.GameException;
import gm.exceptions.GameWarning;
import gm.ia.IaCardsManager;
import gm.info.CardType;
import gt.extras.Converter;

public class GameManager {

	private List<Player> players;

	private TurnsManager turnsManager;

	private PlaysManager playsManager;

	private GameTable gameTable;

	private GameCharacter[][] characterArray;

	public GameManager(List<Player> players, CardManager cardManager, GameCharacter[][] characterArray,
			TableSeat[][] tableSeats) {
		this.players = players;
		this.characterArray = characterArray;
		for (Player team : players) {
			addCardsToPlayerToStart(team, cardManager);
		}
		turnsManager = new TurnsManager(players);
		gameTable = new GameTable(tableSeats);
		playsManager = new PlaysManager(characterArray, gameTable, cardManager, players);
	}

	private void addCardsToPlayerToStart(Player team, CardManager cardManager) {
		while(team.getCards().size() < 5){
			team.addCard(cardManager.getCard());
		}
	}

	public void start() {
		while (players.size() > 1) {
			List<Player> currentPlayer = turnsManager.getCurrentPlayer();
			Player iaPlayer = currentPlayer.get(0);
			String nextTeam = currentPlayer.get(1).getTeam();
			System.out.println("-------------" + iaPlayer.getTeam() + "-----------------");
			playsManager.startTurn(iaPlayer);
			iaPlays(iaPlayer, nextTeam);
			playsManager.finishTurn();
			System.out.println(new Converter(8, 3).cToString(playsManager.getChairs()));
		}
	}

	private void iaPlays(Player iaPlayer, String nextTeam) {
		IaCardsManager iaCardManager = new IaCardsManager(gameTable);
		Card firstCard = iaCardManager.get1stCard(characterArray, iaPlayer, nextTeam, players.size());
		try {
			if (!waitForBestAction(firstCard)) {
				playsManager.play(firstCard);
			}
			MoveCard secondCard = iaCardManager.get2ndCard(characterArray, iaPlayer, nextTeam, players.size(),
					firstCard);
			if (secondCard == null) {
				if (waitForBestAction(firstCard)) {
					playsManager.play(firstCard);
				}
			} else {
				playsManager.play(secondCard);
			}
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

	public String whoWin() {
		return players.get(0).getTeam();
	}

}
