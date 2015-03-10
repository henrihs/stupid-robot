package edu.wsu.sensors.distance;

import edu.wsu.robot.Robot;
import edu.wsu.sensors.ESensor;
import edu.wsu.sensors.ISensorStates;
import static common.Methods.inInterval;
import static common.PropertyReader.*;

public class DistanceState_Obstacle implements ISensorStates {

	@Override
	public ISensorStates doWork(Robot robot, ESensor sensor) {
		int distanceValue = robot.getDistanceValue(sensor.val());
		
		if (inInterval(distanceValue, getClearLowerBoundary(), getClearUpperBoundary()))
			return new DistanceState_Clear();
		else
			return null;
	}
}
