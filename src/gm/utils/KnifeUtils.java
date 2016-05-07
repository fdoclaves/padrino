package gm.utils;

import java.util.ArrayList;
import java.util.List;

import gm.GameCharacter;
import gm.GameTable;
import gm.TableSeat;
import gm.ia.getters.CharateresToAttackByKnifeGetter;
import gm.info.TableObjects;
import gm.pojos.Position;

public class KnifeUtils {

	public static List<Position> getCharacterByTeam(GameTable gameTable, GameCharacter[][] characterArray,
			String team) {
		TableSeat[][] tableSeats = gameTable.getTableSeats();
		List<Position> positions = new ArrayList<Position>();
		for (int x = 0; x < tableSeats[0].length; x++) {
			for (int y = 0; y < tableSeats.length; y++) {
				TableSeat tableSeat = tableSeats[y][x];
				GameCharacter gameCharacter = CharacterUtils.getCharacterByXY(characterArray, x, y);
				if (gameCharacter.isTeam(team) && !gameCharacter.isSleeping()) {
					if (tableSeat.has(TableObjects.KNIFE) || gameCharacter.hasKnife()) {
						Position attackerPosition = new Position(x, y);
						if (thereAreEnemiesToAttack(gameTable, characterArray, team, attackerPosition)) {
							positions.add(attackerPosition);
						}
					}
				}
			}
		}
		return positions;
	}

	private static boolean thereAreEnemiesToAttack(GameTable gameTable, GameCharacter[][] characterArray, String team,
			Position attackerPosition) {
		CharateresToAttackByKnifeGetter knifeGetter = new CharateresToAttackByKnifeGetter(gameTable);
		List<Position> whereAttack = knifeGetter.getMyAttackPositions(characterArray, attackerPosition, team);
		return !whereAttack.isEmpty();
	}
}
