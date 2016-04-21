package gm.ia;

import gm.ia.getters.MoneyNumberGetter;
import gm.info.MoneyValues;

import java.util.List;

public class GeneralTeam {

    private int countCharacters;

    private MoneyNumberGetter moneyNumberSystem;

    public GeneralTeam(int countCharacters, List<MoneyValues> totalMoneyValues) {
        this.countCharacters = countCharacters;
        this.moneyNumberSystem = new MoneyNumberGetter(totalMoneyValues);
    }

    public int getCountCharacters() {
        return countCharacters;
    }

    public MoneyNumberGetter getMoneyNumberSystem() {
        return moneyNumberSystem;
    }

}