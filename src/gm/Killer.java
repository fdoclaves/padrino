package gm;

import java.util.Map;

import gm.pojos.Position;
import gm.utils.CharacterUtils;

public class Killer {

	public static void kill(GameCharacter[][] characters, Position position, Map<String, Player> players) {
		GameCharacter gameCharacter = characters[position.getY()][position.getX()];
		if(CharacterUtils.isValid(gameCharacter)){
			Player player = players.get(gameCharacter.getTeam());
			player.removeOneCharacter();
		}
		gameCharacter.dead();
		characters[position.getY()][position.getX()] = new GameCharacterEmpty();
	}
}
