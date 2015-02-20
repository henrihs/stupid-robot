package edu.wsu.sensors;

import edu.wsu.robot.Navigating;

public interface ISensorStates {
	public void doWork(ObservableSensor observableSensor, ESensor sensor, Navigating robot);
}
