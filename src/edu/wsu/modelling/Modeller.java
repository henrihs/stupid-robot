package edu.wsu.modelling;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import edu.wsu.modelling.EnvModel.IndexPair;
import edu.wsu.sensors.ISensorStates;
import edu.wsu.sensors.ObservableSensor;
import edu.wsu.sensors.SensorHandler;
import edu.wsu.sensors.Status;
import edu.wsu.sensors.distance.DistanceState_Clear;
import edu.wsu.sensors.distance.DistanceState_Obstacle;

public class Modeller implements Observer, TurnListener {
	
	private static int directionNumber;
	private final EnvModel envModel;
	private final RenderedModel rendModel;
	
	public enum EDirection {UP, RIGHT, DOWN, LEFT;}
	
	public Modeller(){
		this(new EnvModel(51));
	}

	public Modeller(EnvModel envModel) {
		this.envModel = envModel;
		envModel.initRobotPresence();
		rendModel = new RenderedModel(envModel);
		directionNumber = 1;
	}
	
	@Override
	public void onTurnInitialized(int angle) {
		changeDirection(angle);
	}

	@Override
	public synchronized void update(Observable arg0, Object arg1) {
		if (!(arg1 instanceof SensorHandler))
			return;
		envModel.moveRobotPresence(getDirectionEnum());
		drawSurroundings(getDirectionEnum(), (SensorHandler)arg1);
		System.out.println(envModel);
	}
	
	public synchronized void drawSurroundings(EDirection directionEnum, SensorHandler sensorHandler) {
		HashMap<ObservableSensor, ISensorStates> distanceSensors = sensorHandler.getDistanceSensorStates();
		HashMap<ObservableSensor, ISensorStates> lightSensors = sensorHandler.getDistanceSensorStates();
		IndexPair currentPosition = envModel.locateRobot();
		
		for (ObservableSensor sensor : distanceSensors.keySet()) {
			IndexPair positionToDraw = envModel.findPositionFromSensorEnum(directionEnum, currentPosition, sensor.getSensor());
			if (positionToDraw == null)
				continue;
			ISensorStates sensorState = distanceSensors.get(sensor);
			System.out.println(sensor.getSensor() + sensorState.getClass().getName());
			if (sensorState instanceof DistanceState_Clear)
				envModel.setCell(positionToDraw.row(), positionToDraw.col(), ECellContent.CLEAR);
			else if (sensorState instanceof DistanceState_Obstacle)
				envModel.setCell(positionToDraw.row(), positionToDraw.col(), ECellContent.OBSTACLE);
		}
	}
	
	public EDirection getDirectionEnum(){
		switch (directionNumber) {
		case 0:
			return EDirection.UP;
		case 1:
			return EDirection.RIGHT;
		case 2:
			return EDirection.DOWN;
		case 3:
			return EDirection.LEFT;
		default:
			return null;
		}
	}
	
	private void changeDirection(int angle){
		int direction = getDirection();
		direction += angle/90;
		direction %= 4;
		if (direction < 0)
			direction += 4;
		setDirection(direction);
	}
	
	private int getDirection(){
		return directionNumber;
	}
	
	private void setDirection(int direction){
		directionNumber = direction;
	}

}
