package edu.wsu.sensors;

import edu.wsu.robot.Navigating;

public class SensorState_Clear implements ISensorStates {

	@Override
	public void doWork(ObservableSensor observableSensor, ESensor sensor, Navigating robot) {
		if (robot.getDistanceValue(sensor.val()) > EBoundary.UPPER.val()) {
			ISensorStates state = new SensorState_Obstacle();
			observableSensor.setChanged();
			observableSensor.notifyObservers(state);
			observableSensor.setState(state);
		}
	}
}
