package edu.wsu.robot;

public class RobotState_InitTurn implements IRobotStates {
	
	private Robot robot;
	private boolean positiveLastTurn;
	private int angle;
	private IRobotStates nextState;
	
	public RobotState_InitTurn(int angle) {
		this(angle, null);
	}
	
	public RobotState_InitTurn(int angle, IRobotStates nextState) {
		this.nextState = nextState;
		this.angle = angle;
	}

	@Override
	public void doWork(Robot robot) {
		if (this.robot == null)
			this.robot = robot;
		robot.stop();
		
		robot.notifyTurnListeners(angle);
		
		robot.setRightWheelEnd(robot.getRightWheelPosition() - angle * 3);
		robot.setLeftWheelEnd(robot.getLeftWheelPosition() + angle * 3);
		robot.setState(new RobotState_Turn(nextState));
	}
	
	// For testing purposes only
	protected boolean wasLastTurnPositive(){
		return positiveLastTurn;
	}
}
