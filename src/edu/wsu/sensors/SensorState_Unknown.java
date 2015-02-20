package edu.wsu.sensors;

import edu.wsu.robot.Robot;

public class SensorState_Unknown implements ISensorStates {
	
	@Override
	public void doWork(ObservableSensor observableSensor, ESensor sensor,
			Robot robot) {
		ISensorStates state = new SensorState_Unknown();
		if (robot.getDistanceValue(sensor.val()) > EBoundary.UPPER.val()) {
			state = new SensorState_Obstacle();
		} else {
			state = new SensorState_Clear();
		}
		observableSensor.setChanged();
		observableSensor.notifyObservers(state);
		observableSensor.setState(state);
	}
}
