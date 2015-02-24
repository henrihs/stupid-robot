package edu.wsu.sensors;

import java.util.Observer;

import edu.wsu.robot.Robot;

public class ObservableSensorFactory {
	
	private final Robot robot;
	private final ISensorHandler sensorHandler;

	public ObservableSensorFactory(Robot robot, ISensorHandler sensorHandler) {
		this.robot = robot;
		this.sensorHandler = sensorHandler;
	}
	
	public Thread create(ESensor sensor){
		ObservableSensor observableSensor = new ObservableSensor(robot, sensor);
		observableSensor.addObserver((Observer) sensorHandler);
		Thread thread = new Thread(observableSensor);
		thread.start();
		return thread;
	}

}
