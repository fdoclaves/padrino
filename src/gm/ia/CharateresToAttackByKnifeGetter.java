package gm.ia;

import java.util.ArrayList;
import java.util.List;

import gm.GameCharacter;
import gm.GameTable;
import gm.Player;
import gm.TableSeat;
import gm.info.TableObjects;
import gm.pojos.Position;

public class CharateresToAttackByKnifeGetter {

	private int maxX;

	private int maxY;

	private GameTable gameTable;

	public CharateresToAttackByKnifeGetter(GameTable gameTable) {
		this.gameTable = gameTable;
		this.maxX = gameTable.getMaxX() - 1;
		this.maxY = gameTable.getMaxY() - 1;
	}

	public List<Position> getMyAttackPositions(GameCharacter[][] characters, Position attackerPosition, Player player) {
		Filter filter = new FilterSameTeam(player.getTeam());
		return buildList(characters, attackerPosition, filter);
	}

	public List<Position> getTheirAttackPositions(GameCharacter[][] characters, Position attackerPosition,
			String myTeam) {
		Filter filter = new FilterOnlyTeam(myTeam);
		return buildList(characters, attackerPosition, filter);
	}

	private List<Position> buildList(GameCharacter[][] characters, Position attackerPosition, Filter filter) {
		if (hasWeaponAndWakeUp(attackerPosition, characters)) {
			if (thereIsOnTheSide(attackerPosition.getX())) {
				return validateSides(characters, filter, attackerPosition);
			} else {
				List<Position> positions = new ArrayList<Position>();
				Position position1 = new Position(attackerPosition.getX() + 1, attackerPosition.getY());
				Position position2 = new Position(attackerPosition.getX() - 1, attackerPosition.getY());
				filter(characters, filter, positions, position1);
				filter(characters, filter, positions, position2);
				return positions;
			}
		} else {
			return new ArrayList<Position>();
		}
	}

	private boolean hasWeaponAndWakeUp(Position attackerPosition, GameCharacter[][] characters) {
		TableSeat tableSeat = gameTable.getTableSeatByPosition(attackerPosition);
		GameCharacter character = characters[attackerPosition.getY()][attackerPosition.getX()];
		return !character.isSleeping() && (character.hasKnife() || tableSeat.has(TableObjects.KNIFE));
	}

	private boolean thereIsOnTheSide(int x) {
		return x == 0 || x == maxX;
	}

	private List<Position> validateSides(GameCharacter[][] characters, Filter filter, Position attackerPosition) {
		if (attackerPosition.getX() == 0) {
			return getPositionsFromSides(characters, filter, attackerPosition, +1);
		} else {
			return getPositionsFromSides(characters, filter, attackerPosition, -1);
		}
	}

	private List<Position> getPositionsFromSides(GameCharacter[][] characters, Filter filter, Position attackerPosition,
			int positionSide) {
		if (attackerPosition.getY() == 0 || attackerPosition.getY() == maxY) {
			return getPositionsFromCorner(attackerPosition, positionSide, filter, characters);
		} else {
			List<Position> positions = new ArrayList<Position>();
			Position position1 = new Position(attackerPosition.getX(), attackerPosition.getY() + 1);
			Position position2 = new Position(attackerPosition.getX(), attackerPosition.getY() - 1);
			filter(characters, filter, positions, position1);
			filter(characters, filter, positions, position2);
			return positions;
		}
	}

	private List<Position> getPositionsFromCorner(Position attackerPosition, int positionSide, Filter filter,
			GameCharacter[][] characters) {
		Position position1;
		Position position2;
		if (attackerPosition.getY() == 0) {
			position1 = new Position(attackerPosition.getX() + positionSide, attackerPosition.getY());
			position2 = new Position(attackerPosition.getX(), attackerPosition.getY() + 1);
		} else {
			position1 = new Position(attackerPosition.getX() + positionSide, attackerPosition.getY());
			position2 = new Position(attackerPosition.getX(), attackerPosition.getY() - 1);
		}
		List<Position> positions = new ArrayList<Position>();
		filter(characters, filter, positions, position1);
		filter(characters, filter, positions, position2);
		return positions;
	}

	private void filter(GameCharacter[][] characters, Filter filter, List<Position> positions,
			Position positionToEvaluate) {
		GameCharacter attackedCharacter = characters[positionToEvaluate.getY()][positionToEvaluate.getX()];
		if (!attackedCharacter.isEmpty() && filter.addIfTeam(attackedCharacter)) {
			positions.add(positionToEvaluate);
		}
	}
}
