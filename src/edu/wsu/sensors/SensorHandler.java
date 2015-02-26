package edu.wsu.sensors;

import java.util.Observable;
import java.util.Observer;

import edu.wsu.robot.IRobotStates;
import edu.wsu.robot.RobotState_Drive;
import edu.wsu.robot.RobotState_InitTurn;


public class SensorHandler extends Observable implements Observer, ISensorHandler {

	// Observer pattern
	@Override
	public void update(Observable arg0, Object arg1) {
		ObservableSensor sensor = (ObservableSensor) arg0;
		ISensorStates sensorState = (ISensorStates) arg1;
		setChanged();
		notifyObservers(getNextState(sensor.getSensor(), sensorState));
	}

	// State pattern
	@Override
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

	private boolean isFront(ESensor sensor) {
		return (sensor == ESensor.FRONTL || sensor == ESensor.FRONTR);
	}

	private boolean isObstacle(ISensorStates sensorState) {
		return (sensorState instanceof SensorState_Obstacle);
	}

	private boolean isClear(ISensorStates sensorState) {
		return (sensorState instanceof SensorState_Clear);
	}	
}

