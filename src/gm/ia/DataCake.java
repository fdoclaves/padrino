package gm.ia;

import gm.pojos.Position;

public class DataCake {

	private Position explotedPosition;
	private boolean fatal;

	public DataCake(Position explotedPosition, boolean fatal) {
		this.explotedPosition = explotedPosition;
		this.fatal = fatal;
	}

	public Position getExplotedPosition() {
		return explotedPosition;
	}

	public boolean isFatal() {
		return fatal;
	}

}
