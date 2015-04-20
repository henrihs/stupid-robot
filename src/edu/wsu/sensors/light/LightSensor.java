package edu.wsu.sensors.light;

import edu.wsu.robot.Robot;
import edu.wsu.sensors.ESensor;
import edu.wsu.sensors.ISensorState;
import edu.wsu.sensors.ObservableSensor;

public class LightSensor extends ObservableSensor {
	public LightSensor(Robot robot, ESensor sensor) {
		super(robot, sensor);
		state = new LightState_Unknown();
	}
	
	@Override
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
