package edu.wsu.sensors.distance;

import edu.wsu.robot.Robot;
import edu.wsu.sensors.ESensor;
import edu.wsu.sensors.ISensorState;
import edu.wsu.sensors.ObservableSensor;

public class DistanceSensor extends ObservableSensor {

	// Observer pattern
	public DistanceSensor(Robot robot, ESensor sensor) {
		super(robot, sensor);
		state = new DistanceState_Unknown();
	}

	public void run() {			
		try {
			ISensorState nextState = state.doWork(robot, sensor);
			if (nextState instanceof ISensorState)
				changeState(nextState);
		} catch (Throwable e) {
			e.printStackTrace();
			throw e;
		}
	}
}
