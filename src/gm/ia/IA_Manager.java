package gm.ia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gm.GameCharacter;
import gm.GameTable;
import gm.Player;
import gm.TableSeat;
import gm.cards.GunCard;
import gm.cards.KnifeCard;
import gm.ia.pojos.InfoAction;
import gm.ia.pojos.ValueData;
import gm.ia.pojos.IA_Character;
import gm.info.CardType;
import gm.info.MoneyValues;
import gm.info.TableObjects;
import gm.pojos.Position;

public class IA_Manager {

	private GameTable gameTable;
	private CharateresToAttackByKnifeGetter knifeGetter;
	private CharateresToAttackByGunGetter gunGetter;
	private int maxX;
	private int middle;

	public IA_Manager(GameTable gameTable) {
		this.gameTable = gameTable;
		this.maxX = gameTable.getMaxX() - 1;
		this.middle = gameTable.getMaxY() - 1 / 2;
		this.knifeGetter = new CharateresToAttackByKnifeGetter(gameTable);
		this.gunGetter = new CharateresToAttackByGunGetter(gameTable);
	}

	public InfoAction whoKill(GameCharacter[][] characterArray, Player player, String nextTeam, int currentGamers) {
		AsleepEnemyGetter asleepEnemyGetter = new AsleepEnemyGetter(characterArray, player, nextTeam, gameTable);
		if (asleepEnemyGetter.getAsleepNumber() >= 2) {
			return asleepEnemyGetter.getBestSleepCard("HAY MAS DE 2 DORMIDOS");
		}
		Map<String, GeneralTeam> generalTeams = new HashMap<String, GeneralTeam>();
		List<InfoAction> characterThatICanKill = getAttactedPositions(player, "PUEDO ATACARLO", characterArray);
		if (characterThatICanKill.size() > 0) {
			List<InfoActionUtils> charactesThatTheirCanAttacksMe = getThreats(player, characterThatICanKill,
					"PUEDE ATACARME", characterArray, currentGamers, generalTeams);
			if (charactesThatTheirCanAttacksMe.size() > 0) {
				if (charactesThatTheirCanAttacksMe.size() == 1) {
					return getOne(charactesThatTheirCanAttacksMe, "GANO ATACARME");
				}
				List<InfoActionUtils> awake = getBestValues(charactesThatTheirCanAttacksMe, "VER DESPIERTOS",
						InfoActionUtils.ENEMY_ASLEEP);
				if (awake.size() == 1) {
					return getOne(awake, InfoActionUtils.ENEMY_ASLEEP, "GANO DESPIERTOS: ");
				}
				List<InfoActionUtils> moreWeapons = getBestValues(awake, "MAS ARMAS",
						InfoActionUtils.ENEMY_WEAPONS_NUMBER);
				if (moreWeapons.size() == 1) {
					return getOne(moreWeapons, InfoActionUtils.ENEMY_WEAPONS_NUMBER, "GANO MAS ARMAS:");
				}
				List<InfoActionUtils> greatestThreats = getBestValues(moreWeapons, "MAS FLANCOS",
						InfoActionUtils.FLANCOS);
				if (greatestThreats.size() == 1) {
					return getOne(greatestThreats, InfoActionUtils.FLANCOS, "GANO FLANCOS:");
				}
				// **(MEJOR ARMA) CAKE, MOVECAKE
				List<InfoActionUtils> greatestWeapon = getBestValues(greatestThreats, "MEJOR ARMA",
						InfoActionUtils.ENEMY_HAS_KNIFE);
				if (greatestWeapon.size() == 1) {
					return getOne(greatestWeapon, "GANO MEJOR ARMA");
				}
				// **(MAS ARMA YO) CAKE, MOVECAKE
				List<InfoActionUtils> moreMeWeapons = getBestValues(greatestWeapon, "TENGO MAS ARMAS",
						InfoActionUtils.ME_WEAPONS_NUMBER);
				if (moreMeWeapons.size() == 1) {
					return getOne(moreMeWeapons, InfoActionUtils.ME_WEAPONS_NUMBER, "GANO TENGO MAS ARMAS:");
				}
				// **(MI MEJOR BUSINESS) CAKE, MOVECAKE
				List<InfoActionUtils> myGreatestBusinnes = getBestValues(moreMeWeapons, "MI MEJOR BUSINESS",
						InfoActionUtils.MY_BUSINESS);
				if (myGreatestBusinnes.size() == 1) {
					return getOne(myGreatestBusinnes, "GANO MI MEJOR BUSINESS");
				}
				return wonWhoHasBestBusinessVsWhoHasMoreCharacter(generalTeams, myGreatestBusinnes, asleepEnemyGetter,
						true, currentGamers, characterArray, player);
			} else {
				// **(NADIE ME ATACA) CAKE, MOVECAKE
				if (currentGamers > 3 && asleepEnemyGetter.getWithoutNextNumber() >= 3
						&& player.getNumberCard(CardType.SLEEP) >= 2) {
					return asleepEnemyGetter.getBestSleepCard("PUEDO ATACARLO//NO ME ATACA//3 o MAS JUGADORES, DORMIR");
				}
				return moreWeaponVsBestWeaponVsBestBussines(characterThatICanKill, asleepEnemyGetter, currentGamers,
						characterArray, generalTeams, player);
			}
		} else {
			// TODO: (NO PUEDO ATACAR) ZZZ, CAKE, MOVECAKE, CHANGE
			return null;
		}
	}

	private InfoAction moreWeaponVsBestWeaponVsBestBussines(List<InfoAction> characterThatICanKill,
			AsleepEnemyGetter sleepGetter, int currentGamers, GameCharacter[][] characterArray,
			Map<String, GeneralTeam> generalTeams, Player player) {
		List<InfoActionUtils> infoActionUtils = convertToAttackToMe(characterThatICanKill, characterArray,
				currentGamers, generalTeams);
		List<InfoActionUtils> awake = getBestValues(infoActionUtils, "VER DESPIERTOS", InfoActionUtils.ENEMY_ASLEEP);
		if (awake.size() == 1) {
			return getOne(awake, InfoActionUtils.ENEMY_ASLEEP, "GANO DESPIERTOS: ");
		}
		List<InfoActionUtils> moreWeapons = getBestValues(awake, "MAS ARMAS", InfoActionUtils.ENEMY_WEAPONS_NUMBER);
		if (moreWeapons.size() == 1) {
			return getOne(moreWeapons, InfoActionUtils.ENEMY_WEAPONS_NUMBER, "GANO MAS ARMAS:");
		}
		// **(MEJOR ARMA) CAKE, MOVECAKE
		List<InfoActionUtils> greatestWeapon = getBestValues(moreWeapons, "MEJOR ARMA",
				InfoActionUtils.ENEMY_HAS_KNIFE);
		if (greatestWeapon.size() == 1) {
			return getOne(greatestWeapon, "GANO MEJOR ARMA");
		}
		if (greatestWeapon.get(0).getValue(InfoActionUtils.ENEMY_WEAPONS_NUMBER) > 0
				&& player.getNumberCard(CardType.SLEEP) >= 2 && sleepGetter.getWithoutNextNumber() >= 2
				&& currentGamers >= 3) {////// DOS
			return sleepGetter.getBestSleepCard(
					"PUEDO ATACARLO//NO ME ATACA//MAS ARMAS=0//GANO MEJOR ARMA=0//2 o MAS JUGADORES NEXT, DORMIR");
		}
		// **(SU MEJOR BUSINESS) CAKE, MOVECAKE
		return wonWhoHasBestBusinessVsWhoHasMoreCharacter(generalTeams, greatestWeapon, sleepGetter, false,
				currentGamers, characterArray, player);

	}

	private List<InfoActionUtils> convertToAttackToMe(List<InfoAction> characterThatICanKill,
			GameCharacter[][] characterArray, int currentGamers, Map<String, GeneralTeam> generalTeamsMap) {
		List<InfoActionUtils> list = new ArrayList<InfoActionUtils>();
		for (InfoAction infoAction : characterThatICanKill) {
			ValueData enemy = buildValueData(infoAction.getAttackedPosition(), currentGamers, characterArray,
					generalTeamsMap);
			ValueData me = buildValueDataMe(infoAction.getAttackerPosition(), currentGamers, characterArray,
					generalTeamsMap, new ArrayList<Position>());
			InfoActionUtils infoActionUtils = new InfoActionUtils(infoAction, new ArrayList<Position>(), enemy, me);
			infoAction.addReason("NO ME ATACA");
			list.add(infoActionUtils);
		}
		return list;
	}

	private InfoAction wonWhoHasBestBusinessVsWhoHasMoreCharacter(Map<String, GeneralTeam> generalTeams,
			List<InfoActionUtils> myGreatestBusinnes, AsleepEnemyGetter sleepGetter, boolean canAttactMe,
			int currentGamers, GameCharacter[][] characterArray, Player player) {
		// **(SU MEJOR BUSINESS) CAKE, MOVECAKE
		List<InfoActionUtils> threirGreatestBusinnes = getBestValues(myGreatestBusinnes, "SU MEJOR BUSINESS",
				InfoActionUtils.THEIR_BUSINESS);
		if (threirGreatestBusinnes.size() == 1) {
			return getOne(threirGreatestBusinnes, "GANO SU MEJOR BUSINESS");
		}
		if (canAttactMe || theyHaveBusiness(threirGreatestBusinnes)
				|| !thereAreSleptAttacteds(sleepGetter, currentGamers, player)) {
			// **(MAS PERSONAJES) CAKE,MOVECAKE
			if (currentGamers < 3) {
				return getOne(threirGreatestBusinnes, "EL QUE SEA, < 3 JUGADORES");
			}
			int maxCharactes = 0;
			InfoActionUtils toReturn = null;
			for (InfoActionUtils attackToMe : threirGreatestBusinnes) {
				String team = attackToMe.getEnemyCharacter(characterArray).getTeam();
				if (generalTeams.get(team).getCountCharacters() >= maxCharactes) {
					maxCharactes = generalTeams.get(team).getCountCharacters();
					toReturn = attackToMe;
				}
			}
			toReturn.addReason("GANO MAS PERSONAJES:" + maxCharactes);
			return toReturn.getInfoAction();

		}
		return sleepGetter.getBestSleepCard("PUEDO ATACAR//NO PUEDE ATACAR//SIN BUSINESS//DUERMO PARA AHORRA CARTA");

	}

	private boolean thereAreSleptAttacteds(AsleepEnemyGetter sleepGetter, int currentGamers, Player player) {
		if (player.getNumberCard(CardType.SLEEP) >= 1) {
			return false;
		}
		int sleeptCharactes = sleepGetter.all();
		if (currentGamers == 3) {
			return sleeptCharactes >= 2 && player.getNumberCard(CardType.SLEEP) >= 2;
		}
		return currentGamers > 3 && sleeptCharactes >= 2;
	}

	private boolean theyHaveBusiness(List<InfoActionUtils> threirGreatestBusinnes) {
		return threirGreatestBusinnes.get(0).getValue(InfoActionUtils.THEIR_BUSINESS) > 0;
	}

	private MoneyNumberSystem getMoneySystemByTeam(Map<String, GeneralTeam> generalTeams, String team,
			GameCharacter[][] characterArray) {
		if (generalTeams.containsKey(team)) {
			return generalTeams.get(team).getMoneyNumberSystem();
		} else {
			GeneralTeam generalTeam = getTotalBusinessByTeam(team, characterArray);
			generalTeams.put(team, generalTeam);
			return generalTeam.getMoneyNumberSystem();
		}
	}

	private GeneralTeam getTotalBusinessByTeam(String team, GameCharacter[][] characterArray) {
		int countCharacters = 0;
		List<MoneyValues> totalMoneyValues = new ArrayList<MoneyValues>();
		for (int x = 0; x < gameTable.getMaxX(); x++) {
			for (int y = 0; y < gameTable.getMaxY(); y++) {
				GameCharacter character = CharacterUtils.getCharacterByXY(characterArray, x, y);
				if (character.isTeam(team)) {
					countCharacters++;
					TableSeat tableSeat = gameTable.getTableSeats()[y][x];
					if (character.isKing()) {
						totalMoneyValues.add(MoneyValues.KING);
					}
					if (tableSeat.has(TableObjects.BAR)) {
						totalMoneyValues.add(MoneyValues.BAR);
					}
					if (tableSeat.has(TableObjects.RESTAURANTS)) {
						totalMoneyValues.add(MoneyValues.RESTAURANT);
					}
					if (tableSeat.has(TableObjects.CASINOS)) {
						totalMoneyValues.add(MoneyValues.CASINO);
					}
					if (tableSeat.has(TableObjects.MACHINE)) {
						totalMoneyValues.add(MoneyValues.MACHINE);
					}
				}
			}
		}
		return new GeneralTeam(countCharacters, totalMoneyValues);
	}

	private class GeneralTeam {
		private int countCharacters;
		private MoneyNumberSystem moneyNumberSystem;

		public GeneralTeam(int countCharacters, List<MoneyValues> totalMoneyValues) {
			this.countCharacters = countCharacters;
			this.moneyNumberSystem = new MoneyNumberSystem(totalMoneyValues);
		}

		public int getCountCharacters() {
			return countCharacters;
		}

		public MoneyNumberSystem getMoneyNumberSystem() {
			return moneyNumberSystem;
		}

	}

	private InfoAction getOne(List<InfoActionUtils> attacksToMe, String key, String reason) {
		InfoActionUtils attackToMe = attacksToMe.get(0);
		InfoAction attackedPositionIA = attackToMe.getInfoAction();
		attackedPositionIA.addReason(reason + attackToMe.getValue(key));
		return attackToMe.getInfoAction();
	}

	private InfoAction getOne(List<InfoActionUtils> attacksToMe, String reason) {
		InfoAction attackedPositionIA = attacksToMe.get(0).getInfoAction();
		attackedPositionIA.addReason(reason);
		return attackedPositionIA;
	}

	private boolean isConer(Position attackerPosition) {
		return ((attackerPosition.getX() == 0 || attackerPosition.getX() == maxX) && attackerPosition.getY() != middle);
	}

	private List<InfoActionUtils> getThreats(Player player, List<InfoAction> attackedPositionIAs, String reason,
			GameCharacter[][] characterArray, int currentGamers, Map<String, GeneralTeam> generalTeamsMap) {
		List<InfoActionUtils> charactesThatTheirCanAttacksMe = new ArrayList<InfoActionUtils>();
		for (InfoAction infoAction : attackedPositionIAs) {
			List<Position> attacks = new ArrayList<Position>();
			attacks.addAll(enemyCanCutMe(player.getTeam(), infoAction, characterArray));
			Position enemyGunPosition = enemyCanShootMe(player.getTeam(), infoAction, characterArray);
			if (enemyGunPosition != null) {
				attacks.add(enemyGunPosition);
			}
			if (attacks.size() != 0) {
				infoAction.addReason(reason);
				ValueData enemy = buildValueData(infoAction.getAttackedPosition(), currentGamers, characterArray,
						generalTeamsMap);
				ValueData me = buildValueDataMe(infoAction.getAttackerPosition(), currentGamers, characterArray,
						generalTeamsMap, attacks);
				charactesThatTheirCanAttacksMe.add(new InfoActionUtils(infoAction, attacks, enemy, me));
			}
		}
		return charactesThatTheirCanAttacksMe;
	}

	private List<InfoActionUtils> getBestValues(List<InfoActionUtils> attacksToMe, String message, String key) {
		List<InfoActionUtils> greatestThreats = new ArrayList<InfoActionUtils>();
		float maxValue = 0;
		for (InfoActionUtils attackToMe : attacksToMe) {
			Float value = attackToMe.getValue(key);
			attackToMe.addReason(message);
			if (greatestThreats.size() == 0) {
				greatestThreats.add(attackToMe);
				maxValue = value;
			} else {
				if (value > maxValue) {
					greatestThreats = new ArrayList<InfoActionUtils>();
					greatestThreats.add(attackToMe);
					maxValue = value;
				} else {
					if (value == maxValue) {
						greatestThreats.add(attackToMe);
						maxValue = value;
					}
				}
			}
		}
		return greatestThreats;
	}

	private List<Position> enemyCanCutMe(String myTeam, InfoAction attackedPositionIA,
			GameCharacter[][] characterArray) {
		return knifeGetter.getTheirAttackPositions(characterArray, attackedPositionIA.getAttackedPosition(), myTeam);
	}

	private Position enemyCanShootMe(String myTeam, InfoAction infoAction, GameCharacter[][] characterArray) {
		return gunGetter.getTheirAttackPosition(characterArray, infoAction.getAttackedPosition(), myTeam);
	}

	public List<InfoAction> getAttactedPositions(Player player, String reason, GameCharacter[][] characterArray) {
		CharacterGetter characterGetter = new CharacterGetter(characterArray, gameTable);
		ManagerKillerCards managerKillerCards = new ManagerKillerCards();
		for (IA_Character myCharacter : characterGetter.getCharactersByTeam(player.getTeam())) {
			Position attackerPosition = myCharacter.getPosition();
			if (player.hasCard(CardType.KNIFE)) {
				List<Position> attackedPositionsList = knifeGetter.getMyAttackPositions(characterArray,
						attackerPosition, player);
				for (Position attackedPosition : attackedPositionsList) {
					managerKillerCards.add(new KnifeCard(attackerPosition, attackedPosition), attackerPosition,
							attackedPosition, reason);
				}
			}
			if (player.hasCard(CardType.GUN)) {
				Position attackedPosition = gunGetter.getMyAttackPosition(characterArray, attackerPosition, player);
				if (attackedPosition != null) {
					managerKillerCards.add(new GunCard(attackerPosition, attackedPosition), attackerPosition,
							attackedPosition, reason);
				}
			}
		}
		return managerKillerCards.getAttackedPositionIAs();
	}

	private ValueData buildValueDataMe(Position position, int currentGamers, GameCharacter[][] characterArray,
			Map<String, GeneralTeam> generalTeamsMap, List<Position> attacks) {
		float mayorHasKnife = 0;
		float mayorWeapons = 0;
		GameCharacter gameCharacter = CharacterUtils.getCharacterByPosition(characterArray, position);
		float mayorBusiness = setBusinessValue(generalTeamsMap, gameCharacter, position, characterArray);
		for (Position attack : attacks) {
			ValueData valueData = buildValueData(attack, currentGamers, characterArray, generalTeamsMap);
			if (valueData.getKnife() > mayorHasKnife) {
				mayorHasKnife = valueData.getKnife();
			}
			if (valueData.getValueWeapon() > mayorWeapons) {
				mayorWeapons = valueData.getValueWeapon();
			}
			if (valueData.getBusiness() > mayorBusiness) {
				mayorBusiness = valueData.getBusiness();
			}
		}

		return new ValueData(mayorWeapons, mayorHasKnife, mayorBusiness, gameCharacter.isSleeping());
	}

	private ValueData buildValueData(Position position, int currentGamers, GameCharacter[][] characterArray,
			Map<String, GeneralTeam> generalTeamsMap) {
		GameCharacter gameCharacter = CharacterUtils.getCharacterByPosition(characterArray, position);
		TableSeat tableSeat = gameTable.getTableSeatByPosition(position);
		float numberWeapon = 0;
		float knife;
		if ((gameCharacter.hasGun() || tableSeat.has(TableObjects.GUN)) && !isConer(position)) {
			numberWeapon = numberWeapon + 1;
		}
		if (gameCharacter.hasKnife() || tableSeat.has(TableObjects.KNIFE)) {
			numberWeapon = numberWeapon + 1;
			knife = 1f;
		} else {
			knife = 0f;
		}
		if (!tableSeat.has(TableObjects.GLASS)) {
			if (currentGamers > 2) {
				numberWeapon = numberWeapon + 0.5f;
			}
		}
		float businessValue = setBusinessValue(generalTeamsMap, gameCharacter, position, characterArray);
		return new ValueData(numberWeapon, knife, businessValue, gameCharacter.isSleeping());
	}

	private float setBusinessValue(Map<String, GeneralTeam> generalTeamsMap, GameCharacter character, Position position,
			GameCharacter[][] characterArray) {
		MoneyValues business = getBusinessByPosition(position);
		MoneyNumberSystem moneyNumberSystem = getMoneySystemByTeam(generalTeamsMap, character.getTeam(),
				characterArray);
		boolean king = character.isKing();
		return moneyNumberSystem.getNumber(new IA_Character(position, business, king));
	}

	private MoneyValues getBusinessByPosition(Position attackedPositionIA) {
		TableSeat tableSeat = gameTable.getTableSeatByPosition(attackedPositionIA);
		if (tableSeat.has(TableObjects.BAR)) {
			return MoneyValues.BAR;
		}
		if (tableSeat.has(TableObjects.RESTAURANTS)) {
			return MoneyValues.RESTAURANT;
		}
		if (tableSeat.has(TableObjects.CASINOS)) {
			return MoneyValues.CASINO;
		}
		if (tableSeat.has(TableObjects.MACHINE)) {
			return MoneyValues.MACHINE;
		}
		return MoneyValues.NOTTHING;
	}

}
