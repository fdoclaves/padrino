package gm.ia;

import gm.Card;
import gm.GameCharacter;
import gm.Player;

public interface PlaysController {

	Card get2ndCard(GameCharacter[][] characterArray, Player player, String nextTeam, int currentGamers,
			Card firstAction);

	Card get1stCard(GameCharacter[][] characterArray, Player player, String nextTeam, int currentGamers);

}