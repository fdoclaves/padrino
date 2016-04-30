package gm;

import java.util.ArrayList;
import java.util.List;

import gm.cards.ChangeCard;
import gm.exceptions.GameException;
import gm.exceptions.GameWarning;
import gm.ia.IA_Manager;
import gm.info.CardType;

public class GameManager {
	
	private List<Player> players;
	
	private PlayersManager playersManager;
	
	private PlaysManager playsManager;
	
	private GameTable gameTable;

	private GameCharacter[][] characterArray;
	
	public GameManager(List<String> namesPlayers, CardManagerImpl cardManager, GameCharacter[][] characterArray, TableSeat[][] tableSeats){
		this.characterArray = characterArray;
		this.players = new ArrayList<Player>();
		for (String name : namesPlayers) {
			players.add(new Player(name, getCardsToStart(cardManager)));
		}
		playersManager = new PlayersManager(players);
		gameTable = new GameTable(tableSeats);
		playsManager = new PlaysManager(characterArray, gameTable,cardManager, players);
	}
	
	private List<CardType> getCardsToStart(CardManager cardManager) {
		List<CardType> cards = new ArrayList<CardType>();
		for (int i = 0; i < 5; i++) {
			cards.add(cardManager.getCard());
		}
		return cards;
	}
	
	public void start() {
		while (players.size() > 1) {	
			List<Player> currentPlayer = playersManager.getCurrentPlayer();
			playsManager.startTurn(currentPlayer.get(0));
			Card card = iaPlay(currentPlayer.get(0), currentPlayer.get(1));
			try {
				playsManager.play(card);
			} catch (GameException e) {
				managerException(e, currentPlayer.get(0), card);
			} catch (GameWarning e) {
				e.printStackTrace();
			}
			playsManager.finishTurn();
		}
	}

	private void managerException(GameException e, Player player, Card card) {
		try {
			playsManager.play(new ChangeCard(card.getType()));
		} catch (GameException e1) {
			e1.printStackTrace();
		} catch (GameWarning e1) {
			e1.printStackTrace();
		}
	}

	private Card iaPlay(Player gaming, Player next) {
		IA_Manager ia_Manager = new IA_Manager(gameTable);//actualizado con Zzz y muertesXpastel
		return ia_Manager.getCard(characterArray, gaming, next.getTeam(), players.size());
	}

	public String whoWin() {
		return players.get(0).getTeam();
	}

}
