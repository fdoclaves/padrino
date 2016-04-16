package gm.ia.pojos;

import gm.Card;
import gm.pojos.Position;

public class EnemyToDead {

	private Card card;
	private String reason;
	private Position position;

	public EnemyToDead(Card card, Position position, String reason) {
		this.setCard(card);
		this.position = position;
		this.reason = reason;
	}

	public Position getPosition() {
		return position;
	}

	public String getReason() {
		return this.reason;
	}

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

}
