package gm;

import java.util.List;

import gm.info.CardType;

public class Player {
	
	private String team;
	
	private List<CardType> cards;

	public Player(String team, List<CardType> cards) {
		this.team = team;
		this.cards = cards;
	}

	public String getTeam() {
		return team;
	}
	
	public List<CardType> getCards() {
		return cards;
	}

	public boolean hasCard(CardType card) {
		return cards.contains(card);
	}

	public int getNumberCard(CardType card) {
		int counter = 0;
		for (CardType cardType : cards) {
			if(cardType==card){
				counter++;
			}
		}
		return counter;
	}
}
