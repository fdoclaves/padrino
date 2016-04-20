package gm.ia.getters;

import gm.Cake;
import gm.GameCharacter;
import gm.ia.DataCake;
import gm.pojos.Position;


public class BoomGetter {
    
    private CharateresToAttackByKnifeGetter knifeGetter;
    private CharateresToAttackByGunGetter gunGetter;

    public BoomGetter(CharateresToAttackByKnifeGetter knifeGetter, CharateresToAttackByGunGetter gunGetter){
        this.knifeGetter = knifeGetter;
        this.gunGetter = gunGetter;
    }
    
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
            values.attacksToMe = getAttacksToMeByCakeKill(dataCake, myTeam, characters);
            return true;
        }
        if (dataCake.enemiesByCake() == values.numberToKill) {
            int attacksToMe = getAttacksToMeByCakeKill(dataCake, myTeam, characters);
            if (attacksToMe > values.attacksToMe) {
                values.numberToKill = dataCake.enemiesByCake();
                values.attacksToMe = getAttacksToMeByCakeKill(dataCake, myTeam, characters);
                return true;
            }
        }
        return false;

    }
    
    private int getAttacksToMeByCakeKill(DataCake dataCake, String myTeam, GameCharacter[][] characters) {
        int attacks = 0;
        for (Position position : dataCake.getEnemiesByCake()) {
            attacks = knifeGetter.getTheirAttackPositions(characters, position, myTeam).size();
            if (gunGetter.getTheirAttackPosition(characters, position, myTeam) != null) {
                attacks++;
            }
        }
        return attacks;
    }

    private class KillNumberOrAttackToMePojo {

        int numberToKill = 0;

        int attacksToMe = 0;
    }
}
