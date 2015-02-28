package edu.wsu.sensors;

import edu.wsu.robot.Robot;


public class DistanceSensor extends ObservableSensor {

	// Observer pattern
	public DistanceSensor(Robot robot, ESensor sensor) {
		super(robot, sensor);
		state = new DistanceState_Unknown();
	}

	public void run() {			
		try {
			ISensorStates nextState = state.doWork(robot, sensor);
			if (nextState instanceof IDistanceStates)
				changeState(nextState);
		} catch (Throwable e) {
			e.printStackTrace();
			throw e;
		}
	}
}
