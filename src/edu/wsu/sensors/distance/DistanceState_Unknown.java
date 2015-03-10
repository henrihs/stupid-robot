package edu.wsu.sensors.distance;

import edu.wsu.robot.Robot;
import edu.wsu.sensors.ESensor;
import edu.wsu.sensors.ISensorStates;
import static common.PropertyReader.*;
import static common.Methods.inInterval;

public class DistanceState_Unknown implements ISensorStates {
	
	@Override
	public ISensorStates doWork(Robot robot, ESensor sensor) {
		int distanceValue = robot.getDistanceValue(sensor.val());
		
		if (inInterval(distanceValue, getClearLowerBoundary(), getClearUpperBoundary()))
			return new DistanceState_Clear();
		else if (inInterval(distanceValue, getObstacleLowerBoundary(), getObstacleUpperBoundary()))
			return new DistanceState_Obstacle();
		else
			return null;
	}
}
