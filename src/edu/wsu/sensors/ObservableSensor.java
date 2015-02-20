package edu.wsu.sensors;

import java.util.Observable;

import edu.wsu.robot.Navigating;


public class ObservableSensor extends Observable implements Runnable {

	private final Navigating robot;
	private final ESensor sensor;	

	private ISensorStates state;

	// Observer pattern
	public ObservableSensor(Navigating robot, ESensor sensor) {
		this.robot = robot;
		this.sensor = sensor;
		state = new SensorState_Unknown();
	}
	
	@Override
	protected void setChanged() {
		super.setChanged();
	}
	
	public void setState(ISensorStates state) {
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
//			switch (state) {
//			case CLEAR:
//				if (robot.getDistanceValue(sensor.val()) > Sensor.UPPER_BOUNDARY.val()){
//					state = SensorStates.OBSTACLE;
//					setChanged();
//					notifyObservers(state);
//				}				
//				break;
//
//			case OBSTACLE:
//				if (robot.getDistanceValue(sensor.val()) <= Sensor.LOWER_BOUNDARY.val()){
//					state = SensorStates.CLEAR;
//					setChanged();
//					notifyObservers(state);
//				}				
//				break;
//			default:
//				if (robot.getDistanceValue(sensor.val()) > Sensor.UPPER_BOUNDARY.val())
//					state = SensorStates.OBSTACLE;
//				else
//					state = SensorStates.CLEAR;
//				setChanged();
//				notifyObservers(state);
//				break;
//			}
//			try {
//				Thread.sleep(10);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
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
