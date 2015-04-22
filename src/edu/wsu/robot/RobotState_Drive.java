package edu.wsu.robot;

import static common.PropertyReader.getWheelInterval;

public class RobotState_Drive implements IRobotStates {

	boolean firstIteration;
	private long start;
	private long last;
	private long lenght;
	
	public RobotState_Drive() {
		firstIteration = true;
	}
	
	@Override
	public void doWork(Robot robot){
		if (firstIteration) {
			start = robot.getLeftWheelPosition();
			last = robot.getLeftWheelPosition();
		}
		
		lenght += robot.getLeftWheelPosition() - last;
		last = robot.getLeftWheelPosition();
		if (lenght > getWheelInterval()*1.1)
			robot.stop();
		else
			robot.drive();
	}

}
