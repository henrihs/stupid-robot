package edu.wsu.modelling;

import java.util.HashMap;

import java.util.Observable;
import java.util.Observer;

import edu.wsu.management.GPS;
import edu.wsu.modelling.IndexPair;
import edu.wsu.sensors.*;
import edu.wsu.sensors.distance.*;
import edu.wsu.sensors.light.*;
import static common.PropertyReader.getModelSize;

public class Modeller extends Observable implements Observer, TurnListener {
	
	private final EnvModel envModel;
	private final RenderedModel rendModel;
	
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
	}
	
	@Override
	public void onTurnInitialized(int angle) {
		envModel.changeRobotDirection(angle);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if (!(arg1 instanceof SensorHandler))
			return;
		envModel.moveRobotPresence();
		envModel.drawSurroundings((SensorHandler)arg1);
	}
}
