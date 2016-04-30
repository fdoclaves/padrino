package gm.ia.getters;

import java.util.ArrayList;
import java.util.List;

import gm.GameCharacter;
import gm.GameTable;
import gm.TableSeat;
import gm.ia.CharacterUtils;
import gm.ia.GeneralTeam;
import gm.info.MoneyValues;
import gm.info.TableObjects;
import gm.pojos.Position;

public class WhereMoveGetter {
    
    private GameTable gameTable;
    
    private CharateresToAttackByGunGetter gunGetter;
    
    private CharateresToAttackByKnifeGetter knifeGetter;

    public WhereMoveGetter(GameTable gameTable){
        this.gameTable = gameTable;
        this.gunGetter = new CharateresToAttackByGunGetter(gameTable);
        this.knifeGetter = new CharateresToAttackByKnifeGetter(gameTable);
    }

    public Position whereMove(GameCharacter[][] characterArray, IaComponentsSetter iaComponentsSetter,
            String myTeam, Position whoMove) {
        List<Position> attackPositions = getAttackPositions(characterArray, iaComponentsSetter.getEnemyAttackDatas());
        float bestValue = -100;
        Position bestPosition = null;
        for (int x = 0; x < characterArray[0].length; x++) {
            for (int y = 0; y < characterArray.length; y++) {
                Position position = new Position(x, y);
                GameCharacter newSeat = CharacterUtils.getCharacterByPosition(characterArray, position);
                if (newSeat.isEmpty() && !newSeat.hasCake()) {
                    GameCharacter whoGameCharacter = CharacterUtils.getCharacterByPosition(characterArray,
                    		whoMove);
                    TableSeat tableSeat = gameTable.getTableSeatByPosition(position);
                    List<MoneyValues> moneyValues = getMoneyValuesByTeam(iaComponentsSetter, myTeam);
                    float value = getValue(whoGameCharacter, newSeat, tableSeat, attackPositions, moneyValues);
                    if (value >= bestValue) {
                        bestPosition = position;
                        bestValue = value;
                    }
                }
            }
        }
        return bestPosition;
    }


    private List<MoneyValues> getMoneyValuesByTeam(IaComponentsSetter iaComponentsSetter, String myTeam) {
        GeneralTeam generalTeam = iaComponentsSetter.getGeneralTeams().get(myTeam);
        return generalTeam.getTotalMoneyValues();
    }

    private List<Position> getAttackPositions(GameCharacter[][] characterArray, List<GameCharacter> enemies) {
        List<Position> attackPositions = new ArrayList<Position>();
        for (GameCharacter gameCharacter : enemies) {
            attackPositions.addAll(knifeGetter.getEmptyAttackPositions(characterArray, gameCharacter.getPosition()));
            Position gunAttackPosition = gunGetter.getEmptyAttackPositions(characterArray, gameCharacter.getPosition());
            if(gunAttackPosition!=null){
                attackPositions.add(gunAttackPosition);
            }
            
        }
        return attackPositions;
    }

    private float getValue(GameCharacter gameCharacterBefore, GameCharacter gameCharacter, TableSeat tableSeat,
            List<Position> attackPositions, List<MoneyValues> moneyValues) {
        MoneyValues moneyValue = getMoneyValues(tableSeat);
        float value = 0;
        for (Position attackPosition : attackPositions) {
            if(attackPosition.isEquals(gameCharacter.getPosition())){
                value = value - 10;
            }
        }
        if (moneyValue != MoneyValues.NOTTHING) {
            value++;
        }
        value = value + getValueBusiness(moneyValue, moneyValues);
        if (tableSeat.has(TableObjects.GLASS)) {
            value--;
        }
        if (tableSeat.has(TableObjects.KNIFE) && !gameCharacterBefore.hasKnife()) {
            value = value + 0.5f;
        }
        if (tableSeat.has(TableObjects.GUN) && !gameCharacterBefore.hasGun()) {
            value = value + 0.5f;
        }
        return value;
    }

    private float getValueBusiness(MoneyValues moneyValue, List<MoneyValues> moneyValues) {
        if (moneyValues.contains(moneyValue)) {
            return 1f;
        }
        return 0f;
    }

    private MoneyValues getMoneyValues(TableSeat tableSeat) {
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
