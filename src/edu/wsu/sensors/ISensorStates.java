package edu.wsu.sensors;

import edu.wsu.robot.Robot;

public interface ISensorStates {
	public void doWork(ObservableSensor observableSensor, ESensor sensor, Robot robot);
}
