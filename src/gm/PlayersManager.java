package gm;

import java.util.ArrayList;
import java.util.List;

public class PlayersManager {

	private List<Player> players;

	private int index;

	private Player[] allPlayers;

	public PlayersManager(List<Player> gamers) {
		this.players = gamers;
		this.allPlayers = new Player[gamers.size()];
		for (int i = 0; i < allPlayers.length; i++) {
			this.allPlayers[i] = gamers.get(i);
		}
	}

	public List<Player> getCurrentPlayer() {
		List<Player> players = new ArrayList<Player>();
		setCurrentIndex();
		players.add(allPlayers[index]);
		setNextIndex();
		players.add(allPlayers[index]);
		return players;
	}

	private void setCurrentIndex() {
		while (!players.contains(allPlayers[index])) {
			if (index >= players.size()) {
				index = 0;
			}else{
				++index;
			}
		}
	}

	private void setNextIndex() {
		do {
			++index;
			if (index >= allPlayers.length) {
				index = 0;
			}
		} while (!players.contains(allPlayers[index]));
	}

}
