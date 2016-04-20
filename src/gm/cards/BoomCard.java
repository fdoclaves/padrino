package gm.cards;

import gm.Cake;
import gm.Card;
import gm.GameCharacter;
import gm.exceptions.GameException;
import gm.exceptions.GameWarning;
import gm.info.CardType;

public class BoomCard extends Card {

	private Cake cake;

	public BoomCard(Cake cake) {
		this.cake = cake;
	}

	@Override
	public void doAction(GameCharacter[][] characters) {
		cake.boom(characters);
	}

	@Override
	public void validateAction(GameCharacter[][] characters, String team) throws GameException, GameWarning {

	}

	@Override
	public CardType getType() {
		return CardType.BOOM;
	}

	public Cake getCake() {
		return cake;
	}

}
