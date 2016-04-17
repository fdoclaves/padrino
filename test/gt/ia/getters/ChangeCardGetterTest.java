package gt.ia.getters;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import gm.cards.ChangeCard;
import gm.ia.getters.ChangeCardGetter;
import gm.info.CardType;

public class ChangeCardGetterTest {
	
	@Before
	public void setUp() throws Exception {
		
	}
	
	@Test
	public void _2Jugadores() {
		List<CardType> cards = new ArrayList<CardType>();
		cards.add(CardType.SLEEP);
		cards.add(CardType.CAKE);
		cards.add(CardType.KNIFE);
		cards.add(CardType.GUN);
		cards.add(CardType.MOVE_CAKE);
		ChangeCardGetter changeCardGetter = new ChangeCardGetter(cards, 2);
		ChangeCard changeCard = changeCardGetter.get();
		assertEquals(CardType.SLEEP, changeCard.getType());
	}

	@Test
	public void _2JugadoresBoomNoSirve() {
		List<CardType> cards = new ArrayList<CardType>();
		cards.add(CardType.BOOM);
		cards.add(CardType.SLEEP);
		cards.add(CardType.KNIFE);
		cards.add(CardType.GUN);
		cards.add(CardType.MOVE_CAKE);
		ChangeCardGetter changeCardGetter = new ChangeCardGetter(cards, 2);
		ChangeCard changeCard = changeCardGetter.get();
		assertEquals(CardType.BOOM, changeCard.getType());
	}
	
	@Test
	public void _3Jugadores_2Sleep() {
		List<CardType> cards = new ArrayList<CardType>();
		cards.add(CardType.SLEEP);
		cards.add(CardType.SLEEP);
		cards.add(CardType.KNIFE);
		cards.add(CardType.GUN);
		cards.add(CardType.MOVE_CAKE);
		ChangeCardGetter changeCardGetter = new ChangeCardGetter(cards, 3);
		ChangeCard changeCard = changeCardGetter.get();
		assertEquals(CardType.SLEEP, changeCard.getType());
	}
	
	@Test
	public void _2JugadoresNoTengoSleep() {
		List<CardType> cards = new ArrayList<CardType>();
		cards.add(CardType.MOVE);
		cards.add(CardType.CAKE);
		cards.add(CardType.KNIFE);
		cards.add(CardType.GUN);
		cards.add(CardType.MOVE_CAKE);
		ChangeCardGetter changeCardGetter = new ChangeCardGetter(cards, 2);
		ChangeCard changeCard = changeCardGetter.get();
		assertEquals(CardType.GUN, changeCard.getType());
	}
	
	@Test
	public void _2JugadoresNoTengoSleepNoGun() {
		List<CardType> cards = new ArrayList<CardType>();
		cards.add(CardType.MOVE);
		cards.add(CardType.CAKE);
		cards.add(CardType.KNIFE);
		cards.add(CardType.CAKE);
		cards.add(CardType.MOVE_CAKE);
		ChangeCardGetter changeCardGetter = new ChangeCardGetter(cards, 2);
		ChangeCard changeCard = changeCardGetter.get();
		assertEquals(CardType.KNIFE, changeCard.getType());
	}
	
	@Test
	public void _2JugadoresNoTengoSleepNoGunNoKnife() {
		List<CardType> cards = new ArrayList<CardType>();
		cards.add(CardType.MOVE);
		cards.add(CardType.CAKE);
		cards.add(CardType.POLICE);
		cards.add(CardType.CAKE);
		cards.add(CardType.MOVE_CAKE);
		ChangeCardGetter changeCardGetter = new ChangeCardGetter(cards, 2);
		ChangeCard changeCard = changeCardGetter.get();
		assertEquals(CardType.POLICE, changeCard.getType());
	}
	
	@Test
	public void _3JugadoresNoTengoSleepNoGunNoKnifeNoPolice() {
		List<CardType> cards = new ArrayList<CardType>();
		cards.add(CardType.MOVE);
		cards.add(CardType.CAKE);
		cards.add(CardType.BOOM);
		cards.add(CardType.CAKE);
		cards.add(CardType.MOVE_CAKE);
		ChangeCardGetter changeCardGetter = new ChangeCardGetter(cards, 3);
		ChangeCard changeCard = changeCardGetter.get();
		assertEquals(CardType.BOOM, changeCard.getType());
	}
	
	@Test
	public void _3JugadoresNoTengoSleepNoGunNoKnifeNoPoliceNoBoom() {
		List<CardType> cards = new ArrayList<CardType>();
		cards.add(CardType.MOVE);
		cards.add(CardType.CAKE);
		cards.add(CardType.CAKE);
		cards.add(CardType.CAKE);
		cards.add(CardType.MOVE_CAKE);
		ChangeCardGetter changeCardGetter = new ChangeCardGetter(cards, 3);
		ChangeCard changeCard = changeCardGetter.get();
		assertEquals(CardType.MOVE_CAKE, changeCard.getType());
	}
	
	@Test
	public void _3JugadoresNoTengoSleepNoGunNoKnifeNoPoliceNoBoomNoMoveCake() {
		List<CardType> cards = new ArrayList<CardType>();
		cards.add(CardType.MOVE);
		cards.add(CardType.CAKE);
		cards.add(CardType.CAKE);
		cards.add(CardType.CAKE);
		cards.add(CardType.MOVE);
		ChangeCardGetter changeCardGetter = new ChangeCardGetter(cards, 3);
		ChangeCard changeCard = changeCardGetter.get();
		assertEquals(CardType.MOVE, changeCard.getType());
	}
	
	@Test
	public void _3JugadoresNoTengoSleepNoGunNoKnifeNoPoliceNoBoomNoMoveCakeNoMove() {
		List<CardType> cards = new ArrayList<CardType>();
		cards.add(CardType.CAKE);
		cards.add(CardType.CAKE);
		cards.add(CardType.CAKE);
		cards.add(CardType.CAKE);
		cards.add(CardType.CAKE);
		ChangeCardGetter changeCardGetter = new ChangeCardGetter(cards, 3);
		ChangeCard changeCard = changeCardGetter.get();
		assertEquals(CardType.CAKE, changeCard.getType());
	}
	
	@Test
	public void _3Jugadores4Repedidas() {
		List<CardType> cards = new ArrayList<CardType>();
		cards.add(CardType.SLEEP);
		cards.add(CardType.CAKE);
		cards.add(CardType.CAKE);
		cards.add(CardType.CAKE);
		cards.add(CardType.CAKE);
		ChangeCardGetter changeCardGetter = new ChangeCardGetter(cards, 3);
		ChangeCard changeCard = changeCardGetter.get();
		assertEquals(CardType.CAKE, changeCard.getType());
	}
	
	@Test
	public void _3Jugadores4Repedidas2() {
		List<CardType> cards = new ArrayList<CardType>();
		cards.add(CardType.SLEEP);
		cards.add(CardType.MOVE);
		cards.add(CardType.MOVE);
		cards.add(CardType.MOVE);
		cards.add(CardType.MOVE);
		ChangeCardGetter changeCardGetter = new ChangeCardGetter(cards, 3);
		ChangeCard changeCard = changeCardGetter.get();
		assertEquals(CardType.MOVE, changeCard.getType());
	}
	
	@Test
	public void _3Jugadores3RepedidaBoom() {
		List<CardType> cards = new ArrayList<CardType>();
		cards.add(CardType.SLEEP);
		cards.add(CardType.MOVE);
		cards.add(CardType.BOOM);
		cards.add(CardType.BOOM);
		cards.add(CardType.BOOM);
		ChangeCardGetter changeCardGetter = new ChangeCardGetter(cards, 3);
		ChangeCard changeCard = changeCardGetter.get();
		assertEquals(CardType.BOOM, changeCard.getType());
	}
	
	@Test
	public void _3Jugadores3RepedidaMoveCake() {
		List<CardType> cards = new ArrayList<CardType>();
		cards.add(CardType.SLEEP);
		cards.add(CardType.MOVE);
		cards.add(CardType.MOVE_CAKE);
		cards.add(CardType.MOVE_CAKE);
		cards.add(CardType.MOVE_CAKE);
		ChangeCardGetter changeCardGetter = new ChangeCardGetter(cards, 3);
		ChangeCard changeCard = changeCardGetter.get();
		assertEquals(CardType.SLEEP, changeCard.getType());
	}
	
	@Test
	public void _3Jugadores3RepedidaMoveCakeSinSleep() {
		List<CardType> cards = new ArrayList<CardType>();
		cards.add(CardType.GUN);
		cards.add(CardType.MOVE);
		cards.add(CardType.MOVE_CAKE);
		cards.add(CardType.MOVE_CAKE);
		cards.add(CardType.MOVE_CAKE);
		ChangeCardGetter changeCardGetter = new ChangeCardGetter(cards, 3);
		ChangeCard changeCard = changeCardGetter.get();
		assertEquals(CardType.MOVE_CAKE, changeCard.getType());
	}

}
