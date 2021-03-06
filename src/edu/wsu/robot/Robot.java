package edu.wsu.robot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import edu.wsu.KheperaSimulator.RobotController;
import edu.wsu.management.GPS;
import edu.wsu.management.StateCompleteListener;
import edu.wsu.modelling.Modeller;
import edu.wsu.modelling.TurnListener;
import edu.wsu.sensors.SensorHandler;
import edu.wsu.sensors.WheelSensor;
import static common.PropertyReader.*;

public class Robot extends RobotController implements Observer {

	private long rightWheelEnd, leftWheelEnd;

	private List<TurnListener> turnListeners = new ArrayList<TurnListener>();
	private List<StateCompleteListener> stateCompleteListeners = new ArrayList<StateCompleteListener>();
	private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	// State pattern
	private IRobotStates state;


	public Robot() throws IOException {
		this(new SensorHandler(), new GPS());
	}

	public Robot(SensorHandler sensorHandler, GPS gps) {
		this(new Modeller(sensorHandler, gps), sensorHandler, gps);
	}

	public Robot(Modeller modeller, SensorHandler sensorHandler, GPS gps) {
		sensorHandler.setRobot(this);
		gps.addObserver(this);
		addTurnListener(modeller);
		addStateCompleteListener(gps);
		state = new RobotState_InitSensors(sensorHandler, gps);
		sleep(1);
	}

	// Observer pattern
	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg1 instanceof WheelSensor)
			return;
		else if (arg1 instanceof IRobotStates)
			setState((IRobotStates) arg1);
		else
			setState(new RobotState_Stop());
	}

	// State pattern
	public IRobotStates getState() {
		return state;
	}

	public void setState(IRobotStates state) {
		stop();
		this.state = state;
	}

	protected void setRightWheelEnd(long rightWheelEnd) {
		this.rightWheelEnd = rightWheelEnd;
	}

	public long getRightWheelEnd() {
		return rightWheelEnd;
	}

	protected void setLeftWheelEnd(long leftWheelEnd) {
		this.leftWheelEnd = leftWheelEnd;
	}

	public long getLeftWheelEnd() {
		return leftWheelEnd;
	}

	@Override
	public void doWork() throws Exception {
		// State pattern
		if (state != null)
			state.doWork(this);
	}

	@Override
	public void close() throws Exception {
		scheduler.awaitTermination(100, TimeUnit.MILLISECONDS);
	}

	public void drive() {
		setMotorSpeeds(getDriveSpeed(), getDriveSpeed());
	}

	public void stop() {
		setMotorSpeeds(0, 0);
	}

	public void addTurnListener(TurnListener listener) {
		turnListeners.add(listener);
	}

	private void addStateCompleteListener(StateCompleteListener listener) {
		stateCompleteListeners.add(listener);
	}

	protected void notifyTurnListeners(int angle) {
		for (TurnListener listener : turnListeners)
			listener.onTurnInitialized(angle);
	}

	protected void notifyStateCompleteListeners() {
		for (StateCompleteListener listener : stateCompleteListeners)
			listener.onStateCompleted(isObjectHeld());
	}

	public void setScheduler(ScheduledExecutorService scheduler) {
		this.scheduler = scheduler;
		
	}

	public ScheduledExecutorService getScheduler() {
		return scheduler;
	}
}