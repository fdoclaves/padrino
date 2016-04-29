package gm.ia;

import gm.GameCharacter;

public class AddEmptySeat implements Filter {

	@Override
	public boolean addIf(GameCharacter attackedCharacter) {
		return attackedCharacter.isEmpty();
	}

	@Override
	public boolean addAsleep() {
		return false;
	}

}