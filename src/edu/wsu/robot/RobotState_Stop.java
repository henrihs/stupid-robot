package edu.wsu.robot;

public class RobotState_Stop implements IRobotStates {

	private boolean firstIteration;
	
	public RobotState_Stop() {
		firstIteration = true;
	}
	
	@Override
	public void doWork(Robot robot) {
		robot.stop();
		if (firstIteration) {
			firstIteration = false;
			robot.notifyStateCompleteListeners();
		}
	}

}
