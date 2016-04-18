package gm.ia;

import java.util.List;

import gm.Cake;
import gm.pojos.Position;

public class DataCake {

	private Position explotedPosition;
	private boolean fatal;
	private Cake cake;
	private List<Position> enemiesByCake;
	private int mineByCake;

	public DataCake(Position explotedPosition, boolean fatal, Cake cake, List<Position> enemiesByCake, int mineByCake) {
		this.explotedPosition = explotedPosition;
		this.fatal = fatal;
		this.cake = cake;
		this.enemiesByCake = enemiesByCake;
		this.mineByCake = mineByCake;
	}

	public Position getExplotedPosition() {
		return explotedPosition;
	}

	public boolean isFatal() {
		return fatal;
	}

	public int enemiesByCake() {
		return enemiesByCake.size();
	}

	public Cake getCake() {
		return cake;
	}

	public int getMineByCake() {
		return mineByCake;
	}
	
	public List<Position> getEnemiesByCake() {
		return enemiesByCake;
	}

}
