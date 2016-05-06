package gm.ia.getters;

import java.util.ArrayList;
import java.util.List;

import gm.CharacterUtils;
import gm.GameCharacter;
import gm.GameTable;
import gm.TableSeat;
import gm.ia.GeneralTeam;
import gm.ia.setters.IaComponentsSetter;
import gm.info.MoneyValues;
import gm.info.TableObjects;
import gm.pojos.Position;

public class WhereMoveGetter {
    
    private GameTable gameTable;
    
    private CharateresToAttackByGunGetter gunGetter;
    
    private CharateresToAttackByKnifeGetter knifeGetter;
    
    private int middle;

    public WhereMoveGetter(GameTable gameTable){
        this.gameTable = gameTable;
        this.gunGetter = new CharateresToAttackByGunGetter(gameTable);
        this.knifeGetter = new CharateresToAttackByKnifeGetter(gameTable);
        middle = (gameTable.getMaxY() - 1) / 2;
    }

    public Position whereMove(GameCharacter[][] characterArray, IaComponentsSetter iaComponentsSetter,
            String myTeam, Position whoMove) {
        List<Position> attackEnemiesPositions = getAttackPositions(characterArray, iaComponentsSetter.getEnemyAttackDatas());
        float bestValue = -100;
        Position bestPosition = null;
        for (int x = 0; x < characterArray[0].length; x++) {
            for (int y = 0; y < characterArray.length; y++) {
                Position position = new Position(x, y);
                GameCharacter newSeat = CharacterUtils.getCharacterByPosition(characterArray, position);
                if (newSeat.isEmpty() && !newSeat.hasCake()) {
                    GameCharacter whoToMove= CharacterUtils.getCharacterByPosition(characterArray,
                    		whoMove);
                    TableSeat tableSeat = gameTable.getTableSeatByPosition(position);
                    List<MoneyValues> moneyValues = getMoneyValuesByTeam(iaComponentsSetter, myTeam);
                    float value = getValue(whoToMove, newSeat, tableSeat, attackEnemiesPositions, 
                            moneyValues, characterArray);
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
            attackPositions.addAll(getAttackPositionByCharacter(characterArray, gameCharacter));
        }
        return attackPositions;
    }

    private List<Position> getAttackPositionByCharacter(GameCharacter[][] characterArray, GameCharacter gameCharacter) {
        List<Position> positions = new ArrayList<Position>();
        positions.addAll(knifeGetter.getEmptyAttackPositions(characterArray, gameCharacter.getPosition()));
        Position gunAttackPosition = gunGetter.getEmptyAttackPositions(characterArray, gameCharacter.getPosition());
        if(gunAttackPosition!=null){
            positions.add(gunAttackPosition);
        }
        return positions;
    }
    
    private List<Position> canAttack(GameCharacter[][] characterArray, GameCharacter newSeat, GameCharacter whoToMove) {
        List<Position> positions = new ArrayList<Position>();
        positions.addAll(knifeGetter.getEmptyAttackPositions(characterArray, newSeat.getPosition(), whoToMove, whoToMove.getTeam()));
        Position gunAttackPosition = gunGetter.getEmptyAttackPositions(characterArray, newSeat.getPosition());
        if(gunAttackPosition!=null){
            positions.add(gunAttackPosition);
        }
        return positions;
    }

    private float getValue(GameCharacter whoToMove, GameCharacter newSeat, TableSeat tableSeat,
            List<Position> attackEnemiesPositions, List<MoneyValues> moneyValues, GameCharacter[][] characterArray) {
        MoneyValues moneyValue = getMoneyValues(tableSeat);
        float value = 0;
        for (Position attackPosition : attackEnemiesPositions) {
            if(attackPosition.isEquals(newSeat.getPosition())){
                value = value - 10;
            }
        }
        List<Position> canAttack = canAttack(characterArray, newSeat, whoToMove);
        value = value + canAttack.size()*5;
        
        if (moneyValue != MoneyValues.NOTTHING) {
            value=value+10;
        }
        value = value + getValueBusiness(moneyValue, moneyValues);
        if (tableSeat.has(TableObjects.GLASS)) {
            value--;
        }
        
        boolean isOnSide = isOnSide(newSeat.getPosition());
		if (isOnSide && (whoToMove.hasKnife() || tableSeat.has(TableObjects.KNIFE))) {
            value++;
        }else{
        	if (tableSeat.has(TableObjects.KNIFE) && !whoToMove.hasKnife()) {
                value = value + 0.5f;
            }
        }
		if(isOnSide && (whoToMove.hasGun())){
			value--;
		}else{
			if (tableSeat.has(TableObjects.GUN) && !whoToMove.hasGun()) {
	            value = value + 0.5f;
	        }
		}
        return value;
    }

	private boolean isOnSide(Position position) {
	    int x = position.getX();
	    int y = position.getY();
		return y != middle && (x == 0 || x == (gameTable.getMaxX() - 1));
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
