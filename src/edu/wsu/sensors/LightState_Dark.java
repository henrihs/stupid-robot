package edu.wsu.sensors;

import edu.wsu.robot.Robot;
import static property.PropertyReader.*;

public class LightState_Dark implements ISensorStates, ILightStates {

	@Override
	public ISensorStates doWork(Robot robot, ESensor sensor) {
		if (robot.getLightValue(sensor.val()) < getLightLowerBoundary()) {
			return new LightState_Light();
		}
		return null;
		
	}
	
}
