package edu.wsu.sensors.distance;

import edu.wsu.robot.Robot;
import edu.wsu.sensors.ESensor;
import edu.wsu.sensors.ISensorState;
import static common.PropertyReader.*;
import static common.Methods.inInterval;

public class DistanceState_Clear implements ISensorState {

	@Override
	public ISensorState doWork(Robot robot, ESensor sensor) {
		int distanceValue = robot.getDistanceValue(sensor.val());
		
		if (inInterval(distanceValue, getObstacleLowerBoundary(), getObstacleUpperBoundary()))
			return new DistanceState_Obstacle();
		else
			return null;
	}
}