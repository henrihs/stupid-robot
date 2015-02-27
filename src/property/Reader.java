package property;

import java.io.IOException;
import java.util.Properties;

public class Reader {
	
	private Properties prop;
	
	public Reader(String filename) throws IOException {
		prop = new Properties();
		prop.load(getClass().getClassLoader().getResourceAsStream(filename));
	}

	public int getDistanceLowerBoundary() {
		return getProperty("distanceLowerBoundary");
	}
	
	public int getDistanceUpperBoundary() {
		return getProperty("distanceUpperBoundary");
	}
	
	public int getLightLowerBoundary() {
		return getProperty("lightLowerBoundary");
	}
	
	public int getLightUpperBoundary() {
		return getProperty("lightUpperBoundary");
	}
	
	public int getTurnBoundary() {
		return getProperty("turnBoundary");
	}
	
	public int getLeftTurn() {
		return getProperty("leftTurn");
	}
	
	public int getRightTurn() {
		return getProperty("rightTurn");
	}
	
	public int getTurnAround() {
		return getProperty("turnAround");
	}
	
	public int getDriveSpeed() {
		return getProperty("driveSpeed");
	}
	
	public int getTurnSlowSpeed() {
		return getProperty("turnSlowSpeed");
	}
	
	public int getTurnFastSpeed() {
		return getProperty("turnFastSpeed");
	}
	
	public int getScheduleRate() {
		return getProperty("scheduleRate");
	}
	
	private int getProperty(String property) {
		return Integer.parseInt(prop.getProperty(property));
	}
}
