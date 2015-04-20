package edu.wsu.modelling;

import static common.PropertyReader.getModelSize;

public class IndexPair extends Object {

	private int row, col;
	
	public IndexPair(int row, int col) throws IndexOutOfBoundsException {
		if (row >= getModelSize())
			throw new IndexOutOfBoundsException("row argument exceeds size of model");
		else if (col >= getModelSize())
			throw new IndexOutOfBoundsException("col argument exceeds size of model");
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
	public boolean equals(Object obj) {
		if (obj instanceof IndexPair)
			return (((IndexPair)obj).row() == this.row && ((IndexPair)obj).col() == this.col);
		return false;
	}
	
	@Override
	public String toString() {
		return "Row: " + String.valueOf(row) + ", Col: " + String.valueOf(col);
	}
}
