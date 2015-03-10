package edu.wsu.sensors;

import java.util.Observer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import edu.wsu.robot.Robot;
import edu.wsu.sensors.distance.DistanceSensor;
import edu.wsu.sensors.light.LightSensor;
import static property.PropertyReader.getScheduleRate;

public class ObservableSensorFactory {
	
	private final Robot robot;
	private final Observer sensorHandler;
	private final ScheduledExecutorService scheduler;

	public ObservableSensorFactory(Robot robot, ISensorHandler sensorHandler, int totalSensors) {
		this.robot = robot;
		this.sensorHandler = (Observer) sensorHandler;
		scheduler = Executors.newScheduledThreadPool(totalSensors*2);
	}
	
	public void createDistAndLightSensors(ESensor sensor){
		schedule(createDistanceSensor(sensor));
		schedule(createLightSensor(sensor));
	}
	
	private void schedule(ObservableSensor observableSensor){
		scheduler.scheduleAtFixedRate(observableSensor, 
														getScheduleRate(), 
														getScheduleRate(), 
														TimeUnit.MILLISECONDS);
	}
	
	private LightSensor createLightSensor(ESensor sensor){
		LightSensor lightSensor = new LightSensor(robot, sensor);
		lightSensor.addObserver(sensorHandler);
		return lightSensor;
	}
	
	private DistanceSensor createDistanceSensor(ESensor sensor){
		DistanceSensor distanceSensor = new DistanceSensor(robot, sensor);
		distanceSensor.addObserver(sensorHandler);
		return distanceSensor;
	}

}
