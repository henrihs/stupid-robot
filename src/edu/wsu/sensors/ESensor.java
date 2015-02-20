package edu.wsu.sensors;

public enum ESensor { 
	LEFT(0), ANGLEL(1), FRONTL(2),
	RIGHT(5), ANGLER(4), FRONTR(3),
	BACKR(6), BACKL(7);
	
	private int value;
	
	private ESensor(int value) {
		this.value = value;
	}
	
	public int val() {
		return this.value;
	}
}
