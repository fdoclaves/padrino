package gm.ia.getters;

import java.util.ArrayList;
import java.util.List;

import gm.Cake;
import gm.CharacterUtils;
import gm.GameCharacter;
import gm.GameTable;
import gm.TableSeat;
import gm.cards.CakeUtils;
import gm.info.TableObjects;
import gm.pojos.Position;

public class WhoMoveGetter {

    public Position whoMove(GameCharacter[][] characterArray, String myTeam, CakeUtils cakeUtils,
            List<GameCharacter> iaTeam, List<GameCharacter> enemies, GameTable gameTable, String nextTeam) {
        List<Position> dangerourMoveCake = getDangerousMoveCake(characterArray, myTeam, cakeUtils,
                gameTable.getCakeList(), nextTeam);
        List<Position> dangerousWeapon = getDangerousWeapons(enemies);
        return getPositionByBestValue(iaTeam, gameTable, dangerourMoveCake,dangerousWeapon);
    }

    private Position getPositionByBestValue(List<GameCharacter> iaTeam, GameTable gameTable,
            List<Position> dangerourMoveCake, List<Position> dangerousWeapon) {
        Position moreDangerous = null;
        float moreValue = 0f;
        for (GameCharacter gameCharacter : iaTeam) {
            TableSeat tableSeat = gameTable.getTableSeatByPosition(gameCharacter.getPosition());
            float value = getValueByGameCharacter(gameCharacter, dangerourMoveCake, tableSeat, dangerousWeapon);
            if (value >= moreValue && !gameCharacter.isSleeping()) {
                moreDangerous = gameCharacter.getPosition();
                moreValue = value;
            }
        }
        return moreDangerous;
    }

    private List<Position> getDangerousWeapons(List<GameCharacter> enemies) {
        List<Position> dangerousWeapons = new ArrayList<Position>();
        for (GameCharacter enemy : enemies) {
            dangerousWeapons.addAll(enemy.getAttackData().getAttackPositions());
        }
        return dangerousWeapons;
    }

    private List<Position> getDangerousMoveCake(GameCharacter[][] characterArray, String myTeam, CakeUtils cakeUtils,
            List<Cake> cakeList, String nextTeam) {
        List<Position> dangerourMoveCake = new ArrayList<Position>();
        for (Cake cake : cakeList) {
            if(!cake.getTeam().equals(nextTeam)){
                List<Position> validPositions = cakeUtils.getMoveCakesPositions(cake, characterArray);
                for (Position position : validPositions) {
                    GameCharacter gameCharacter = CharacterUtils.getCharacterByPosition(characterArray, position);
                    if (gameCharacter.isTeam(myTeam)) {
                        dangerourMoveCake.add(position);
                    }
                }
            }
        }
        return dangerourMoveCake;
    }

    private float getValueByGameCharacter(GameCharacter gameCharacter, List<Position> dangerourMoveCake, TableSeat tableSeat, List<Position> dangerousWeapon) {
        float value = 0;
            if (gameCharacter.hasCake()) {
                value = getValueIfHasCake(gameCharacter);
            } else {
                value = getValueIfHasNotCake(gameCharacter, dangerourMoveCake, tableSeat, dangerousWeapon);
            }
        return value;
    }

    private float getValueIfHasNotCake(GameCharacter gameCharacter, List<Position> dangerourMoveCake,
            TableSeat tableSeat, List<Position> dangerousWeapon) {
        float value = 0;
        for (Position position : dangerousWeapon) {
            if(position.isEquals(gameCharacter.getPosition())){
                value += 50;
            }
        }

        float valueBusiness = gameCharacter.getBusinessValue() * 10;
        if (valueBusiness == 0 && !gameCharacter.getAttackData().canAttack()) {
            value += 10;
            value += getMoveCakeDangerous(gameCharacter, dangerourMoveCake);
        }
        value -= valueBusiness;
        if (tableSeat.has(TableObjects.GLASS)) {
            value += 1;
        }
        if (tableSeat.has(TableObjects.GUN) || tableSeat.has(TableObjects.KNIFE)) {
            value -= 1;
        }
        return value;
    }

    private float getMoveCakeDangerous(GameCharacter gameCharacter, List<Position> dangerourMoveCake) {
        float value = 0;
        for (Position position : dangerourMoveCake) {
            if (gameCharacter.getPosition().isEquals(position)) {
                value += 5;
                break;
            }
        }
        return value;
    }

    private float getValueIfHasCake(GameCharacter gameCharacter) {
        float value = 1000;
        if (gameCharacter.hasFatalCake()) {
            value += 100;
        }
        if (gameCharacter.isKing()) {
            value += 10;
        }
        if (gameCharacter.hasGun() || gameCharacter.hasKnife()) {
            value += 5;
        }
        return value;
    }

}
