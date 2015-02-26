package edu.wsu.sensors;

import edu.wsu.robot.Robot;

public class SensorState_Unknown implements ISensorStates {
	
	@Override
	public ISensorStates doWork(Robot robot, ESensor sensor) {
		if (robot.getDistanceValue(sensor.val()) > EBoundary.UPPER.val())
			return new SensorState_Obstacle();
		return new SensorState_Clear();
	}
}
