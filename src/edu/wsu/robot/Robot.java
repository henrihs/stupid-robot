package edu.wsu.robot;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;

import edu.wsu.KheperaSimulator.RobotController;
import edu.wsu.sensors.ISensorHandler;
import edu.wsu.sensors.SensorHandler;

public class Robot extends RobotController implements Observer {
	
	private long rightWheelEnd, leftWheelEnd;
	private Properties prop;
	private int driveSpeed;
	
	// State pattern
	private static IRobotStates state;
	
	public Robot() {
		this(new SensorHandler());
	}

	public Robot(ISensorHandler sensorHandler) {
		this.prop = new Properties();
		readProperties();
		sensorHandler.addObserver(this);
		// State pattern
		state = new RobotState_InitSensors(sensorHandler);
		this.driveSpeed = Integer.parseInt(prop.getProperty("drivespeed"));
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
		setMotorSpeeds(driveSpeed, driveSpeed);
	}
	
	public void stop(){
		setMotorSpeeds(0, 0);
	}
	
	private boolean shouldUpdate() {
		return (!(getState() instanceof RobotState_InitTurn) &&
				!(getState() instanceof RobotState_Turn));
	}
	
	private void readProperties() {
		try {
			this.prop.load(getClass().getClassLoader().getResourceAsStream("config.properties"));
		} catch (IOException e) {
			System.out.println("Could not load the configuration file.");
		}
	}
}