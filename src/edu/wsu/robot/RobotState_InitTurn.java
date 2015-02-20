package edu.wsu.robot;

import edu.wsu.sensors.SensorHandler;

public class RobotState_InitTurn implements IRobotStates {

	@Override
	public void doWork(Navigating robot) {
		SensorHandler sensorHandler = SensorHandler.getInstance();
		robot.stop();
		int angle = sensorHandler.calculateTurn();
		
		robot.setRightWheelEnd(robot.getRightWheelPosition() - angle * 3);
		robot.setLeftWheelEnd(robot.getLeftWheelPosition() + angle * 3);
		robot.setState(new RobotState_Turn());
	}

}
