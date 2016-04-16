package gm.ia;

import gm.GameCharacter;

public class FilterOnlyTeam implements Filter {

	private String team;

	public FilterOnlyTeam(String team) {
		this.team = team;
	}

	@Override
	public boolean addIfTeam(GameCharacter attackedCharacter) {
		return attackedCharacter.isTeam(team);
	}

	@Override
	public boolean addAsleep() {
		return true;
	}

}