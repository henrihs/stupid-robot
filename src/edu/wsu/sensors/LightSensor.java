package edu.wsu.sensors;

import edu.wsu.robot.Robot;

public class LightSensor extends ObservableSensor {

	public LightSensor(Robot robot, ESensor sensor) {
		super(robot, sensor);
		state = new LightState_Unknown();
	}

	@Override
	public void run() {
		try {
			ISensorStates nextState = state.doWork(robot, sensor);
			if (nextState instanceof ILightStates)
					changeState(nextState);
		} catch (Throwable e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public String toString() {
		return sensor.toString();
	}

	public ESensor getSensor() {
		return sensor;
	}

}
