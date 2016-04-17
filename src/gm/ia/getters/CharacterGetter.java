package gm.ia.getters;

import java.util.ArrayList;
import java.util.List;

import gm.GameCharacter;
import gm.GameTable;
import gm.TableSeat;
import gm.ia.pojos.IA_Character;
import gm.info.MoneyValues;
import gm.info.TableObjects;
import gm.pojos.Position;

public class CharacterGetter {

	private GameCharacter[][] characterArray;
	private GameTable gameTable;

	public CharacterGetter(GameCharacter[][] characterArray, GameTable gameTable) {
		this.characterArray = characterArray;
		this.gameTable = gameTable;
	}

	public List<IA_Character> getCharactersByTeam(String team) {
		List<IA_Character> iaCharacters = new ArrayList<IA_Character>();
		for (int y = 0; y < gameTable.getMaxY(); y++) {
			for (int x = 0; x < gameTable.getMaxX(); x++) {
				GameCharacter character = characterArray[y][x];
				if (character.isTeam(team)) {
					Position position = new Position(x, y);
					MoneyValues money = getMoney(gameTable.getTableSeatByPosition(position));
					iaCharacters.add(new IA_Character(position, money, character.isKing()));
				}
			}
		}
		return iaCharacters;
	}

	private MoneyValues getMoney(TableSeat tableSeat) {
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
