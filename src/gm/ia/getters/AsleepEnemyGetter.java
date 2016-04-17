package gm.ia.getters;

import java.util.ArrayList;
import java.util.List;

import gm.GameCharacter;
import gm.GameTable;
import gm.Player;
import gm.TableSeat;
import gm.cards.SleepCard;
import gm.ia.CharacterUtils;
import gm.ia.pojos.InfoAction;
import gm.info.TableObjects;
import gm.pojos.Position;

public class AsleepEnemyGetter {

	private GameTable gameTable;
	private List<Position> others;
	private List<Position> withoutNext;
	private List<Position> asleep;

	public AsleepEnemyGetter(GameCharacter[][] characterArray, Player player, String nextTeam, GameTable gameTable) {
		this.gameTable = gameTable;
		fillSleepList(characterArray, player.getTeam(), nextTeam);
	}

	public AsleepEnemyGetter(List<Position> others, List<Position> withoutNext, List<Position> asleep) {
		this.others = others;
		this.withoutNext = withoutNext;
		this.asleep = asleep;
	}

	public int getWithoutNextNumber() {
		return withoutNext.size();
	}

	private void fillSleepList(GameCharacter[][] characterArray, String myTeam, String nextTeam) {
		withoutNext = new ArrayList<Position>();
		others = new ArrayList<Position>();
		asleep = new ArrayList<Position>();
		for (int x = 0; x < gameTable.getMaxX(); x++) {
			for (int y = 0; y < gameTable.getMaxY(); y++) {
				Position position = new Position(x, y);
				TableSeat tableSeat = gameTable.getTableSeatByPosition(position);
				if (tableSeat.has(TableObjects.GLASS)) {
					GameCharacter character = CharacterUtils.getCharacterByPosition(characterArray, position);
					if (!character.isEmpty() && !character.isTeam(myTeam)) {
						if (character.isSleeping()) {
							asleep.add(position);
						} else {
							if (!character.isTeam(nextTeam)) {
								withoutNext.add(position);
							} else {
								others.add(position);
							}
						}
					}
				}
			}
		}
	}

	public InfoAction getBestSleepCard(String reason) {
		List<Position> toSleep = new ArrayList<Position>();
		if (asleep.size() >= 1) {
			toSleep.add(asleep.get(0));
		}
		if (asleep.size() >= 2) {
			toSleep.add(asleep.get(1));
		}
		if (asleep.size() >= 3) {
			toSleep.add(asleep.get(2));
		}
		if (getWithoutNextNumber() >= 1 && toSleep.size() < 3) {
			toSleep.add(withoutNext.get(0));
		}
		if (getWithoutNextNumber() >= 2 && toSleep.size() < 3) {
			toSleep.add(withoutNext.get(1));
		}
		if (getWithoutNextNumber() >= 3 && toSleep.size() < 3) {
			toSleep.add(withoutNext.get(2));
		}
		if (getOthers() >= 1 && toSleep.size() < 3) {
			toSleep.add(others.get(0));
		}
		if (getOthers() >= 2 && toSleep.size() < 3) {
			toSleep.add(others.get(1));
		}
		if (getOthers() >= 3 && toSleep.size() < 3) {
			toSleep.add(others.get(2));
		}

		if (toSleep.size() == 1) {
			SleepCard sleepCard = new SleepCard(toSleep.get(0));
			return new InfoAction(sleepCard, null, null, reason);
		}
		if (toSleep.size() == 2) {
			SleepCard sleepCard = new SleepCard(toSleep.get(0), toSleep.get(1));
			return new InfoAction(sleepCard, null, null, reason);
		}
		if (toSleep.size() == 3) {
			SleepCard sleepCard = new SleepCard(toSleep.get(0), toSleep.get(1), toSleep.get(2));
			return new InfoAction(sleepCard, null, null, reason);
		}
		return null;
	}

	public int getOthers() {
		return others.size();
	}

	public int getAsleepNumber() {
		return asleep.size();
	}

	public int all() {
		return others.size() + asleep.size() + withoutNext.size();
	}
}
