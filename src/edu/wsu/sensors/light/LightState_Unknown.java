package edu.wsu.sensors.light;

import edu.wsu.robot.Robot;
import edu.wsu.sensors.ESensor;
import edu.wsu.sensors.ISensorStates;
import static common.PropertyReader.*;
import static common.Methods.inInterval;

public class LightState_Unknown implements ISensorStates {

	@Override
	public ISensorStates doWork(Robot robot, ESensor sensor) {
		int lightValue = robot.getLightValue(sensor.val());
		
		if (inInterval(lightValue, getLightLowerBoundary(), getLightUpperBoundary()))
			return new LightState_Light();
		else if (inInterval(lightValue, getDuskyLowerBoundary(), getDuskyUpperBoundary()))
			return new LightState_Dusky();
		else if (inInterval(lightValue, getDarkLowerBoundary(), getDarkUpperBoundary()))
			return new LightState_Dark();
		return null;
	}	
}
