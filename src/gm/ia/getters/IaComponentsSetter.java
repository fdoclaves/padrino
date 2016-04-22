package gm.ia.getters;

import gm.GameCharacter;
import gm.GameTable;
import gm.Player;
import gm.TableSeat;
import gm.ia.AttackData;
import gm.ia.CharacterUtils;
import gm.ia.GeneralTeam;
import gm.info.CardType;
import gm.info.TableObjects;
import gm.pojos.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IaComponentsSetter {

    private List<GameCharacter> iaTeam;

    private List<GameCharacter> enemies;
    
    private BusinessValueGetter businessValueGetter;
    
    private Map<String, GeneralTeam> generalTeams;

    private int currentGamers;
    
    private int maxX;

    private int middle;

    public IaComponentsSetter(GameTable gameTable, GameCharacter[][] characterArray, Player player, int currentGamers) {
        this.currentGamers = currentGamers;
        this.maxX = gameTable.getMaxX() - 1;
        this.middle = (gameTable.getMaxY() - 1) / 2;
        generalTeams = new HashMap<String, GeneralTeam>();
        iaTeam = new ArrayList<GameCharacter>();
        enemies = new ArrayList<GameCharacter>();
        businessValueGetter = new BusinessValueGetter(characterArray, gameTable);
        fillList(gameTable, characterArray, player);
    }

    private void fillList(GameTable gameTable, GameCharacter[][] characterArray, Player player) {
        CharateresToAttackByKnifeGetter knifeGetter = new CharateresToAttackByKnifeGetter(gameTable);
        CharateresToAttackByGunGetter gunGetter = new CharateresToAttackByGunGetter(gameTable);
        for (int x = 0; x < gameTable.getMaxX(); x++) {
            for (int y = 0; y < gameTable.getMaxY(); y++) {
                Position position = new Position(x, y);
                GameCharacter gameCharacter = CharacterUtils.getCharacterByPosition(characterArray, position);
                TableSeat tableSeat = gameTable.getTableSeatByPosition(position);
                if (CharacterUtils.isValid(gameCharacter)) {
                    float businessValue = businessValueGetter.setBusinessValue(generalTeams, gameCharacter, position);
                    gameCharacter.setBusinessValue(businessValue);
                    boolean hasKnife = hasKnife(gameCharacter, tableSeat);
                    float numberWeapon = getWeaponValueAndKnowIfHasKnife(position, gameCharacter, tableSeat, hasKnife);
                    gameCharacter.setWeaponValue(numberWeapon);
                    if (gameCharacter.isTeam(player.getTeam())) {
                        AttackData attackData = buildAttackDataMine(characterArray, gameCharacter, knifeGetter,
                                gunGetter, position, player,hasKnife);
                        gameCharacter.setAttackData(attackData);
                        iaTeam.add(gameCharacter);
                    } else {
                        AttackData attackData = buildAttackData(characterArray, gameCharacter, knifeGetter,
                                gunGetter, position, player.getTeam(), hasKnife);
                        gameCharacter.setAttackData(attackData);
                        enemies.add(gameCharacter);
                    }
                }
            }
        }
    }
    
    private float getWeaponValueAndKnowIfHasKnife(Position position ,GameCharacter gameCharacter, TableSeat tableSeat, boolean hasKnife){
        float numberWeapon = 0;
        if (hasGun(gameCharacter, tableSeat) && !isConer(position)) {
            numberWeapon++;
        }
        if (hasKnife) {
            numberWeapon++;
        }
        if (!tableSeat.has(TableObjects.GLASS)) {
            if (currentGamers > 2) {
                numberWeapon += 0.5f;
            }
        }
        return numberWeapon;
    }

    private boolean hasGun(GameCharacter gameCharacter, TableSeat tableSeat) {
        return gameCharacter.hasGun() || tableSeat.has(TableObjects.GUN);
    }

    private boolean hasKnife(GameCharacter gameCharacter, TableSeat tableSeat) {
        return gameCharacter.hasKnife() || tableSeat.has(TableObjects.KNIFE);
    }
    
    private boolean isConer(Position attackerPosition) {
        return ((attackerPosition.getX() == 0 || attackerPosition.getX() == maxX) && attackerPosition.getY() != middle);
    }

    private AttackData buildAttackDataMine(GameCharacter[][] characters, GameCharacter gameCharacter,
            CharateresToAttackByKnifeGetter knifeGetter, CharateresToAttackByGunGetter gunGetter, Position position,
            Player player, boolean hasKnife) {
        List<Position> knifeAttacks = getKnifeAttack(characters, knifeGetter, position, player);
        Position gunAttack = getGunAttack(characters, gunGetter, position, player);
        return new AttackData(knifeAttacks, gunAttack, position, hasKnife);
    }

    private List<Position> getKnifeAttack(GameCharacter[][] characters, CharateresToAttackByKnifeGetter knifeGetter,
            Position position, Player player) {
        if (player.hasCard(CardType.KNIFE)) {
            return knifeGetter.getMyAttackPositions(characters, position, player);
        }
        return new ArrayList<Position>();
    }

    private Position getGunAttack(GameCharacter[][] characters, CharateresToAttackByGunGetter gunGetter,
            Position position, Player player) {
        if (player.hasCard(CardType.GUN)) {
            return gunGetter.getMyAttackPosition(characters, position, player);
        }
        return null;
    }

    private AttackData buildAttackData(GameCharacter[][] characterArray, GameCharacter gameCharacter,
            CharateresToAttackByKnifeGetter knifeGetter, CharateresToAttackByGunGetter gunGetter, Position position,
            String myTeam, boolean hasKnife) {
        List<Position> knifeAttacks = knifeGetter.getTheirAttackPositions(characterArray, position, myTeam);
        Position gunAttack = gunGetter.getTheirAttackPosition(characterArray, position, myTeam);
        return new AttackData(knifeAttacks, gunAttack, position, hasKnife);
    }

    public List<GameCharacter> getIaTeamThatCanBeAttackByThey() {
        return iaTeam;
    }

    public List<GameCharacter> getEnemyAttackDatas() {
        return enemies;
    }
    
    public Map<String, GeneralTeam> getGeneralTeams() {
        return this.generalTeams;
    }
}
