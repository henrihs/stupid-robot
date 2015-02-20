package edu.wsu.robot;

import java.util.Observable;
import java.util.Observer;

import edu.wsu.KheperaSimulator.RobotController;

public class Robot extends RobotController implements Observer {
	
	private long rightWheelEnd, leftWheelEnd;
	
	// State pattern
	private static IRobotStates state;

	public Robot() {
		// State pattern
		state = new RobotState_InitSensors();
	}
	
	// Observer pattern
	@Override
	public void update(Observable arg0, Object arg1) {
		state = (IRobotStates) arg1;
	}

	// State pattern
	public void setState(final IRobotStates newState) {
		state = newState;
	}
	
	// State pattern
	public IRobotStates getState() {
		return state;
	}

	@Override
	public synchronized int getDistanceValue(int sensorID) {
		return super.getDistanceValue(sensorID);
	}
	
	@Override
	public void doWork() throws Exception {
		// State pattern
		state.doWork(this);
	}
	
	@Override
	public void close() throws Exception {
		
	}
	
	public void drive(){
		setMotorSpeeds(5, 5);
	}
	
	public void stop(){
		setMotorSpeeds(0, 0);
	}
	
	public void setRightWheelEnd(long rightWheelEnd){
		this.rightWheelEnd = rightWheelEnd;
	}
	
	public long getRightWheelEnd(){
		return rightWheelEnd;
	}
	
	public void setLeftWheelEnd(long leftWheelEnd){
		this.leftWheelEnd = leftWheelEnd;
	}
	
	public long getLeftWheelEnd(){
		return leftWheelEnd;
	}
}