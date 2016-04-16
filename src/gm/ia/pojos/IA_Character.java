package gm.ia.pojos;

import gm.info.MoneyValues;
import gm.pojos.Position;

public class IA_Character {

	private MoneyValues money;

	private boolean king;

	private Position position;

	public IA_Character(Position position, MoneyValues money, boolean king) {
		this.position = position;
		this.money = money;
		this.king = king;
	}

	public IA_Character(Position position, MoneyValues money) {
		this.position = position;
		this.money = money;
		this.king = false;
	}

	public MoneyValues getMoney() {
		return money;
	}

	public boolean isKing() {
		return king;
	}

	public Position getPosition() {
		return position;
	}

}
