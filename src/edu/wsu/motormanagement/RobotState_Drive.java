package edu.wsu.motormanagement;

public class RobotState_Drive implements IRobotStates {

	@Override
	public void doWork(Robot robot){
		robot.drive();
	}

}
