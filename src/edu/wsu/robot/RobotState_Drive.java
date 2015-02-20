package edu.wsu.robot;

public class RobotState_Drive implements IRobotStates {

	@Override
	public void doWork(Navigating robot) {
		robot.drive();
	}

}
