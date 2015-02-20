package edu.wsu.sensors;

public enum EBoundary {
	LOWER(500), UPPER(500);
	
	private int value;
	
	private EBoundary(int value) {
		this.value = value;
	}
	
	public int val() {
		return this.value;
	}
}
