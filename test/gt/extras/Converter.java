package gt.extras;

import gm.GameCharacter;
import gm.GameCharacterEmpty;
import gm.TableSeat;

public class Converter {

	private int maxY;
	private int maxX;

	public Converter(int maxX, int maxY) {
		this.maxX = maxX;
		this.maxY = maxY;
	}

	public String toString(String[][] array) {
		String text = "";
		for (String[] values : array) {
			for (String value : values) {
				text += value + ",";
			}
			text += "\n";
		}
		return text.substring(0, text.length() - 1);
	}

	public String cToString(GameCharacter[][] array) {
		String text = "";
		for (GameCharacter[] characters : array) {
			for (GameCharacter character : characters) {
				if (character instanceof GameCharacterEmpty) {
					text += "V" + ",";
				} else {
					text += character.toString() + ",";
				}
			}
			text += "\n";
		}
		return text.substring(0, text.length() - 1);
	}

	public GameCharacter[][] toCharacterArray(String[][] array) {
		GameCharacter[][] characters = new GameCharacter[maxY][maxX];
		for (int i = 0; i < maxY; i++) {
			for (int j = 0; j < maxX; j++) {
				characters[i][j] = new CharacterString(array[i][j]);
			}
		}
		return characters;
	}

	public TableSeat[][] to(String[][] tableValues) {
		TableSeat[][] tableSeat = new TableSeat[maxY][maxX];
		for (int i = 0; i < maxY; i++) {
			for (int j = 0; j < maxX; j++) {
				tableSeat[i][j] = new TableSeat(tableValues[i][j]);
			}
		}
		return tableSeat;
	}

}
