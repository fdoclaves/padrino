package gt.ia.getters;

import java.util.ArrayList;
import java.util.List;

import gm.Cake;
import gm.GameCharacter;
import gm.GameTable;
import gm.TableSeat;
import gm.cards.CakeUtils;
import gm.ia.CharacterUtils;
import gm.ia.DataCake;
import gm.ia.getters.DataCakeGetter;
import gm.ia.getters.IaComponentsSetter;
import gm.info.TableObjects;
import gm.pojos.Position;

public class MoveGetter {

    public Position whoMove(GameCharacter[][] characterArray, String myTeam, CakeUtils cakeUtils,
            IaComponentsSetter iaComponentsSetter, GameTable gameTable) {

        List<Position> dangerousWeapons = getDangerousWeapons(iaComponentsSetter);
        List<Position> dangerourMoveCake = getDangerousMoveCake(characterArray, myTeam, cakeUtils, gameTable);
        List<GameCharacter> iaTeam = iaComponentsSetter.getIaTeam();
        Position moreDangerous = iaTeam.get(0).getPosition();
        float moreValue = 0f;
        for (GameCharacter gameCharacter : iaTeam) {
            float value = getValue(gameCharacter, dangerourMoveCake, dangerousWeapons, gameTable);
            if (value >= moreValue) {
                moreDangerous = gameCharacter.getPosition();
                moreValue = value;
            }
        }
        return moreDangerous;
    }

    private List<Position> getDangerousWeapons(IaComponentsSetter iaComponentsSetter) {
        List<Position> dangerousWeapons = new ArrayList<Position>();
        for (GameCharacter enemy : iaComponentsSetter.getEnemyAttackDatas()) {
            dangerousWeapons.addAll(enemy.getAttackData().getAttackPositions());
        }
        return dangerousWeapons;
    }

    private List<Position> getDangerousMoveCake(GameCharacter[][] characterArray, String myTeam, CakeUtils cakeUtils,
            GameTable gameTable) {
        List<Position> dangerourMoveCake = new ArrayList<Position>();
        for (Cake cake : gameTable.getCakeList()) {
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

    private float getValue(GameCharacter gameCharacter, List<Position> dangerourMoveCake,
            List<Position> dangerousWeapons, GameTable gameTable) {
        float value = 0;
        if (gameCharacter.hasCake()) {
            value = value + 1000;
            if (gameCharacter.hasFatalCake()) {
                value = value + 100;
            }
            if (gameCharacter.isKing()) {
                value = value + 10;
            }
            if (gameCharacter.hasGun() || gameCharacter.hasKnife()) {
                value = value + 5;
            }
        } else {
            for (Position position : dangerourMoveCake) {
                if (gameCharacter.getPosition().isEquals(position)) {
                    value = value + 50;
                    break;
                }
            }
            for (Position position : dangerousWeapons) {
                if (gameCharacter.getPosition().isEquals(position)) {
                    value = value + 50;
                    break;
                }
            }
            float valueBusiness = gameCharacter.getBusinessValue()*5;
            if(valueBusiness==0 && !gameCharacter.getAttackData().canAttack()){
                value = value + 10;
            }
            value = value - valueBusiness*5;
            TableSeat tableSeat = gameTable.getTableSeatByPosition(gameCharacter.getPosition());
            if (tableSeat.has(TableObjects.GLASS)) {
                value = value + 10;
            }
        }

        return value;
    }

}
