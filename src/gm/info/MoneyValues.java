package gm.info;

public enum MoneyValues {
	KING("KING"), BAR("BAR"), RESTAURANT("RESTAURANT"), CASINO("CASINO"), NOTTHING(""), MACHINE("MACHINE");

	private String value;

	MoneyValues(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}
