package gm;

import java.util.List;

import gm.info.CardType;

public interface CardManager {

	CardType getCard();

	void setCard(CardType cardType);

	void removeLastCard();

	void setCards(List<CardType> cards);

}