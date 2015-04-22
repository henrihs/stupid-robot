package edu.wsu.modelling;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JTable;

import edu.wsu.management.GPS;
import edu.wsu.sensors.*;
import static common.PropertyReader.getModelSize;

public class Modeller extends Observable implements Observer, TurnListener {
	
	private final EnvModel envModel;
	private final RenderedModel rendModel;
	private JTable board;
	private final ScheduledExecutorService scheduler;
	
	public Modeller(SensorHandler sensorHandler, GPS gps){
		this(new EnvModel(getModelSize()), sensorHandler, gps);
	}

	public Modeller(EnvModel envModel, SensorHandler sensorHandler, GPS gps) {
		envModel.addObserver((Observer) gps);
		sensorHandler.addObserver(this);
		this.envModel = envModel;
		envModel.initRobotPresence();
		gps.init(envModel);
		rendModel = new RenderedModel(envModel);
		new Frame(envModel);
		scheduler = Executors.newScheduledThreadPool(1);
		scheduleParseMap();
	}
	
	private void scheduleParseMap() {
		Thread t = new Thread() {
			public void run() {
				envModel.parseMap();
			}
		};
		scheduler.scheduleAtFixedRate(t, 100, 333, TimeUnit.MILLISECONDS);
	}

	@Override
	public void onTurnInitialized(int angle) {
		envModel.changeRobotDirection(angle);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg1 instanceof WheelSensor)
			envModel.moveRobotPresence();
		envModel.drawSurroundings((SensorHandler)arg0);
//		System.out.println(envModel);
	}
}
