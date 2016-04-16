package gm.cards;

import gm.Card;
import gm.GameCharacter;
import gm.Killer;
import gm.exceptions.GameException;
import gm.exceptions.GameWarning;
import gm.info.CardType;
import gm.info.GameMessages;
import gm.pojos.Position;

public class KnifeCard extends Card {

	private Position attackedPosition;
	private Position attackerPosition;

	public KnifeCard(Position attackerPosition, Position attackedPosition) {
		this.attackedPosition = attackedPosition;
		this.attackerPosition = attackerPosition;
	}

	@Override
	public void doAction(GameCharacter[][] characters) {
		Killer.kill(characters, attackedPosition);
	}

	@Override
	public void validateAction(GameCharacter[][] characters, String team) throws GameException, GameWarning {
		validateAttackerCharacter(attackerPosition, characters, team);
		if (!gameTable.getTableSeatByPosition(attackerPosition).has(KNIFE_ON_TABLE)
				&& !getCharacterFromChair(attackerPosition, characters).hasKnife()) {
			throw new GameException(GameMessages.YOU_HAS_NOT_KNIFE);
		}
		if (thereIsOnTheSide(attackerPosition.getX())) {
			validateSides(attackerPosition, attackedPosition);
		} else {
			if (!((attackedPosition.getX() == attackerPosition.getX() + 1)
					|| (attackedPosition.getX() == attackerPosition.getX() - 1))) {
				throw new GameException(GameMessages.ESTA_LEJOS);
			}
		}
		warnFromAttacted(attackedPosition, characters, team);
	}

	private boolean thereIsOnTheSide(int x) {
		return x == 0 || x == MAX_X_POSITION;
	}

	private void validateSides(Position attackerPosition, Position attackedPosition) throws GameException {
		if (attackerPosition.getX() == 0) {
			validateSide(attackerPosition, attackedPosition, +1);
		} else {
			validateSide(attackerPosition, attackedPosition, -1);
		}
	}

	private void validateSide(Position attackerPosition, Position attackedPosition, int positionSide)
			throws GameException {
		if (attackerPosition.getY() == 0 || attackerPosition.getY() == MAX_Y_POSITION) {
			validateAttackerOnCorner(attackerPosition, attackedPosition, positionSide);
		}
		if (attackerPosition.getY() != 0 && attackerPosition.getY() != MAX_Y_POSITION) {
			if (!((attackedPosition.getY() == attackerPosition.getY() + 1)
					|| (attackedPosition.getY() == attackerPosition.getY() - 1))) {
				throw new GameException(GameMessages.ESTA_LEJOS);
			}
		}
	}

	private void validateAttackerOnCorner(Position attackerPosition, Position attackedPosition, int positionSide)
			throws GameException {
		if (attackedPosition.getX() == attackerPosition.getX()) {
			if (attackerPosition.getY() == 0) {
				if (attackedPosition.getY() != 1) {
					throw new GameException(GameMessages.ESTA_LEJOS);
				}
			} else {
				if (attackedPosition.getY() != MAX_Y_POSITION - 1) {
					throw new GameException(GameMessages.ESTA_LEJOS);
				}
			}
		} else {
			if (attackedPosition.getX() != attackerPosition.getX() + positionSide) {
				throw new GameException(GameMessages.ESTA_LEJOS);
			}
		}
	}

	@Override
	public CardType getType() {
		return CardType.KNIFE;
	}

	public Position getAttackedPosition() {
		return attackedPosition;
	}

	public Position getAttackerPosition() {
		return attackerPosition;
	}

}
