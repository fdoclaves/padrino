package gm;

import gm.info.TableObjects;
import gm.pojos.Position;

import java.util.ArrayList;
import java.util.List;


public class KnifeUtils {

    public List<Position> getCharacterByTeam(TableSeat[][] tableSeats, GameCharacter[][] characterArray, String team) {
        List<Position> positions = new ArrayList<Position>();
        for (int x = 0; x < tableSeats[0].length; x++) {
            for (int y = 0; y < tableSeats.length; y++) {
                TableSeat tableSeat = tableSeats[y][x];
                GameCharacter gameCharacter = CharacterUtils.getCharacterByXY(characterArray, x, y);
                if(gameCharacter.isTeam(team)){
                    if(tableSeat.has(TableObjects.KNIFE) || gameCharacter.hasKnife()){
                        positions.add(new Position(x, y));
                    }
                }
            }
        }
        return positions;
    }
}
