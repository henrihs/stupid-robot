package edu.wsu.sensors;

import java.util.Observable;

import edu.wsu.robot.Robot;

public abstract class ObservableSensor extends Observable implements Runnable {

	protected Robot robot;
	protected ESensor sensor;
	protected ISensorStates state;

	public ObservableSensor(Robot robot, ESensor sensor) {
		this.robot = robot;
		this.sensor = sensor;
	}
	
	public abstract void run();
		
	@Override
	public String toString(){
		return sensor.toString();
	}
	
	public ESensor getSensor(){
		return sensor;
	}
	
	protected void changeState(ISensorStates nextState) {
		setChanged();
		notifyObservers(nextState);
		setState(nextState);
	}
	
	protected void setState(ISensorStates state) {
		this.state = state;
	}
	
	public ISensorStates getState() {
		return state;
	}
}
