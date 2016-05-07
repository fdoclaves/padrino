package gm;

import java.util.List;

import gm.cards.BoomCard;
import gm.cards.CakeCard;
import gm.cards.CakeUtils;
import gm.cards.ChangeCard;
import gm.cards.GunCard;
import gm.cards.KnifeCard;
import gm.cards.MoveCakeCard;
import gm.cards.MoveCard;
import gm.cards.SleepCard;
import gm.exceptions.InterfaceException;
import gm.ia.PlaysController;
import gm.ia.getters.CharateresToAttackByKnifeGetter;
import gm.info.CardType;
import gm.info.Side;
import gm.pojos.Position;
import gm.utils.CharacterUtils;
import gm.utils.GunUtils;
import gm.utils.KnifeUtils;
import gm.utils.MoveUtils;
import gm.utils.SleepUtils;
import gt.extras.Converter;

public class Human_PlaysController implements PlaysController {

	private GameTable gameTable;
	private ExternalDataGetter externalData;
	private Writter writter;

	public Human_PlaysController(GameTable gameTable, ExternalDataGetter externalData, Writter writter) {
		this.gameTable = gameTable;
		this.externalData = externalData;
		this.writter = writter;
	}

	@Override
	public Card get1stCard(GameCharacter[][] characterArray, Player player, String nextTeam, int currentGamers) {
		writter.log(new Converter(8, 3).cToString(characterArray));
		for (Cake cake : gameTable.getCakeList()) {
			writter.log("cake:" + cake.getPosition() + ", team:" + cake.getTeam() + " ");
		}
		CardType cardType = externalData.getCardToPlay(player);
		Card card = null;
		if (cardType == null) {
			card = new ChangeCard(externalData.getChangedCard(player));
		} else {
			if (cardType == CardType.GUN) {
				card = playGunCard(characterArray, player.getTeam());
			}
			if (cardType == CardType.MOVE) {
				card = playMoveCard(characterArray, player.getTeam());
			}
			if (cardType == CardType.KNIFE) {
				card = playKnifeCard(characterArray, player.getTeam());
			}
			if (cardType == CardType.CAKE) {
				Cake cake = new Cake(externalData.getNewPositionCake(), player.getTeam());
				card = new CakeCard(cake);
			}
			if (cardType == CardType.BOOM) {
				Cake cake = getCake(externalData.getPositionCake(gameTable.getCakeList()));
				card = new BoomCard(cake);
			}
			if (cardType == CardType.MOVE_CAKE) {
				card = playMoveCake(characterArray);
			}
			if (cardType == CardType.SLEEP) {
				card = playSleepCard(characterArray, gameTable.getTableSeats(), player.getTeam());
			}
		}
		return card;
	}

	private MoveCakeCard playMoveCake(GameCharacter[][] characterArray) {
		Cake cake = getCake(externalData.getPositionCake(gameTable.getCakeList()));
		Position newPosition = getSideToMove(characterArray, cake);
		return new MoveCakeCard(cake, newPosition);
	}

	private Position getSideToMove(GameCharacter[][] characterArray, Cake cake) {
		Side side = externalData.getWhereMoveCake();
		CakeUtils cakeUtils = new CakeUtils(gameTable.getMaxX(), gameTable.getMaxY());
		List<Position> moveCakesPositions = cakeUtils.getMoveCakesPositions(cake, characterArray);
		Position position;
		if(side == Side.CLOCK_DIRECTION){
			position = moveCakesPositions.get(0);
		}else{
			position = moveCakesPositions.get(1);
		}
		return position;
	}

	private SleepCard playSleepCard(GameCharacter[][] characterArray, TableSeat[][] tableSeats, String team)
			throws NumberFormatException {
		List<Position> list = SleepUtils.get(characterArray, tableSeats, team);
		List<Position> sleepPositions = externalData.getSleepPositions(list);
		SleepCard sleepCard = new SleepCard(sleepPositions.get(0));
		if (sleepPositions.size() == 2) {
			sleepCard = new SleepCard(sleepPositions.get(0), sleepPositions.get(1));
		}
		if (sleepPositions.size() == 3) {
			sleepCard = new SleepCard(sleepPositions.get(0), sleepPositions.get(1), sleepPositions.get(2));
		}
		return sleepCard;
	}

	private Cake getCake(Position position) {
		for (Cake cake : gameTable.getCakeList()) {
			if (cake.getPosition().isEquals(position)) {
				return cake;
			}
		}
		throw new InterfaceException("NO EXIST CAKE");
	}

	private Card playKnifeCard(GameCharacter[][] characterArray, String team) {
		List<Position> knifePositions = KnifeUtils.getCharacterByTeam(gameTable, characterArray, team);
		Position attackerPosition = externalData.getChosePosition(knifePositions);
		CharateresToAttackByKnifeGetter knifeGetter = new CharateresToAttackByKnifeGetter(gameTable);
		List<Position> whereAttack = knifeGetter.getMyAttackPositions(characterArray, attackerPosition, team);
		return new KnifeCard(attackerPosition, externalData.getChosePosition(whereAttack));
	}

	private Card playMoveCard(GameCharacter[][] characterArray, String team) {
		List<Position> movePositions = MoveUtils.getCharacterByTeam(characterArray, team);
		Position whoMove = externalData.getChosePosition(movePositions);
		List<Position> emptySeats = MoveUtils.getEmptySeats(characterArray);
		Position whereMove = externalData.getPositionToMove(emptySeats);
		return new MoveCard(whoMove, whereMove);
	}

	private Card playGunCard(GameCharacter[][] characterArray, String team) {
		List<Position> gunPositions = GunUtils.getCharacterByTeam(gameTable, characterArray, team);
		Position attackerPosition = externalData.getChosePosition(gunPositions);
		Position attackedPosition = GunUtils.getPositionToShoot(attackerPosition, characterArray, team, gameTable);
		return new GunCard(attackerPosition, attackedPosition);
	}

	@Override
	public Card get2ndCard(GameCharacter[][] characterArray, Player player, String nextTeam, int currentGamers,
			Card firstAction) {
		if (player.hasCard(CardType.MOVE) && !(firstAction instanceof ChangeCard)) {
			if(!CharacterUtils.hasAsleepAllCharacter(player, characterArray)){
				if(player.getCounterCharacters() > 1 || !(firstAction instanceof MoveCard)){
					if (externalData.getcontinue()) {
						return playMoveCard(characterArray, player.getTeam());
					}
				}
			}
		}
		return null;
	}

}
