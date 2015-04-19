package edu.wsu.modelling;

public class IndexPair {

	private int row, col;
	
	public IndexPair(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	public int row() {
		return row;
	}
	
	public int col() {
		return col;
	}
	
	@Override
	public String toString() {
		return "Row: " + String.valueOf(row) + ", Col: " + String.valueOf(col);
	}
}
