package edu.wsu.modelling;

enum ECellContent {
	OBSTACLE("#"), CLEAR(" "), UNKNOWN("?");
	
	private String symbol;
	
	private ECellContent(String s){
		symbol = s;
	}
	
	public String toString(){
		return symbol;
	}
}
