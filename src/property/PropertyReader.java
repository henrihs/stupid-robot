package property;

import java.io.IOException;
import java.util.Properties;

public class PropertyReader {
	
	private static Properties properties;
	
	private PropertyReader(){}
	
	static {
		properties = loadProperties("config.properties");
	}

	public static int getDistanceLowerBoundary() {
		return getProperty("distanceLowerBoundary");
	}
	
	public static int getDistanceUpperBoundary() {
		return getProperty("distanceUpperBoundary");
	}
	
	public static int getLightLowerBoundary() {
		return getProperty("lightLowerBoundary");
	}
	
	public static int getLightUpperBoundary() {
		return getProperty("lightUpperBoundary");
	}
	
	public static int getTurnBoundary() {
		return getProperty("turnBoundary");
	}
	
	public static int getLeftTurn() {
		return getProperty("leftTurn");
	}
	
	public static int getRightTurn() {
		return getProperty("rightTurn");
	}
	
	public static int getTurnAround() {
		return getProperty("turnAround");
	}
	
	public static int getDriveSpeed() {
		return getProperty("driveSpeed");
	}
	
	public static int getTurnSlowSpeed() {
		return getProperty("turnSlowSpeed");
	}
	
	public static int getTurnFastSpeed() {
		return getProperty("turnFastSpeed");
	}
	
	public static int getScheduleRate() {
		return getProperty("scheduleRate");
	}
	
	public static int getProperty(String propertyKey){
		return Integer.parseInt(properties.getProperty(propertyKey));
	}
	
	private static Properties loadProperties(String filename){
		Properties properties = new Properties();
		try {
			properties.load(PropertyReader.class.getClassLoader().getResourceAsStream(filename));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return properties;
	}

}
