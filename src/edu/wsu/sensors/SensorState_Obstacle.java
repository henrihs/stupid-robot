package edu.wsu.sensors;

import edu.wsu.robot.Robot;
import static property.PropertyReader.*;

public class SensorState_Obstacle implements ISensorStates {

	@Override
	public ISensorStates doWork(Robot robot, ESensor sensor) {
		if (robot.getDistanceValue(sensor.val()) <= getDistanceLowerBoundary())
			return new SensorState_Clear();
		return null;
	}
}
