package edu.wsu.robot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import edu.wsu.KheperaSimulator.RobotController;
import edu.wsu.management.GPS;
import edu.wsu.management.StateCompleteListener;
import edu.wsu.modelling.Modeller;
import edu.wsu.modelling.TurnListener;
import edu.wsu.sensors.ISensorHandler;
import edu.wsu.sensors.SensorHandler;
import static common.PropertyReader.*;

public class Robot extends RobotController implements Observer {
	
	private long rightWheelEnd, leftWheelEnd;
	
	private List<TurnListener> turnListeners = new ArrayList<TurnListener>();
	private List<StateCompleteListener> stateCompleteListeners = new ArrayList<StateCompleteListener>();
	
	// State pattern
	private static IRobotStates state;
	
	public Robot() throws IOException {
		this(new SensorHandler(), new GPS());
	}
	
	public Robot(SensorHandler sensorHandler, GPS gps) {
		this(new Modeller(sensorHandler, gps), sensorHandler, gps);
	}

	public Robot(Modeller modeller, SensorHandler sensorHandler, GPS gps) {
		gps.addObserver(this);
		sensorHandler.addObserver(this);
		addTurnListener(modeller);
		addStateCompleteListener(gps);
		state = new RobotState_InitSensors(sensorHandler, gps);
	}

	// Observer pattern
	@Override
	public void update(Observable arg0, Object arg1) {
		stop();			
		if (arg1 instanceof ISensorHandler) {
			state = new RobotState_Stop();
		}
		else if (arg1 instanceof IRobotStates && shouldUpdate())
		{
			System.out.println("state changed from: " + state + "to: " + arg1);
			state = (IRobotStates)arg1;
		}
	}
	
	// State pattern
	public IRobotStates getState() {
		return state;
	}
	
	public void setState(IRobotStates state) {
		Robot.state = state;
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
		if (state != null)
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
	
	public void addTurnListener(TurnListener listener){
		turnListeners.add(listener);
	}
	
	private void addStateCompleteListener(StateCompleteListener listener) {
		stateCompleteListeners.add(listener);
	}
	
	protected void notifyTurnListeners(int angle){
		for (TurnListener listener : turnListeners)
			listener.onTurnInitialized(angle);
	}
	
	protected void notifyStateCompleteListeners(){
		for (StateCompleteListener listener: stateCompleteListeners)
			listener.onStateCompleted();
	}
	
	
	private boolean shouldUpdate() {
		return (!(getState() instanceof RobotState_InitTurn) &&
				!(getState() instanceof RobotState_Turn));
	}
}