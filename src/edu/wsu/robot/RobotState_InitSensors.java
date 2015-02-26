package edu.wsu.robot;

import edu.wsu.sensors.ISensorHandler;
import edu.wsu.sensors.ESensor;
import edu.wsu.sensors.ObservableSensorFactory;

public class RobotState_InitSensors implements IRobotStates {
	
	private ISensorHandler sensorHandler;
	
	public RobotState_InitSensors(ISensorHandler sensorHandler) {
		this.sensorHandler = sensorHandler;
	}

	@Override
	public void doWork(Robot robot) {
		doWork(robot, new ObservableSensorFactory(robot, sensorHandler, ESensor.values().length));
	}
	
	protected void doWork(Robot robot, ObservableSensorFactory factory){
		for (ESensor sensor : ESensor.values()) {
			factory.create(sensor);
		}
		robot.setState(new RobotState_Stop());
	}

}
