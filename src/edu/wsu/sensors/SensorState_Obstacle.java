package edu.wsu.sensors;

import edu.wsu.robot.Robot;

public class SensorState_Obstacle implements ISensorStates {

	@Override
	public ISensorStates doWork(Robot robot, ESensor sensor) {
		if (robot.getDistanceValue(sensor.val()) <= EBoundary.LOWER.val())
			return new SensorState_Clear();
		return null;
	}
}
