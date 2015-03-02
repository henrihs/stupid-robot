package edu.wsu.sensors;

import java.util.Observable;
import java.util.Observer;

import edu.wsu.robot.IRobotStates;
import edu.wsu.sensors.distance.DistanceSensor;

public interface ISensorHandler {
	public void update(Observable arg0, Object arg1);
	public void addObserver(Observer observer);
	public IRobotStates getNextState(DistanceSensor sensor, ISensorStates sensorState);
}
