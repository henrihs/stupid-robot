package edu.wsu.robot;

import edu.wsu.sensors.ObservableSensor;
import edu.wsu.sensors.ESensor;
import edu.wsu.sensors.SensorHandler;

public class RobotState_InitSensors implements IRobotStates {

	@Override
	public void doWork(Robot robot) {
		SensorHandler sensorHandler = SensorHandler.getInstance();
		sensorHandler.setRobot(robot);
		for (ESensor sensor : ESensor.values()) {
			ObservableSensor observableSensor = new ObservableSensor(robot, sensor);
			observableSensor.addObserver(sensorHandler);
			Thread thread = new Thread(observableSensor);
			thread.start();
		}
		robot.setState(new RobotState_Stop());
	}

}
