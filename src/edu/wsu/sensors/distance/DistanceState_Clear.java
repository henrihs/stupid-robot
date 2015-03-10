package edu.wsu.sensors.distance;

import edu.wsu.robot.Robot;
import edu.wsu.sensors.ESensor;
import edu.wsu.sensors.ISensorStates;
import static property.PropertyReader.*;

public class DistanceState_Clear implements ISensorStates {

	@Override
	public ISensorStates doWork(Robot robot, ESensor sensor) {
		if (robot.getDistanceValue(sensor.val()) > getDistanceUpperBoundary())
			return new DistanceState_Obstacle();
		return null;
	}
}
