package edu.wsu.sensors;

import edu.wsu.robot.Robot;
import static property.PropertyReader.*;

public class LightState_Light implements ILightStates {

	@Override
	public void doWork(LightSensor lightSensor, ESensor sensor, Robot robot) {
		if (robot.getLightValue(sensor.val()) > getLightLowerBoundary()) {
			ILightStates state = new LightState_Dark();
			lightSensor.setChanged();
			lightSensor.notifyObservers(state);
			lightSensor.setState(state);
		}
	}
}
