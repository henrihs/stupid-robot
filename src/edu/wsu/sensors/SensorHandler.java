package edu.wsu.sensors;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import edu.wsu.sensors.distance.DistanceSensor;
import edu.wsu.sensors.distance.DistanceState_Obstacle;
import edu.wsu.sensors.light.LightSensor;

public class SensorHandler extends Observable implements Observer {

	private volatile HashMap<LightSensor, ISensorState> lightSensorStates;
	private volatile HashMap<DistanceSensor, ISensorState> distanceSensorStates;

	public SensorHandler() {
		lightSensorStates = new HashMap<LightSensor, ISensorState>();
		distanceSensorStates = new HashMap<DistanceSensor, ISensorState>();
	}

	public HashMap<LightSensor, ISensorState> getLightSensorStates() {
		return lightSensorStates;
	}

	public HashMap<DistanceSensor, ISensorState> getDistanceSensorStates() {
		return distanceSensorStates;
	}

	// Observer pattern
	@Override
	public synchronized void update(Observable sensor, Object data) {
		if (sensor instanceof DistanceSensor) {
			distanceSensorStates.put((DistanceSensor) sensor, (ISensorState) data);
			if (isFront(((DistanceSensor) sensor).getSensor()) && isObstacle((ISensorState)data)) {
				setChanged();
				notifyObservers();
			}
			// update((DistanceSensor)sensor, (ISensorStates)data);
		} else if (sensor instanceof LightSensor)
			lightSensorStates.put((LightSensor) sensor,	(ISensorState) data);
		else if (sensor instanceof WheelSensor) {
			setChanged();
			notifyObservers(this);
		}
	}

	//
	// Method listening for updates from the distance sensors.
	//
	// public void update(DistanceSensor sensor, ISensorStates state) {
	// setChanged();
	// notifyObservers(getNextState(sensor.getSensor(), state));
	// }

	// public void update(LightSensor sensor, ISensorStates state) {
	// System.out.println(sensor.toString());
	// setChanged();
	// notifyObservers(sensor);
	// }

	// @Override
	// public IRobotStates getNextState(ESensor sensor, ISensorStates
	// sensorState) {
	// if (isObstacle(sensorState)) {
	// if (isFront(sensor)) {
	// return new RobotState_InitTurn();
	// }
	// }
	// else if (isClear(sensorState)) {
	// if (isFront(sensor)) {
	// return new RobotState_Drive();
	// }
	// }
	// return new RobotState_Drive();
	// }
	
	 private boolean isFront(ESensor sensor) {
	 return (sensor == ESensor.FRONTL || sensor == ESensor.FRONTR);
	 }
	
	 private boolean isObstacle(ISensorState sensorState) {
	 return (sensorState instanceof DistanceState_Obstacle);
	 }
	
	// private boolean isClear(ISensorStates sensorState) {
	// return (sensorState instanceof DistanceState_Clear);
	// }
}
