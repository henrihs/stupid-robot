package edu.wsu.modelling;

public enum EDirection {
	UP(0), RIGHT(1), DOWN(2), LEFT(3);
	
	private int value;
	
	private EDirection(int value){
		this.value = value;
	}
	
	public int value() {
		return value;
	}
	
	public static int moduloValue(int value){
		value %= 4;
		if (value < 0)
			value += 4;
		return value;
	}
}
