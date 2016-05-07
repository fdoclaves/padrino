package gm;

import java.util.ArrayList;
import java.util.List;

import gm.cards.ChangeCard;
import gm.exceptions.GameException;
import gm.exceptions.GameWarning;
import gm.exceptions.InterfaceException;
import gm.ia.IA_PlaysController;
import gm.ia.PlaysController;
import gm.info.CardType;
import gt.extras.Converter;

public class GameManager {

	private List<Player> players;

	private TurnsManager turnsManager;

	private PlaysManager playsManager;

	private GameTable gameTable;
	
	private int cycles;

	private ExternalDataGetter externalDataGetter;
	
	private Writter writter;

	public GameManager(List<Player> players, CardManager cardManager, GameCharacter[][] characterArray,
			TableSeat[][] tableSeats, Integer totalMoney, ExternalDataGetter externalDataGetter, Writter writter) {
		this.players = players;
		this.externalDataGetter = externalDataGetter;
		this.writter = writter;
		for (Player team : players) {
			addCardsToPlayerToStart(team, cardManager);
		}
		turnsManager = new TurnsManager(players);
		gameTable = new GameTable(tableSeats, totalMoney);
		playsManager = new PlaysManager(characterArray, gameTable, cardManager, players);
		cycles = 0;
	}
	
	public GameManager(List<Player> players, GameCharacter[][] characterArray,
			TableSeat[][] tableSeats, Integer totalMoney, ExternalDataGetter externalDataGetter, 
			Writter writter) {
		this(players, new CardManagerImpl(), characterArray, tableSeats, totalMoney, externalDataGetter, writter);
	}

	private void addCardsToPlayerToStart(Player team, CardManager cardManager) {
		while(team.getCards().size() < 5){
			team.addCard(cardManager.getCard());
		}
	}

	public void start() {
		while (canKeepPlaying(players, gameTable.getTotalMoney())) {
			List<Player> currentPlayer = turnsManager.getCurrentPlayer();
			Player nowPlayer = currentPlayer.get(0);
			String nextTeam = currentPlayer.get(1).getTeam();
			writter.log("-------------" + nowPlayer.getTeam() + "-----------------");
			playsManager.startTurn(nowPlayer);
			if(sigueVivo(nowPlayer)){
			    playCards(nowPlayer, nextTeam);
			}
			playsManager.finishTurn();
			writter.log(new Converter(8, 3).cToString(playsManager.getChairs()));
			for (Cake cake : gameTable.getCakeList()) {
				writter.log("cake:" + cake.getPosition()+", team:"+cake.getTeam() + " ");
			}
			cycles++;
		}
	}

    protected boolean canKeepPlaying(List<Player> players, int totalMoney) {
        return players.size() > 1 && totalMoney > 0;
    }

	private boolean sigueVivo(Player nowPlayer) {
		return nowPlayer.getCounterCharacters() > 0;
	}
	
	private void playCards(Player player, String nextTeam) {
		try {
		    PlaysController playsController = getPlayController(player);
            play(player, nextTeam, playsController);
		} catch (InterfaceException e) {
			e.printStackTrace();
			playsManager.resert();
			playCards(player, nextTeam);
		} catch (GameException e) {
			e.printStackTrace();
			getManagerException(player, nextTeam).managerGameExceptionException();
		} catch (GameWarning e) {
			writter.log("Warning!");
			e.printStackTrace();
		}
	}
	
    private ManagerExceptions getManagerException(Player nowPlayer, String nextTeam) {
        ManagerExceptions managerExceptions;
        if(nowPlayer.isHuman()){
            managerExceptions = new ManagerExceptionsHuman(nowPlayer, nextTeam);
        }else{
            managerExceptions = new ManagerExceptionsIA(nowPlayer.getCards());
        }
        return managerExceptions;
    }

    private PlaysController getPlayController(Player nowPlayer) {
        PlaysController playsController;
        if(nowPlayer.isHuman()){
            playsController = new Human_PlaysController(gameTable, externalDataGetter, writter);
        }else{
            playsController = new IA_PlaysController(gameTable);
        }
        return playsController;
    }

    private class ManagerExceptionsHuman implements ManagerExceptions {

        private Player iaPlayer;

        private String nextTeam;

        public ManagerExceptionsHuman(Player iaPlayer, String nextTeam) {
            this.iaPlayer = iaPlayer;
            this.nextTeam = nextTeam;
        }

        @Override
        public void managerGameExceptionException() {
            playsManager.resert();
            playCards(iaPlayer, nextTeam);
        }

    }

    private class ManagerExceptionsIA implements ManagerExceptions {

        private List<CardType> cardList;

        public ManagerExceptionsIA(List<CardType> cardList) {
            this.cardList = cardList;
        }

        @Override
        public void managerGameExceptionException() {
            try {
                playsManager.play(new ChangeCard(cardList.get(0)));
            } catch (GameException e1) {
            	writter.log("Se salvo!");
            	writter.log(e1.getMessage());
            } catch (GameWarning e1) {

            }
        }

    }

    private void play(Player player, String nextTeam, PlaysController playsController) throws GameException,
            GameWarning {
        Card firstCard = playsController.get1stCard(playsManager.getChairs(), player, nextTeam, players.size());
		if (!waitForBestAction(firstCard)) {
            playsManager.play(firstCard);
            writter.log("played: " + firstCard);
        }
		Card secondCard = playsController.get2ndCard(playsManager.getChairs(), player, nextTeam, players.size(),
					firstCard);
		if (secondCard == null) {
            if (waitForBestAction(firstCard)) {
                playsManager.play(firstCard);
                writter.log("played: " + firstCard);
            }
        } else {
            playsManager.play(secondCard);
            writter.log("played: " + secondCard);
        }
    }

	private boolean waitForBestAction(Card firstCard) {
		return firstCard instanceof ChangeCard;
	}

	public List<Player> whoWin() {
		List<Player> wonPlayer = new ArrayList<Player>();
		if(players.size() > 1){
			int moreMoney = getMoreMoney();
			for (Player player : players) {
				if(player.getMoney() == moreMoney){
					wonPlayer.add(player);
				}
			}
			
		}else{
			wonPlayer.add(players.get(0));
		}
			return wonPlayer;
	}

	private int getMoreMoney() {
		int moreMoney = -100;
		for (Player player : players) {
			if(player.getMoney() > moreMoney){
				moreMoney = player.getMoney();
			}
		}
		return moreMoney;
	}

	public GameTable getGameTable() {
		return gameTable;
	}
	
	public int getCycles(){
	    return cycles;
	}

}
