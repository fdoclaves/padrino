package gt.extras;

import gm.Card;
import gm.GameCharacter;

public class CharacterString extends GameCharacter {

	private static final String KING = "K";

	private static final String EMPTY = "V";

	private String value;

	public CharacterString(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}

	@Override
	public void dead() {
		value = "V";
	}

	@Override
	public boolean isTeam(String team) {
		return value.contains(team);
	}

	@Override
	public boolean isInvalidSeat() {
		return value.contains("*");
	}

	@Override
	public boolean hasGun() {
		return value.contains(Card.GUN_ON_TABLE.getValue());
	}

	@Override
	public boolean hasKnife() {
		return value.contains(Card.KNIFE_ON_TABLE.getValue());
	}

	@Override
	public boolean isEmpty() {
		return value.contains(EMPTY);
	}

	@Override
	public void sleep() {
		if (value.contains("_")) {
			value = value.replace("_", "Z");
		} else {
			value += "Z";
		}
	}

	@Override
	public boolean isSleeping() {
		return value.contains("Z");
	}

	@Override
	public void wakeUp() {
		value = value.replace("Z", "_");
	}

	@Override
	public GameCharacter cloneCharacters() {
		return new CharacterString(value);
	}

	@Override
	public boolean isKing() {
		return value.contains(KING);
	}

	@Override
	public String getTeam() {
		if (value.contains("1")) {
			return "1";
		}
		if (value.contains("2")) {
			return "2";
		}
		if (value.contains("3")) {
			return "3";
		}
		if (value.contains("A")) {
			return "A";
		}
		if (value.contains("R")) {
			return "R";
		}
		if (value.contains("B")) {
			return "B";
		}
		if (value.contains("N")) {
            return "N";
        }
		return null;
	}

}
