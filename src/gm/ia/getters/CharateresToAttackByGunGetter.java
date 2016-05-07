package gm.ia.getters;

import gm.GameCharacter;
import gm.GameTable;
import gm.Player;
import gm.TableSeat;
import gm.ia.AddAttackToIaTeam;
import gm.ia.AddEmptySeat;
import gm.ia.AddEnemiesTeam;
import gm.ia.Filter;
import gm.info.TableObjects;
import gm.pojos.Position;
import gm.utils.CharacterUtils;

public class CharateresToAttackByGunGetter {

	private int maxX;

	private int maxY;

	private int middle;

	private GameTable gameTable;

	public CharateresToAttackByGunGetter(GameTable gameTable) {
		this.gameTable = gameTable;
		this.maxX = gameTable.getMaxX() - 1;
		this.maxY = gameTable.getMaxY() - 1;
		this.middle = maxY / 2;
	}

	public Position getMyAttackPosition(GameCharacter[][] characters, Position attackerPosition, Player player) {
		Filter filter = new AddEnemiesTeam(player.getTeam());
		return buildAttackPosition(characters, attackerPosition, filter);
	}

	public Position getTheirAttackPosition(GameCharacter[][] characters, Position attackerPosition, String myTeam) {
		Filter filter = new AddAttackToIaTeam(myTeam);
		return buildAttackPosition(characters, attackerPosition, filter);
	}
	
	   public Position getEmptyAttackPositions(GameCharacter[][] characters, Position attackerPosition) {
	        Filter filter = new AddEmptySeat();
	        return buildAttackPosition(characters, attackerPosition, filter);
	    }

	private Position buildAttackPosition(GameCharacter[][] characters, Position attackerPosition, Filter filter) {
		GameCharacter attackerCharacter = CharacterUtils.getCharacterByPosition(characters, attackerPosition);
		if (hasWeapon(attackerPosition, characters) && addAsleep(attackerCharacter, filter)) {
			return getAttackedPositionWhenItIsPosibleAttack(characters, attackerPosition, filter);
		} else {
			return null;
		}
	}

	public Position getAttackedPositionWhenItIsPosibleAttack(GameCharacter[][] characters, Position attackerPosition,
			Filter filter) {
		if (isConer(attackerPosition)) {
			return null;
		}
		if (isMiddle(attackerPosition.getY())) {
			if (attackerPosition.getX() == 0) {
				return filterByTeamAndEmpty(characters, filter, new Position(maxX, attackerPosition.getY()));
			} else {
				return filterByTeamAndEmpty(characters, filter, new Position(0, attackerPosition.getY()));
			}
		} else {
			if (attackerPosition.getY() == 0) {
				return filterByTeamAndEmpty(characters, filter, new Position(attackerPosition.getX(), maxY));
			} else {
				return filterByTeamAndEmpty(characters, filter, new Position(attackerPosition.getX(), 0));
			}
		}
	}

	private boolean addAsleep(GameCharacter attackedCharacter, Filter filter) {
		if (attackedCharacter.isSleeping()) {
			return filter.addAsleep();
		}
		return true;
	}

	private boolean hasWeapon(Position attackerPosition, GameCharacter[][] characters) {
		TableSeat tableSeat = gameTable.getTableSeatByPosition(attackerPosition);
		GameCharacter character = characters[attackerPosition.getY()][attackerPosition.getX()];
		return tableSeat.has(TableObjects.GUN) || character.hasGun();
	}

	private Position filterByTeamAndEmpty(GameCharacter[][] characterArray, Filter filter, Position attackedPosition) {
		GameCharacter character = CharacterUtils.getCharacterByPosition(characterArray, attackedPosition);
		if (character.isValidSeat() && filter.addIf(character)) {
			return attackedPosition;
		} else {
			return null;
		}
	}

	private boolean isMiddle(int y) {
		return y == middle;
	}

	private boolean isConer(Position attackerPosition) {
		return ((attackerPosition.getX() == 0 || attackerPosition.getX() == maxX) && attackerPosition.getY() != middle);
	}

}
