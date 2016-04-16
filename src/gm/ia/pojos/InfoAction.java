package gm.ia.pojos;

import java.util.ArrayList;
import java.util.List;

import gm.Card;
import gm.pojos.Position;

public class InfoAction {

	private List<Card> cards;

	private Position attackedPosition;

	private String reason;

	private Position attackerPosition;

	public InfoAction(Card card, Position attackerPosition, Position attackedPosition, String reason) {
		this.attackerPosition = attackerPosition;
		this.cards = new ArrayList<Card>();
		this.reason = reason;
		this.cards.add(card);
		this.attackedPosition = attackedPosition;
	}

	public void addCard(Card card) {
		this.cards.add(card);
	}

	public Position getAttackedPosition() {
		return this.attackedPosition;
	}

	public Position getAttackerPosition() {
		return attackerPosition;
	}

	public List<Card> getCards() {
		return this.cards;
	}

	public void addReason(String reason) {
		this.reason += "//" + reason;
	}

	public String getReason() {
		return reason;
	}

}