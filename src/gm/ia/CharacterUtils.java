package gm.ia;

import gm.GameCharacter;
import gm.pojos.Position;

public class CharacterUtils {

	public static GameCharacter getCharacterByPosition(GameCharacter[][] characterArray, Position position) {
		return characterArray[position.getY()][position.getX()];
	}

	public static GameCharacter getCharacterByXY(GameCharacter[][] characterArray, int x, int y) {
		return characterArray[y][x];
	}
	
	public static boolean isValid(GameCharacter gameCharacter){
	    return gameCharacter.isValidSeat() && !gameCharacter.isEmpty();
	}
}
