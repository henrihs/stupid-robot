package edu.wsu.sensors;

import java.util.Observable;
import java.util.Observer;

import edu.wsu.robot.IRobotStates;

public interface ISensorHandler {
	public void update(Observable arg0, Object arg1);
	public void addObserver(Observer observer);
//	public IRobotStates getNextState(ESensor sensor, ISensorStates sensorState);
	public IRobotStates getNextState(ESensor sensor, ISensorStates sensorState);
}
