package gm.ia.getters;

import java.util.List;

import gm.cards.ChangeCard;
import gm.info.CardType;

public class ChangeCardGetter {

	private int sleepCards = 0;

	private int boomCards = 0;

	private int knifeCards = 0;

	private int gunCards = 0;

	private int cakeCards = 0;

	private int moveCakeCards = 0;

	private int moveCards = 0;

	private int currentGamers;

	public ChangeCardGetter(List<CardType> cards, int currentGamers) {
		this.currentGamers = currentGamers;
		for (CardType cardType : cards) {
			if (cardType == CardType.GUN) {
				gunCards++;
			}
			if (cardType == CardType.KNIFE) {
				knifeCards++;
			}
			if (cardType == CardType.SLEEP) {
				sleepCards++;
			}
			if (cardType == CardType.BOOM) {
				boomCards++;
			}
			if (cardType == CardType.MOVE_CAKE) {
				moveCakeCards++;
			}
			if (cardType == CardType.MOVE) {
				moveCards++;
			}
			if (cardType == CardType.CAKE) {
				cakeCards++;
			}
		}
	}

	public ChangeCard get() {
		return new ChangeCard(getCardType());
	}

	private CardType getCardType() {
		if (moveCakeCards >= 2 && sleepCards == 0) {
			return CardType.MOVE_CAKE;
		}
		if (boomCards >= 3) {
			return CardType.BOOM;
		}
		if (boomCards >= 1 && currentGamers == 2) {
			return CardType.BOOM;
		}
		if (sleepCards >= 4) {
			return CardType.SLEEP;
		}
		if (boomCards >= 4) {
			return CardType.BOOM;
		}
		if (knifeCards >= 4) {
			return CardType.KNIFE;
		}
		if (gunCards >= 4) {
			return CardType.GUN;
		}
		if (cakeCards >= 4) {
			return CardType.CAKE;
		}
		if (moveCakeCards >= 4) {
			return CardType.MOVE_CAKE;
		}
		if (moveCards >= 4) {
			return CardType.MOVE;
		}
		return defautCard();
	}

	private CardType defautCard() {
		if (sleepCards == 0) {
			if (gunCards == 0) {
				if (knifeCards == 0) {
						if (boomCards == 0) {
							if (moveCakeCards == 0) {
								if (moveCards == 0) {
									return CardType.CAKE;
								}
								return CardType.MOVE;
							}
							return CardType.MOVE_CAKE;
						}
						return CardType.BOOM;
				}
				return CardType.KNIFE;
			}
			return CardType.GUN;
		}
		return CardType.SLEEP;
	}

}
