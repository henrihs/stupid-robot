package edu.wsu.sensors;

import java.util.Observer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import edu.wsu.robot.Robot;

public class ObservableSensorFactory {
	
	private final Robot robot;
	private final ISensorHandler sensorHandler;
	private final ScheduledExecutorService scheduler;

	public ObservableSensorFactory(Robot robot, ISensorHandler sensorHandler, int totalSensors) {
		this.robot = robot;
		this.sensorHandler = sensorHandler;
		scheduler = Executors.newScheduledThreadPool(totalSensors);
	}
	
	public void create(ESensor sensor){
		ObservableSensor observableSensor = new ObservableSensor(robot, sensor);
		observableSensor.addObserver((Observer) sensorHandler);
//		Thread thread = new Thread(observableSensor);
		scheduler.scheduleAtFixedRate(observableSensor, robot.getScheduleRate(), robot.getScheduleRate(), TimeUnit.MILLISECONDS);
//		thread.start();

	}

}
