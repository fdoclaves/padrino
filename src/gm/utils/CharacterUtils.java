package gm.utils;

import gm.GameCharacter;
import gm.Player;
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

	public static boolean hasAsleepAllCharacter(Player player, GameCharacter[][] characterArray) {
		int counter = 0;
		for (GameCharacter[] gameCharacters : characterArray) {
			for (GameCharacter gameCharacter : gameCharacters) {
				if(gameCharacter.isSleeping() && gameCharacter.isTeam(player.getTeam())){
					 counter++;
				}
			}
		}
		if(counter == player.getCounterCharacters()){
			return true;
		}
		return false;
	}
}
