package gm;

import java.util.Map;

import gm.exceptions.GameException;
import gm.exceptions.GameWarning;
import gm.info.CardType;
import gm.info.GameMessages;
import gm.info.TableObjects;
import gm.pojos.Position;

public abstract class Card {

	public static final TableObjects GUN_ON_TABLE = TableObjects.GUN;

	public static final TableObjects KNIFE_ON_TABLE = TableObjects.KNIFE;

	public static final TableObjects GLASS_ON_TABLE = TableObjects.GLASS;

	protected int MAX_Y_POSITION;

	protected int MAX_X_POSITION;

	protected int MIDDLE;

	protected GameTable gameTable;

	public void inicialize(GameTable gameTable) {
		this.gameTable = gameTable;
		MAX_X_POSITION = gameTable.getMaxX() - 1;
		MAX_Y_POSITION = gameTable.getMaxY() - 1;
		MIDDLE = MAX_Y_POSITION / 2;
	}

	public abstract void doAction(GameCharacter[][] characters, Map<String, Player> players);

	public abstract void validateAction(GameCharacter[][] characters, String team) throws GameException, GameWarning;

	public abstract CardType getType();

	protected void validateAttackerCharacter(Position attackerPosition, GameCharacter[][] characters, String team)
			throws GameException {

		if (attackerPosition == null) {
			throw new GameException(GameMessages.ATTACKER_NULL);
		}

		GameCharacter attackerChair = characters[attackerPosition.getY()][attackerPosition.getX()];
		
	    if (attackerChair.isEmpty()) {
	            throw new GameException(GameMessages.SEAT_EMPTY);
	    }

		if (attackerChair.isSleeping()) {
			throw new GameException(GameMessages.ESTA_DORMIDO);
		}

		if (attackerChair.isInvalidSeat()) {
			throw new GameException(GameMessages.IT_ISNT_SEAT);
		}

		if (!attackerChair.isTeam(team)) {
			throw new GameException(GameMessages.NOT_SAME_TEAM);
		}
	}

	protected void warnFromAttacted(Position attackedPosition, GameCharacter[][] characters, String team)
			throws GameWarning {
		GameCharacter attackedChair = getCharacterFromChair(attackedPosition, characters);
		if (attackedChair.isEmpty()) {
			throw new GameWarning(GameMessages.ATTACKER_SEAT_IS_EMPTY);
		} else {
			if (attackedChair.isTeam(team)) {
				throw new GameWarning(GameMessages.SAME_TEAM);
			}
		}
	}

	protected GameCharacter getCharacterFromChair(Position position, GameCharacter[][] characters) {
		return characters[position.getY()][position.getX()];
	}
}
