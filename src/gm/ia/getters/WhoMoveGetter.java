package gm.ia.getters;

import java.util.ArrayList;
import java.util.List;

import gm.Cake;
import gm.GameCharacter;
import gm.GameTable;
import gm.TableSeat;
import gm.cards.CakeUtils;
import gm.ia.CharacterUtils;
import gm.info.TableObjects;
import gm.pojos.Position;

public class WhoMoveGetter {

    public Position whoMove(GameCharacter[][] characterArray, String myTeam, CakeUtils cakeUtils,
            List<GameCharacter> iaTeam, List<GameCharacter> enemies, GameTable gameTable) {
        List<Position> dangerourMoveCake = getDangerousMoveCake(characterArray, myTeam, cakeUtils,
                gameTable.getCakeList());
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
            if (value >= moreValue) {
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
            List<Cake> cakeList) {
        List<Position> dangerourMoveCake = new ArrayList<Position>();
        for (Cake cake : cakeList) {
            List<Position> validPositions = cakeUtils.getValidPositions(cake, characterArray);
            for (Position position : validPositions) {
                GameCharacter gameCharacter = CharacterUtils.getCharacterByPosition(characterArray, position);
                if (gameCharacter.isTeam(myTeam)) {
                    dangerourMoveCake.add(position);
                }
            }
        }
        return dangerourMoveCake;
    }

    private float getValueByGameCharacter(GameCharacter gameCharacter, List<Position> dangerourMoveCake, TableSeat tableSeat, List<Position> dangerousWeapon) {
        float value = 0;
        if (gameCharacter.isSleeping()) {
            return -10000f;
        } else {
            if (gameCharacter.hasCake()) {
                value = setValueIfHasCake(gameCharacter, value);
            } else {
                value = setValueIfHasNotCake(gameCharacter, dangerourMoveCake, tableSeat, value, dangerousWeapon);
            }
        }
        return value;
    }

    private float setValueIfHasNotCake(GameCharacter gameCharacter, List<Position> dangerourMoveCake,
            TableSeat tableSeat, float value, List<Position> dangerousWeapon) {
        for (Position position : dangerourMoveCake) {
            if (gameCharacter.getPosition().isEquals(position)) {
                value += 50;
                break;
            }
        }
        for (Position position : dangerousWeapon) {
            if(position.isEquals(gameCharacter.getPosition())){
                value += 50;
            }
        }

        float valueBusiness = gameCharacter.getBusinessValue() * 5;
        if (valueBusiness == 0 && !gameCharacter.getAttackData().canAttack()) {
            value += 10;
        }
        value -= valueBusiness * 5;
        if (tableSeat.has(TableObjects.GLASS)) {
            value += 5;
        }
        if (tableSeat.has(TableObjects.GUN) || tableSeat.has(TableObjects.KNIFE)) {
            value -= 1;
        }
        return value;
    }

    private float setValueIfHasCake(GameCharacter gameCharacter, float value) {
        value += 1000;
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
