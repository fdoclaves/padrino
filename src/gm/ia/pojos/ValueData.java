package gm.ia.pojos;

public class ValueData {

	private float valueWeapon;

	private float knife;

	private float business;

	private float awake;

	public ValueData(float valueWeapon, float knife, float business, boolean sleeping) {
		this.valueWeapon = valueWeapon;
		this.knife = knife;
		this.business = business;
		if (sleeping) {
			this.awake = 0f;
		} else {
			this.awake = 1f;
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
}
