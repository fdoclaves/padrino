package gm.ia;

import java.util.ArrayList;
import java.util.List;

import gm.Cake;
import gm.GameCharacter;
import gm.cards.CakeUtils;
import gm.pojos.Position;

public class MoverCakeGetter {
	
	private CakeUtils cakeUtils;
	
	public MoverCakeGetter(CakeUtils cakeUtils){
		this.cakeUtils = cakeUtils;
	}

	public ValueAndDataCake getBestMoveCake(Cake cake, GameCharacter[][] characterArray, String myTeam, String nextTeam) {
		List<Position> posiblePositions = cakeUtils.getValidPositions(cake, characterArray);
		ValueAndDataCake bestValueAndDataCake = null;
		float mayorValue = -1000;
		for (Position posiblePosition : posiblePositions) {
			ValueAndDataCake valueAndDataCake = getValueAndDataCake(cake, characterArray, posiblePosition, myTeam, nextTeam);
			if(valueAndDataCake.getValue() >= mayorValue){
				bestValueAndDataCake = valueAndDataCake;
				mayorValue = valueAndDataCake.getValue();
			}
		}
		List<Position> currentBoomPositions = cake.getBoomPositions(characterArray);
		int currentHurtMe = 0;
		for (Position boomPosition : currentBoomPositions) {
			GameCharacter gameCharacter = CharacterUtils.getCharacterByPosition(characterArray, boomPosition);
			if(gameCharacter.isTeam(myTeam)){
				currentHurtMe++;
			}
		}
		if(currentHurtMe > bestValueAndDataCake.getDataCake().getMineByCake()){
			bestValueAndDataCake.addValue(1000*currentHurtMe);
		}
		return bestValueAndDataCake;
	}

	private ValueAndDataCake getValueAndDataCake(Cake cake, GameCharacter[][] characterArray, 
			Position posiblePosition, String myTeam, String nextTeam) {
		float value = 0;
		List<Position> enemies = new ArrayList<Position>();
		int mine = 0;
		List<Position> boomPositions = cakeUtils.getBoomPositions(posiblePosition, characterArray);
		boolean isFatal = cake.getTeam().equals(nextTeam);
		for (Position boomPosition : boomPositions) {
			GameCharacter gameCharacter = CharacterUtils.getCharacterByPosition(characterArray, boomPosition);
			if(CharacterUtils.isValid(gameCharacter)){
				if(gameCharacter.isTeam(myTeam)){
					mine++;
					value = value - 100;
					if(gameCharacter.getAttackData().canAttack()){
						value = value - 10;
					}
					value = value - gameCharacter.getBusinessValue();
				}else{
					enemies.add(boomPosition);
					value = value + 100;
					if(gameCharacter.getAttackData().canAttack()){
						value = value + 10;
					}
					value = value + gameCharacter.getBusinessValue();
				}
				if(gameCharacter.hasCake()){
					value = value - 40;
				}
			}
		}
		if(isFatal){
			value = value + 0.5f;
		}
		return new ValueAndDataCake(value,new DataCake(posiblePosition, isFatal, cake, enemies, mine));
	}

}
