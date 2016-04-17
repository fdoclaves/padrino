package gm;

import java.util.ArrayList;
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
	
	public Cake(Position position, String team, GameTable gameTable) {
		this.position = position;
		this.team = team;
		this.gameTable = gameTable;
	}

	public void inicialize(GameTable gameTable) { 
		this.gameTable = gameTable;
	}

	public String getTeam() {
		return team;
	}

	public void boom(GameCharacter[][] characters) {
		for (Position position : getBoomPositions(characters)) {
			Killer.kill(characters, position);
		}
		List<Cake> cakeList = gameTable.getCakeList();
		cakeList.remove(this);
	}

	public List<Position> getBoomPositions(GameCharacter[][] characters) {
		List<Position> exploteCharacters = new ArrayList<Position>();
		exploteCharacters.add(position);
		if (isOnSide()) {
			killSideCharacters(characters, exploteCharacters);
		} else {
			exploteCharacters.add(new Position(position.getX() - 1, position.getY()));
			exploteCharacters.add(new Position(position.getX() + 1, position.getY()));
		}
		return exploteCharacters;
	}

	private void killSideCharacters(GameCharacter[][] characters, List<Position> exploteCharacters) {
		if (isConner()) {
			if (position.getX() == 0) {
				killConnerCharecters(exploteCharacters, characters, 1);
			} else {
				killConnerCharecters(exploteCharacters, characters, -1);
			}
		} else {
			exploteCharacters.add(new Position(position.getX(), position.getY() - 1));
			exploteCharacters.add(new Position(position.getX(), position.getY() + 1));
		}
	}

	private void killConnerCharecters(List<Position> exploteCharacters, GameCharacter[][] characters, int next) {
		if (position.getY() == 0) {
			exploteCharacters.add(new Position(position.getX(), position.getY() + 1));
			exploteCharacters.add(new Position(position.getX() + next, position.getY()));
		} else {
			exploteCharacters.add(new Position(position.getX(), position.getY() - 1));
			exploteCharacters.add(new Position(position.getX() + next, position.getY()));
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
