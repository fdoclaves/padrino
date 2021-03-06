package gm.ia;

import java.util.HashMap;
import java.util.Map;

import gm.ia.pojos.InfoAction;
import gm.ia.pojos.ValueData;
import gm.pojos.Position;
import gm.utils.CharacterUtils;
import gm.GameCharacter;

public class DataInfoActionGetter {
	public static final String THEIR_BUSINESS = "THEIR_BUSINESS";
	public static final String ME_WEAPONS_NUMBER = "ME_WEAPONS_NUMBER";
	public static final String ME_HAS_KNIFE = "ME_HAS_KNIFE";
	public static final String FLANCOS = "FLANCOS";
	public static final String ENEMY_WEAPONS_NUMBER = "ENEMY_WEAPONS_NUMBER";
	public static final String MY_BUSINESS = "BUSINESS";
	public static final String ENEMY_HAS_KNIFE = "ENEMY_HAS_KNIFE";
	public static final String ENEMY_ASLEEP = "ENEMY_ASLEEP";
	public static final String ENEMY_HAS_NOT_CAKE = "ENEMY_HAS_NOT_CAKE";
	public static final String ENEMY_HAS_NOT_LETAL_CAKE = "ENEMY_HAS_NOT_LETAL_CAKE";
	private Map<String, Float> valores;
	private Position enemyPosition;
	private Position myPosition;
	private InfoAction infoAction;

	DataInfoActionGetter(InfoAction infoAction, ValueData enemy, ValueData me, AttackData enemyAttackData) {
		this.infoAction = infoAction;
		enemyPosition = infoAction.getAttackedPosition();
		myPosition = infoAction.getAttackerPosition();
		this.valores = new HashMap<String, Float>();
		this.valores.put(ENEMY_WEAPONS_NUMBER, enemy.getValueWeapon());
		this.valores.put(ENEMY_HAS_KNIFE, enemy.getKnife());
		this.valores.put(THEIR_BUSINESS, enemy.getBusiness());
		this.valores.put(MY_BUSINESS, me.getBusiness());
		this.valores.put(ME_HAS_KNIFE, me.getKnife());
		this.valores.put(ME_WEAPONS_NUMBER, me.getValueWeapon());
		this.valores.put(ENEMY_ASLEEP, enemy.getAwake());
		this.valores.put(FLANCOS, enemyAttackData.getTypeFlankNumber());
		this.valores.put(ENEMY_HAS_NOT_CAKE, enemy.getHasNotCake());
		this.valores.put(ENEMY_HAS_NOT_LETAL_CAKE, enemy.getHasNotFatalCake());
	}

	public void addReason(String reason) {
		this.infoAction.addReason(reason);
	}

	public Float getValue(String key) {
		return this.valores.get(key);
	}

	public Position getEnemyPosition() {
		return enemyPosition;
	}

	public Position getMyPosition() {
		return myPosition;
	}

	public GameCharacter getEnemyCharacter(GameCharacter[][] characterArray) {
		return CharacterUtils.getCharacterByPosition(characterArray, enemyPosition);
	}

	public GameCharacter getMyCharacter(GameCharacter[][] characterArray) {
		return CharacterUtils.getCharacterByPosition(characterArray, myPosition);
	}

	public InfoAction getInfoAction() {
		return this.infoAction;
	}

}