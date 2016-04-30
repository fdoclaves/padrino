package gm.cards;

import java.util.Map;

import gm.Cake;
import gm.Card;
import gm.GameCharacter;
import gm.Player;
import gm.exceptions.GameException;
import gm.exceptions.GameWarning;
import gm.info.CardType;

public class BoomCard extends Card {

	private Cake cake;

	public BoomCard(Cake cake, String reason) {
		this.cake = cake;
		setReason(reason);
	}
	
	public BoomCard(Cake cake) {
		this.cake = cake;
	}

	@Override
	public void doAction(GameCharacter[][] characters, Map<String, Player> players) {
		cake.boom(characters, players);
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
