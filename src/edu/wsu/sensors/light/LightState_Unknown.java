package edu.wsu.sensors.light;

import edu.wsu.robot.Robot;
import edu.wsu.sensors.ESensor;
import edu.wsu.sensors.ISensorStates;
import static property.PropertyReader.*;

public class LightState_Unknown implements ISensorStates {

	@Override
	public ISensorStates doWork(Robot robot, ESensor sensor) {
		ISensorStates state = new LightState_Unknown();
		if (robot.getLightValue(sensor.val()) > getLightUpperBoundary())
			state = new LightState_Dark();
		else if (robot.getLightValue(sensor.val()) < getLightLowerBoundary())
			state = new LightState_Light();
		return state;
	}
	
}
