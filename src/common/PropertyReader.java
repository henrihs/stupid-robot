package common;

import java.io.IOException;
import java.util.Properties;

public class PropertyReader {
	
	private static Properties properties;
	
	private PropertyReader(){}
	
	static {
		properties = loadProperties("config.properties");
	}
	
	public static int getWheelInterval() {
		return getProperty("wheelInterval");
	}
	
	public static int getWallLenght() {
		return getProperty("wallLength");
	}
	
	public static int getCheckBorderBoundary() {
		return getProperty("checkBorderBoundary");
	}
	
	public static int getEmergencyStopBoundary() {
		return getProperty("emergencyStopBoundary");
	}
	
	public static int getObstacleLowerBoundary() {
		return getProperty("obstacleLowerBoundary");
	}
	
	public static int getObstacleUpperBoundary() {
		return getProperty("obstacleUpperBoundary");
	}
	
	public static int getClearLowerBoundary() {
		return getProperty("clearLowerBoundary");
	}
	
	public static int getClearUpperBoundary() {
		return getProperty("clearUpperBoundary");
	}
	
	public static int getAngleClearUpperBoundary() {
		return getProperty("angleClearUpperBoundary");
	}
	
	public static int getAngleObstacleLowerBoundary(){
		return getProperty("angleObstacleLowerBoundary");
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
	
	public static int getLightLowerBoundary() {
		return getProperty("lightLowerBoundary");
	}
	
	public static int getLightUpperBoundary() {
		return getProperty("lightUpperBoundary");
	}
	
	public static int getDarkLowerBoundary() {
		return getProperty("darkLowerBoundary");
	}
	
	public static int getDarkUpperBoundary() {
		return getProperty("darkUpperBoundary");
	}
	
	public static int getInitBallSearchAngle() {
		return getProperty("initBallSearchAngle");
	}
	
	public static int getModelSize() {
		return getProperty("modelSize");
	}
	
	public static int getBallClusterLower() {
		return getProperty("ballClusterLower");
	}
	
	public static int getBallClusterUpper() {
		return getProperty("ballClusterUpper");
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
