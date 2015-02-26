package edu.wsu.sensors;

import edu.wsu.robot.Robot;

public interface ISensorStates {
	public ISensorStates doWork(Robot robot, ESensor sensor);
}
