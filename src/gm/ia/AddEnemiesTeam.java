package gm.ia;

import gm.GameCharacter;

public class AddEnemiesTeam implements Filter {

	private String team;

	public AddEnemiesTeam(String team) {
		this.team = team;
	}

	@Override
	public boolean addIf(GameCharacter attackedCharacter) {
		return !attackedCharacter.isEmpty() && !attackedCharacter.isTeam(team);
	}

	@Override
	public boolean addAsleep() {
		return false;
	}

}