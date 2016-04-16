package gm;

import java.util.List;

import gm.pojos.Position;

public class Cake {

	private GameTable gameTable;

	private Position position;

	private String team;

	public Cake(Position position, String team) {
		this.position = position;
		this.team = team;
	}

	public void inicialize(GameTable gameTable) {
		this.gameTable = gameTable;
	}

	public String getTeam() {
		return team;
	}

	public void boom(GameCharacter[][] characters) {
		Killer.kill(characters, position);
		if (isOnSide()) {
			killSideCharacters(characters);
		} else {
			Killer.kill(characters, new Position(position.getX() - 1, position.getY()));
			Killer.kill(characters, new Position(position.getX() + 1, position.getY()));
		}
		List<Cake> cakeList = gameTable.getCakeList();
		cakeList.remove(this);
	}

	private void killSideCharacters(GameCharacter[][] characters) {
		if (isConner()) {
			if (position.getX() == 0) {
				killConnerCharecters(characters, 1);
			} else {
				killConnerCharecters(characters, -1);
			}
		} else {
			Killer.kill(characters, new Position(position.getX(), position.getY() - 1));
			Killer.kill(characters, new Position(position.getX(), position.getY() + 1));
		}
	}

	private void killConnerCharecters(GameCharacter[][] characters, int next) {
		if (position.getY() == 0) {
			Killer.kill(characters, new Position(position.getX(), position.getY() + 1));
			Killer.kill(characters, new Position(position.getX() + next, position.getY()));
		} else {
			Killer.kill(characters, new Position(position.getX(), position.getY() - 1));
			Killer.kill(characters, new Position(position.getX() + next, position.getY()));
		}
	}

	private boolean isConner() {
		return position.getY() == 0 || position.getY() == gameTable.getMaxY() - 1;
	}

	private boolean isOnSide() {
		return position.getX() == 0 || position.getX() == gameTable.getMaxX() - 1;
	}

	public Position getPosition() {
		return position;
	}

	public void changePosition(Position newPosition) {
		this.position = newPosition;
	}

}
