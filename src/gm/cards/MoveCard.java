package gm.cards;

import gm.Card;
import gm.GameCharacter;
import gm.exceptions.GameException;
import gm.info.CardType;
import gm.info.GameMessages;
import gm.pojos.Position;

public class MoveCard extends Card {

	private Position attackerPosition;
	private Position attackedPosition;

	public MoveCard(Position attackerPosition, Position attackedPosition) {
		this.attackerPosition = attackerPosition;
		this.attackedPosition = attackedPosition;
	}

	@Override
	public void doAction(GameCharacter[][] characters) {
		characters[attackedPosition.getY()][attackedPosition
				.getX()] = characters[attackerPosition.getY()][attackerPosition.getX()];
		characters[attackerPosition.getY()][attackerPosition.getX()] = null;
	}

	@Override
	public void validateAction(GameCharacter[][] characters, String team) throws GameException {
		validateAttackerCharacter(attackerPosition, characters, team);
		GameCharacter attackedChair = getCharacterFromChair(attackedPosition, characters);
		if (attackedChair != null && !attackedChair.isEmpty()) {
			throw new GameException(GameMessages.OCCUPIED);
		}
	}

	@Override
	public CardType getType() {
		return CardType.MOVE;
	}

}
