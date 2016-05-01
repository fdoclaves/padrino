package gt;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import gm.Player;
import gm.TurnsManager;
import gm.info.CardType;

public class TurnsManagerTest {
	
	private static final int C = 2;
	private static final int B = 1;
	private static final int A = 0;
	private TurnsManager playerManager;
	private List<Player> players;

	@Before
	public void setUp() throws Exception {
		players = new ArrayList<Player>();
		players.add(new Player("A", new ArrayList<CardType>()));
		players.add(new Player("B", new ArrayList<CardType>()));
		players.add(new Player("C", new ArrayList<CardType>()));
		playerManager = new TurnsManager(players);
	}

	@Test
	public void happyPathWithLosers() {
		List<Player> gamers = playerManager.getCurrentPlayer();
		assertEquals("A", gamers.get(0).getTeam());
		assertEquals("B", gamers.get(1).getTeam());
		gamers = playerManager.getCurrentPlayer();
		assertEquals("B", gamers.get(0).getTeam());
		assertEquals("C", gamers.get(1).getTeam());
		gamers = playerManager.getCurrentPlayer();
		assertEquals("C", gamers.get(0).getTeam());
		assertEquals("A", gamers.get(1).getTeam());
	}
	
	@Test
	public void withADead() {
		List<Player> gamers = playerManager.getCurrentPlayer();
		assertEquals("A", gamers.get(0).getTeam());
		assertEquals("B", gamers.get(1).getTeam());
		gamers = playerManager.getCurrentPlayer();
		assertEquals("B", gamers.get(0).getTeam());
		assertEquals("C", gamers.get(1).getTeam());
		players.remove(A);
		gamers = playerManager.getCurrentPlayer();
		assertEquals("C", gamers.get(0).getTeam());
		assertEquals("B", gamers.get(1).getTeam());
	}
	
	@Test
	public void withBDead() {
		List<Player> gamers = playerManager.getCurrentPlayer();
		assertEquals("A", gamers.get(0).getTeam());
		assertEquals("B", gamers.get(1).getTeam());
		gamers = playerManager.getCurrentPlayer();
		assertEquals("B", gamers.get(0).getTeam());
		assertEquals("C", gamers.get(1).getTeam());
		players.remove(B);
		gamers = playerManager.getCurrentPlayer();
		assertEquals("C", gamers.get(0).getTeam());
		assertEquals("A", gamers.get(1).getTeam());
	}
	
	@Test
	public void withCDead() {
		List<Player> gamers = playerManager.getCurrentPlayer();
		assertEquals("A", gamers.get(0).getTeam());
		assertEquals("B", gamers.get(1).getTeam());
		gamers = playerManager.getCurrentPlayer();
		assertEquals("B", gamers.get(0).getTeam());
		assertEquals("C", gamers.get(1).getTeam());
		players.remove(C);
		gamers = playerManager.getCurrentPlayer();
		assertEquals("A", gamers.get(0).getTeam());
		assertEquals("B", gamers.get(1).getTeam());
	}
	
	@Test
	public void withExtrasDead() {
		List<Player> gamers = playerManager.getCurrentPlayer();
		assertEquals("A", gamers.get(0).getTeam());
		assertEquals("B", gamers.get(1).getTeam());
		players.remove(A);
		gamers = playerManager.getCurrentPlayer();
		assertEquals("B", gamers.get(0).getTeam());
		assertEquals("C", gamers.get(1).getTeam());
		gamers = playerManager.getCurrentPlayer();
		assertEquals("C", gamers.get(0).getTeam());
		assertEquals("B", gamers.get(1).getTeam());
	}
	
	@Test
	public void withExtrasDead2() {
		List<Player> gamers = playerManager.getCurrentPlayer();
		assertEquals("A", gamers.get(0).getTeam());
		assertEquals("B", gamers.get(1).getTeam());
		players.remove(B);
		gamers = playerManager.getCurrentPlayer();
		assertEquals("C", gamers.get(0).getTeam());
		assertEquals("A", gamers.get(1).getTeam());
		gamers = playerManager.getCurrentPlayer();
		assertEquals("A", gamers.get(0).getTeam());
		assertEquals("C", gamers.get(1).getTeam());
	}
	
	@Test
	public void withExtrasDead3() {
		List<Player> gamers = playerManager.getCurrentPlayer();
		assertEquals("A", gamers.get(0).getTeam());
		assertEquals("B", gamers.get(1).getTeam());
		players.remove(C);
		gamers = playerManager.getCurrentPlayer();
		assertEquals("B", gamers.get(0).getTeam());
		assertEquals("A", gamers.get(1).getTeam());
		gamers = playerManager.getCurrentPlayer();
		assertEquals("A", gamers.get(0).getTeam());
		assertEquals("B", gamers.get(1).getTeam());
	}


}
