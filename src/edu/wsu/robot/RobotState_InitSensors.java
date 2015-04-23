package edu.wsu.robot;

import java.util.concurrent.ScheduledExecutorService;

import edu.wsu.management.GPS;
import edu.wsu.sensors.ObservableSensorFactory;
import edu.wsu.sensors.SensorHandler;

public class RobotState_InitSensors implements IRobotStates {
	
	private SensorHandler sensorHandler;

	public RobotState_InitSensors(SensorHandler sensorHandler, GPS gps) {
		this.sensorHandler = sensorHandler;
	}

	@Override
	public void doWork(Robot robot) {
		doWork(robot, new ObservableSensorFactory(robot, sensorHandler));
	}
	
	protected void doWork(Robot robot, ObservableSensorFactory factory){
		factory.createSensors();
		robot.setState(new RobotState_Stop());
	}
}
