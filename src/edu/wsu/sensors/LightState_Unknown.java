package edu.wsu.sensors;

import edu.wsu.robot.Robot;

public class LightState_Unknown implements ILightStates {

	@Override
	public void doWork(LightSensor lightSensor, ESensor sensor, Robot robot) {
		ILightStates state = new LightState_Unknown();
		if (robot.getLightValue(sensor.val()) > EBoundary.LIGHT_LOWER.val()) {
			state = new LightState_Dark();
		} else {
			state = new LightState_Light();
		}
		lightSensor.setChanged();
		lightSensor.notifyObservers(state);
		lightSensor.setState(state);
	}

}
