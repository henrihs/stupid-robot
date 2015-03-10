package common;

public class Methods {

	private Methods() {}
	
	public static boolean inInterval(int value, int lowerBoundary, int upperBoundary) {
		return (value >= lowerBoundary && value < upperBoundary);
	}
	
}
