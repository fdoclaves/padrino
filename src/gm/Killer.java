package gm;

import gm.pojos.Position;
import gt.extras.CharacterString;

public class Killer {

	public static void kill(GameCharacter[][] characters, Position position) {
		GameCharacter character = characters[position.getY()][position.getX()];
		if (character != null) {
			character.dead();
		}
		characters[position.getY()][position.getX()] = new CharacterString("V");
	}
}
