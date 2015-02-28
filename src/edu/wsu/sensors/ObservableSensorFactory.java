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
	private final int scheduleInterval_ms;

	public ObservableSensorFactory(Robot robot, ISensorHandler sensorHandler, int totalSensors) {
		this.robot = robot;
		this.sensorHandler = sensorHandler;
		scheduler = Executors.newScheduledThreadPool(totalSensors);
		scheduleInterval_ms = 10; //TODO: Bytt ut med property fra settings-fil
	}
	
	public void create(ESensor sensor){
		ObservableSensor observableSensor = new ObservableSensor(robot, sensor);
		observableSensor.addObserver((Observer) sensorHandler);
//		Thread thread = new Thread(observableSensor);
		scheduler.scheduleAtFixedRate(observableSensor, scheduleInterval_ms, scheduleInterval_ms, TimeUnit.MILLISECONDS);
//		thread.start();

	}

}
