package edu.wsu.robot;

public class RobotState_Turn implements IRobotStates {

	private Robot robot;

	@Override
	public void doWork(Robot robot) {
		if (this.robot == null)
			this.robot = robot;
		turnRobot();
	}

	private void turnRobot() {
		int wheelSpeed = robot.getTurnFastSpeed();
		if (almostDoneTurning())
			wheelSpeed = robot.getTurnSlowSpeed();
		
		if (doneTurning()) {
			robot.stop();
			robot.setState(new RobotState_Drive());
		}
		else if (shouldKeepTurning()) {
			robot.setMotorSpeeds(wheelSpeed, -wheelSpeed);
		}
		else if (turnedTooFar()) {
			robot.setMotorSpeeds(-wheelSpeed, wheelSpeed);
		}
	}
	
	private boolean almostDoneTurning() {
		return (Math.abs(robot.getRightWheelPosition() - robot.getRightWheelEnd()) < 30 ||
				Math.abs(robot.getLeftWheelPosition() - robot.getLeftWheelEnd()) < 30);
	}

	private boolean doneTurning() {
		return (robot.getRightWheelPosition() == robot.getRightWheelEnd() &&
				robot.getLeftWheelPosition() == robot.getLeftWheelEnd());
	}

	private boolean shouldKeepTurning() {
		return (robot.getRightWheelPosition() > robot.getRightWheelEnd() ||
				robot.getLeftWheelPosition() < robot.getLeftWheelEnd());
	}

	private boolean turnedTooFar() {
		return (robot.getLeftWheelPosition() > robot.getLeftWheelEnd() ||
				robot.getRightWheelPosition() < robot.getRightWheelEnd());
	}

}
