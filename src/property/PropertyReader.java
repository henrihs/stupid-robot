package property;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertyReader {
	
	// Lazy initialization of singleton
//	private static volatile IPropertyReader instance = null;
	
	private final static Map<String, Integer> propertyMap;
	
	private PropertyReader(){}
	
	static {
		propertyMap = getProperties("config.properties");
	}
	
//	private PropertyReader() throws IOException {
//		this(new Properties(), "config.properties");
//	}
	
//	@
//	protected PropertyReader(Properties properties, String filename) throws IOException{
//		properties.load(PropertyReader.class.getResourceAsStream(filename));
//		propertyMap = getProperties(properties);
//	}
//	
//	@Override
//	public IPropertyReader getInstance() {
//		if
//	}

	public static int getDistanceLowerBoundary() {
		return propertyMap.get("distanceLowerBoundary");
	}
	
	public static int getDistanceUpperBoundary() {
		return propertyMap.get("distanceUpperBoundary");
	}
	
	public static int getLightLowerBoundary() {
		return propertyMap.get("lightLowerBoundary");
	}
	
	public static int getLightUpperBoundary() {
		return propertyMap.get("lightUpperBoundary");
	}
	
	public static int getTurnBoundary() {
		return propertyMap.get("turnBoundary");
	}
	
	public static int getLeftTurn() {
		return propertyMap.get("leftTurn");
	}
	
	public static int getRightTurn() {
		return propertyMap.get("rightTurn");
	}
	
	public static int getTurnAround() {
		return propertyMap.get("turnAround");
	}
	
	public static int getDriveSpeed() {
		return propertyMap.get("driveSpeed");
	}
	
	public static int getTurnSlowSpeed() {
		return propertyMap.get("turnSlowSpeed");
	}
	
	public static int getTurnFastSpeed() {
		return propertyMap.get("turnFastSpeed");
	}
	
	public static int getScheduleRate() {
		return propertyMap.get("scheduleRate");
	}
	
	private static Map<String, Integer> getProperties(String filename){
		Properties properties = new Properties();
		try {
			properties.load(PropertyReader.class.getClassLoader().getResourceAsStream(filename));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (String key: properties.stringPropertyNames()) {
			int value = Integer.parseInt(properties.getProperty(key));
			map.put(key, value);
		}
		return map;
	}

}
