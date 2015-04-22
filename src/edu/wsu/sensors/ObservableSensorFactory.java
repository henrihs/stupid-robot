package edu.wsu.sensors;

import java.util.Observer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import edu.wsu.robot.Robot;
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
		scheduler.scheduleAtFixedRate(createWheelSensor(), 1, getScheduleRate(), TimeUnit.MILLISECONDS);
	}
	
	private WheelSensor createWheelSensor() {
		WheelSensor wheelSensor = new WheelSensor(robot);
		wheelSensor.addObserver(sensorHandler);
		return wheelSensor;
	}
}
