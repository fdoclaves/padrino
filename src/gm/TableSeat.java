package gm;

import gm.info.TableObjects;

public class TableSeat {

	private String value;

	public TableSeat(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public boolean has(TableObjects tableObjects) {
		return value.contains(tableObjects.getValue());
	}
}
