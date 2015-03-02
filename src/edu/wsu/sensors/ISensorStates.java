package edu.wsu.sensors;

import edu.wsu.motormanagement.Robot;

public interface ISensorStates {
	public ISensorStates doWork(Robot robot, ESensor sensor);
}
