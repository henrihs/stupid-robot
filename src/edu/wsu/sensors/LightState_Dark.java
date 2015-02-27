package edu.wsu.sensors;

import edu.wsu.robot.Robot;

public class LightState_Dark implements ILightStates {

	@Override
	public void doWork(LightSensor lightSensor, ESensor sensor, Robot robot) {
		if (robot.getLightValue(sensor.val()) < EBoundary.LIGHT_LOWER.val()) {
			ILightStates state = new LightState_Light();
			lightSensor.setChanged();
			lightSensor.notifyObservers(state);
			lightSensor.setState(state);
		}
		
	}
	
}
