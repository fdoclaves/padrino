package gm.ia;

public class ValueAndDataCake {

	private float value;
	private DataCake dataCake;

	public ValueAndDataCake(float value, DataCake dataCake) {
		this.value = value;
		this.dataCake = dataCake;
	}

	public float getValue() {
		return value;
	}

	public DataCake getDataCake() {
		return dataCake;
	}

	public void addValue(int value) {
		this.value = this.value + value;
	}

}