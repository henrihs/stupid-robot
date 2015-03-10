package edu.wsu.sensors.distance;

import edu.wsu.robot.Robot;
import edu.wsu.sensors.ESensor;
import edu.wsu.sensors.ISensorStates;
import static property.PropertyReader.*;

public class DistanceState_Unknown implements ISensorStates {
	
	@Override
	public ISensorStates doWork(Robot robot, ESensor sensor) {
		ISensorStates state = new DistanceState_Unknown();
		if (robot.getDistanceValue(sensor.val()) > getDistanceUpperBoundary())
			state = new DistanceState_Obstacle();
		else if (robot.getDistanceValue(sensor.val()) < getDistanceLowerBoundary())
			state = new DistanceState_Clear();
		return state;
	}
}
