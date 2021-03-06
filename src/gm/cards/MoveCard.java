package gm.cards;

import java.util.Map;

import gm.Card;
import gm.GameCharacter;
import gm.GameCharacterEmpty;
import gm.Player;
import gm.exceptions.GameException;
import gm.info.CardType;
import gm.info.GameMessages;
import gm.pojos.Position;

public class MoveCard extends Card {

	private Position whoMove;
	private Position whereMove;

	public MoveCard(Position whoMove, Position whereMove, String reason) {
		this.whoMove = whoMove;
		this.whereMove = whereMove;
		setReason(reason);
	}
	
	public MoveCard(Position whoMove, Position whereMove) {
		this.whoMove = whoMove;
		this.whereMove = whereMove;
	}

	@Override
	public void doAction(GameCharacter[][] characters, Map<String, Player> players) {
		characters[whereMove.getY()][whereMove
				.getX()] = characters[whoMove.getY()][whoMove.getX()];
		characters[whoMove.getY()][whoMove.getX()] = new GameCharacterEmpty();
	}

	@Override
	public void validateAction(GameCharacter[][] characters, String team) throws GameException {
		validateAttackerCharacter(whoMove, characters, team);
		GameCharacter attackedChair = getCharacterFromChair(whereMove, characters);
		if (!attackedChair.isEmpty()) {
			throw new GameException(GameMessages.OCCUPIED + ", " + getReason());
		}
	}

	@Override
	public CardType getType() {
		return CardType.MOVE;
	}
	
	public Position getWhoToMove(){
		return whoMove;
	}

    public Position getWhereMove() {
        return whereMove;
    }

}
