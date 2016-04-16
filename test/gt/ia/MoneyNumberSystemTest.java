package gt.ia;

import static org.junit.Assert.*;

import gm.ia.MoneyNumberSystem;
import gm.ia.pojos.IA_Character;
import gm.info.MoneyValues;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class MoneyNumberSystemTest {

	private static final MoneyValues KING = MoneyValues.KING;
	private static final MoneyValues NOTTHING = MoneyValues.NOTTHING;
	private static final MoneyValues MACHINE = MoneyValues.MACHINE;
	private static final MoneyValues CASINOS = MoneyValues.CASINO;
	private static final MoneyValues RESTAURANT = MoneyValues.RESTAURANT;
	private static final MoneyValues BARS = MoneyValues.BAR;

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void allLosMonopolios() {
		List<MoneyValues> totalMoneyValues = new ArrayList<MoneyValues>();
		totalMoneyValues.add(BARS);
		totalMoneyValues.add(BARS);
		totalMoneyValues.add(RESTAURANT);
		totalMoneyValues.add(RESTAURANT);
		totalMoneyValues.add(CASINOS);
		totalMoneyValues.add(CASINOS);
		totalMoneyValues.add(MACHINE);
		totalMoneyValues.add(KING);
		MoneyNumberSystem moneyNumberSystem = new MoneyNumberSystem(totalMoneyValues);
		assertEquals(5, moneyNumberSystem.getNumber(new IA_Character(null, BARS)));
		assertEquals(5, moneyNumberSystem.getNumber(new IA_Character(null, RESTAURANT)));
		assertEquals(5, moneyNumberSystem.getNumber(new IA_Character(null, CASINOS)));
		assertEquals(10, moneyNumberSystem.getNumber(new IA_Character(null, MACHINE)));
		assertEquals(1, moneyNumberSystem.getNumber(new IA_Character(null, NOTTHING, true)));
		assertEquals(11, moneyNumberSystem.getNumber(new IA_Character(null, MACHINE, true)));
		assertEquals(6, moneyNumberSystem.getNumber(new IA_Character(null, CASINOS, true)));
		assertEquals(0, moneyNumberSystem.getNumber(new IA_Character(null, NOTTHING)));
	}

	@Test
	public void dosMonopolios() {
		List<MoneyValues> totalMoneyValues = new ArrayList<MoneyValues>();
		totalMoneyValues.add(BARS);
		totalMoneyValues.add(BARS);
		totalMoneyValues.add(RESTAURANT);
		totalMoneyValues.add(RESTAURANT);
		totalMoneyValues.add(CASINOS);
		totalMoneyValues.add(MACHINE);
		totalMoneyValues.add(KING);
		MoneyNumberSystem moneyNumberSystem = new MoneyNumberSystem(totalMoneyValues);
		assertEquals(5, moneyNumberSystem.getNumber(new IA_Character(null, BARS)));
		assertEquals(5, moneyNumberSystem.getNumber(new IA_Character(null, RESTAURANT)));
		assertEquals(1, moneyNumberSystem.getNumber(new IA_Character(null, CASINOS)));
		assertEquals(10, moneyNumberSystem.getNumber(new IA_Character(null, MACHINE)));
		assertEquals(1, moneyNumberSystem.getNumber(new IA_Character(null, NOTTHING, true)));
		assertEquals(11, moneyNumberSystem.getNumber(new IA_Character(null, MACHINE, true)));
		assertEquals(2, moneyNumberSystem.getNumber(new IA_Character(null, CASINOS, true)));
		assertEquals(6, moneyNumberSystem.getNumber(new IA_Character(null, BARS, true)));
		assertEquals(0, moneyNumberSystem.getNumber(new IA_Character(null, NOTTHING)));
	}

	@Test
	public void unMonopolios() {
		List<MoneyValues> totalMoneyValues = new ArrayList<MoneyValues>();
		totalMoneyValues.add(BARS);
		totalMoneyValues.add(BARS);
		totalMoneyValues.add(RESTAURANT);
		totalMoneyValues.add(CASINOS);
		totalMoneyValues.add(MACHINE);
		totalMoneyValues.add(KING);
		MoneyNumberSystem moneyNumberSystem = new MoneyNumberSystem(totalMoneyValues);
		assertEquals(5, moneyNumberSystem.getNumber(new IA_Character(null, BARS)));
		assertEquals(1, moneyNumberSystem.getNumber(new IA_Character(null, RESTAURANT)));
		assertEquals(1, moneyNumberSystem.getNumber(new IA_Character(null, CASINOS)));
		assertEquals(10, moneyNumberSystem.getNumber(new IA_Character(null, MACHINE)));
		assertEquals(1, moneyNumberSystem.getNumber(new IA_Character(null, NOTTHING, true)));
		assertEquals(11, moneyNumberSystem.getNumber(new IA_Character(null, MACHINE, true)));
		assertEquals(2, moneyNumberSystem.getNumber(new IA_Character(null, CASINOS, true)));
		assertEquals(6, moneyNumberSystem.getNumber(new IA_Character(null, BARS, true)));
		assertEquals(0, moneyNumberSystem.getNumber(new IA_Character(null, NOTTHING)));
	}

	@Test
	public void negocios() {
		List<MoneyValues> totalMoneyValues = new ArrayList<MoneyValues>();
		totalMoneyValues.add(BARS);
		totalMoneyValues.add(RESTAURANT);
		totalMoneyValues.add(CASINOS);
		totalMoneyValues.add(MACHINE);
		totalMoneyValues.add(KING);
		MoneyNumberSystem moneyNumberSystem = new MoneyNumberSystem(totalMoneyValues);
		assertEquals(1, moneyNumberSystem.getNumber(new IA_Character(null, BARS)));
		assertEquals(1, moneyNumberSystem.getNumber(new IA_Character(null, RESTAURANT)));
		assertEquals(1, moneyNumberSystem.getNumber(new IA_Character(null, CASINOS)));
		assertEquals(10, moneyNumberSystem.getNumber(new IA_Character(null, MACHINE)));
		assertEquals(1, moneyNumberSystem.getNumber(new IA_Character(null, NOTTHING, true)));
		assertEquals(11, moneyNumberSystem.getNumber(new IA_Character(null, MACHINE, true)));
		assertEquals(2, moneyNumberSystem.getNumber(new IA_Character(null, CASINOS, true)));
		assertEquals(2, moneyNumberSystem.getNumber(new IA_Character(null, BARS, true)));
		assertEquals(0, moneyNumberSystem.getNumber(new IA_Character(null, NOTTHING)));
	}

	@Test
	public void unNegocioReyYCaja() {
		List<MoneyValues> totalMoneyValues = new ArrayList<MoneyValues>();
		totalMoneyValues.add(BARS);
		totalMoneyValues.add(MACHINE);
		totalMoneyValues.add(KING);
		MoneyNumberSystem moneyNumberSystem = new MoneyNumberSystem(totalMoneyValues);
		assertEquals(1, moneyNumberSystem.getNumber(new IA_Character(null, BARS)));
		assertEquals(10, moneyNumberSystem.getNumber(new IA_Character(null, MACHINE)));
		assertEquals(1, moneyNumberSystem.getNumber(new IA_Character(null, NOTTHING, true)));
		assertEquals(11, moneyNumberSystem.getNumber(new IA_Character(null, MACHINE, true)));
		assertEquals(0, moneyNumberSystem.getNumber(new IA_Character(null, NOTTHING)));
	}

	@Test
	public void unNegocioYCaja() {
		List<MoneyValues> totalMoneyValues = new ArrayList<MoneyValues>();
		totalMoneyValues.add(BARS);
		totalMoneyValues.add(MACHINE);
		totalMoneyValues.add(NOTTHING);
		MoneyNumberSystem moneyNumberSystem = new MoneyNumberSystem(totalMoneyValues);
		assertEquals(1, moneyNumberSystem.getNumber(new IA_Character(null, BARS)));
		assertEquals(0, moneyNumberSystem.getNumber(new IA_Character(null, MACHINE)));
		assertEquals(0, moneyNumberSystem.getNumber(new IA_Character(null, NOTTHING)));
	}

	@Test
	public void reyYCaja() {
		List<MoneyValues> totalMoneyValues = new ArrayList<MoneyValues>();
		totalMoneyValues.add(KING);
		totalMoneyValues.add(MACHINE);
		totalMoneyValues.add(NOTTHING);
		MoneyNumberSystem moneyNumberSystem = new MoneyNumberSystem(totalMoneyValues);
		assertEquals(0, moneyNumberSystem.getNumber(new IA_Character(null, MACHINE)));
		assertEquals(1, moneyNumberSystem.getNumber(new IA_Character(null, NOTTHING, true)));
		assertEquals(1, moneyNumberSystem.getNumber(new IA_Character(null, MACHINE, true)));
		assertEquals(0, moneyNumberSystem.getNumber(new IA_Character(null, NOTTHING)));
	}
}
