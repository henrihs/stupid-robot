package edu.wsu.robot;

import java.util.Random;

import edu.wsu.sensors.ESensor;
import static common.PropertyReader.*;

public class RobotState_InitTurn implements IRobotStates {
	
	private Robot robot;
	private boolean positiveLastTurn;

	@Override
	public void doWork(Robot robot) {
		if (this.robot == null)
			this.robot = robot;
		robot.stop();
		int angle = calculateTurn();
		
		robot.setRightWheelEnd(robot.getRightWheelPosition() - angle * 3);
		robot.setLeftWheelEnd(robot.getLeftWheelPosition() + angle * 3);
		robot.setState(new RobotState_Turn());
	}
	
	private int calculateTurn() {
		if (shouldTurnAround()) {
			return getTurnAround();
		}
		else if (shouldTurnRight()) {
			return getRightTurn();
		}
		else if (shouldTurnLeft()) {
			return getLeftTurn();
		}
		else {
			return randomTurn();
		}
	}
	
	// For testing purposes only
	protected boolean wasLastTurnPositive(){
		return positiveLastTurn;
	}
	
	private boolean shouldTurnAround() {
		return (robot.getDistanceValue(ESensor.LEFT.val()) > getTurnBoundary() &&
				robot.getDistanceValue(ESensor.RIGHT.val()) > getTurnBoundary());
	}
	
	private boolean shouldTurnRight() {
		return (robot.getDistanceValue(ESensor.LEFT.val()) > getTurnBoundary());
	}
	
	private boolean shouldTurnLeft() {
		return (robot.getDistanceValue(ESensor.RIGHT.val()) > getTurnBoundary());
	}
	
	private int randInt(int min, int max) {
	    Random rand = new Random();
	    return rand.nextInt((max - min) + 1) + min;
	}
	
	private int randomTurn() {
		if (randInt(0, 1) == 0){
			positiveLastTurn = true;
			return getRightTurn();
		}
		else{
			positiveLastTurn = false;
			return getLeftTurn();
		}
	}

}
