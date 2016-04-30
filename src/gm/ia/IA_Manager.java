package gm.ia;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import gm.Cake;
import gm.Card;
import gm.GameCharacter;
import gm.GameTable;
import gm.Player;
import gm.cards.BoomCard;
import gm.cards.CakeCard;
import gm.cards.CakeUtils;
import gm.cards.ChangeCard;
import gm.cards.GunCard;
import gm.cards.KnifeCard;
import gm.cards.MoveCakeCard;
import gm.cards.MoveCard;
import gm.ia.getters.AsleepEnemyGetter;
import gm.ia.getters.BoomGetter;
import gm.ia.getters.CakeGetter;
import gm.ia.getters.ChangeCardGetter;
import gm.ia.getters.DataCakeSetter;
import gm.ia.getters.IaComponentsSetter;
import gm.ia.getters.WhereMoveGetter;
import gm.ia.getters.WhoMoveGetter;
import gm.ia.pojos.InfoAction;
import gm.ia.pojos.ValueData;
import gm.info.CardType;
import gm.pojos.Position;

public class IA_Manager {

	private GameTable gameTable;

	public IA_Manager(GameTable gameTable) {
		this.gameTable = gameTable;
	}

	public InfoAction whoKill(GameCharacter[][] characterArray, Player player, String nextTeam, int currentGamers) {
		IaComponentsSetter componentSetter = new IaComponentsSetter(gameTable, characterArray, player, currentGamers);
		DataCakeSetter dataCakeSetter = new DataCakeSetter(characterArray, gameTable, player, nextTeam);
		CakeUtils cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
		MoverCakeGetter moverCakeGetter = new MoverCakeGetter(cakeUtils);
		int best = 0;
		ValueAndDataCake[] valueAndDataCakes = new ValueAndDataCake[3];
		if (player.hasCard(CardType.MOVE_CAKE)) {
			int moreEnemiesIndex = 1;
			int saveIndex = 2;
			fillValueAndDataCakesArray(characterArray, player.getTeam(), nextTeam, moverCakeGetter, best, moreEnemiesIndex, saveIndex,
					valueAndDataCakes);
			InfoAction infoAction = buildInfoAction(saveIndex, valueAndDataCakes, "ATTACK CAKE ME, MOVE CAKE");
			if (infoAction == null) {
				infoAction = buildInfoAction(moreEnemiesIndex, valueAndDataCakes, "MORE ENEMIES, MOVE CAKE");
			}
			if(infoAction != null){
				return infoAction;
			}
		}
		
		List<GameCharacter> iaTeam = componentSetter.getIaTeam();
		WhoMoveGetter whoMoveGetter = new WhoMoveGetter();
		WhereMoveGetter whereMoveGetter = new WhereMoveGetter(gameTable);
		if(dataCakeSetter.getExploitedMine().size() > 1 && player.getNumberCard(CardType.MOVE)==2){
			List<GameCharacter> enemies = componentSetter.getEnemyAttackDatas();
			Position whoMove = whoMoveGetter.whoMove(characterArray, player.getTeam(), cakeUtils, iaTeam, enemies, gameTable);
			Position whereMove = whereMoveGetter.whereMove(characterArray, componentSetter, player.getTeam(), whoMove);
			MoveCard moveCard = new MoveCard(whoMove, whereMove);
			return new InfoAction(moveCard, whoMove, whereMove, "MOVE, CAKE:"+dataCakeSetter.getExploitedMine().size());
			
		}
		
		AsleepEnemyGetter asleepEnemyGetter = new AsleepEnemyGetter(characterArray, player, nextTeam, gameTable);
		if (player.hasCard(CardType.SLEEP) && asleepEnemyGetter.getAsleepNumber() >= 3) {
			return asleepEnemyGetter.getBestSleepCard("HAY MAS DE 3 DORMIDOS");
		}

		DataCake dataCakeToNewCake = null;
		CakeGetter cakeGetter = new CakeGetter(cakeUtils, characterArray, player.getTeam());
		if (player.hasCard(CardType.CAKE)) {
			dataCakeToNewCake = cakeGetter.getBestPosition();
			if (dataCakeToNewCake != null && dataCakeToNewCake.enemiesByCake() >= 2) {
				Card card = new CakeCard(dataCakeToNewCake.getCake());
				return new InfoAction(card, null, null, "PUT CAKE:ENEMIES:" + dataCakeToNewCake.enemiesByCake());
			}
		}

		if (player.hasCard(CardType.SLEEP) && asleepEnemyGetter.getAsleepNumber() >= 2) {
			return asleepEnemyGetter.getBestSleepCard("HAY MAS DE 2 DORMIDOS");
		}

		BoomGetter boomGetter = new BoomGetter();
		for (DataCake dataCake : dataCakeSetter.getExploitedEnemies()) {
			if (!dataCake.isFatal() && dataCake.enemiesByCake() > 1 && dataCake.getMineByCake() == 0
					&& player.hasCard(CardType.BOOM)) {
				Cake bestCakeToBoom = boomGetter.getBestBoom(gameTable.getCakeList(), characterArray, nextTeam);
				if (bestCakeToBoom != null) {
					Card card = new BoomCard(bestCakeToBoom);
					return new InfoAction(card, null, null, "BOOM, 2 OR MORE GAMERS");
				}
			}
		}

		List<InfoAction> enemiesThatIaTeamCanKill = getAttactedPositions(iaTeam, "PUEDO ATACARLO");
		if (enemiesThatIaTeamCanKill.size() > 0) {
			List<GameCharacter> enemyAttackDatas = componentSetter.getEnemyAttackDatas();
			List<DataInfoActionGetter> enemiesThatIaTeamCanKillAndTheyCanAttackHim = getCharactesThatTheirCanAttacksMeAndICanKill(
					enemiesThatIaTeamCanKill, "PUEDE ATACARME", characterArray, currentGamers, dataCakeSetter,
					enemyAttackDatas);
			if (enemiesThatIaTeamCanKillAndTheyCanAttackHim.size() == 1) {
				return getOne(enemiesThatIaTeamCanKillAndTheyCanAttackHim, "GANO ATACARME");
			}
			List<DataInfoActionGetter> freeLetalCake = getBestValues(enemiesThatIaTeamCanKillAndTheyCanAttackHim,
					"VER FREE LETAL PASTEL", DataInfoActionGetter.ENEMY_HAS_NOT_LETAL_CAKE);
			if (freeLetalCake.size() == 1) {
				return getOne(freeLetalCake, DataInfoActionGetter.ENEMY_HAS_NOT_LETAL_CAKE, "GANO FREE FATAL CAKE: ");
			}
			List<DataInfoActionGetter> freeCake = getBestValues(freeLetalCake, "VER FREE PASTEL",
					DataInfoActionGetter.ENEMY_HAS_NOT_CAKE);
			if (freeCake.size() == 1) {
				return getOne(freeCake, DataInfoActionGetter.ENEMY_HAS_NOT_CAKE, "GANO FREE CAKE: ");
			}
			if (enemiesThatIaTeamCanKillAndTheyCanAttackHim.size() > 1) {
				List<DataInfoActionGetter> awake = getBestValues(freeCake, "VER DESPIERTOS",
						DataInfoActionGetter.ENEMY_ASLEEP);
				if (awake.size() == 1) {
					return getOne(awake, DataInfoActionGetter.ENEMY_ASLEEP, "GANO DESPIERTOS: ");
				}
				List<DataInfoActionGetter> greatestThreats = getBestValues(awake, "MAS FLANCOS",
						DataInfoActionGetter.FLANCOS);
				if (greatestThreats.size() == 1) {
					return getOne(greatestThreats, DataInfoActionGetter.FLANCOS, "GANO FLANCOS:");
				}
				List<DataInfoActionGetter> moreWeapons = getBestValues(greatestThreats, "MAS ARMAS",
						DataInfoActionGetter.ENEMY_WEAPONS_NUMBER);
				if (moreWeapons.size() == 1) {
					return getOne(moreWeapons, DataInfoActionGetter.ENEMY_WEAPONS_NUMBER, "GANO MAS ARMAS:");
				}
				List<DataInfoActionGetter> greatestWeapon = getBestValues(moreWeapons, "MEJOR ARMA",
						DataInfoActionGetter.ENEMY_HAS_KNIFE);
				if (greatestWeapon.size() == 1) {
					return getOne(greatestWeapon, "GANO MEJOR ARMA");
				}
				List<DataInfoActionGetter> moreMeWeapons = getBestValues(greatestWeapon, "TENGO MAS ARMAS",
						DataInfoActionGetter.ME_WEAPONS_NUMBER);
				if (moreMeWeapons.size() == 1) {
					return getOne(moreMeWeapons, DataInfoActionGetter.ME_WEAPONS_NUMBER, "GANO TENGO MAS ARMAS:");
				}
				List<DataInfoActionGetter> myGreatestBusinnes = getBestValues(moreMeWeapons, "MI MEJOR BUSINESS",
						DataInfoActionGetter.MY_BUSINESS);
				if (myGreatestBusinnes.size() == 1) {
					return getOne(myGreatestBusinnes, "GANO MI MEJOR BUSINESS");
				}
				return wonWhoHasBestBusinessVsWhoHasMoreCharacter(componentSetter.getGeneralTeams(), myGreatestBusinnes,
						asleepEnemyGetter, true, currentGamers, characterArray, player);
			} else {
				if (currentGamers > 3 && asleepEnemyGetter.getWithoutNextNumber() >= 3
						&& player.getNumberCard(CardType.SLEEP) >= 2) {
					return asleepEnemyGetter.getBestSleepCard("PUEDO ATACARLO//NO ME ATACA//3 o MAS JUGADORES, DORMIR");
				}
				return moreWeaponVsBestWeaponVsBestBussines(enemiesThatIaTeamCanKill, asleepEnemyGetter, currentGamers,
						characterArray, componentSetter.getGeneralTeams(), player, dataCakeSetter, enemyAttackDatas);
			}
		} else {
			// ** (NO PUEDO ATACAR) MOVE
			if (player.hasCard(CardType.SLEEP) && asleepEnemyGetter.getAsleepNumber() >= 1) {
				return asleepEnemyGetter.getBestSleepCard("NO PUEDO ATACAR, HAY AL MENOS UNO DORMIDO");
			}

			if (player.hasCard(CardType.CAKE)) {
				if (dataCakeToNewCake != null && dataCakeToNewCake.enemiesByCake() >= 1) {
					Card card = new CakeCard(dataCakeToNewCake.getCake());
					return new InfoAction(card, null, null, "PUT CAKE:ENEMIES:" + dataCakeToNewCake.enemiesByCake());
				}
			}

			if (player.hasCard(CardType.BOOM) && dataCakeSetter.getExploitedEnemies().size() >= 1) {
				Cake bestCakeToBoom = boomGetter.getBestBoom(gameTable.getCakeList(), characterArray, nextTeam);
				if (bestCakeToBoom != null) {
					return new InfoAction(new BoomCard(bestCakeToBoom), null, null, "NO PUEDO ATACAR//BEST BOOM CAKE");
				}
			}
			if (player.hasCard(CardType.SLEEP) && asleepEnemyGetter.all() >= 1) {
				return asleepEnemyGetter.getBestSleepCard("NO PUEDO ATACAR:" + asleepEnemyGetter.all());
			}
			if (player.hasCard(CardType.MOVE_CAKE)) {
				for (Cake cake : gameTable.getCakeList()) {
					float currentValor = getCakeValor(cake, cakeUtils, player.getTeam(), characterArray);
					if (valueAndDataCakes[best] != null && valueAndDataCakes[best].getValue() > currentValor) {
						return buildInfoActionMoveCard(valueAndDataCakes[best], cake);
					}
				}
			}
			
			if(player.getNumberCard(CardType.MOVE)==2){
				List<GameCharacter> enemies = componentSetter.getEnemyAttackDatas();
				Position whoMove = whoMoveGetter.whoMove(characterArray, player.getTeam(), cakeUtils, iaTeam, enemies, gameTable);
				Position whereMove = whereMoveGetter.whereMove(characterArray, componentSetter, player.getTeam(), whoMove);
				MoveCard moveCard = new MoveCard(whoMove, whereMove);
				return new InfoAction(moveCard, whoMove, whereMove, "NADA MAS QUE HACER, MOVE");
			}

			if (player.hasCard(CardType.CAKE)) {
				Position peorEsNada = cakeGetter.getPeorEsNada();
				if (peorEsNada != null) {
					Card card = new CakeCard(new Cake(peorEsNada, player.getTeam()));
					return new InfoAction(card, null, null, "PEOR ES NADA, PUT CAKE");
				}
			}
			ChangeCardGetter changeCardGetter = new ChangeCardGetter(player.getCards(), currentGamers);
			ChangeCard card = changeCardGetter.get();
			return new InfoAction(card, null, null, "NO PUEDO ATACAR//I DONT HAVE SLEEPCARD//CHANGE CARD");
		}
	}

	private InfoAction buildInfoAction(int index, ValueAndDataCake[] valueAndDataCakes, String reason) {
		InfoAction moveCakeAction = null;
		if (valueAndDataCakes[index] != null) {
			Position newPosition = valueAndDataCakes[index].getDataCake().getExplotedPosition();
			MoveCakeCard moveCakeCard = new MoveCakeCard(valueAndDataCakes[index].getDataCake().getCake(), newPosition);
			moveCakeAction = new InfoAction(moveCakeCard, null, null, reason);
		}
		return moveCakeAction;
	}

	private void fillValueAndDataCakesArray(GameCharacter[][] characterArray, String teamIa, String nextTeam,
			MoverCakeGetter moverCakeGetter, int best, int moreEnemies, int save,
			ValueAndDataCake[] valueAndDataCakes) {
		for (Cake cake : gameTable.getCakeList()) {
			DataCake oldPosition = cake.getDataCake();
			ValueAndDataCake valueAndDataCake = moverCakeGetter.getBestMoveCake(oldPosition.getCake(), characterArray,
					teamIa, nextTeam);
			DataCake newPosition = valueAndDataCake.getDataCake();
			if (oldPosition.getMineByCake() > newPosition.getMineByCake()
					&& bestValueFrom(valueAndDataCake.getValue(), valueAndDataCakes[save])) {
				valueAndDataCakes[save] = valueAndDataCake;
			} else {
				if (oldPosition.enemiesByCake() < newPosition.enemiesByCake()
						&& bestValueFrom(valueAndDataCake.getValue(), valueAndDataCakes[moreEnemies])) {
					if (newPosition.enemiesByCake() > oldPosition.enemiesByCake()) {
						valueAndDataCakes[moreEnemies] = valueAndDataCake;
					}
				} else {
					if (bestValueFrom(valueAndDataCake.getValue(), valueAndDataCakes[best])) {
						valueAndDataCakes[best] = valueAndDataCake;
					}
				}
			}
		}
	}

	private boolean bestValueFrom(float bestValorCake, ValueAndDataCake newValue) {
		if (newValue == null) {
			return true;
		}
		return bestValorCake > newValue.getValue();
	}

	private InfoAction buildInfoActionMoveCard(ValueAndDataCake valueAndDataCake, Cake cake) {
		String reason = "MOVE CAKE BY BUSINESS: " + valueAndDataCake.getValue();
		Position newPosition = valueAndDataCake.getDataCake().getExplotedPosition();
		MoveCakeCard moveCakeCard = new MoveCakeCard(cake, newPosition);
		return new InfoAction(moveCakeCard, null, null, reason);
	}

	private float getCakeValor(Cake cake, CakeUtils cakeUtils, String myTeam, GameCharacter[][] characterArray) {
		float value = 0;
		List<Position> boomPositions = cakeUtils.getBoomPositions(cake.getPosition(), characterArray);
		for (Position boomPosition : boomPositions) {
			GameCharacter gameCharacter = CharacterUtils.getCharacterByPosition(characterArray, boomPosition);
			if (CharacterUtils.isValid(gameCharacter)) {
				if (gameCharacter.isTeam(myTeam)) {
					value = value - 100;
					if (gameCharacter.getAttackData().canAttack()) {
						value = value - 10;
					}
					value = value - gameCharacter.getBusinessValue();
				} else {
					value = value + 100;
					if (gameCharacter.getAttackData().canAttack()) {
						value = value + 10;
					}
					value = value + gameCharacter.getBusinessValue();
				}
			}
		}
		return value;
	}

	private InfoAction moreWeaponVsBestWeaponVsBestBussines(List<InfoAction> enemiesThatIaTeamCanKill,
			AsleepEnemyGetter sleepGetter, int currentGamers, GameCharacter[][] characterArray,
			Map<String, GeneralTeam> generalTeams, Player player, DataCakeSetter boomCakeGetter,
			List<GameCharacter> enemyAttackDatas) {
		List<DataInfoActionGetter> infoActionUtils = convertToAttackToMe(enemiesThatIaTeamCanKill, characterArray,
				currentGamers, generalTeams, boomCakeGetter, enemyAttackDatas);
		List<DataInfoActionGetter> freeLetalCake = getBestValues(infoActionUtils, "VER FREE LETAL PASTEL",
				DataInfoActionGetter.ENEMY_HAS_NOT_LETAL_CAKE);
		if (freeLetalCake.size() == 1) {
			return getOne(freeLetalCake, DataInfoActionGetter.ENEMY_HAS_NOT_LETAL_CAKE, "GANO FREE FATAL CAKE: ");
		}
		List<DataInfoActionGetter> freeCake = getBestValues(freeLetalCake, "VER FREE PASTEL",
				DataInfoActionGetter.ENEMY_HAS_NOT_CAKE);
		if (freeCake.size() == 1) {
			return getOne(freeCake, DataInfoActionGetter.ENEMY_HAS_NOT_CAKE, "GANO FREE CAKE: ");
		}
		List<DataInfoActionGetter> awake = getBestValues(freeCake, "VER DESPIERTOS", DataInfoActionGetter.ENEMY_ASLEEP);
		if (awake.size() == 1) {
			return getOne(awake, DataInfoActionGetter.ENEMY_ASLEEP, "GANO DESPIERTOS: ");
		}
		List<DataInfoActionGetter> moreWeapons = getBestValues(awake, "MAS ARMAS",
				DataInfoActionGetter.ENEMY_WEAPONS_NUMBER);
		if (moreWeapons.size() == 1) {
			return getOne(moreWeapons, DataInfoActionGetter.ENEMY_WEAPONS_NUMBER, "GANO MAS ARMAS:");
		}
		// **(MEJOR ARMA) CAKE, MOVECAKE
		List<DataInfoActionGetter> greatestWeapon = getBestValues(moreWeapons, "MEJOR ARMA",
				DataInfoActionGetter.ENEMY_HAS_KNIFE);
		if (greatestWeapon.size() == 1) {
			return getOne(greatestWeapon, "GANO MEJOR ARMA");
		}
		if (greatestWeapon.get(0).getValue(DataInfoActionGetter.ENEMY_WEAPONS_NUMBER) > 0
				&& player.getNumberCard(CardType.SLEEP) >= 2 && sleepGetter.getWithoutNextNumber() >= 2
				&& currentGamers >= 3) {// //// DOS
			return sleepGetter.getBestSleepCard(
					"PUEDO ATACARLO//NO ME ATACA//MAS ARMAS=0//GANO MEJOR ARMA=0//2 o MAS JUGADORES NEXT, DORMIR");
		}
		// **(SU MEJOR BUSINESS) CAKE, MOVECAKE
		return wonWhoHasBestBusinessVsWhoHasMoreCharacter(generalTeams, greatestWeapon, sleepGetter, false,
				currentGamers, characterArray, player);

	}

	private List<DataInfoActionGetter> convertToAttackToMe(List<InfoAction> enemiesThatIaTeamCanKill,
			GameCharacter[][] characterArray, int currentGamers, Map<String, GeneralTeam> generalTeamsMap,
			DataCakeSetter boomCakeGetter, List<GameCharacter> enemyAttackDatas) {
		List<DataInfoActionGetter> list = new ArrayList<DataInfoActionGetter>();
		for (InfoAction infoAction : enemiesThatIaTeamCanKill) {
			for (GameCharacter GameCharacter : enemyAttackDatas) {
				AttackData attackData = GameCharacter.getAttackData();
				if (attackData.getPosition().isEquals(infoAction.getAttackedPosition())) {
					list.add(buildInfoActionUtils(characterArray, currentGamers, infoAction, "NO ME ATACA",
							boomCakeGetter, attackData));
					break;
				}
			}
		}
		return list;
	}

	private InfoAction wonWhoHasBestBusinessVsWhoHasMoreCharacter(Map<String, GeneralTeam> generalTeams,
			List<DataInfoActionGetter> myGreatestBusinnes, AsleepEnemyGetter sleepGetter, boolean canAttactMe,
			int currentGamers, GameCharacter[][] characterArray, Player player) {
		// **(SU MEJOR BUSINESS) CAKE, MOVECAKE
		List<DataInfoActionGetter> threirGreatestBusinnes = getBestValues(myGreatestBusinnes, "SU MEJOR BUSINESS",
				DataInfoActionGetter.THEIR_BUSINESS);
		if (threirGreatestBusinnes.size() == 1) {
			return getOne(threirGreatestBusinnes, "GANO SU MEJOR BUSINESS");
		}
		if (thereAreSleptAttacteds(sleepGetter, currentGamers, player) && !canAttactMe
				&& !theyHaveBusiness(threirGreatestBusinnes)) {
			return sleepGetter
					.getBestSleepCard("PUEDO ATACAR//NO PUEDE ATACAR//SIN BUSINESS//DUERMO PARA AHORRA CARTA");
		} else {
			// **(MAS PERSONAJES) CAKE,MOVECAKE
			if (currentGamers < 3) {
				return getOne(threirGreatestBusinnes, "EL QUE SEA, < 3 JUGADORES");
			}
			int maxCharactes = 0;
			DataInfoActionGetter toReturn = null;
			for (DataInfoActionGetter attackToMe : threirGreatestBusinnes) {
				String team = attackToMe.getEnemyCharacter(characterArray).getTeam();
				if (generalTeams.get(team).getCountCharacters() >= maxCharactes) {
					maxCharactes = generalTeams.get(team).getCountCharacters();
					toReturn = attackToMe;
				}
			}
			toReturn.addReason("GANO MAS PERSONAJES:" + maxCharactes);
			return toReturn.getInfoAction();
		}
	}

	private boolean thereAreSleptAttacteds(AsleepEnemyGetter sleepGetter, int currentGamers, Player player) {
		if (player.getNumberCard(CardType.SLEEP) >= 1) {
			return false;
		}
		int sleeptCharactes = sleepGetter.all();
		if (currentGamers == 3 && sleeptCharactes >= 2) {
			return player.getNumberCard(CardType.SLEEP) >= 2;
		}
		return currentGamers > 3 && sleeptCharactes >= 2;
	}

	private boolean theyHaveBusiness(List<DataInfoActionGetter> threirGreatestBusinnes) {
		return threirGreatestBusinnes.get(0).getValue(DataInfoActionGetter.THEIR_BUSINESS) > 0;
	}

	private InfoAction getOne(List<DataInfoActionGetter> attacksToMe, String key, String reason) {
		DataInfoActionGetter attackToMe = attacksToMe.get(0);
		InfoAction infoAction = attackToMe.getInfoAction();
		infoAction.addReason(reason + attackToMe.getValue(key));
		return attackToMe.getInfoAction();
	}

	private InfoAction getOne(List<DataInfoActionGetter> attacksToMe, String reason) {
		InfoAction infoAction = attacksToMe.get(0).getInfoAction();
		infoAction.addReason(reason);
		return infoAction;
	}

	private List<DataInfoActionGetter> getCharactesThatTheirCanAttacksMeAndICanKill(List<InfoAction> iaTeamInfoAction,
			String reason, GameCharacter[][] characterArray, int currentGamers, DataCakeSetter boomCakeGetter,
			List<GameCharacter> enemyAttackDatas) {
		List<DataInfoActionGetter> charactesThatTheirCanAttacksMeAndICanKill = new ArrayList<DataInfoActionGetter>();
		for (GameCharacter gameCharacter : enemyAttackDatas) {
			AttackData enemyAttackData = gameCharacter.getAttackData();
			if (enemyAttackData.canAttack()) {
				int totalAttack = 0;
				for (InfoAction infoAction : iaTeamInfoAction) {
					if (enemyAttackData.getPosition().isEquals(infoAction.getAttackedPosition())) {
						totalAttack++;
						charactesThatTheirCanAttacksMeAndICanKill.add(buildInfoActionUtils(characterArray,
								currentGamers, infoAction, reason, boomCakeGetter, enemyAttackData));
					}
					if (totalAttack < enemyAttackData.getTypeFlankNumber()) {
						continue;
					}
				}
			}
		}
		return charactesThatTheirCanAttacksMeAndICanKill;
	}

	private DataInfoActionGetter buildInfoActionUtils(GameCharacter[][] characterArray, int currentGamers,
			InfoAction infoAction, String reason, DataCakeSetter boomCakeGetter, AttackData enemyAttackData) {
		GameCharacter attackedGameCharacter = CharacterUtils.getCharacterByPosition(characterArray,
				infoAction.getAttackedPosition());
		GameCharacter attackerGameCharacter = CharacterUtils.getCharacterByPosition(characterArray,
				infoAction.getAttackerPosition());
		ValueData enemy = buildValueData(currentGamers, attackedGameCharacter, boomCakeGetter.getExploitedEnemies());
		ValueData me = buildValueDataMe(attackerGameCharacter, currentGamers, characterArray,
				enemyAttackData.getAttackPositions(), boomCakeGetter.getExploitedMine());
		DataInfoActionGetter infoActionUtils = new DataInfoActionGetter(infoAction, enemy, me, enemyAttackData);
		infoAction.addReason(reason);
		return infoActionUtils;
	}

	private List<DataInfoActionGetter> getBestValues(List<DataInfoActionGetter> attacksToMe, String message,
			String key) {
		List<DataInfoActionGetter> greatestThreats = new ArrayList<DataInfoActionGetter>();
		float maxValue = 0;
		for (DataInfoActionGetter attackToMe : attacksToMe) {
			Float value = attackToMe.getValue(key);
			attackToMe.addReason(message);
			if (greatestThreats.size() == 0) {
				greatestThreats.add(attackToMe);
				maxValue = value;
			} else {
				if (value > maxValue) {
					greatestThreats = new ArrayList<DataInfoActionGetter>();
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

	public List<InfoAction> getAttactedPositions(List<GameCharacter> iaTeam, String reason) {
		ManagerKillerCards managerKillerCards = new ManagerKillerCards();
		for (GameCharacter gameCharacter : iaTeam) {
			AttackData attackData = gameCharacter.getAttackData();
			Position position = gameCharacter.getPosition();
			if (attackData.canAttackWithKnife()) {
				for (Position attackedPosition : attackData.getKnifeAttacksPositions()) {
					managerKillerCards.add(new KnifeCard(position, attackedPosition), position, attackedPosition,
							reason);
				}
			}
			if (attackData.canAttackWithGun()) {
				Position attackedPosition = attackData.getGunAttackPosition();
				managerKillerCards.add(new GunCard(position, attackedPosition), position, attackedPosition, reason);
			}
		}
		return managerKillerCards.getAttackedPositionIAs();
	}

	private ValueData buildValueDataMe(GameCharacter gameCharacter, int currentGamers, GameCharacter[][] characterArray,
			List<Position> attacks, List<DataCake> dataCakes) {
		float mayorBusiness = gameCharacter.getBusinessValue();
		boolean mayorHasKnife = false;
		float mayorWeapons = 0f;
		for (Position attacked : attacks) {
			GameCharacter attackedGameCharacter = CharacterUtils.getCharacterByPosition(characterArray, attacked);
			if (attackedGameCharacter.hasKnife()) {
				mayorHasKnife = true;
			}
			if (attackedGameCharacter.getWeaponValue() > mayorWeapons) {
				mayorWeapons = attackedGameCharacter.getWeaponValue();
			}
			if (attackedGameCharacter.getBusinessValue() > mayorBusiness) {
				mayorBusiness = attackedGameCharacter.getBusinessValue();
			}
		}
		DataCake dataCake = hasCake(gameCharacter.getPosition(), dataCakes, gameCharacter);
		return new ValueData(mayorWeapons, mayorHasKnife, mayorBusiness, gameCharacter.isSleeping(), dataCake);
	}

	private DataCake hasCake(Position characterPosition, List<DataCake> dataCakes, GameCharacter gameCharacter) {
		if (gameCharacter.hasCake()) {
			for (DataCake dataCake : dataCakes) {
				Position explotedPosition = dataCake.getExplotedPosition();
				if (characterPosition.isEquals(explotedPosition)) {
					return dataCake;
				}
			}
		}
		return null;
	}

	private ValueData buildValueData(int currentGamers, GameCharacter gameCharacter, List<DataCake> dataCakes) {
		Position position = gameCharacter.getPosition();
		float businessValue = gameCharacter.getBusinessValue();
		float weaponValue = gameCharacter.getWeaponValue();
		boolean hasKnife = gameCharacter.getAttackData().hasKnife();
		DataCake dataCake = hasCake(position, dataCakes, gameCharacter);
		return new ValueData(weaponValue, hasKnife, businessValue, gameCharacter.isSleeping(), dataCake);
	}
}
