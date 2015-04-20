package edu.wsu.sensors;

import java.util.Observable;

import edu.wsu.robot.Robot;

public abstract class ObservableSensor extends Observable implements Runnable {

	protected Robot robot;
	protected ESensor sensor;
	protected ISensorState state;

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
	
	protected void changeState(ISensorState nextState) {
		setChanged();
		notifyObservers(nextState);
		setState(nextState);
	}
	
	public void setState(ISensorState state) {
		this.state = state;
	}
	
	public ISensorState getState() {
		return state;
	}
}
