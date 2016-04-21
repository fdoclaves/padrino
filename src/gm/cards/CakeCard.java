package gm.cards;

import gm.Cake;
import gm.Card;
import gm.GameCharacter;
import gm.exceptions.GameException;
import gm.info.CardType;
import gm.info.GameMessages;

public class CakeCard extends Card {

	private Cake cake;

	public CakeCard(Cake cake) {
		this.cake = cake;
	}

	@Override
	public void doAction(GameCharacter[][] characters) {
		cake.inicialize(gameTable);
		gameTable.getCakeList().add(cake);
	}

	@Override
	public void validateAction(GameCharacter[][] characters, String team) throws GameException {
		if (characters[cake.getPosition().getY()][cake.getPosition().getX()].isInvalidSeat()) {
			throw new GameException(GameMessages.IT_ISNT_SEAT);
		}
	}

	@Override
	public CardType getType() {
		return CardType.CAKE;
	}

}
