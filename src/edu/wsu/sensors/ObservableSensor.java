package edu.wsu.sensors;

import java.util.Observable;

import edu.wsu.robot.Robot;


public class ObservableSensor extends Observable implements Runnable {

	private final Robot robot;
	private final ESensor sensor;	

	private ISensorStates state;

	// Observer pattern
	public ObservableSensor(Robot robot, ESensor sensor) {
		this.robot = robot;
		this.sensor = sensor;
		state = new SensorState_Unknown();
	}
	
	public void setState(ISensorStates state) {
		this.state = state;
	}
	
	public ISensorStates getState(){
		return state;
	}

	@Override
	public void run() {			
		ISensorStates nextState = state.doWork(robot, sensor);
		if (nextState != null) {
			setChanged();
			notifyObservers(nextState);
			setState(nextState);
		}
	}
	
	@Override
	public String toString(){
		return sensor.toString();
	}
	
	public ESensor getSensor(){
		return sensor;
	}


}
