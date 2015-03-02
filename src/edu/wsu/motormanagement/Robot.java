package edu.wsu.motormanagement;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import edu.wsu.KheperaSimulator.RobotController;
import edu.wsu.sensors.ISensorHandler;
import edu.wsu.sensors.SensorHandler;
import static property.PropertyReader.*;

public class Robot extends RobotController implements Observer {
	
	private long rightWheelEnd, leftWheelEnd;
	
	// State pattern
	private static IRobotStates state;
	
	public Robot() throws IOException {
		this(new SensorHandler());
	}

	public Robot(ISensorHandler sensorHandler) {
		sensorHandler.addObserver(this);
		state = new RobotState_InitSensors(sensorHandler);
	}
	
	// Observer pattern
	@Override
	public void update(Observable arg0, Object arg1) {
		if (shouldUpdate())
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
	
	@Override
	public void doWork() throws Exception { //TODO: why throw exception here?
		// State pattern
		state.doWork(this);
	}
	
	@Override
	public void close() throws Exception {//TODO: why throw exception here?
		
	}
	
	public void drive(){
		setMotorSpeeds(getDriveSpeed(), getDriveSpeed());
	}
	
	public void stop(){
		setMotorSpeeds(0, 0);
	}
	
	private boolean shouldUpdate() {
		return (!(getState() instanceof RobotState_InitTurn) &&
				!(getState() instanceof RobotState_Turn));
	}
}