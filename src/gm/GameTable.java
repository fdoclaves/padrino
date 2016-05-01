package gm;

import java.util.ArrayList;
import java.util.List;

import gm.pojos.Position;

public class GameTable {

	private final TableSeat[][] tableSeats;

	private List<Cake> cakeList;

	private final int maxX;

	private final int maxY;
	
	private int totalMoney;

	public GameTable(TableSeat[][] tableSeats, int totalMoney) {
		this.tableSeats = tableSeats;
		this.totalMoney = totalMoney;
		this.maxX = tableSeats[0].length;
		this.maxY = tableSeats.length;
		this.cakeList = new ArrayList<Cake>();
	}

	public List<Cake> getCakeList() {
		return cakeList;
	}

	public TableSeat[][] getTableSeats() {
		return tableSeats;
	}

	public int getMaxX() {
		return maxX;
	}

	public int getMaxY() {
		return maxY;
	}

	public void setCakeList(List<Cake> cakeList) {
		this.cakeList = cakeList;
	}

	public TableSeat getTableSeatByPosition(Position position) {
		return tableSeats[position.getY()][position.getX()];
	}

	public void add(Cake cake) {
		this.cakeList.add(cake);
	}

	public int getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(int totalMoney) {
		this.totalMoney = totalMoney;
	}
}
