package edu.wsu.robot;

public class RobotState_Drive implements IRobotStates {

	public RobotState_Drive() {}
	
	@Override
	public void doWork(Robot robot){
		robot.drive();
	}
}
