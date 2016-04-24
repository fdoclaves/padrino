package gm;

import gm.ia.AttackData;
import gm.pojos.Position;

public abstract class GameCharacter {
    
    private AttackData attackData;
    
    private float businessValue;

    private float weaponValue;

	private boolean hasCake;

    public float getBusinessValue() {
        return this.businessValue;
    }
    
    public void setBusinessValue(float businessValue) {
        this.businessValue = businessValue;
    }

    public AttackData getAttackData() {
        return this.attackData;
    }
    
    public void setAttackData(AttackData attackData) {
        this.attackData = attackData;
    }
    
    public Position getPosition(){
        return this.attackData.getPosition();
    }
    
    public float getWeaponValue() {
        return this.weaponValue;
    }
    
    public void setWeaponValue(float weaponValue) {
        this.weaponValue = weaponValue;
    }

    abstract public void dead();

    abstract public boolean isTeam(String team);

    abstract public boolean isInvalidSeat();

    abstract public boolean hasGun();

    abstract public boolean hasKnife();

    abstract public boolean isEmpty();

    abstract public void sleep();

    abstract public boolean isSleeping();

    abstract public void wakeUp();

    abstract public GameCharacter cloneCharacters();

    abstract public boolean isKing();

    abstract public String getTeam();

	public void setCake(boolean hasCake) {
		this.hasCake = hasCake;
	}
	
	public boolean hasCake(){
		return this.hasCake;
	}

}
