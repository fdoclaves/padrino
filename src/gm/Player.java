package gm;

import java.util.List;

import gm.info.CardType;

public class Player {
	
	private String team;
	
	private int money;
	
	private List<CardType> cards;
	
	private int counterCharacters;

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

	public void removeCard(CardType type) {
		for (CardType cardType : cards) {
			if(cardType == type){
				cards.remove(cardType);
				break;
			}
		}
	}

	public void addCard(CardType card) {
		cards.add(card);
	}

	public void setCardList(List<CardType> beforeCardType) {
		this.cards = beforeCardType;
	}

	public int getMoney() {
		return money;
	}
	
	public void addMoney(int money) {
		this.money = this.money + money;
	}

	public void removeOneCharacter() {
		counterCharacters--;
	}

	public int getCounterCharacters() {
		return counterCharacters;
	}

	public void plusCounterCharacterOne() {
		counterCharacters++;
	}
}
