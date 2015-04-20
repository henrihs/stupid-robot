package edu.wsu.sensors.light;

import edu.wsu.robot.Robot;
import edu.wsu.sensors.ESensor;
import edu.wsu.sensors.ISensorState;
import static common.PropertyReader.*;
import static common.Methods.inInterval;

public class LightState_Dark implements ISensorState {
	
	private int lightValue;
	
	@Override
	public ISensorState doWork(Robot robot, ESensor sensor) {
		lightValue = robot.getLightValue(sensor.val());
		
		if (inInterval(lightValue, getLightLowerBoundary(), getLightUpperBoundary()))
			return new LightState_Light();
		else if (inInterval(lightValue, getDuskyLowerBoundary(), getDuskyUpperBoundary()))
			return new LightState_Dusky();
		else
			return null;
	}
}
