package edu.wsu.sensors;

import edu.wsu.robot.Navigating;

public class SensorState_Obstacle implements ISensorStates {

	@Override
	public void doWork(ObservableSensor observableSensor, ESensor sensor,
			Navigating robot) {
		if (robot.getDistanceValue(sensor.val()) <= EBoundary.LOWER.val()) {
			ISensorStates state = new SensorState_Clear();
			observableSensor.setChanged();
			observableSensor.notifyObservers(state);
			observableSensor.setState(state);
		}	
	}
}
