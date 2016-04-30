package gm.cards;

import java.util.Map;

import gm.Card;
import gm.GameCharacter;
import gm.Player;
import gm.exceptions.GameException;
import gm.exceptions.GameWarning;
import gm.info.CardType;

public class ChangeCard extends Card{
	
	private CardType cardType;

	public ChangeCard(CardType cardType){
		this.cardType = cardType;
		
	}

	@Override
	public void doAction(GameCharacter[][] characters, Map<String, Player> players) {

	}

	@Override
	public void validateAction(GameCharacter[][] characters, String team) throws GameException, GameWarning {

	}

	@Override
	public CardType getType() {
		return cardType;
	}

}
