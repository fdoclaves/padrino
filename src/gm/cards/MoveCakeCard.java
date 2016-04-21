package gm.cards;

import java.util.ArrayList;
import java.util.List;

import gm.Cake;
import gm.Card;
import gm.GameCharacter;
import gm.exceptions.GameException;
import gm.exceptions.GameWarning;
import gm.info.CardType;
import gm.info.GameMessages;
import gm.pojos.Position;

public class MoveCakeCard extends Card {

	private Cake cake;
	private Position newPosition;

	public MoveCakeCard(Cake cake, Position newPosition) {
		this.cake = cake;
		this.newPosition = newPosition;
	}

	@Override
	public void doAction(GameCharacter[][] characters) {
		this.cake.changePosition(newPosition);
	}

	@Override
	public void validateAction(GameCharacter[][] characters, String team) throws GameException, GameWarning {
		GameCharacter character = characters[newPosition.getY()][newPosition.getX()];
		if (character.isInvalidSeat()) {
			throw new GameException(GameMessages.IT_ISNT_SEAT);
		}
		if (character.isEmpty()) {
			throw new GameException(GameMessages.SEAT_EMPTY);
		}
		boolean isValid = false;
		for (Position position : getValidPositions(characters)) {
			if (newPosition.isEquals(position)) {
				isValid = true;
				return;
			}
		}
		if (!isValid) {
			throw new GameException(GameMessages.INVALID_CAKE_MOVE);
		}
	}

	private List<Position> getValidPositions(GameCharacter[][] characters) {
		List<Position> validPositions = new ArrayList<Position>();
		validPositions.add(getNextPosition(characters, true));
		validPositions.add(getNextPosition(characters, false));
		return validPositions;
	}

	private Position getNextPosition(GameCharacter[][] characters, boolean reloj) {
		Integer desplazamientoX = getX(cake.getPosition().getX(), cake.getPosition().getY(), reloj, getStartX(reloj));
		Integer desplazamientoY = getY(cake.getPosition().getX(), cake.getPosition().getY(), reloj, getStartY(reloj));
		int x = cake.getPosition().getX() + desplazamientoX;
		int y = cake.getPosition().getY() + desplazamientoY;
		GameCharacter character = getCharacterFromChair(new Position(x, y), characters);
		while (character.isEmpty()) {
			desplazamientoX = getX(x, y, reloj, desplazamientoX);
			desplazamientoY = getY(x, y, reloj, desplazamientoY);
			x = x + desplazamientoX;
			y = y + desplazamientoY;
			character = getCharacterFromChair(new Position(x, y), characters);
		}
		return new Position(x, y);
	}

	private int getStartY(boolean reloj) {
		if (cake.getPosition().getX() == 0) {
			if (reloj) {
				return -1;
			} else {
				return 1;
			}
		}
		if (cake.getPosition().getX() == MAX_X_POSITION) {
			if (reloj) {
				return 1;
			} else {
				return -1;
			}
		}
		return 0;
	}

	private int getStartX(boolean reloj) {
		if (cake.getPosition().getY() == 0) {
			if (reloj) {
				return 1;
			} else {
				return -1;
			}
		}
		if (cake.getPosition().getY() == MAX_Y_POSITION) {
			if (reloj) {
				return -1;
			} else {
				return 1;
			}
		}
		return 0;
	}

	private Integer getY(int x, int y, boolean reloj, int actualDesplamiento) {
		if (x == 0 && y == 0) {
			if (reloj) {
				return 0;
			} else {
				return 1;
			}
		} else if (x == 0 && y == MAX_Y_POSITION) {
			if (reloj) {
				return -1;
			} else {
				return 0;
			}
		} else if (x == MAX_X_POSITION && y == 0) {
			if (reloj) {
				return 1;
			} else {
				return 0;
			}
		} else if (x == MAX_X_POSITION && y == MAX_Y_POSITION) {
			if (reloj) {
				return 0;
			} else {
				return -1;
			}
		}
		return actualDesplamiento;
	}

	private Integer getX(int x, int y, boolean reloj, int actualDesplamiento) {
		if (x == 0 && y == 0) {
			if (reloj) {
				return 1;
			} else {
				return 0;
			}
		} else if (x == 0 && y == MAX_Y_POSITION) {
			if (reloj) {
				return 0;
			} else {
				return 1;
			}
		} else if (x == MAX_X_POSITION && y == 0) {
			if (reloj) {
				return 0;
			} else {
				return -1;
			}
		} else if (x == MAX_X_POSITION && y == MAX_Y_POSITION) {
			if (reloj) {
				return -1;
			} else {
				return 0;
			}
		}
		return actualDesplamiento;
	}

	@Override
	public CardType getType() {
		return CardType.MOVE_CAKE;
	}

}
