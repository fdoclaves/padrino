package gm.cards;

import java.util.Map;

import gm.Card;
import gm.GameCharacter;
import gm.Killer;
import gm.Player;
import gm.exceptions.GameException;
import gm.exceptions.GameWarning;
import gm.info.CardType;
import gm.info.GameMessages;
import gm.pojos.Position;

public class GunCard extends Card {

	private Position attackedPosition;

	private Position attackerPosition;

	public GunCard(Position attackerPosition, Position attackedPosition) {
		this.attackedPosition = attackedPosition;
		this.attackerPosition = attackerPosition;
	}

	@Override
	public void doAction(GameCharacter[][] characters, Map<String, Player> players) {
		Killer.kill(characters, attackedPosition, players);
	}

	@Override
	public void validateAction(GameCharacter[][] characters, String team) throws GameException, GameWarning {
		validateAttackerCharacter(attackerPosition, characters, team);
		if (!gameTable.getTableSeatByPosition(attackerPosition).has(GUN_ON_TABLE)
				&& !getCharacterFromChair(attackerPosition, characters).hasGun()) {
			throw new GameException(GameMessages.YOU_HAS_NOT_GUN + ", " + getReason());
		}
		if (isConer(attackerPosition, attackedPosition)) {
			throw new GameException(GameMessages.ESQUINA_NO_PUEDE_DISPARAR + ", " + getReason());
		}
		if (isMiddle(attackerPosition.getY())) {
			if (attackerPosition.getY() != attackedPosition.getY()) {
				throw new GameException(GameMessages.NO_ESTA_ENFRENTE + ", " + getReason());
			}
		} else {
			if (attackerPosition.getX() != attackedPosition.getX()) {
				throw new GameException(GameMessages.NO_ESTA_ENFRENTE + ", " + getReason());
			}
		}
		warnFromAttacted(attackedPosition, characters, team);
	}

	private boolean isMiddle(int y) {
		return y == MIDDLE;
	}

	private boolean isConer(Position attackerPosition, Position attackedPosition) {
		return (attackerPosition.getX() == 0 || attackerPosition.getX() == MAX_X_POSITION)
				&& attackedPosition.getY() != MIDDLE;
	}

	@Override
	public CardType getType() {
		return CardType.GUN;
	}

	public Position getAttackedPosition() {
		return attackedPosition;
	}

	public Position getAttackerPosition() {
		return attackerPosition;
	}

}
