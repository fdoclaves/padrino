package gm.cards;

import java.util.ArrayList;
import java.util.List;

import gm.Cake;
import gm.CharacterUtils;
import gm.GameCharacter;
import gm.pojos.Position;

public class CakeUtils {
	
	private int MAX_Y_POSITION;

	private int MAX_X_POSITION;

	public CakeUtils(int maxX, int maxY) {
		MAX_X_POSITION = maxX - 1;
		MAX_Y_POSITION = maxY - 1;
	}
	
	public List<Position> getBoomPositions(Position cakePosition, GameCharacter[][] characters) {
		List<Position> exploteCharacters = new ArrayList<Position>();
		exploteCharacters.add(cakePosition);
		if (isOnSide(cakePosition)) {
			killSideCharacters(cakePosition, characters, exploteCharacters);
		} else {
			exploteCharacters.add(new Position(cakePosition.getX() - 1, cakePosition.getY()));
			exploteCharacters.add(new Position(cakePosition.getX() + 1, cakePosition.getY()));
		}
		return exploteCharacters;
	}
	
	private void killSideCharacters(Position cakePosition, GameCharacter[][] characters, List<Position> exploteCharacters) {
		if (isConner(cakePosition)) {
			if (cakePosition.getX() == 0) {
				killConnerCharecters(cakePosition, exploteCharacters, characters, 1);
			} else {
				killConnerCharecters(cakePosition, exploteCharacters, characters, -1);
			}
		} else {
			exploteCharacters.add(new Position(cakePosition.getX(), cakePosition.getY() - 1));
			exploteCharacters.add(new Position(cakePosition.getX(), cakePosition.getY() + 1));
		}
	}

	private void killConnerCharecters(Position cakePosition, List<Position> exploteCharacters, GameCharacter[][] characters, int next) {
		if (cakePosition.getY() == 0) {
			exploteCharacters.add(new Position(cakePosition.getX(), cakePosition.getY() + 1));
			exploteCharacters.add(new Position(cakePosition.getX() + next, cakePosition.getY()));
		} else {
			exploteCharacters.add(new Position(cakePosition.getX(), cakePosition.getY() - 1));
			exploteCharacters.add(new Position(cakePosition.getX() + next, cakePosition.getY()));
		}
	}

	private boolean isConner(Position cakePosition) {
		return cakePosition.getY() == 0 || cakePosition.getY() == MAX_Y_POSITION;
	}

	private boolean isOnSide(Position cakePosition) {
		return cakePosition.getX() == 0 || cakePosition.getX() == MAX_X_POSITION;
	}

	public List<Position> getMoveCakesPositions(Cake cake, GameCharacter[][] characters) {
		List<Position> validPositions = new ArrayList<Position>();
		validPositions.add(getNextPosition(cake, characters, true));
		validPositions.add(getNextPosition(cake, characters, false));
		return validPositions;
	}
	
	private Position getNextPosition(Cake cake, GameCharacter[][] characters, boolean reloj) {
		Integer desplazamientoX = getX(cake.getPosition().getX(), cake.getPosition().getY(), reloj, getStartX(cake, reloj));
		Integer desplazamientoY = getY(cake.getPosition().getX(), cake.getPosition().getY(), reloj, getStartY(cake, reloj));
		int x = cake.getPosition().getX() + desplazamientoX;
		int y = cake.getPosition().getY() + desplazamientoY;
		GameCharacter character = CharacterUtils.getCharacterByPosition(characters, new Position(x, y));
		while (character.isEmpty()) {
			desplazamientoX = getX(x, y, reloj, desplazamientoX);
			desplazamientoY = getY(x, y, reloj, desplazamientoY);
			x = x + desplazamientoX;
			y = y + desplazamientoY;
			character = CharacterUtils.getCharacterByPosition(characters, new Position(x, y));
		}
		return new Position(x, y);
	}
	
	private int getStartY(Cake cake, boolean reloj) {
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

	private int getStartX(Cake cake, boolean reloj) {
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

    public static List<Position> getCharacterByTeam(List<Cake> cakeList) {
        List<Position> cakePositions = new ArrayList<Position>();
        for (Cake cake : cakeList) {
            cakePositions.add(cake.getPosition());
        }
        return cakePositions;
    }
}
