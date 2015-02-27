package edu.wsu.sensors;

import edu.wsu.robot.Robot;

public class SensorState_Clear implements ISensorStates {

	@Override
	public ISensorStates doWork(Robot robot, ESensor sensor) {
		if (robot.getDistanceValue(sensor.val()) > robot.getDistanceUpperBoundary())
			return new SensorState_Obstacle();
		return null;
	}
}
