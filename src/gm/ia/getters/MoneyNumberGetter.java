package gm.ia.getters;

import gm.ia.pojos.IA_Character;
import gm.info.MoneyValues;

import java.util.List;

public class MoneyNumberGetter {

	private int casinosCounter = 0;

	private int barsCounter = 0;

	private int restaurantsCounter = 0;

	private boolean hasKing;

	public MoneyNumberGetter(List<MoneyValues> totalMoneyValues) {
		for (MoneyValues moneyValue : totalMoneyValues) {
			if (moneyValue == MoneyValues.CASINO) {
				casinosCounter++;
			}
			if (moneyValue == MoneyValues.BAR) {
				barsCounter++;
			}
			if (moneyValue == MoneyValues.RESTAURANT) {
				restaurantsCounter++;
			}
			if (moneyValue == MoneyValues.KING) {
				hasKing = true;
			}
		}
	}

	public int getValue(IA_Character moneyCharacter) {
		int value = 0;
		value += getValueByBusiness(barsCounter, MoneyValues.BAR, moneyCharacter);
		value += getValueByBusiness(restaurantsCounter, MoneyValues.RESTAURANT, moneyCharacter);
		value += getValueByBusiness(casinosCounter, MoneyValues.CASINO, moneyCharacter);
		if (moneyCharacter.isKing()) {
			value += 1;
		}
		int totalBusiness = casinosCounter + barsCounter + restaurantsCounter;
		if (moneyCharacter.getMoney() == MoneyValues.MACHINE
				&& (totalBusiness > 1 || (totalBusiness >= 1 && hasKing))) {
			value += 10;
		}
		return value;
	}

	private int getValueByBusiness(int businessCounter, MoneyValues business, IA_Character moneyCharacter) {
		if (moneyCharacter.getMoney() == business) {
			if (businessCounter == 2) {
				return 5;
			} else {
				return 1;
			}
		}
		return 0;
	}

}
