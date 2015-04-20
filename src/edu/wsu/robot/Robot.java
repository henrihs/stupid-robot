package edu.wsu.robot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import edu.wsu.KheperaSimulator.RobotController;
import edu.wsu.management.DecisionMaker;
import edu.wsu.management.GPS;
import edu.wsu.modelling.Modeller;
import edu.wsu.modelling.TurnListener;
import edu.wsu.sensors.ISensorHandler;
import edu.wsu.sensors.SensorHandler;
import static common.PropertyReader.*;

public class Robot extends RobotController implements Observer {
	
	private long rightWheelEnd, leftWheelEnd;
	
	private List<TurnListener> listeners = new ArrayList<TurnListener>();
	
	// State pattern
	private static IRobotStates state;
	private LinkedList<IRobotStates> stateQueue;
	
	public Robot() throws IOException {
		this(new SensorHandler(), new Modeller());
	}

	public Robot(ISensorHandler sensorHandler, Modeller modeller) {
		sensorHandler.addObserver(this);
		addListener(modeller);
		state = new RobotState_InitSensors(sensorHandler);
		stateQueue = new LinkedList<IRobotStates>();
	}
	
	// Observer pattern
	@Override
	public void update(Observable arg0, Object arg1) {
//		if (!(arg1 instanceof IRobotStates))
		if (!(arg1 instanceof GPS))
			return;
		if (shouldUpdate())
//			state = (IRobotStates) arg1;
			pollNextState();
	}

	// State pattern
	public void setNextState(final IRobotStates state) {
		stateQueue.addFirst(state);
	}
	
	public void appendStateToQueue(IRobotStates state) {
		stateQueue.add(state);
	}
	
	protected void pollNextState(){
		state = stateQueue.pollFirst();
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
	
	public void addListener(TurnListener listener){
		listeners.add(listener);
	}
	
	protected void notifyListeners(int angle){
		for (TurnListener listener : listeners)
			listener.onTurnInitialized(angle);
	}
	
	private boolean shouldUpdate() {
		return (!(getState() instanceof RobotState_InitTurn) &&
				!(getState() instanceof RobotState_Turn));
	}
}