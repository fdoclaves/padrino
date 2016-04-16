package gm.ia;

import java.util.ArrayList;
import java.util.List;

import gm.Card;
import gm.ia.pojos.InfoAction;
import gm.pojos.Position;

public class ManagerKillerCards {

	private List<InfoAction> infoActionList = new ArrayList<InfoAction>();

	public void add(Card card, Position attackerPosition, Position attackedPosition, String reason) {
		boolean exits = false;
		for (InfoAction infoAction : infoActionList) {
			if (infoAction.getAttackedPosition().isEquals(attackedPosition)) {
				infoAction.addCard(card);
				exits = true;
			}
		}
		if (!exits) {
			infoActionList.add(new InfoAction(card, attackerPosition, attackedPosition, reason));
		}
	}

	public List<InfoAction> getAttackedPositionIAs() {
		return this.infoActionList;
	}

}