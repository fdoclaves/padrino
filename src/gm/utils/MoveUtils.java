package gm.utils;

import java.util.ArrayList;
import java.util.List;

import gm.GameCharacter;
import gm.pojos.Position;

public class MoveUtils {

    public static List<Position> getCharacterByTeam(GameCharacter[][] characterArray, String team) {
        List<Position> positions = new ArrayList<Position>();
        for (int x = 0; x < characterArray[0].length; x++) {
            for (int y = 0; y < characterArray.length; y++) {
                GameCharacter gameCharacter = CharacterUtils.getCharacterByXY(characterArray, x, y);
                if (gameCharacter.isTeam(team)) {
                    positions.add(new Position(x, y));
                }
            }
        }
        return positions;
    }

	public static List<Position> getEmptySeats(GameCharacter[][] characterArray) {
		List<Position> positions = new ArrayList<Position>();
        for (int x = 0; x < characterArray[0].length; x++) {
            for (int y = 0; y < characterArray.length; y++) {
                GameCharacter gameCharacter = CharacterUtils.getCharacterByXY(characterArray, x, y);
                if (gameCharacter.isValidSeat() && gameCharacter.isEmpty()) {
                    positions.add(new Position(x, y));
                }
            }
        }
        return positions;
	}
}
