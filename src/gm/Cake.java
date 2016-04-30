package gm;

import java.util.List;
import java.util.Map;

import gm.cards.CakeUtils;
import gm.ia.DataCake;
import gm.pojos.Position;

public class Cake {

	private GameTable gameTable;

	private Position position;

	private String team;

	private DataCake dataCake;
	
	private CakeUtils cakeUtils;
	
	public Cake(Position position, String team) {
		this.position = position;
		this.team = team;
	}
	
	public Cake(Position position, String team, GameTable gameTable) {
		this.position = position;
		this.team = team;
		this.gameTable = gameTable;
		this.cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
	}

	public void inicialize(GameTable gameTable) { 
		this.gameTable = gameTable;
		this.cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
	}

	public String getTeam() {
		return team;
	}

	public void boom(GameCharacter[][] characters, Map<String, Player> players) {
		for (Position position : getBoomPositions(characters)) {
			Killer.kill(characters, position, players);
		}
		List<Cake> cakeList = gameTable.getCakeList();
		cakeList.remove(this);
	}

	public List<Position> getBoomPositions(GameCharacter[][] characters) {
		return cakeUtils.getBoomPositions(position, characters);
	}

	public Position getPosition() {
		return position;
	}

	public void changePosition(Position newPosition) {
		this.position = newPosition;
	}

	public void setDataCake(DataCake dataCake) {
		this.dataCake = dataCake;
	}
	
	public DataCake getDataCake(){
		return this.dataCake;
	}

}
