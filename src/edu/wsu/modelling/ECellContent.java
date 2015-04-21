package edu.wsu.modelling;

public enum ECellContent {
	OBSTACLE("#"), CLOSE_TO_OBSTACLE("_"), CLEAR(" "), UNKNOWN("?"), BALL("B");
	
	private String symbol;
	
	private ECellContent(String s){
		symbol = s;
	}
	
	public String toString(){
		return symbol;
	}
}
