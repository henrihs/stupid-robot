package edu.wsu.robot;

import java.util.Random;

import edu.wsu.sensors.ESensor;

public class RobotState_InitTurn implements IRobotStates {
	
	private final static int TURN_BOUNDARY = 300;
	private Robot robot;
	
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
			return 180;
		}
		else if (shouldTurnRight()) {
			return 90;
		}
		else if (shouldTurnLeft()) {
			return -90;
		}
		else {
			return randomTurn();
		}
	}
	
	private boolean shouldTurnAround() {
		return (robot.getDistanceValue(ESensor.LEFT.val()) > TURN_BOUNDARY &&
				robot.getDistanceValue(ESensor.RIGHT.val()) > TURN_BOUNDARY);
	}
	
	private boolean shouldTurnRight() {
		return (robot.getDistanceValue(ESensor.LEFT.val()) > TURN_BOUNDARY);
	}
	
	private boolean shouldTurnLeft() {
		return (robot.getDistanceValue(ESensor.RIGHT.val()) > TURN_BOUNDARY);
	}
	
	private int randInt(int min, int max) {
	    Random rand = new Random();
	    return rand.nextInt((max - min) + 1) + min;
	}
	
	private int randomTurn() {
		if (randInt(0, 1) == 0)
			return 90;
		else
			return -90;
	}

}
