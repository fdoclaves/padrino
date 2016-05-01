package gm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import gm.info.CardType;

public class CardManagerImpl implements CardManager {

	private List<CardType> chooseCard;

	private List<CardType> temporalCards;

	private Random random;

	public CardManagerImpl() {
		// 9 pinchazo, 9 disparo, 8 pide pastel, 6 pasa el pastel, 4 bomba,
		// 6 somníferos, 2 redada, 16 ahueca el ala.
		chooseCard = new ArrayList<CardType>();
		temporalCards = new ArrayList<CardType>();
		random = new Random();
		fillCards(chooseCard);
	}

	protected void fillCards(List<CardType> chooseCard) {
		for (int i = 1; i <= 16; i++) {
			chooseCard.add(CardType.MOVE);
		}
		for (int i = 1; i <= 2; i++) {
			chooseCard.add(CardType.POLICE);
		}
		for (int i = 1; i <= 6; i++) {
			chooseCard.add(CardType.SLEEP);
		}
		for (int i = 1; i <= 4; i++) {
			chooseCard.add(CardType.BOOM);
		}
		for (int i = 1; i <= 6; i++) {
			chooseCard.add(CardType.MOVE_CAKE);
		}
		for (int i = 1; i <= 8; i++) {
			chooseCard.add(CardType.CAKE);
		}
		for (int i = 1; i <= 9; i++) {
			chooseCard.add(CardType.GUN);
		}
		for (int i = 1; i <= 9; i++) {
			chooseCard.add(CardType.KNIFE);
		}
	}

	@Override
	public CardType getCard() {
		if (chooseCard.size() == 0) {
			for (CardType cardType : temporalCards) {
				chooseCard.add(cardType);
			}
			temporalCards = new ArrayList<CardType>();
		}
		int cardIndex = random.nextInt(chooseCard.size());
		CardType cardType = chooseCard.get(cardIndex);
		chooseCard.remove(cardIndex);
		return cardType;
	}
	
	public int getTotalCard(){
		return temporalCards.size() + chooseCard.size();
	}


	@Override
	public void setCard(CardType cardType) {
		temporalCards.add(cardType);
	}

	@Override
	public void removeLastCard() {
		temporalCards.remove(temporalCards.size()-1);
	}

	@Override
	public void setCards(List<CardType> cards) {
		temporalCards.addAll(cards);
	}

}
