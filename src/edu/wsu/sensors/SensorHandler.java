package edu.wsu.sensors;

import java.util.Observable;
import java.util.Observer;

import edu.wsu.robot.IRobotStates;
import edu.wsu.robot.RobotState_Drive;
import edu.wsu.robot.RobotState_InitTurn;
import edu.wsu.sensors.distance.DistanceSensor;
import edu.wsu.sensors.distance.DistanceState_Clear;
import edu.wsu.sensors.distance.DistanceState_Obstacle;
import edu.wsu.sensors.light.LightSensor;


public class SensorHandler extends Observable implements Observer, ISensorHandler {

	// Observer pattern
	@Override
	public void update(Observable sensor, Object state) {
		if (sensor instanceof DistanceSensor)
			update((DistanceSensor)sensor, (ISensorStates)state);
		else if (sensor instanceof LightSensor)
			update((LightSensor)sensor, (ISensorStates)state);
	}
	
	//
	// Method listening for updates from the distance sensors.
	//
	public void update(DistanceSensor sensor, ISensorStates state){
		setChanged();
		notifyObservers(getNextState(sensor.getSensor(), state));
	}
	
	public void update(LightSensor sensor, ISensorStates state) {
		System.out.println(sensor.toString());
//		setChanged();
//		notifyObservers(sensor);
	}
	
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
		return (sensorState instanceof DistanceState_Obstacle);
	}

	private boolean isClear(ISensorStates sensorState) {
		return (sensorState instanceof DistanceState_Clear);
	}
}

