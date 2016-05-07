package gm.utils;

import java.util.ArrayList;
import java.util.List;

import gm.GameCharacter;
import gm.TableSeat;
import gm.info.TableObjects;
import gm.pojos.Position;


public class SleepUtils {

    public static List<Position> get(GameCharacter[][] gameCharacters, TableSeat[][] tableSeats, String team) {
        List<Position> positions = new ArrayList<Position>();
        for (int x = 0; x < tableSeats[0].length; x++) {
            for (int y = 0; y < tableSeats.length; y++) {
                TableSeat tableSeat = tableSeats[y][x];
                GameCharacter gameCharacter = CharacterUtils.getCharacterByXY(gameCharacters, x, y);
                if(CharacterUtils.isValid(gameCharacter) && !gameCharacter.isTeam(team) && tableSeat.has(TableObjects.GLASS)){
                    positions.add(new Position(x, y));
                }
            }
        }
        return positions;
    }

}
