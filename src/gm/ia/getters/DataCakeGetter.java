package gm.ia.getters;

import java.util.ArrayList;
import java.util.List;

import gm.Cake;
import gm.GameCharacter;
import gm.GameTable;
import gm.Player;
import gm.ia.CharacterUtils;
import gm.ia.DataCake;
import gm.pojos.Position;

public class DataCakeGetter {

	private GameTable gameTable;
	private GameCharacter[][] characterArray;
	private List<DataCake> enemies;
	private List<DataCake> me;
	private Player player;

	public DataCakeGetter(GameCharacter[][] characterArray, GameTable gameTable, Player player, String nextTeam) {
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
				if(!character.isEmpty()){
					boolean fatal = nextTeam.equals(cake.getTeam());
					if(character.isTeam(player.getTeam())){
						me.add(new DataCake(position, fatal,cake,counter.counterThem, counter.counterMine));
					}else{
						enemies.add(new DataCake(position, fatal,cake,counter.counterThem, counter.counterMine));
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
			if(!character.isEmpty()){
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
