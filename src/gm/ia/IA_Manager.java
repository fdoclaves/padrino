package gm.ia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gm.Cake;
import gm.Card;
import gm.GameCharacter;
import gm.GameTable;
import gm.Player;
import gm.TableSeat;
import gm.cards.BoomCard;
import gm.cards.ChangeCard;
import gm.cards.GunCard;
import gm.cards.KnifeCard;
import gm.ia.pojos.InfoAction;
import gm.ia.pojos.ValueData;
import gm.ia.getters.AsleepEnemyGetter;
import gm.ia.getters.BoomGetter;
import gm.ia.getters.ChangeCardGetter;
import gm.ia.getters.DataCakeGetter;
import gm.ia.getters.CharateresToAttackByGunGetter;
import gm.ia.getters.CharateresToAttackByKnifeGetter;
import gm.ia.getters.MoneyNumberGetter;
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
        this.middle = (gameTable.getMaxY() - 1) / 2;
        this.knifeGetter = new CharateresToAttackByKnifeGetter(gameTable);
        this.gunGetter = new CharateresToAttackByGunGetter(gameTable);
    }

    public InfoAction whoKill(GameCharacter[][] characterArray, Player player, String nextTeam, int currentGamers) {
        AsleepEnemyGetter asleepEnemyGetter = new AsleepEnemyGetter(characterArray, player, nextTeam, gameTable);
        if (asleepEnemyGetter.getAsleepNumber() >= 2) {
            return asleepEnemyGetter.getBestSleepCard("HAY MAS DE 2 DORMIDOS");
        }
        DataCakeGetter dataCakeGetter = new DataCakeGetter(characterArray, gameTable, player, nextTeam);
        BoomGetter boomGetter = new BoomGetter(knifeGetter, gunGetter);
        for (DataCake dataCake : dataCakeGetter.getExploitedEnemies()) {
            if (!dataCake.isFatal() && dataCake.enemiesByCake() > 1 && dataCake.getMineByCake() == 0
                    && player.hasCard(CardType.BOOM)) {
                Cake bestCakeToBoom = boomGetter.getBoomCake(dataCakeGetter, player.getTeam(), characterArray);
                Card card = new BoomCard(bestCakeToBoom);
                return new InfoAction(card, null, null, "BOOM, 2 OR MORE GAMERS");
            }
        }
        AttackDataGetter attackDataGetter = new AttackDataGetter(gameTable, characterArray, player);
        List<AttackData> iaTeam = attackDataGetter.getIaTeamThatCanBeAttackByThey();
        Map<String, GeneralTeam> generalTeams = new HashMap<String, GeneralTeam>();
        List<InfoAction> enemiesThatIaTeamCanKill = getAttactedPositions(iaTeam, "PUEDO ATACARLO");
        if (enemiesThatIaTeamCanKill.size() > 0) {
            List<AttackData> enemyAttackDatas = attackDataGetter.getEnemyAttackDatas();
            List<DataInfoActionGetter> enemiesThatIaTeamCanKillAndTheyCanAttackHim = getCharactesThatTheirCanAttacksMeAndICanKill(
                    enemiesThatIaTeamCanKill, "PUEDE ATACARME", characterArray, currentGamers, generalTeams,
                    dataCakeGetter, enemyAttackDatas);
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
                List<DataInfoActionGetter> moreWeapons = getBestValues(awake, "MAS ARMAS",
                        DataInfoActionGetter.ENEMY_WEAPONS_NUMBER);
                if (moreWeapons.size() == 1) {
                    return getOne(moreWeapons, DataInfoActionGetter.ENEMY_WEAPONS_NUMBER, "GANO MAS ARMAS:");
                }
                List<DataInfoActionGetter> greatestThreats = getBestValues(moreWeapons, "MAS FLANCOS",
                        DataInfoActionGetter.FLANCOS);
                if (greatestThreats.size() == 1) {
                    return getOne(greatestThreats, DataInfoActionGetter.FLANCOS, "GANO FLANCOS:");
                }
                List<DataInfoActionGetter> greatestWeapon = getBestValues(greatestThreats, "MEJOR ARMA",
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
                return wonWhoHasBestBusinessVsWhoHasMoreCharacter(generalTeams, myGreatestBusinnes,
                        asleepEnemyGetter, true, currentGamers, characterArray, player);
            } else {
                // **(NADIE ME ATACA) ************CONSIDERAR BOOM (gamers > 2
                // attackToMe >= 1)
                if (currentGamers > 3 && asleepEnemyGetter.getWithoutNextNumber() >= 3
                        && player.getNumberCard(CardType.SLEEP) >= 2) {
                    return asleepEnemyGetter
                            .getBestSleepCard("PUEDO ATACARLO//NO ME ATACA//3 o MAS JUGADORES, DORMIR");
                }
                return moreWeaponVsBestWeaponVsBestBussines(enemiesThatIaTeamCanKill, asleepEnemyGetter, currentGamers,
                        characterArray, generalTeams, player, dataCakeGetter, enemyAttackDatas);
            }
        } else {
            // ** (NO PUEDO ATACAR) CAKE, MOVECAKE
            if (player.hasCard(CardType.SLEEP) && asleepEnemyGetter.all() >= 1) {
                return asleepEnemyGetter.getBestSleepCard("NO PUEDO ATACAR");
            }
            if (player.hasCard(CardType.BOOM) && dataCakeGetter.getExploitedEnemies().size() >= 1) {
                Cake bestCakeToBoom = boomGetter.getBoomCake(dataCakeGetter, player.getTeam(), characterArray);
                if (bestCakeToBoom != null) {
                    return new InfoAction(new BoomCard(bestCakeToBoom), null, null, "NO PUEDO ATACAR//BEST BOOM CAKE");
                }
            }
            ChangeCardGetter changeCardGetter = new ChangeCardGetter(player.getCards(), currentGamers);
            ChangeCard card = changeCardGetter.get();
            return new InfoAction(card, null, null, "NO PUEDO ATACAR//I DONT HAVE SLEEPCARD");
        }
    }

    private InfoAction moreWeaponVsBestWeaponVsBestBussines(List<InfoAction> enemiesThatIaTeamCanKill,
            AsleepEnemyGetter sleepGetter, int currentGamers, GameCharacter[][] characterArray,
            Map<String, GeneralTeam> generalTeams, Player player, DataCakeGetter boomCakeGetter,
            List<AttackData> enemyAttackDatas) {
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
        List<DataInfoActionGetter> awake = getBestValues(freeCake, "VER DESPIERTOS",
                DataInfoActionGetter.ENEMY_ASLEEP);
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
            return sleepGetter
                    .getBestSleepCard("PUEDO ATACARLO//NO ME ATACA//MAS ARMAS=0//GANO MEJOR ARMA=0//2 o MAS JUGADORES NEXT, DORMIR");
        }
        // **(SU MEJOR BUSINESS) CAKE, MOVECAKE
        return wonWhoHasBestBusinessVsWhoHasMoreCharacter(generalTeams, greatestWeapon, sleepGetter, false,
                currentGamers, characterArray, player);

    }

    private List<DataInfoActionGetter> convertToAttackToMe(List<InfoAction> enemiesThatIaTeamCanKill,
            GameCharacter[][] characterArray, int currentGamers, Map<String, GeneralTeam> generalTeamsMap,
            DataCakeGetter boomCakeGetter, List<AttackData> enemyAttackDatas) {
        List<DataInfoActionGetter> list = new ArrayList<DataInfoActionGetter>();
        for (InfoAction infoAction : enemiesThatIaTeamCanKill) {
            for (AttackData attackData : enemyAttackDatas) {
                if (attackData.getPosition().isEquals(infoAction.getAttackedPosition())) {
                    list.add(buildInfoActionUtils(characterArray, currentGamers, generalTeamsMap, infoAction,
                            "NO ME ATACA", boomCakeGetter, attackData));
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
        if (canAttactMe || theyHaveBusiness(threirGreatestBusinnes)
                || !thereAreSleptAttacteds(sleepGetter, currentGamers, player)) {
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

    private boolean theyHaveBusiness(List<DataInfoActionGetter> threirGreatestBusinnes) {
        return threirGreatestBusinnes.get(0).getValue(DataInfoActionGetter.THEIR_BUSINESS) > 0;
    }

    private MoneyNumberGetter getMoneySystemByTeam(Map<String, GeneralTeam> generalTeams, String team,
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

        private MoneyNumberGetter moneyNumberSystem;

        public GeneralTeam(int countCharacters, List<MoneyValues> totalMoneyValues) {
            this.countCharacters = countCharacters;
            this.moneyNumberSystem = new MoneyNumberGetter(totalMoneyValues);
        }

        public int getCountCharacters() {
            return countCharacters;
        }

        public MoneyNumberGetter getMoneyNumberSystem() {
            return moneyNumberSystem;
        }

    }

    private InfoAction getOne(List<DataInfoActionGetter> attacksToMe, String key, String reason) {
        DataInfoActionGetter attackToMe = attacksToMe.get(0);
        InfoAction attackedPositionIA = attackToMe.getInfoAction();
        attackedPositionIA.addReason(reason + attackToMe.getValue(key));
        return attackToMe.getInfoAction();
    }

    private InfoAction getOne(List<DataInfoActionGetter> attacksToMe, String reason) {
        InfoAction attackedPositionIA = attacksToMe.get(0).getInfoAction();
        attackedPositionIA.addReason(reason);
        return attackedPositionIA;
    }

    private boolean isConer(Position attackerPosition) {
        return ((attackerPosition.getX() == 0 || attackerPosition.getX() == maxX) && attackerPosition.getY() != middle);
    }

    private List<DataInfoActionGetter> getCharactesThatTheirCanAttacksMeAndICanKill(
            List<InfoAction> iaTeamInfoAction, String reason, GameCharacter[][] characterArray, int currentGamers,
            Map<String, GeneralTeam> generalTeamsMap, DataCakeGetter boomCakeGetter, List<AttackData> enemyAttackDatas) {
        List<DataInfoActionGetter> charactesThatTheirCanAttacksMeAndICanKill = new ArrayList<DataInfoActionGetter>();
        for (AttackData enemyAttackData : enemyAttackDatas) {
            if (enemyAttackData.canAttack()) {
                int totalAttack = 0;
                for (InfoAction infoAction : iaTeamInfoAction) {
                    if (enemyAttackData.getPosition().isEquals(infoAction.getAttackedPosition())) {
                        totalAttack++;
                        charactesThatTheirCanAttacksMeAndICanKill.add(buildInfoActionUtils(characterArray,
                                currentGamers, generalTeamsMap, infoAction, reason, boomCakeGetter, enemyAttackData));
                    }
                    if (totalAttack < enemyAttackData.totalFlank()) {
                        continue;
                    }
                }
            }
        }
        return charactesThatTheirCanAttacksMeAndICanKill;
    }

    private DataInfoActionGetter buildInfoActionUtils(GameCharacter[][] characterArray, int currentGamers,
            Map<String, GeneralTeam> generalTeamsMap, InfoAction infoAction, String reason,
            DataCakeGetter boomCakeGetter, AttackData enemyAttackData) {
        ValueData enemy = buildValueData(infoAction.getAttackedPosition(), currentGamers, characterArray,
                generalTeamsMap, boomCakeGetter.getExploitedEnemies());
        ValueData me = buildValueDataMe(infoAction.getAttackerPosition(), currentGamers, characterArray,
                generalTeamsMap, enemyAttackData.getAttackPositions(), boomCakeGetter.getExploitedMine());
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

    public List<InfoAction> getAttactedPositions(List<AttackData> iaTeam, String reason) {
        ManagerKillerCards managerKillerCards = new ManagerKillerCards();
        for (AttackData attackData : iaTeam) {
            if (attackData.canAttackWithKnife()) {
                Position attackerPosition = attackData.getPosition();
                for (Position attackedPosition : attackData.getKnifeAttacksPositions()) {
                    managerKillerCards.add(new KnifeCard(attackerPosition, attackedPosition), attackerPosition,
                            attackedPosition, reason);
                }
            }
            if (attackData.canAttackWithGun()) {
                Position attackerPosition = attackData.getPosition();
                Position attackedPosition = attackData.getGunAttackPosition();
                managerKillerCards.add(new GunCard(attackerPosition, attackedPosition), attackerPosition,
                        attackedPosition, reason);
            }
        }
        return managerKillerCards.getAttackedPositionIAs();
    }

    private ValueData buildValueDataMe(Position position, int currentGamers, GameCharacter[][] characterArray,
            Map<String, GeneralTeam> generalTeamsMap, List<Position> attacks, List<DataCake> dataCakes) {
        float mayorHasKnife = 0;
        float mayorWeapons = 0;
        GameCharacter gameCharacter = CharacterUtils.getCharacterByPosition(characterArray, position);
        float mayorBusiness = setBusinessValue(generalTeamsMap, gameCharacter, position, characterArray);
        for (Position attack : attacks) {
            ValueData valueData = buildValueData(attack, currentGamers, characterArray, generalTeamsMap, dataCakes);
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
        return new ValueData(mayorWeapons, mayorHasKnife, mayorBusiness, gameCharacter.isSleeping(), hasCake(
                position, dataCakes));
    }

    private DataCake hasCake(Position characterPosition, List<DataCake> dataCakes) {
        for (DataCake dataCake : dataCakes) {
            Position explotedPosition = dataCake.getExplotedPosition();
            if (characterPosition.isEquals(explotedPosition)) {
                return dataCake;
            }
        }
        return null;
    }

    private ValueData buildValueData(Position position, int currentGamers, GameCharacter[][] characterArray,
            Map<String, GeneralTeam> generalTeamsMap, List<DataCake> dataCakes) {
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
        return new ValueData(numberWeapon, knife, businessValue, gameCharacter.isSleeping(), hasCake(position,
                dataCakes));
    }

    private float setBusinessValue(Map<String, GeneralTeam> generalTeamsMap, GameCharacter character,
            Position position, GameCharacter[][] characterArray) {
        MoneyValues business = getBusinessByPosition(position);
        MoneyNumberGetter moneyNumberSystem = getMoneySystemByTeam(generalTeamsMap, character.getTeam(),
                characterArray);
        boolean king = character.isKing();
        return moneyNumberSystem.getValue(new IA_Character(position, business, king));
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
