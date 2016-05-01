package gm;

import java.util.Scanner;

import gm.cards.BoomCard;
import gm.cards.CakeCard;
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

	public Human_PlaysController(GameTable gameTable) {
		this.gameTable = gameTable;
		scanner = new Scanner(System.in);
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
			return null;
		}
		CardType cardType = player.getCards().get(numberCard - 1);
		System.out.println("You chose : " + cardType);
		Card card = null;
		if(cardType == CardType.GUN){
			card = playGunCard();
		}
		if(cardType == CardType.MOVE){
			card = playMoveCard();
		}
		if(cardType == CardType.KNIFE){
			card = playKnifeCard();
		}
		if(cardType == CardType.CAKE){
			System.out.println("Cake position: ");
			Cake cake = new Cake(getPosition(), player.getTeam());
			card = new CakeCard(cake);
		}
		if(cardType == CardType.BOOM){
			System.out.println("Cake position: ");
			Cake cake = getCake(getPosition());
			card = new BoomCard(cake);
		}
		if(cardType == CardType.MOVE_CAKE){
			System.out.println("Cake position: ");
			Cake cake = getCake(getPosition());
			System.out.println("New position: ");
			card = new MoveCakeCard(cake, getPosition());
		}
		if(cardType == CardType.SLEEP){
			System.out.println("Sleep position: ");
			card = new SleepCard(getPosition());
		}
		//scanner.close();
		return card;
	}

	private Cake getCake(Position position) {
		for (Cake cake : gameTable.getCakeList()) {
			if(cake.getPosition().isEquals(position)){
				return cake;
			}
		}
		throw new HumanException("NO EXIST CAKE");
	}

	private Card playKnifeCard() {
		System.out.print("attackerPosition: ");
		Position attackerPosition = getPosition();
		System.out.print("attackedPosition: ");
		Position attackedPosition = getPosition();
		return new KnifeCard(attackerPosition, attackedPosition);
	}

	private Card playMoveCard() {
		System.out.print("whoMove: ");
		Position whoMove = getPosition();
		System.out.print("whereMove: ");
		Position whereMove = getPosition();
		return new MoveCard(whoMove, whereMove);
	}

	private Card playGunCard() {
		System.out.print("AttackerPosition: ");
		Position attackerPosition = getPosition();
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
		if(player.getCards().size() == 5){
			System.out.println("Chose one card to change:");
			Integer numberCard = Integer.parseInt(scanner.nextLine());
			CardType cardType = player.getCards().get(numberCard - 1);
			System.out.println("You chose : " + cardType);
			return new ChangeCard(cardType);
		}
		if(player.hasCard(CardType.MOVE)){
			System.out.println("Use 'Move Card':");
			String answer = scanner.nextLine();
			if(answer.equalsIgnoreCase("y")){
				return playMoveCard();
			}
		}
		return null;
	}



}
