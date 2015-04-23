package edu.wsu.robot;

import java.util.Stack;

import edu.wsu.sensors.ESensor;
import static common.PropertyReader.*;
import edu.wsu.KheperaSimulator.KSGripperStates;

public class RobotState_PickupBall implements IRobotStates {

	private Robot robot;
	private Stack<Integer> turnHistory;
	private boolean failed;

	public RobotState_PickupBall() {
		turnHistory = new Stack<Integer>();
		failed = false;
	}

	@Override
	public void doWork(Robot robot) throws Exception {
		if (this.robot == null)
			this.robot = robot;
		if (robot.isObjectHeld() || failed) {
			if (!turnHistory.empty()) {
				returnToInitPosition();	
			} else {
				robot.setState(new RobotState_InitTurn(180));				
			}
			return;
		}
		
		if (checkIfObjectIsInFront()) {
			try {
				pickUpBall();
			} catch (BallNotFoundException e) {
				robot.setArmState(KSGripperStates.ARM_UP);
				failed = true;
			}
			return;
		}
		
		try {
			turnToLocateBall();
		} catch (BallNotFoundException e) {
			failed = true;
		}
	}

	private void returnToInitPosition() {
		setToInitTurn(-turnHistory.pop());
	}

	private void turnToLocateBall() throws Exception {
		int multiplier = directionToTurn();
		int angle = getInitBallSearchAngle() - (turnHistory.size() * 5);
		angle *= multiplier;
		
		if (turnHistory.size() > 10) {
			throw new BallNotFoundException(
					"The ball pickup algorithm needs improval");
		}
		
		turnHistory.add(angle);
		setToInitTurn(angle);
	}
	
	private void setToInitTurn(int angle) {
		IRobotStates initTurnState = new RobotState_InitTurn(angle, this);
		robot.setState(initTurnState);
	}

	private boolean checkIfObjectIsInFront() {
		if (robot.getDistanceValue(ESensor.FRONTL.val()) > 300 && 
		robot.getDistanceValue(ESensor.FRONTR.val()) > 300 )
			return true;
		return false;
	}

	private int directionToTurn() {
		if (robot.getDistanceValue(ESensor.FRONTL.val()) 
				> robot.getDistanceValue(ESensor.FRONTR.val()) + 30)
			return -1;
		else if (robot.getDistanceValue(ESensor.FRONTR.val()) 
				> robot.getDistanceValue(ESensor.FRONTL.val()) + 30)
			return 1;
		else if (robot.getDistanceValue(ESensor.ANGLEL.val()) 
				> robot.getDistanceValue(ESensor.ANGLER.val()) + 30)
			return -2;
		else if (robot.getDistanceValue(ESensor.ANGLER.val()) 
				> robot.getDistanceValue(ESensor.ANGLEL.val()) + 30)
			return 2;
		else if (robot.getDistanceValue(ESensor.LEFT.val()) 
				> robot.getDistanceValue(ESensor.RIGHT.val()) + 30)
			return -3;
		else if (robot.getDistanceValue(ESensor.RIGHT.val()) 
				> robot.getDistanceValue(ESensor.LEFT.val()) + 30)
			return 3;
		else if (robot.getDistanceValue(ESensor.BACKL.val()) 
				> robot.getDistanceValue(ESensor.BACKR.val()) + 30)
			return -6;
		else if (robot.getDistanceValue(ESensor.RIGHT.val()) 
				> robot.getDistanceValue(ESensor.LEFT.val()) + 30)
			return 6;
		
		
		return 0;
	}

	private void pickUpBall() throws BallNotFoundException {
		robot.setGripperState(KSGripperStates.GRIP_OPEN);
		robot.setArmState(KSGripperStates.ARM_DOWN);
		robot.sleep(5);
		robot.setGripperState(KSGripperStates.GRIP_CLOSED);
		robot.sleep(5);
		if (robot.isObjectHeld())
			robot.setArmState(KSGripperStates.ARM_UP);
		else
			throw new BallNotFoundException("Improve pickupBall()");
		robot.sleep(5);
	}

	class BallNotFoundException extends Exception {
		public BallNotFoundException() {
		}

		public BallNotFoundException(String message) {
			super(message);
		}
	}
}
