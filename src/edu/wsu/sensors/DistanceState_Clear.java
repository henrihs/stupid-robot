package edu.wsu.sensors;

import edu.wsu.robot.Robot;
import static property.PropertyReader.*;

public class DistanceState_Clear implements ISensorStates, IDistanceStates {

	@Override
	public ISensorStates doWork(Robot robot, ESensor sensor) {
		if (robot.getDistanceValue(sensor.val()) > getDistanceUpperBoundary())
			return new DistanceState_Obstacle();
		return null;
	}
}
