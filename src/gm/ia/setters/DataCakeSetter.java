package gm.ia.setters;

import java.util.ArrayList;
import java.util.List;

import gm.Cake;
import gm.GameCharacter;
import gm.GameTable;
import gm.Player;
import gm.ia.CharacterUtils;
import gm.ia.DataCake;
import gm.pojos.Position;

public class DataCakeSetter {

	private GameTable gameTable;
	private GameCharacter[][] characterArray;
	private List<DataCake> enemies;
	private List<DataCake> me;
	private Player player;

	public DataCakeSetter(GameCharacter[][] characterArray, GameTable gameTable, Player player, String nextTeam) {
		this.characterArray = characterArray;
		this.gameTable = gameTable;
		this.player = player;
		this.enemies = new ArrayList<DataCake>();
		this.me = new ArrayList<DataCake>();
		if(!gameTable.getCakeList().isEmpty()){
			evalue(nextTeam);
		}
	}

	public void evalue(String nextTeam) {
		for (Cake cake : gameTable.getCakeList()) {
			List<Position> boomPositions = cake.getBoomPositions(characterArray);
			Counter counter = new Counter();
			fillCounters(boomPositions, counter);
			for (Position position : boomPositions) {
				GameCharacter character = CharacterUtils.getCharacterByPosition(characterArray, position);
				if(character.isValidSeat()){
				    boolean fatal = nextTeam.equals(cake.getTeam());
					character.setCake(true);
					character.setFatalCake(fatal);
					if(!character.isEmpty()){
					    DataCake dataCake = new DataCake(position, fatal,cake,counter.counterThem, counter.counterMine);
					    if(character.isTeam(player.getTeam())){
	                        me.add(dataCake);
	                    }else{
	                        enemies.add(dataCake);
	                    }
					    cake.setDataCake(dataCake);
					}
				}
			}
		}
	}

    private class Counter{
		
		private int counterMine;
		private List<Position> counterThem;
		
		public Counter(){
			  counterMine = 0;
			  counterThem = new ArrayList<Position>();
		}
		
		public void add(Position position){
			counterThem.add(position);
		}
		
	}

	private void fillCounters(List<Position> boomPositions, Counter counter) {
		for (Position position : boomPositions) {
			GameCharacter character = CharacterUtils.getCharacterByPosition(characterArray, position);
			if(CharacterUtils.isValid(character)){
				if(character.isTeam(player.getTeam())){
					counter.counterMine++;
				}else{
					counter.add(position);
				}
			}
		}
	}
	
	public List<DataCake> getExploitedEnemies() {
		return this.enemies;
	}
	
	public List<DataCake> getExploitedMine() {
		return this.me;
	}
}
