package gt.ia;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import gm.cards.SleepCard;
import gm.ia.AsleepEnemyGetter;
import gm.pojos.Position;

public class SleepGetterTest {

	private AsleepEnemyGetter sleepGetter;

	private List<Position> others;

	private List<Position> withoutNext;

	private List<Position> isSlept;

	@Before
	public void setUp() throws Exception {
		others = new ArrayList<Position>();
		withoutNext = new ArrayList<Position>();
		isSlept = new ArrayList<Position>();
		sleepGetter = new AsleepEnemyGetter(others, withoutNext, isSlept);
	}

	@Test
	public void normalesSleeps() {
		assertNull(sleepGetter.getBestSleepCard(""));
		others.add(new Position(0, 0));
		SleepCard card = (SleepCard) sleepGetter.getBestSleepCard("").getCards().get(0);
		List<Position> positionList = card.getPositionList();
		assertEquals(1, positionList.size());
		others.add(new Position(1, 1));
		card = (SleepCard) sleepGetter.getBestSleepCard("").getCards().get(0);
		positionList = card.getPositionList();
		assertEquals(2, positionList.size());
		others.add(new Position(2, 2));
		card = (SleepCard) sleepGetter.getBestSleepCard("").getCards().get(0);
		positionList = card.getPositionList();
		assertEquals(3, positionList.size());
	}

	@Test
	public void nextSleeps1() {
		others.add(new Position(0, 0));
		others.add(new Position(1, 1));
		others.add(new Position(2, 2));
		withoutNext.add(new Position(3, 3));
		SleepCard card = (SleepCard) sleepGetter.getBestSleepCard("").getCards().get(0);
		List<Position> positionList = card.getPositionList();
		assertEquals(3, positionList.size());
		assertTrue(positionList.get(0).isEquals(new Position(3, 3)));
		assertTrue(positionList.get(1).isEquals(new Position(0, 0)));
		assertTrue(positionList.get(2).isEquals(new Position(1, 1)));
	}

	@Test
	public void nextSleeps2() {
		others.add(new Position(0, 0));
		others.add(new Position(1, 1));
		others.add(new Position(2, 2));
		withoutNext.add(new Position(3, 3));
		withoutNext.add(new Position(4, 4));
		SleepCard card = (SleepCard) sleepGetter.getBestSleepCard("").getCards().get(0);
		List<Position> positionList = card.getPositionList();
		assertEquals(3, positionList.size());
		assertTrue(positionList.get(0).isEquals(new Position(3, 3)));
		assertTrue(positionList.get(1).isEquals(new Position(4, 4)));
		assertTrue(positionList.get(2).isEquals(new Position(0, 0)));
	}

	@Test
	public void nextSleeps3() {
		others.add(new Position(0, 0));
		others.add(new Position(1, 1));
		others.add(new Position(2, 2));
		withoutNext.add(new Position(3, 3));
		withoutNext.add(new Position(4, 4));
		withoutNext.add(new Position(5, 5));
		SleepCard card = (SleepCard) sleepGetter.getBestSleepCard("").getCards().get(0);
		List<Position> positionList = card.getPositionList();
		assertEquals(3, positionList.size());
		assertTrue(positionList.get(0).isEquals(new Position(3, 3)));
		assertTrue(positionList.get(1).isEquals(new Position(4, 4)));
		assertTrue(positionList.get(2).isEquals(new Position(5, 5)));
	}

	@Test
	public void slept1() {
		others.add(new Position(0, 0));
		others.add(new Position(1, 1));
		others.add(new Position(2, 2));
		withoutNext.add(new Position(3, 3));
		withoutNext.add(new Position(4, 4));
		withoutNext.add(new Position(5, 5));
		isSlept.add(new Position(6, 6));
		SleepCard card = (SleepCard) sleepGetter.getBestSleepCard("").getCards().get(0);
		List<Position> positionList = card.getPositionList();
		assertEquals(3, positionList.size());
		assertTrue(positionList.get(0).isEquals(new Position(6, 6)));
		assertTrue(positionList.get(1).isEquals(new Position(3, 3)));
		assertTrue(positionList.get(2).isEquals(new Position(4, 4)));
	}

	@Test
	public void slept2() {
		others.add(new Position(0, 0));
		others.add(new Position(1, 1));
		others.add(new Position(2, 2));
		withoutNext.add(new Position(3, 3));
		withoutNext.add(new Position(4, 4));
		withoutNext.add(new Position(5, 5));
		isSlept.add(new Position(6, 6));
		isSlept.add(new Position(7, 7));
		SleepCard card = (SleepCard) sleepGetter.getBestSleepCard("").getCards().get(0);
		List<Position> positionList = card.getPositionList();
		assertEquals(3, positionList.size());
		assertTrue(positionList.get(0).isEquals(new Position(6, 6)));
		assertTrue(positionList.get(1).isEquals(new Position(7, 7)));
		assertTrue(positionList.get(2).isEquals(new Position(3, 3)));
	}

	@Test
	public void slept3() {
		others.add(new Position(0, 0));
		others.add(new Position(1, 1));
		others.add(new Position(2, 2));
		withoutNext.add(new Position(3, 3));
		withoutNext.add(new Position(4, 4));
		withoutNext.add(new Position(5, 5));
		isSlept.add(new Position(6, 6));
		isSlept.add(new Position(7, 7));
		isSlept.add(new Position(8, 8));
		SleepCard card = (SleepCard) sleepGetter.getBestSleepCard("").getCards().get(0);
		List<Position> positionList = card.getPositionList();
		assertEquals(3, positionList.size());
		assertTrue(positionList.get(0).isEquals(new Position(6, 6)));
		assertTrue(positionList.get(1).isEquals(new Position(7, 7)));
		assertTrue(positionList.get(2).isEquals(new Position(8, 8)));
	}

}
