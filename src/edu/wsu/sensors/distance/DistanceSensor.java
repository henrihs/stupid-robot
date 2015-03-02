package edu.wsu.sensors.distance;

import edu.wsu.motormanagement.Robot;
import edu.wsu.sensors.ESensor;
import edu.wsu.sensors.ISensorStates;
import edu.wsu.sensors.ObservableSensor;

public class DistanceSensor extends ObservableSensor {

	// Observer pattern
	public DistanceSensor(Robot robot, ESensor sensor) {
		super(robot, sensor);
		state = new DistanceState_Unknown();
	}

	public void run() {			
		try {
			ISensorStates nextState = state.doWork(robot, sensor);
			if (nextState instanceof ISensorStates)
				changeState(nextState);
		} catch (Throwable e) {
			e.printStackTrace();
			throw e;
		}
	}
}
