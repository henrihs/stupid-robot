package edu.wsu.robot;

import edu.wsu.KheperaSimulator.KSGripperStates;

public class RobotState_DropBall implements IRobotStates {

	@Override
	public void doWork(Robot robot) {
		robot.stop();
		robot.setArmState(KSGripperStates.ARM_DOWN);
		robot.sleep(5);
		robot.setGripperState(KSGripperStates.GRIP_OPEN);
		robot.sleep(5);
		robot.setArmState(KSGripperStates.ARM_UP);
		robot.sleep(5);
		robot.setState(new RobotState_Stop());		
	}
}
