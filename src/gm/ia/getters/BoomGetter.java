package gm.ia.getters;

import gm.Cake;
import gm.GameCharacter;
import gm.ia.CharacterUtils;
import gm.ia.DataCake;
import gm.pojos.Position;


public class BoomGetter {
    
    public Cake getBoomCake(DataCakeGetter boomCakeGetter, String myTeam, GameCharacter[][] characters) {
        KillNumberOrAttackToMePojo killNumberOrAttackToMePojo = new KillNumberOrAttackToMePojo();
        Cake bestCakeToBoom = null;
        for (DataCake dataCake : boomCakeGetter.getExploitedEnemies()) {
            if (!dataCake.isFatal() && dataCake.getMineByCake() == 0
                    && killNumberOrAttackToMe(killNumberOrAttackToMePojo, dataCake, myTeam, characters)) {
                bestCakeToBoom = dataCake.getCake();

            }
        }
        return bestCakeToBoom;
    }
    
    private boolean killNumberOrAttackToMe(KillNumberOrAttackToMePojo values, DataCake dataCake, String myTeam,
            GameCharacter[][] characters) {

        if (dataCake.enemiesByCake() > values.numberToKill) {
            values.numberToKill = dataCake.enemiesByCake();
            values.attacksToMe = getAttacksToMeByCakeKill(dataCake, characters);
            return true;
        }
        if (dataCake.enemiesByCake() == values.numberToKill) {
            int attacksToMe = getAttacksToMeByCakeKill(dataCake, characters);
            if (attacksToMe > values.attacksToMe) {
                values.numberToKill = dataCake.enemiesByCake();
                values.attacksToMe = getAttacksToMeByCakeKill(dataCake, characters);
                return true;
            }
        }
        return false;

    }
    
    private int getAttacksToMeByCakeKill(DataCake dataCake , GameCharacter[][] characterArray) {
        int attacks = 0;
        for (Position position : dataCake.getEnemiesByCake()) {
            GameCharacter gameCharacter = CharacterUtils.getCharacterByPosition(characterArray, position);
            attacks = attacks + gameCharacter.getAttackData().getAttackPositions().size();
        }
        return attacks;
    }

    private class KillNumberOrAttackToMePojo {

        int numberToKill = 0;

        int attacksToMe = 0;
    }
}
