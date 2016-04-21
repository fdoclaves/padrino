package gm.cards;

import java.util.ArrayList;
import java.util.List;

import gm.Card;
import gm.GameCharacter;
import gm.Killer;
import gm.exceptions.GameException;
import gm.info.CardType;
import gm.info.GameMessages;
import gm.pojos.Position;

public class SleepCard extends Card {

	private List<Position> positionList;

	public SleepCard(Position position1, Position position2, Position position3) {
		this.positionList = new ArrayList<Position>();
		this.positionList.add(position1);
		this.positionList.add(position2);
		this.positionList.add(position3);
	}

	public SleepCard(Position position1, Position position2) {
		this.positionList = new ArrayList<Position>();
		this.positionList.add(position1);
		this.positionList.add(position2);
	}

	public SleepCard(Position position1) {
		this.positionList = new ArrayList<Position>();
		this.positionList.add(position1);
	}

	public List<Position> getPositionList() {
		return positionList;
	}

	@Override
	public void doAction(GameCharacter[][] characters) {
		for (Position position : positionList) {
			GameCharacter character = characters[position.getY()][position.getX()];
			if (character.isSleeping()) {
				Killer.kill(characters, position);
			} else {
				character.sleep();
			}
		}
	}

	@Override
	public void validateAction(GameCharacter[][] characters, String team) throws GameException {
		for (Position position : positionList) {
			if (!gameTable.getTableSeatByPosition(position).has(GLASS_ON_TABLE)) {
				throw new GameException(GameMessages.THERE_ISNT_GLASS);
			}
			if (characters[position.getY()][position.getX()].isInvalidSeat()) {
				throw new GameException(GameMessages.IT_ISNT_SEAT);
			}
		}
		for (int i = 0; i < positionList.size() - 1; i++) {
			if (positionList.get(i).getX() == positionList.get(i + 1).getX()
					&& positionList.get(i).getY() == positionList.get(i + 1).getY()) {
				throw new GameException(GameMessages.SAME_POSITIONS);
			}
		}
	}

	@Override
	public CardType getType() {
		return CardType.SLEEP;
	}

}
