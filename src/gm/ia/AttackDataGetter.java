package gm.ia;

import gm.GameCharacter;
import gm.GameTable;
import gm.Player;
import gm.ia.getters.CharateresToAttackByGunGetter;
import gm.ia.getters.CharateresToAttackByKnifeGetter;
import gm.info.CardType;
import gm.pojos.Position;
import java.util.ArrayList;
import java.util.List;

public class AttackDataGetter {

    private List<AttackData> iaTeam;

    private List<AttackData> enemies;

    public AttackDataGetter(GameTable gameTable, GameCharacter[][] characterArray, Player player) {
        iaTeam = new ArrayList<AttackData>();
        enemies = new ArrayList<AttackData>();
        fillList(gameTable, characterArray, player);
    }

    private void fillList(GameTable gameTable, GameCharacter[][] characterArray, Player player) {
        CharateresToAttackByKnifeGetter knifeGetter = new CharateresToAttackByKnifeGetter(gameTable);
        CharateresToAttackByGunGetter gunGetter = new CharateresToAttackByGunGetter(gameTable);
        for (int x = 0; x < gameTable.getMaxX(); x++) {
            for (int y = 0; y < gameTable.getMaxY(); y++) {
                Position position = new Position(x, y);
                GameCharacter gameCharacter = CharacterUtils.getCharacterByPosition(characterArray, position);
                if (!gameCharacter.isEmpty()) {

                    if (gameCharacter.isTeam(player.getTeam())) {
                        AttackData attackData = buildAttackDataMine(characterArray, gameCharacter, knifeGetter,
                                gunGetter, position, player);
                        iaTeam.add(attackData);
                    } else {
                        AttackData attackData = buildAttackData(characterArray, gameCharacter, knifeGetter,
                                gunGetter, position, player.getTeam());
                        enemies.add(attackData);
                    }
                }
            }
        }
    }

    private AttackData buildAttackDataMine(GameCharacter[][] characters, GameCharacter gameCharacter,
            CharateresToAttackByKnifeGetter knifeGetter, CharateresToAttackByGunGetter gunGetter, Position position,
            Player player) {
        List<Position> knifeAttacks = getKnifeAttack(characters, knifeGetter, position, player);
        Position gunAttack = getGunAttack(characters, gunGetter, position, player);
        return new AttackData(knifeAttacks, gunAttack, position);
    }

    private List<Position> getKnifeAttack(GameCharacter[][] characters, CharateresToAttackByKnifeGetter knifeGetter,
            Position position, Player player) {
        if (player.hasCard(CardType.KNIFE)) {
            return knifeGetter.getMyAttackPositions(characters, position, player);
        }
        return new ArrayList<Position>();
    }

    private Position getGunAttack(GameCharacter[][] characters, CharateresToAttackByGunGetter gunGetter,
            Position position, Player player) {
        if (player.hasCard(CardType.GUN)) {
            return gunGetter.getMyAttackPosition(characters, position, player);
        }
        return null;
    }

    private AttackData buildAttackData(GameCharacter[][] characterArray, GameCharacter gameCharacter,
            CharateresToAttackByKnifeGetter knifeGetter, CharateresToAttackByGunGetter gunGetter, Position position,
            String myTeam) {
        List<Position> knifeAttacks = knifeGetter.getTheirAttackPositions(characterArray, position, myTeam);
        Position gunAttack = gunGetter.getTheirAttackPosition(characterArray, position, myTeam);
        return new AttackData(knifeAttacks, gunAttack, position);
    }

    public List<AttackData> getIaTeamThatCanBeAttackByThey() {
        return iaTeam;
    }

    public List<AttackData> getEnemyAttackDatas() {
        return enemies;
    }
}
