package edu.wsu.modelling;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import edu.wsu.management.GPS;
import edu.wsu.modelling.IndexPair;
import edu.wsu.sensors.ESensor;
import edu.wsu.sensors.ISensorHandler;
import edu.wsu.sensors.ISensorState;
import edu.wsu.sensors.ObservableSensor;
import edu.wsu.sensors.SensorHandler;
import edu.wsu.sensors.Status;
import edu.wsu.sensors.distance.DistanceState_Clear;
import edu.wsu.sensors.distance.DistanceState_Obstacle;
import edu.wsu.sensors.light.LightState_Dark;

public class Modeller extends Observable implements Observer, TurnListener {
	
	private final EnvModel envModel;
	private final RenderedModel rendModel;
	
	public Modeller(SensorHandler sensorHandler, GPS gps){
		this(new EnvModel(51), sensorHandler, gps);
	}

	public Modeller(EnvModel envModel, SensorHandler sensorHandler, GPS gps) {
		addObserver((Observer) gps);
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
	public synchronized void update(Observable arg0, Object arg1) {
		if (!(arg1 instanceof SensorHandler))
			return;
		envModel.moveRobotPresence();
		drawSurroundings((SensorHandler)arg1);
		System.out.println(envModel);
		setChanged();
		notifyObservers(envModel);
	}
	
	private synchronized void drawSurroundings(SensorHandler sensorHandler) {
		HashMap<ObservableSensor, ISensorState> distanceSensors = sensorHandler.getDistanceSensorStates();
		HashMap<ObservableSensor, ISensorState> lightSensors = sensorHandler.getDistanceSensorStates();
		IndexPair currentPosition = envModel.locateRobot();
		
		for (ObservableSensor sensor : distanceSensors.keySet()) {
			drawObstacle(sensor.getSensor(), distanceSensors.get(sensor), currentPosition);
		}
	}
	
	private void drawObstacle(ESensor sensor, ISensorState sensorState, IndexPair currentPosition) {
		IndexPair positionToDraw = envModel.findPositionFromSensorEnum(currentPosition, sensor);
		if (positionToDraw == null)
			return;
		if (sensorState instanceof DistanceState_Clear)
			envModel.setCell(positionToDraw.row(), positionToDraw.col(), ECellContent.CLEAR);
		else if (sensorState instanceof DistanceState_Obstacle)
			envModel.setCell(positionToDraw.row(), positionToDraw.col(), ECellContent.OBSTACLE);
	}
	
	private void drawLight(ESensor sensor, ISensorState sensorState, IndexPair currentPosition) {
		IndexPair positionToDraw = envModel.findPositionFromSensorEnum(currentPosition, sensor);
		if (positionToDraw == null)
			return;
		
		if (sensorState instanceof LightState_Dark)
			envModel.setCell(positionToDraw, 0);
	}
}
