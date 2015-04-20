package edu.wsu.sensors.light;

import edu.wsu.robot.Robot;
import edu.wsu.sensors.ESensor;
import edu.wsu.sensors.ISensorState;
import static common.PropertyReader.*;
import static common.Methods.inInterval;

public class LightState_Light implements ISensorState {
	
	@Override
	public ISensorState doWork(Robot robot, ESensor sensor) {
		int lightValue = robot.getLightValue(sensor.val());
		
		if (inInterval(lightValue, getDuskyLowerBoundary(), getDuskyUpperBoundary()))
			return new LightState_Dusky();
		else if (inInterval(lightValue, getDarkLowerBoundary(), getDarkUpperBoundary()))
			return new LightState_Dark();
		else
			return null;
	}
}