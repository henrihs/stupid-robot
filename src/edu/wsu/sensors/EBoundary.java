package edu.wsu.sensors;

public enum EBoundary {
	LOWER(500), UPPER(500),
	LIGHT_LOWER(250), LIGHT_UPPER(250),
	TURN_BOUNDARY(300);
	
	private int value;
	
	private EBoundary(int value) {
		this.value = value;
	}
	
	public int val() {
		return this.value;
	}
}
