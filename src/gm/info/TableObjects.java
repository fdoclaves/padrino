package gm.info;

public enum TableObjects {
	KNIFE("k"), GUN("P"), RESTAURANTS("1$"), BAR("2$"), CASINOS("3$"), CAKE("C"), MACHINE("M"), GLASS("G"), NOTTHING(
			"n");

	String value;

	TableObjects(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
