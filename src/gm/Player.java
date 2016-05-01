package gm;

import java.util.ArrayList;
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
	
	public Player(String team) {
		this.team = team;
		this.cards = new ArrayList<CardType>();
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

	public Card getCard(Card cardType1, Card cardType2) {
		int counter1 = 0;
		int counter2 = 0;
		for (CardType cardType : cards) {
			if(cardType == cardType1.getType()){
				counter1++;
			}
			if(cardType == cardType2.getType()){
				counter2++;
			}
		}
		if(counter2 > counter1){
			return cardType2;
		}else{
			return cardType1;
		}
	}
}
