package gm.exceptions;

public class GameWarning extends Exception {

	private static final long serialVersionUID = 4831083687416438683L;

	public GameWarning(String sameTeam) {
		super(sameTeam);
	}

}
