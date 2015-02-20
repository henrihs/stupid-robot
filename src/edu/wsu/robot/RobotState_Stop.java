package edu.wsu.robot;

public class RobotState_Stop implements IRobotStates {

	@Override
	public void doWork(Navigating robot) {
		robot.stop();
	}

}
