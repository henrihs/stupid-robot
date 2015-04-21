package edu.wsu.sensors;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import edu.wsu.modelling.ECellContent;
import edu.wsu.robot.Robot;
import edu.wsu.sensors.distance.DistanceSensor;
import edu.wsu.sensors.distance.DistanceState_Obstacle;
import edu.wsu.sensors.distance.EDistanceSensorState;
import edu.wsu.sensors.light.ELightSensorState;
import edu.wsu.sensors.light.LightSensor;
import static common.Methods.*;
import static common.PropertyReader.*;

public class SensorHandler extends Observable implements Observer {

	private HashMap<ESensor, ELightSensorState> lightSensorStates;
	private HashMap<ESensor, EDistanceSensorState> distanceSensorStates;
	private Robot robot;

	public SensorHandler() {
		lightSensorStates = new HashMap<ESensor, ELightSensorState>();
		distanceSensorStates = new HashMap<ESensor, EDistanceSensorState>();
	}
	
	public void setRobot(Robot robot) {
		this.robot = robot;
	}

	public HashMap<ESensor, ELightSensorState> getLightSensorStates() {
		return lightSensorStates;
	}

	public HashMap<ESensor, EDistanceSensorState> getDistanceSensorStates() {
		return distanceSensorStates;
	}

	// Observer pattern
	@Override
	public void update(Observable sensor, Object data) {
		if (sensor instanceof WheelSensor) {
			updateSensorValues();
			setChanged();
			notifyObservers(this);
		}
	}
	
	private void updateSensorValues() {
		for (ESensor sensor : ESensor.values()) {
			int distance = robot.getDistanceValue(sensor.val());
			int light = robot.getLightValue(sensor.val());
			setDistanceSensorState(sensor, distance);
			setLightSensorState(sensor, light);
		}
	}
	
	private void setDistanceSensorState(ESensor sensor, int value) {
		EDistanceSensorState state;
		if (inInterval(value, getClearLowerBoundary(), getClearUpperBoundary()))
			state = EDistanceSensorState.CLEAR;
		else if (inInterval(value, getObstacleLowerBoundary(), getObstacleUpperBoundary()))
			state = EDistanceSensorState.OBSTACLE;
		else
			state = EDistanceSensorState.UNKNOWN;
		distanceSensorStates.put(sensor, state);
	}
	
	private void setLightSensorState(ESensor sensor, int value) {
		ELightSensorState state;
		if (inInterval(value, getDarkLowerBoundary(), getDarkUpperBoundary()))
			state = ELightSensorState.DARK;
		else if (inInterval(value, getDuskyLowerBoundary(), getDuskyUpperBoundary()))
			state = ELightSensorState.DUSKY;
		else if (inInterval(value, getLightLowerBoundary(), getLightUpperBoundary()))
			state = ELightSensorState.LIGHT;
		else
			state = ELightSensorState.UNKNOWN;
		lightSensorStates.put(sensor, state);
	}
	
	 private boolean isFront(ESensor sensor) {
	 return (sensor == ESensor.FRONTL || sensor == ESensor.FRONTR);
	 }
	
	 private boolean isObstacle(ISensorState sensorState) {
	 return (sensorState instanceof DistanceState_Obstacle);
	 }
}
