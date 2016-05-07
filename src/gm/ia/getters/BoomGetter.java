package gm.ia.getters;

import java.util.ArrayList;
import java.util.List;

import gm.Cake;
import gm.GameCharacter;
import gm.cards.CakeUtils;
import gm.ia.DataCake;
import gm.pojos.Position;
import gm.utils.CharacterUtils;

public class BoomGetter {

	public Cake getBestBoom(GameCharacter[][] characters, List<Cake> cakes, CakeUtils cakeUtils, String nextTeam,
			String iaTeam) {
		List<KillNumberOrAttackToMePojo> pojosList = fillPojosList(cakes, characters, nextTeam, cakeUtils, iaTeam);
		if (pojosList.isEmpty()) {
			return null;
		}
		KillNumberOrAttackToMePojo best = whichIsBestPojoToBoom(pojosList);
		return best.cake;
	}

	private KillNumberOrAttackToMePojo whichIsBestPojoToBoom(List<KillNumberOrAttackToMePojo> pojosList) {
		KillNumberOrAttackToMePojo best = pojosList.get(0);
		for (KillNumberOrAttackToMePojo pojo : pojosList) {
			if(pojo.moveCake > best.moveCake) {
				best = pojo;
			}else{
				if (pojo.numberToKill > best.numberToKill) {
					best = pojo;
				}
				if (pojo.numberToKill == best.numberToKill) {
					if (pojo.attacksToMe > best.attacksToMe) {
						best = pojo;
					}
					if (pojo.attacksToMe == best.attacksToMe) {
						if (pojo.businessValue > best.businessValue) {
							best = pojo;
						}
					}
				}
			}
		}
		return best;
	}

	private List<KillNumberOrAttackToMePojo> fillPojosList(List<Cake> cakes, GameCharacter[][] characters,
			String nextTeam, CakeUtils cakeUtils, String iaTeam) {
		List<KillNumberOrAttackToMePojo> list = new ArrayList<KillNumberOrAttackToMePojo>();
		for (Cake cake : cakes) {
			DataCake dataCake = cake.getDataCake();
			if (!isFatal(nextTeam, cake) && !canHurtMe(dataCake.getMineByCake())) {
				addToList(characters, list, cake, dataCake, cakeUtils, iaTeam);
			}
		}
		return list;
	}

	private void addToList(GameCharacter[][] characters, List<KillNumberOrAttackToMePojo> list, Cake cake,
			DataCake dataCake, CakeUtils cakeUtils, String iaTeam) {
		int attacksToMe = getAttacksToMeByCakeKill(dataCake, characters);
		float businessValue = getBusinessValue(dataCake, characters);
		int numberToKill = dataCake.enemiesByCake();
		int moveCake = getMoveCakeNumber(cakeUtils, characters, cake, iaTeam);
		list.add(new KillNumberOrAttackToMePojo(cake, numberToKill, attacksToMe, businessValue, moveCake));
	}

	private int getMoveCakeNumber(CakeUtils cakeUtils, GameCharacter[][] characters, Cake cake, String iaTeam) {
		int moveCakesPositionMe = 0;
		List<Position> moveCakesPositions = cakeUtils.getMoveCakesPositions(cake, characters);
		for (Position moveCakesPosition : moveCakesPositions) {
			GameCharacter gameCharacter = CharacterUtils.getCharacterByPosition(characters, moveCakesPosition);
			if (CharacterUtils.isValid(gameCharacter) && gameCharacter.isTeam(iaTeam)) {
				moveCakesPositionMe++;
			}
		}
		return moveCakesPositionMe;
	}

	private boolean isFatal(String nextTeam, Cake cake) {
		return cake.getTeam().equals(nextTeam);
	}

	private boolean canHurtMe(int hurtME) {
		return hurtME > 0;
	}

	private float getBusinessValue(DataCake dataCake, GameCharacter[][] characterArray) {
		float bussinessValue = 0;
		for (Position position : dataCake.getEnemiesByCake()) {
			GameCharacter gameCharacter = CharacterUtils.getCharacterByPosition(characterArray, position);
			bussinessValue += gameCharacter.getBusinessValue();
		}
		return bussinessValue;
	}

	private int getAttacksToMeByCakeKill(DataCake dataCake, GameCharacter[][] characterArray) {
		int attacks = 0;
		for (Position position : dataCake.getEnemiesByCake()) {
			GameCharacter gameCharacter = CharacterUtils.getCharacterByPosition(characterArray, position);
			attacks += gameCharacter.getAttackData().getAttackPositions().size();
		}
		return attacks;
	}

	private class KillNumberOrAttackToMePojo {

		private int moveCake;

		public Cake cake;

		int numberToKill = 0;

		int attacksToMe = 0;

		float businessValue = 0;

		public KillNumberOrAttackToMePojo(Cake cake, int numberToKill, int attacksToMe, float businessValue,
				int moveCake) {
			this.cake = cake;
			this.numberToKill = numberToKill;
			this.attacksToMe = attacksToMe;
			this.businessValue = businessValue;
			this.moveCake = moveCake;
		}

	}
}
