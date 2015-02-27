package edu.wsu.sensors;

import java.util.Observable;

import edu.wsu.robot.Robot;

public class LightSensor extends Observable implements Runnable {

	private final Robot robot;
	private final ESensor sensor;
	
	private ILightStates state;
	
	public LightSensor(Robot robot, ESensor sensor) {
		this.robot = robot;
		this.sensor = sensor;
		state = new LightState_Unknown();
	}
	
	@Override
	protected void setChanged() {
		super.setChanged();
	}
	
	public void setState(ILightStates state) {
		this.state = state;
	}
	
	@Override
	public void run() {
		while (true) {
			state.doWork(this, sensor, robot);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public String toString() {
		return sensor.toString();
	}
	
	public ESensor getSensor() {
		return sensor;
	}
	
}
