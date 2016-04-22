package gm.ia.getters;

import gm.Cake;
import gm.GameCharacter;
import gm.ia.CharacterUtils;
import gm.ia.DataCake;
import gm.pojos.Position;


public class BoomGetter {
    
    public Cake getBestBoom(DataCakeGetter boomCakeGetter, GameCharacter[][] characters) {
        Cake bestCakeToBoom = null;
        for (DataCake dataCake : boomCakeGetter.getExploitedEnemies()) {
            if (!dataCake.isFatal() && dataCake.getMineByCake() == 0
                    && killNumberOrAttackToMe(dataCake, characters)) {
                bestCakeToBoom = dataCake.getCake();

            }
        }
        return bestCakeToBoom;
    }
    
    private boolean killNumberOrAttackToMe(DataCake dataCake, GameCharacter[][] characters) {
        KillNumberOrAttackToMePojo values = new KillNumberOrAttackToMePojo();
        if (dataCake.enemiesByCake() > values.numberToKill) {
            values.numberToKill = dataCake.enemiesByCake();
            values.attacksToMe = getAttacksToMeByCakeKill(dataCake, characters);
            return true;
        }
        if (dataCake.enemiesByCake() == values.numberToKill) {
            if (getAttacksToMeByCakeKill(dataCake, characters) > values.attacksToMe) {
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
