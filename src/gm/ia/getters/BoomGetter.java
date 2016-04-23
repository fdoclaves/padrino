package gm.ia.getters;

import java.util.ArrayList;
import java.util.List;

import gm.Cake;
import gm.GameCharacter;
import gm.ia.CharacterUtils;
import gm.ia.DataCake;
import gm.pojos.Position;

public class BoomGetter {

	public Cake getBestBoom(List<Cake> cakes, GameCharacter[][] characters, String nextTeam) {
		List<KillNumberOrAttackToMePojo> pojosList = new ArrayList<KillNumberOrAttackToMePojo>();
		fillPojosList(cakes, characters, nextTeam, pojosList);
		KillNumberOrAttackToMePojo best = whichIsBestPojoToBoom(pojosList);
		return best.cake;
	}

	private KillNumberOrAttackToMePojo whichIsBestPojoToBoom(List<KillNumberOrAttackToMePojo> pojosList) {
		KillNumberOrAttackToMePojo best = pojosList.get(0);
		for (KillNumberOrAttackToMePojo pojo : pojosList) {
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
		return best;
	}

	private void fillPojosList(List<Cake> cakes, GameCharacter[][] characters, String nextTeam,
			List<KillNumberOrAttackToMePojo> list) {
		for (Cake cake : cakes) {
			DataCake dataCake = cake.getDataCake();
			if (!isFatal(nextTeam, cake) && !canHurtMe(dataCake.getMineByCake())) {
				addToList(characters, list, cake, dataCake);
			}
		}
	}

	private void addToList(GameCharacter[][] characters, List<KillNumberOrAttackToMePojo> list, Cake cake,
			DataCake dataCake) {
		int attacksToMe = getAttacksToMeByCakeKill(dataCake, characters);
		float businessValue = getBusinessValue(dataCake, characters);
		int numberToKill = dataCake.enemiesByCake();
		list.add(new KillNumberOrAttackToMePojo(cake, numberToKill, attacksToMe, businessValue));
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

		public KillNumberOrAttackToMePojo(Cake cake, int numberToKill, int attacksToMe, float businessValue) {
			this.cake = cake;
			this.numberToKill = numberToKill;
			this.attacksToMe = attacksToMe;
			this.businessValue = businessValue;
		}

		public Cake cake;

		int numberToKill = 0;

		int attacksToMe = 0;

		float businessValue = 0;
	}
}
