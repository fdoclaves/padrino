package gm.ia.getters;

import java.util.ArrayList;
import java.util.List;

import gm.Cake;
import gm.GameCharacter;
import gm.cards.CakeUtils;
import gm.ia.CharacterUtils;
import gm.ia.DataCake;
import gm.pojos.Position;

public class CakeGetter {

	private EnemyVsPosition best = null;
	private EnemyVsPosition peorEsNada = null;
	private CakeUtils cakeUtils;
	private GameCharacter[][] characterArray;
	private String myTeam;

	public CakeGetter(CakeUtils cakeUtils, GameCharacter[][] characterArray, String myTeam) {
		this.cakeUtils = cakeUtils;
		this.characterArray = characterArray;
		this.myTeam = myTeam;
	}

	public DataCake getBestPosition() {
		float bestValue = 0;
		float bestValuePeorEsNada = 0;
		for (int x = 0; x < characterArray[0].length; x++) {
			for (int y = 0; y < characterArray.length; y++) {
				Position posiblePosition = new Position(x, y);
				GameCharacter cakeCharacter = CharacterUtils.getCharacterByPosition(characterArray, posiblePosition);
				if (!cakeCharacter.isInvalidSeat()) {
					List<Position> enemies = new ArrayList<Position>();
					int attackMe = 0;
					float valueBusiness = 0;
					boolean hasCake = false;
					boolean hurtMe = false;
					List<Position> boomPositions = cakeUtils.getBoomPositions(posiblePosition, characterArray);
					for (Position boomPosition : boomPositions) {
						GameCharacter gameCharacter = CharacterUtils.getCharacterByPosition(characterArray,
								boomPosition);
						if (CharacterUtils.isValid(gameCharacter)) {
							if (gameCharacter.isTeam(myTeam)) {
								hurtMe = true;
								break;
							}
							enemies.add(boomPosition);
							if (gameCharacter.getAttackData().canAttack()) {
								attackMe++;
							}
							if (gameCharacter.hasCake()) {
								hasCake = true;
							}
							valueBusiness = valueBusiness + gameCharacter.getBusinessValue();
						}
					}
					int moveCake_iaTeam = getIfUseMoveCakeCardCanAttackMe(posiblePosition);
					float value = (attackMe*10 + valueBusiness)*enemies.size() + enemies.size()*100 - moveCake_iaTeam*0.5f;
					if(!hurtMe){
						if(hasCake){
							if(value > bestValuePeorEsNada){
								peorEsNada = new EnemyVsPosition(posiblePosition, enemies);
								bestValuePeorEsNada = value;
							}
						} else{
							if(value > bestValue){
								best = new EnemyVsPosition(posiblePosition, enemies);
								bestValue = value;
							}
						}
					}
					
				}
			}
		}

		if (best == null) {
			return null;
		}
		Cake cake = new Cake(best.position, myTeam);
		DataCake dataCake = new DataCake(best.position, false, cake, best.enemiesToKill, 0);
		cake.setDataCake(dataCake);
		return dataCake;
	}

	private int getIfUseMoveCakeCardCanAttackMe(Position posiblePosition) {
	    int counterMyTeam = 0;
	    Cake cake = new Cake(posiblePosition, myTeam);
	    List<Position> positions = cakeUtils.getMoveCakesPositions(cake, characterArray);
	    for (Position position : positions) {
	        GameCharacter gameCharacter = CharacterUtils.getCharacterByPosition(characterArray, position);
            if(CharacterUtils.isValid(gameCharacter)){
               if(gameCharacter.isTeam(myTeam)){
                   counterMyTeam++;
               }
            }
        }
        return counterMyTeam;
    }

    public Position getPeorEsNada() {
		if (peorEsNada == null) {
			return null;
		}
		return peorEsNada.position;
	}

	private class EnemyVsPosition {

		private Position position;
		private List<Position> enemiesToKill;


		public EnemyVsPosition(Position position, List<Position> enemiesToKill) {
			this.position = position;
			this.enemiesToKill = enemiesToKill;


		}

	}

}
