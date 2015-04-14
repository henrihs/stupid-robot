package edu.wsu.modelling;

enum ECellContent {
	OBSTACLE("#"), CLEAR("Ø"), UNKNOWN("?");
	
	private String symbol;
	
	private ECellContent(String s){
		symbol = s;
	}
	
	public String toString(){
		return symbol;
	}
}
