package gm.ia;

import java.util.ArrayList;
import java.util.List;

import gm.pojos.Position;

public class AttackData {

    private int knifeAttacks;

    private boolean gunAttack;

    private Position position;

    private List<Position> knifeAttacksPositions;

    private Position gunAttackPosition;
    
    private List<Position> attacks;

    public AttackData(List<Position> knifeAttacksPositions, Position gunAttackPosition, Position position) {
        this.attacks = new ArrayList<Position>();
        this.knifeAttacksPositions = knifeAttacksPositions;
        this.gunAttackPosition = gunAttackPosition;
        this.knifeAttacks = knifeAttacksPositions.size();
        this.gunAttack = gunAttackPosition != null;
        this.position = position;
        this.attacks.addAll(knifeAttacksPositions);
        if(gunAttack){
            this.attacks.add(gunAttackPosition);
        }
    }

    public boolean canAttack() {
        return knifeAttacks > 0 || gunAttack;
    }

    public int totalFlank() {
        return attacks.size();
    }
    
    public boolean canAttackWithKnife(){
        return knifeAttacks > 0;
    }

    public Position getPosition() {
        return this.position;
    }

    public Position getGunAttackPosition() {
        return this.gunAttackPosition;
    }
    
    public boolean canAttackWithGun(){
        return gunAttack;
    }

    public List<Position> getKnifeAttacksPositions() {
        return this.knifeAttacksPositions;
    }
    
    public List<Position> getAttackPositions(){
        return attacks;
    }
}
