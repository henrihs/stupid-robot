package edu.wsu.sensors;

import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import edu.wsu.robot.IRobotStates;
import edu.wsu.robot.Robot;
import edu.wsu.robot.RobotState_Drive;
import edu.wsu.robot.RobotState_InitTurn;
import edu.wsu.robot.RobotState_Turn;


public class SensorHandler extends Observable implements Observer {
	
	// Singleton pattern
	private final static SensorHandler INSTANCE = new SensorHandler();
	private final static int TURN_BOUNDARY = 300;
	
	private Robot robot;
	
	private SensorHandler() {
	}
	
	// Observer pattern
	@Override
	public void update(Observable arg0, Object arg1) {
		if (shouldUpdate()) {
			ObservableSensor sensor = (ObservableSensor) arg0;
			ISensorStates sensorState = (ISensorStates) arg1;
			setChanged();
			notifyObservers(getNextState(sensor.getSensor(), sensorState));
		}
	}
	
	public void setRobot(Robot robot) {
		// Observer pattern
		this.robot = robot;
	}
	
	// Singleton pattern
	public static SensorHandler getInstance() {
		return INSTANCE;
	}
	
	// State pattern
	public IRobotStates getNextState(ESensor sensor, ISensorStates sensorState) {
		if (isObstacle(sensorState)) {
			if (isFront(sensor)) {
				return new RobotState_InitTurn();
			}
		}
		else if (isClear(sensorState)) {
			if (isFront(sensor)) {
				return new RobotState_Drive();
			}
		}
		return new RobotState_Drive();
	}
	
	public int calculateTurn() {
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
	
	private boolean shouldUpdate() {
		return (!(robot.getState() instanceof RobotState_InitTurn) &&
				!(robot.getState() instanceof RobotState_Turn));
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
	
	private boolean isFront(ESensor sensor) {
		return (sensor == ESensor.FRONTL || sensor == ESensor.FRONTR);
	}
	
	private boolean isObstacle(ISensorStates sensorState) {
		return (sensorState instanceof SensorState_Obstacle);
	}
	
	private boolean isClear(ISensorStates sensorState) {
		return (sensorState instanceof SensorState_Clear);
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

