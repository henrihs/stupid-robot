package edu.wsu.sensors;

import edu.wsu.robot.Robot;
import static property.PropertyReader.*;

public class DistanceState_Obstacle implements ISensorStates, IDistanceStates {

	@Override
	public ISensorStates doWork(Robot robot, ESensor sensor) {
		if (robot.getDistanceValue(sensor.val()) <= getDistanceLowerBoundary())
			return new DistanceState_Clear();
		return null;
	}
}
