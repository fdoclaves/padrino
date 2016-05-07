package gm.utils;

import java.util.ArrayList;
import java.util.List;

import gm.GameCharacter;
import gm.GameTable;
import gm.TableSeat;
import gm.ia.AddEnemiesTeam;
import gm.ia.Filter;
import gm.ia.getters.CharateresToAttackByGunGetter;
import gm.info.TableObjects;
import gm.pojos.Position;

public class GunUtils {

	public static List<Position> getCharacterByTeam(GameTable gameTable, GameCharacter[][] characterArray,
			String team) {
		TableSeat[][] tableSeats = gameTable.getTableSeats();
		List<Position> positions = new ArrayList<Position>();
		for (int x = 0; x < tableSeats[0].length; x++) {
			for (int y = 0; y < tableSeats.length; y++) {
				TableSeat tableSeat = tableSeats[y][x];
				GameCharacter gameCharacter = CharacterUtils.getCharacterByXY(characterArray, x, y);
				if (gameCharacter.isTeam(team) && !gameCharacter.isSleeping()) {
					if (tableSeat.has(TableObjects.GUN) || gameCharacter.hasGun()) {
						Position attackerPosition = new Position(x, y);
						Position attackedPosition = getPositionToShoot(attackerPosition, characterArray, team, gameTable);
						if(attackedPosition != null && isValidPosition(gameTable, attackedPosition, attackerPosition)){
							positions.add(attackerPosition);
						}
					}
				}
			}
		}
		return positions;
	}

	public static Position getPositionToShoot(Position attackerPosition, GameCharacter[][] characterArray, String team,
			GameTable gameTable) {
		CharateresToAttackByGunGetter gunGetter = new CharateresToAttackByGunGetter(gameTable);
		Filter filter = new AddEnemiesTeam(team);
		return gunGetter.getAttackedPositionWhenItIsPosibleAttack(characterArray, attackerPosition, filter);
	}
	
	private static boolean isValidPosition(GameTable gameTable, Position attackedPosition, Position attackerPosition){
		int maxX = gameTable.getMaxX() - 1;
		int maxY = gameTable.getMaxY() - 1;
		int middle = maxY / 2;
		if (isConer(attackerPosition, attackedPosition, maxX, middle)) {
			return false;
		}
		if (attackerPosition.getY() == middle) {
			return attackerPosition.getY() == attackedPosition.getY();
		} else {
			return attackerPosition.getX() == attackedPosition.getX();
		}
	}

	private static boolean isConer(Position attackerPosition, Position attackedPosition, int maxX, int middle) {
		return (attackerPosition.getX() == 0 || attackerPosition.getX() == maxX)
				&& attackedPosition.getY() != middle;
	}
}
