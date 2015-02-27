package edu.wsu.robot;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import property.Reader;
import edu.wsu.KheperaSimulator.RobotController;
import edu.wsu.sensors.ISensorHandler;
import edu.wsu.sensors.SensorHandler;

public class Robot extends RobotController implements Observer {
	
	private Reader reader;
	
	private long rightWheelEnd, leftWheelEnd;

	private int driveSpeed;
	private int turnSlowSpeed, turnFastSpeed;
	private int leftTurn, rightTurn, turnAround;
	private int scheduleRate;
	
	private int distanceLowerBoundary, distanceUpperBoundary;
	private int lightLowerBoundary, lightUpperBoundary;
	private int turnBoundary;
	
	// State pattern
	private static IRobotStates state;
	
	public Robot() throws IOException {
		this(new SensorHandler(), new Reader("config.properties"));
	}

	public Robot(ISensorHandler sensorHandler, Reader reader) {
		this.reader = reader;
		initializeProperties();
		sensorHandler.addObserver(this);
		// State pattern
		state = new RobotState_InitSensors(sensorHandler);
	}
	
	public int getDistanceLowerBoundary() {
		return distanceLowerBoundary;
	}

	public int getDistanceUpperBoundary() {
		return distanceUpperBoundary;
	}

	public int getLightLowerBoundary() {
		return lightLowerBoundary;
	}

	public int getLightUpperBoundary() {
		return lightUpperBoundary;
	}

	public int getTurnBoundary() {
		return turnBoundary;
	}

	public int getScheduleRate() {
		return scheduleRate;
	}
	
	public int getLeftTurn() {
		return leftTurn;
	}
	
	public int getTurnSlowSpeed() {
		return turnSlowSpeed;
	}
	
	public int getTurnFastSpeed() {
		return turnFastSpeed;
	}
	
	public int getRightTurn() {
		return rightTurn;
	}
	
	public int getTurnAround() {
		return turnAround;
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
	
	private void initializeProperties() {
		leftTurn = reader.getLeftTurn();
		rightTurn = reader.getRightTurn();
		turnAround = reader.getTurnAround();
		driveSpeed = reader.getDriveSpeed();
		turnSlowSpeed = reader.getTurnSlowSpeed();
		turnFastSpeed = reader.getTurnFastSpeed();
		scheduleRate = reader.getScheduleRate();
		distanceLowerBoundary = reader.getDistanceLowerBoundary();
		distanceUpperBoundary = reader.getDistanceUpperBoundary();
		lightLowerBoundary = reader.getLightLowerBoundary();
		lightUpperBoundary = reader.getLightUpperBoundary();
		turnBoundary = reader.getTurnBoundary();
	}
}