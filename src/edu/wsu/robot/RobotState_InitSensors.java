package edu.wsu.robot;

import edu.wsu.management.GPS;
import edu.wsu.modelling.IndexPair;
import edu.wsu.sensors.ISensorHandler;
import edu.wsu.sensors.ESensor;
import edu.wsu.sensors.ObservableSensorFactory;

public class RobotState_InitSensors implements IRobotStates {
	
	private ISensorHandler sensorHandler;

	public RobotState_InitSensors(ISensorHandler sensorHandler) {
		this.sensorHandler = sensorHandler;
		
		// TODO: REMOVE GPS (REMEMBER IMPORTS)		
		// GPS gps = new GPS();
		// gps.pathTo(new IndexPair(50, 40));
	}

	@Override
	public void doWork(Robot robot) {
		doWork(robot, new ObservableSensorFactory(robot, sensorHandler, ESensor.values().length));
	}
	
	protected void doWork(Robot robot, ObservableSensorFactory factory){
		for (ESensor sensor : ESensor.values()) {
			factory.createSensors(sensor);
		}
		robot.setState(new RobotState_Drive());
	}
}
