package edu.wsu.sensors.light;

public enum ELightSensorState {
	DARK("-"), DUSKY("+"), LIGHT("*"), UNKNOWN(" ");
	
	private String symbol;
	
	private ELightSensorState(String s) {
		symbol = s;
	}
	
	public String toString() {
		return symbol;
	}
	
}
