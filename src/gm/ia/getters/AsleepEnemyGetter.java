package gm.ia.getters;

import java.util.ArrayList;
import java.util.List;

import gm.Card;
import gm.GameCharacter;
import gm.GameTable;
import gm.Player;
import gm.TableSeat;
import gm.cards.SleepCard;
import gm.ia.CharacterUtils;
import gm.info.TableObjects;
import gm.pojos.Position;

public class AsleepEnemyGetter {

    private GameTable gameTable;

    private List<Position> others;

    private List<Position> withoutNext;

    private List<Position> asleep;

    private List<Position> othersAttackME;

    private List<Position> withoutNextAttackME;

    private List<Position> asleepAttackMe;
    
    private List<Position> cakeAttackMe;

    private List<Position> cakeOther;

    public AsleepEnemyGetter(GameCharacter[][] characterArray, Player player, String nextTeam, GameTable gameTable) {
        this.gameTable = gameTable;
        fillSleepList(characterArray, player.getTeam(), nextTeam);
    }

    public AsleepEnemyGetter(List<Position> others, List<Position> withoutNext, List<Position> asleep,
            List<Position> othersAttackME, List<Position> withoutNextAttackME, List<Position> asleepAttackMe) {
        this.others = others;
        this.withoutNext = withoutNext;
        this.asleep = asleep;
        this.othersAttackME = othersAttackME;
        this.withoutNextAttackME = withoutNextAttackME;
        this.asleepAttackMe = asleepAttackMe;
        this.cakeAttackMe = new ArrayList<Position>();
        this.cakeOther = new ArrayList<Position>();
    }

    public int getWithoutNextNumber() {
        return withoutNext.size();
    }

    private void fillSleepList(GameCharacter[][] characterArray, String myTeam, String nextTeam) {
        withoutNext = new ArrayList<Position>();
        others = new ArrayList<Position>();
        asleep = new ArrayList<Position>();
        withoutNextAttackME = new ArrayList<Position>();
        othersAttackME = new ArrayList<Position>();
        asleepAttackMe = new ArrayList<Position>();
        cakeAttackMe = new ArrayList<Position>();
        cakeOther = new ArrayList<Position>();
        asleepAttackMe = new ArrayList<Position>();
        for (int x = 0; x < gameTable.getMaxX(); x++) {
            for (int y = 0; y < gameTable.getMaxY(); y++) {
                Position position = new Position(x, y);
                TableSeat tableSeat = gameTable.getTableSeatByPosition(position);
                if (tableSeat.has(TableObjects.GLASS)) {
                    GameCharacter character = CharacterUtils.getCharacterByPosition(characterArray, position);
                    if (CharacterUtils.isValid(character) && !character.isTeam(myTeam) && !character.hasFatalCake()) {
                        if (character.isSleeping()) {
                            if (character.getAttackData().canAttack()) {
                                asleepAttackMe.add(position);
                            } else {
                                asleep.add(position);
                            }
						} else {
							if (character.hasCake()) {
								if (character.getAttackData().canAttack()) {
									cakeAttackMe.add(position);
								} else {
									cakeOther.add(position);
								}
							} else {
								if (!character.isTeam(nextTeam)) {
									if (character.getAttackData().canAttack()) {
										withoutNextAttackME.add(position);
									} else {
										withoutNext.add(position);
									}

								} else {
									if (character.getAttackData().canAttack()) {
										othersAttackME.add(position);
									} else {
										others.add(position);
									}
								}
							}
                        }
                    }
                }
            }
        }
    }

    public Card getBestSleepCard(String reason) {
        List<Position> positions = new ArrayList<Position>();
        addToListIfCan(positions, asleepAttackMe);
        addToListIfCan(positions, asleep);
        addToListIfCan(positions, cakeAttackMe);
        addToListIfCan(positions, cakeOther);
        addToListIfCan(positions, withoutNextAttackME);
        addToListIfCan(positions, othersAttackME);
        addToListIfCan(positions, withoutNext);
        addToListIfCan(positions, others);
        return buildSleepCard(reason, positions);
    }

    private Card buildSleepCard(String reason, List<Position> positions) {
        if (positions.size() == 1) {
            SleepCard sleepCard = new SleepCard(positions.get(0));
            sleepCard.setReason(reason);
            return sleepCard;
        }
        if (positions.size() == 2) {
            SleepCard sleepCard = new SleepCard(positions.get(0), positions.get(1));
            sleepCard.setReason(reason);
            return sleepCard;
        }
        if (positions.size() == 3) {
            SleepCard sleepCard = new SleepCard(positions.get(0), positions.get(1), positions.get(2));
            sleepCard.setReason(reason);
            return sleepCard;
        }
        return null;
    }

    private void addToListIfCan(List<Position> positions, List<Position> list) {
        if (list.size() >= 1 && positions.size() < 3) {
            positions.add(list.get(0));
        }
        if (list.size() >= 2 && positions.size() < 3) {
            positions.add(list.get(1));
        }
        if (list.size() >= 3 && positions.size() < 3) {
            positions.add(list.get(2));
        }
    }

    public int getOthers() {
        return others.size();
    }

    public int getAsleepNumber() {
        return asleep.size();
    }

    public int all() {
        return others.size() + asleep.size() + withoutNext.size() 
        + othersAttackME.size() + withoutNextAttackME.size() 
        + asleepAttackMe.size() + cakeAttackMe.size() + cakeOther.size();
    }
}
