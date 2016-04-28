package gm;

import gm.pojos.Position;

public class Killer {

	public static void kill(GameCharacter[][] characters, Position position) {
		GameCharacter character = characters[position.getY()][position.getX()];
			character.dead();
		characters[position.getY()][position.getX()] = new GameCharacterEmpty();
	}
}
