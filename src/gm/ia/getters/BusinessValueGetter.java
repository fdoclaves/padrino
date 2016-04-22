package gm.ia.getters;

import gm.GameCharacter;
import gm.GameTable;
import gm.TableSeat;
import gm.ia.CharacterUtils;
import gm.ia.GeneralTeam;
import gm.ia.pojos.IA_Character;
import gm.info.MoneyValues;
import gm.info.TableObjects;
import gm.pojos.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BusinessValueGetter {

    private GameCharacter[][] characterArray;

    private GameTable gameTable;

    public BusinessValueGetter(GameCharacter[][] characterArray, GameTable gameTable) {
        this.characterArray = characterArray;
        this.gameTable = gameTable;
    }

    public float setBusinessValue(Map<String, GeneralTeam> generalTeamsMap, GameCharacter character, Position position) {
        MoneyValues business = getBusinessByPosition(position);
        MoneyNumberGetter moneyNumberSystem = getMoneySystemByTeam(generalTeamsMap, character.getTeam());
        boolean king = character.isKing();
        return moneyNumberSystem.getValue(new IA_Character(position, business, king));
    }

    private MoneyNumberGetter getMoneySystemByTeam(Map<String, GeneralTeam> generalTeams, String team) {
        if (generalTeams.containsKey(team)) {
            return generalTeams.get(team).getMoneyNumberSystem();
        } else {
            GeneralTeam generalTeam = getTotalBusinessByTeam(team);
            generalTeams.put(team, generalTeam);
            return generalTeam.getMoneyNumberSystem();
        }
    }

    private GeneralTeam getTotalBusinessByTeam(String team) {
        int countCharacters = 0;
        List<MoneyValues> totalMoneyValues = new ArrayList<MoneyValues>();
        for (int x = 0; x < gameTable.getMaxX(); x++) {
            for (int y = 0; y < gameTable.getMaxY(); y++) {
                GameCharacter character = CharacterUtils.getCharacterByXY(characterArray, x, y);
                if (!character.isEmpty() && !character.isInvalidSeat() && character.isTeam(team)) {
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
