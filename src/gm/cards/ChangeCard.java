package gm.cards;

import gm.Card;
import gm.GameCharacter;
import gm.exceptions.GameException;
import gm.exceptions.GameWarning;
import gm.info.CardType;

public class ChangeCard extends Card{
	
	private CardType cardType;

	public ChangeCard(CardType cardType){
		this.cardType = cardType;
		
	}

	@Override
	public void doAction(GameCharacter[][] characters) {

	}

	@Override
	public void validateAction(GameCharacter[][] characters, String team) throws GameException, GameWarning {

	}

	@Override
	public CardType getType() {
		return cardType;
	}

}
