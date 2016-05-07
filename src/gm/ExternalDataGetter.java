package gm;

import java.util.List;

import gm.info.CardType;
import gm.info.Side;
import gm.pojos.Position;

public interface ExternalDataGetter {

	boolean getcontinue();

	CardType getCardToPlay(Player player);

	Position getNewPositionCake();

	List<Position> getSleepPositions(List<Position> list);

	Position getPositionCake(List<Cake> cakes);

	CardType getChangedCard(Player player);

	Position getChosePosition(List<Position> validPositions);

	Side getWhereMoveCake();

	Position getPositionToMove(List<Position> emptySeats);

}
