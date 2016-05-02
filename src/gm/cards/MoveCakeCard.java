package gm.cards;

import java.util.Map;

import gm.Cake;
import gm.Card;
import gm.GameCharacter;
import gm.Player;
import gm.exceptions.GameException;
import gm.exceptions.GameWarning;
import gm.info.CardType;
import gm.info.GameMessages;
import gm.pojos.Position;

public class MoveCakeCard extends Card {

	private Cake cake;

	private Position newPosition;

	public MoveCakeCard(Cake cake, Position newPosition, String reason) {
		this.cake = cake;
		this.newPosition = newPosition;
		setReason(reason);
	}
	
	public MoveCakeCard(Cake cake, Position newPosition) {
		this.cake = cake;
		this.newPosition = newPosition;
	}

	@Override
	public void doAction(GameCharacter[][] characters, Map<String, Player> players) {
		this.cake.changePosition(newPosition);
	}

	@Override
	public void validateAction(GameCharacter[][] characters, String team) throws GameException, GameWarning {
		GameCharacter character = characters[newPosition.getY()][newPosition.getX()];
		if (character.isEmpty()) {
            throw new GameException(GameMessages.SEAT_EMPTY);
        }
		
		if (!character.isValidSeat()) {
			throw new GameException(GameMessages.IT_ISNT_SEAT);
		}
		
		boolean isValid = false;
		CakeUtils cakeUtils = buildCakeUtils(characters);
		for (Position position : cakeUtils.getMoveCakesPositions(cake, characters)) {
			if (newPosition.isEquals(position)) {
				isValid = true;
				return;
			}
		}
		if (!isValid) {
			throw new GameException(GameMessages.INVALID_CAKE_MOVE + newPosition + ":" + cake.getPosition());
		}
	}

	private CakeUtils buildCakeUtils(GameCharacter[][] characters) {
		int maxX = characters[0].length;
		int maxY = characters.length;
		CakeUtils cakeUtils = new CakeUtils(maxX, maxY);
		return cakeUtils;
	}

	@Override
	public CardType getType() {
		return CardType.MOVE_CAKE;
	}

	public Position getNewPosition() {
		return newPosition;
	}
	
	public Cake getCake() {
		return cake;
	}

}
