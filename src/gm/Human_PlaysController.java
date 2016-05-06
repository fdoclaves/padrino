package gm;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import gm.cards.BoomCard;
import gm.cards.CakeCard;
import gm.cards.CakeUtils;
import gm.cards.ChangeCard;
import gm.cards.GunCard;
import gm.cards.KnifeCard;
import gm.cards.MoveCakeCard;
import gm.cards.MoveCard;
import gm.cards.SleepCard;
import gm.exceptions.HumanException;
import gm.ia.PlaysController;
import gm.info.CardType;
import gm.pojos.Position;
import gt.extras.Converter;

public class Human_PlaysController implements PlaysController {
	
	private Scanner scanner;
	private GameTable gameTable;
	private GunUtils gunUtils;
	private KnifeUtils knifeUtils;
	private MoveUtils moveUtils;
	private SleepUtils sleepUtils;

	public Human_PlaysController(GameTable gameTable) {
		this.gameTable = gameTable;
		this.scanner = new Scanner(System.in);
		this.gunUtils = new GunUtils();
		this.knifeUtils = new KnifeUtils();
		this.moveUtils = new MoveUtils();
		this.sleepUtils = new SleepUtils();
	}
	
	@Override
	public Card get1stCard(GameCharacter[][] characterArray, Player player, String nextTeam, int currentGamers) {
		System.out.println(new Converter(8, 3).cToString(characterArray));
		for (Cake cake : gameTable.getCakeList()) {
			System.out.print("cake:" + cake.getPosition()+", team:"+cake.getTeam() + " ");
		}
		System.out.println("Choose one card: " + player.getCards());
		Integer numberCard = Integer.parseInt(scanner.nextLine());
		if(numberCard > 5){
            return changeCard(player);
		}
		CardType cardType = player.getCards().get(numberCard - 1);
		System.out.println("You chose : " + cardType);
		Card card = null;
		if(cardType == CardType.GUN){
			card = playGunCard(player.getTeam(),gameTable.getTableSeats(),characterArray);
		}
		if(cardType == CardType.MOVE){
			card = playMoveCard(characterArray, player.getTeam());
		}
		if(cardType == CardType.KNIFE){
			card = playKnifeCard(characterArray, gameTable.getTableSeats(), player.getTeam());
		}
		if(cardType == CardType.CAKE){
			System.out.println("Cake position: ");
			Cake cake = new Cake(getPosition(), player.getTeam());
			card = new CakeCard(cake);
		}
		if(cardType == CardType.BOOM){
		    Position cakePosition = getPositionCake();
            Cake cake = getCake(cakePosition);
			card = new BoomCard(cake);
		}
		if(cardType == CardType.MOVE_CAKE){
		    Position cakePosition = getPositionCake();
			Cake cake = getCake(cakePosition);
			System.out.println("New position: ");
			card = new MoveCakeCard(cake, getPosition());
		}
		if(cardType == CardType.SLEEP){
			card = playSleepCard(gameTable.getTableSeats(), player.getTeam(), characterArray);
		}
		//scanner.close();
		return card;
	}

    private SleepCard playSleepCard(TableSeat[][] tableSeats, String team, GameCharacter[][] characterArray) throws NumberFormatException {
        List<Position> list = sleepUtils.get(characterArray, tableSeats, team);
        System.out.println("Sleep position: " + list);
        System.out.println("Cuantos: ");
        Integer counter = Integer.parseInt(scanner.nextLine());
        List<Position> sleepPositions = new ArrayList<Position>();
        for (int i = 0; i < counter; i++) {
            System.out.println("chose: ");
            Integer indexPosition = Integer.parseInt(scanner.nextLine());
            sleepPositions.add(list.get(indexPosition - 1));
        }
        SleepCard sleepCard = new SleepCard(sleepPositions.get(0));
        if(sleepPositions.size()==2){
            sleepCard = new SleepCard(sleepPositions.get(0), sleepPositions.get(1));
        }
        if(sleepPositions.size()==3){
            sleepCard = new SleepCard(sleepPositions.get(0), sleepPositions.get(1), sleepPositions.get(2));
        }
        return sleepCard;
    }

    private Position getPositionCake() throws NumberFormatException {
        List<Position> cakePositions = CakeUtils.getCharacterByTeam(gameTable.getCakeList());
        System.out.print("CakePositions: " + cakePositions);
        Integer numberPosition = Integer.parseInt(scanner.nextLine());
        return cakePositions.get(numberPosition - 1);
    }

    private Card changeCard(Player player) throws NumberFormatException {
        System.out.println("Chose one card to change:");
        Integer numberChangeCard = Integer.parseInt(scanner.nextLine());
        CardType cardType = player.getCards().get(numberChangeCard - 1);
        System.out.println("You chose : " + cardType);
        return new ChangeCard(cardType);
    }

	private Cake getCake(Position position) {
		for (Cake cake : gameTable.getCakeList()) {
			if(cake.getPosition().isEquals(position)){
				return cake;
			}
		}
		throw new HumanException("NO EXIST CAKE");
	}

	private Card playKnifeCard(GameCharacter[][] characterArray, TableSeat[][] tableSeats, String team) {
	    List<Position> knifePositions = knifeUtils.getCharacterByTeam(tableSeats, characterArray, team);
        System.out.print("AttackerPosition: " + knifePositions);
        Integer numberPosition = Integer.parseInt(scanner.nextLine());
        Position attackerPosition = knifePositions.get(numberPosition - 1);
		System.out.print("attackedPosition: ");
		Position attackedPosition = getPosition();
		return new KnifeCard(attackerPosition, attackedPosition);
	}

	private Card playMoveCard(GameCharacter[][] characterArray, String team) {
	    List<Position> movePositions = moveUtils.getCharacterByTeam(characterArray, team);
        System.out.print("who: " + movePositions);
        Integer numberPosition = Integer.parseInt(scanner.nextLine());
        Position whoMove = movePositions.get(numberPosition - 1);
		System.out.print("whereMove: ");
		Position whereMove = getPosition();
		return new MoveCard(whoMove, whereMove);
	}

	private Card playGunCard(String team, TableSeat[][] tableSeats, GameCharacter[][] characterArray) {
	    List<Position> gunPositions = gunUtils.getCharacterByTeam(tableSeats, characterArray, team);
		System.out.print("who: " + gunPositions);
		Integer numberPosition = Integer.parseInt(scanner.nextLine());
		Position attackerPosition = gunPositions.get(numberPosition - 1);
		System.out.print("attackedPosition: ");
		Position attackedPosition = getPosition();
		return new GunCard(attackerPosition, attackedPosition);
	}

	private Position getPosition() {
		System.out.println("X,Y");
		String coordinates = scanner.nextLine();
		int x = Integer.valueOf(coordinates.substring(0, 1));
		int y = Integer.valueOf(coordinates.substring(2));
		return new Position(x - 1, y - 1);
	}

	@Override
	public Card get2ndCard(GameCharacter[][] characterArray, Player player, String nextTeam, int currentGamers,
			Card firstAction) {
		if(player.hasCard(CardType.MOVE) && !(firstAction instanceof ChangeCard)){
			System.out.println("Use 'Move Card':");
			String answer = scanner.nextLine();
			if(answer.equalsIgnoreCase("y")){
				return playMoveCard(characterArray, player.getTeam());
			}
		}
		return null;
	}

}
