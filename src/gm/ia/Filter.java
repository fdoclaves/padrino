package gm.ia;

import gm.GameCharacter;

public interface Filter {
	boolean addIf(GameCharacter attackedCharacter);

	boolean addAsleep();
}