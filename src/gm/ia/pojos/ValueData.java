package gm.ia.pojos;

import gm.ia.DataCake;

public class ValueData {

	private float valueWeapon;

	private float knife;

	private float business;

	private float awake;
	
	private float hasNotCake;
	
	private float hasNotFatalCake;

	public ValueData(float valueWeapon, boolean knife, float business, boolean sleeping, DataCake dataCake) {
		this.valueWeapon = valueWeapon;
		this.business = business;
		if (knife) {
            this.knife = 1f;
        } else {
            this.knife = 0f;
        }
		if (sleeping) {
			this.awake = 0f;
		} else {
			this.awake = 1f;
		}
		if (dataCake == null) {
			this.hasNotCake = 1f;
			this.hasNotFatalCake = 1f;
		} else {
			this.hasNotCake = 0f;
			if (dataCake.isFatal()) {
				this.hasNotFatalCake = 0f;
			} else {
				this.hasNotFatalCake = 1f;
			}
		}
	}

	public float getBusiness() {
		return business;
	}

	public float getValueWeapon() {
		return valueWeapon;
	}

	public float getKnife() {
		return knife;
	}

	public float getAwake() {
		return awake;
	}
	
	public float getHasNotCake(){
		return hasNotCake;
	}

	public Float getHasNotFatalCake() {
		return hasNotFatalCake;
	}
}
