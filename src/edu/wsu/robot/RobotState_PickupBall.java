package edu.wsu.robot;

import java.util.Stack;

import edu.wsu.sensors.ESensor;
import static common.PropertyReader.*;
import edu.wsu.KheperaSimulator.KSGripperStates;

public class RobotState_PickupBall implements IRobotStates {

	private Robot robot;
	private Stack<Integer> turnHistory;

	public RobotState_PickupBall() {
	}

	@Override
	public void doWork(Robot robot) throws Exception {
		if (this.robot == null)
			this.robot = robot;
		if (robot.isObjectHeld()) {
			returnToInitPosition();
			//TODO: Fire event to DecisionMaker, telling it to move on
			robot.setNextState(null);
			robot.pollNextState();
			return;
		}
		if (checkIfObjectIsInFront()) {
			pickUpBall();
			return;
		}
		
		turnToLocateBall();
	}

	private void returnToInitPosition() {
		for (Integer angle : turnHistory) {
			setToInitTurn(-angle);
		}		
	}

	private void turnToLocateBall() throws Exception {
		int multiplier = directionToTurn();
		int angle = getInitBallSearchAngle() - (turnHistory.size() * 5);
		angle *= multiplier;
		
		if (turnHistory.size() > 10)
			throw new BallNotFoundException(
					"The ball pickup algorithm needs improval");
		
		setToInitTurn(angle);
	}
	
	private void setToInitTurn(int angle) {
		IRobotStates initTurnState = new RobotState_InitTurn(angle);
		robot.setNextState(initTurnState);
		robot.appendStateToQueue(this);
		turnHistory.add(angle);
		robot.pollNextState();
	}

	private boolean checkIfObjectIsInFront() {
		if (robot.getDistanceValue(ESensor.LEFT.val()) > 900 && 
		robot.getDistanceValue(ESensor.RIGHT.val()) > 900 )
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
