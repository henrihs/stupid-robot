package edu.wsu.sensors;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import edu.wsu.modelling.Modeller;
import edu.wsu.robot.IRobotStates;
import edu.wsu.robot.RobotState_Drive;
import edu.wsu.robot.RobotState_InitTurn;
import edu.wsu.sensors.distance.DistanceSensor;
import edu.wsu.sensors.distance.DistanceState_Clear;
import edu.wsu.sensors.distance.DistanceState_Obstacle;
import edu.wsu.sensors.light.LightSensor;


public class SensorHandler extends Observable implements Observer, ISensorHandler {
	
	private HashMap<ObservableSensor, ISensorStates> lightSensorStates;
	private HashMap<ObservableSensor, ISensorStates> distanceSensorStates;
	
	public SensorHandler() {
		this(new Modeller());
	}
	
	public SensorHandler(Modeller modeller) {
		addObserver(modeller);
		lightSensorStates = new HashMap<ObservableSensor, ISensorStates>();
		distanceSensorStates = new HashMap<ObservableSensor, ISensorStates>();
	}

	public HashMap<ObservableSensor, ISensorStates> getLightSensorStates() {
		return lightSensorStates;
	}
	
	public HashMap<ObservableSensor, ISensorStates> getDistanceSensorStates() {
		return distanceSensorStates;
	}
	
	// Observer pattern
	@Override
	public synchronized void update(Observable sensor, Object data) {
		if (sensor instanceof DistanceSensor) {
			distanceSensorStates.put((ObservableSensor) sensor, (ISensorStates)data);
			update((DistanceSensor)sensor, (ISensorStates)data);
		}
		else if (sensor instanceof LightSensor)
			lightSensorStates.put((ObservableSensor)sensor, (ISensorStates)data);
		else if (sensor instanceof WheelSensor) {
			setChanged();
			notifyObservers(this);
		}
			
	}
	
	//
	// Method listening for updates from the distance sensors.
	//
	public void update(DistanceSensor sensor, ISensorStates state) {
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

