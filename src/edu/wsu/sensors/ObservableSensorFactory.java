package edu.wsu.sensors;

import java.util.Observer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import edu.wsu.robot.Robot;
import edu.wsu.sensors.distance.DistanceSensor;
import edu.wsu.sensors.light.LightSensor;
import static common.PropertyReader.getScheduleRate;

public class ObservableSensorFactory {
	
	private final Robot robot;
	private final Observer sensorHandler;
	private final ScheduledExecutorService scheduler;

	public ObservableSensorFactory(Robot robot, SensorHandler sensorHandler) {
		this.robot = robot;
		this.sensorHandler = (Observer) sensorHandler;
		scheduler = Executors.newScheduledThreadPool(1);
	}
	
	public void createSensors(){
//		schedule(createDistanceSensor(sensor));
//		schedule(createLightSensor(sensor));
		scheduler.scheduleAtFixedRate(createWheelSensor(), 1, getScheduleRate(), TimeUnit.MILLISECONDS);
		scheduler.scheduleAtFixedRate(createFrontSensor(), getScheduleRate(), 1, TimeUnit.MILLISECONDS);
	}
	
//	private void schedule(Runnable observableSensor){
//		scheduler.scheduleAtFixedRate(observableSensor, 
//														getScheduleRate(), 
//														getScheduleRate(), 
//														TimeUnit.MILLISECONDS);
//	}
	
	private WheelSensor createWheelSensor() {
		WheelSensor wheelSensor = new WheelSensor(robot);
		wheelSensor.addObserver(sensorHandler);
		return wheelSensor;
	}
	
	private FrontSensor createFrontSensor() {
		FrontSensor frontSensor = new FrontSensor(robot);
		frontSensor.addObserver(sensorHandler);
		return frontSensor;
	}
	
//	private LightSensor createLightSensor(ESensor sensor){
//		LightSensor lightSensor = new LightSensor(robot, sensor);
//		lightSensor.addObserver(sensorHandler);
//		return lightSensor;
//	}
//	
}
