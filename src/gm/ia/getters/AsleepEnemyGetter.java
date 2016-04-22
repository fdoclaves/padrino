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

    private List<Position> othersAttackME;

    private List<Position> withoutNextAttackME;

    private List<Position> asleepAttackMe;

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
        for (int x = 0; x < gameTable.getMaxX(); x++) {
            for (int y = 0; y < gameTable.getMaxY(); y++) {
                Position position = new Position(x, y);
                TableSeat tableSeat = gameTable.getTableSeatByPosition(position);
                if (tableSeat.has(TableObjects.GLASS)) {
                    GameCharacter character = CharacterUtils.getCharacterByPosition(characterArray, position);
                    if (CharacterUtils.isValid(character) && !character.isTeam(myTeam)) {
                        if (character.isSleeping()) {
                            if (character.getAttackData().canAttack()) {
                                asleepAttackMe.add(position);
                            } else {
                                asleep.add(position);
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

    public InfoAction getBestSleepCard(String reason) {
        List<Position> positions = new ArrayList<Position>();
        addToListIfCan(positions, asleepAttackMe);
        addToListIfCan(positions, asleep);
        addToListIfCan(positions, withoutNextAttackME);
        addToListIfCan(positions, othersAttackME);
        addToListIfCan(positions, withoutNext);
        addToListIfCan(positions, others);
        return buildSleepCard(reason, positions);
    }

    private InfoAction buildSleepCard(String reason, List<Position> positions) {
        if (positions.size() == 1) {
            SleepCard sleepCard = new SleepCard(positions.get(0));
            return new InfoAction(sleepCard, null, null, reason);
        }
        if (positions.size() == 2) {
            SleepCard sleepCard = new SleepCard(positions.get(0), positions.get(1));
            return new InfoAction(sleepCard, null, null, reason);
        }
        if (positions.size() == 3) {
            SleepCard sleepCard = new SleepCard(positions.get(0), positions.get(1), positions.get(2));
            return new InfoAction(sleepCard, null, null, reason);
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
        return others.size() + asleep.size() + withoutNext.size();
    }
}
