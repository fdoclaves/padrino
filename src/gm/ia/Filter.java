package gm.ia;

import gm.GameCharacter;

public interface Filter {
	boolean addIfTeam(GameCharacter attackedCharacter);

	boolean addAsleep();
}