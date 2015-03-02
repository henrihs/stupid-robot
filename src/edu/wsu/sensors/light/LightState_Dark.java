package edu.wsu.sensors.light;

import edu.wsu.robot.Robot;
import edu.wsu.sensors.ESensor;
import edu.wsu.sensors.ISensorStates;
import static property.PropertyReader.*;

public class LightState_Dark implements ISensorStates {
	
	@Override
	public ISensorStates doWork(Robot robot, ESensor sensor) {
		if (robot.getLightValue(sensor.val()) < getLightLowerBoundary()) {
			return new LightState_Light();
		}
		return null;
	}
}
