package gm.ia;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gm.ia.pojos.InfoAction;
import gm.ia.pojos.ValueData;
import gm.pojos.Position;
import gm.GameCharacter;

public class InfoActionUtils {
	public static final String THEIR_BUSINESS = "THEIR_BUSINESS";
	public static final String ME_WEAPONS_NUMBER = "ME_WEAPONS_NUMBER";
	public static final String ME_HAS_KNIFE = "ME_HAS_KNIFE";
	public static final String FLANCOS = "FLANCOS";
	public static final String ENEMY_WEAPONS_NUMBER = "ENEMY_WEAPONS_NUMBER";
	public static final String MY_BUSINESS = "BUSINESS";
	public static final String ENEMY_HAS_KNIFE = "ENEMY_HAS_KNIFE";
	public static final String ENEMY_ASLEEP = "ENEMY_ASLEEP";
	private Map<String, Float> valores;
	private Position enemyPosition;
	private Position myPosition;
	private InfoAction infoAction;
	private List<Position> attacks;

	InfoActionUtils(InfoAction infoAction, List<Position> attacks, ValueData enemy, ValueData me) {
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
		this.attacks = attacks;
		this.valores.put(FLANCOS, Float.valueOf(attacks.size()));
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

	public List<Position> getTheirAttacks() {
		return this.attacks;
	}

	public List<Position> getAttacks() {
		return attacks;
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