package edu.wsu.sensors;

import edu.wsu.robot.Robot;

public interface ISensorState {	
	public ISensorState doWork(Robot robot, ESensor sensor);
}
