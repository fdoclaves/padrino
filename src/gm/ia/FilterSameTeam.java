package gm.ia;

import gm.GameCharacter;

public class FilterSameTeam implements Filter {

	private String team;

	public FilterSameTeam(String team) {
		this.team = team;
	}

	@Override
	public boolean addIfTeam(GameCharacter attackedCharacter) {
		return !attackedCharacter.isTeam(team);
	}

	@Override
	public boolean addAsleep() {
		return false;
	}

}