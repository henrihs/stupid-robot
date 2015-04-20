package edu.wsu.management;

import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import edu.wsu.modelling.EDirection;
import edu.wsu.modelling.EnvModel;
import edu.wsu.modelling.IndexPair;
import edu.wsu.robot.IRobotStates;
import edu.wsu.robot.RobotState_Drive;
import edu.wsu.robot.RobotState_InitTurn;
import edu.wsu.robot.RobotState_Stop;

public class GPS extends Observable implements Observer, StateCompleteListener {
	
	private EnvModel envModel;
	private PathFinder pathFinder;
	private LinkedList<IRobotStates> stateQueue;
	private IndexPair destination;
		
	public GPS() {
		stateQueue = new LinkedList<IRobotStates>();
	}
	
	public void init(EnvModel envModel) {
		this.envModel = envModel;
		pathFinder = new PathFinder(envModel);		
	}
	
	@Override
	public synchronized void update(Observable o, Object arg) {
		if (hasDestination()) {
			if (robotAtDestination()) {
				setDestination(null);
				stateQueue.clear();
				stateQueue.add(new RobotState_Stop());
			} else if (stateQueue.isEmpty()) {
				addToQueue(pathToDestination());
			}
		} else {
			Order order = pathToUnknown();
			setDestination(order.getExpectedEnd());
			addToQueue(order);
		}
		
		setChanged();
		notifyObservers(stateQueue.pollFirst());
		System.out.println(envModel);
	}
	
	public void setDestination(IndexPair destination) {
		this.destination = destination;
	}
	
	private void addToQueue(Order order) {
		int angle = getTurnAngle(order);
		if (angle != 0) {
			stateQueue.add(new RobotState_InitTurn(angle));
		}
		for (int i = 0; i < order.getLength(); i++) {
			stateQueue.add(new RobotState_Drive());
		}
		stateQueue.add(new RobotState_Stop());
	}
	
	private boolean hasDestination() {
		return (destination != null);
	}
	
	private boolean robotAtDestination() {
		return (destination.row() == envModel.locateRobot().row() &&
				destination.col() == envModel.locateRobot().col());
	}
	
	private Order pathToDestination() {
		return new Order(pathFinder.pathTo(destination));
	}
	
	private Order pathToUnknown() {
		return new Order(pathFinder.pathToUnknown());
	}
	
	private int getTurnAngle(Order order) {
		int directionDiff = (order.getDiretion().value() - envModel.getRobotDirection().value());
		return EDirection.moduloValue(directionDiff) * 90;
	}

	@Override
	public void onStateCompleted() {
		update(null, null);
	}
}
