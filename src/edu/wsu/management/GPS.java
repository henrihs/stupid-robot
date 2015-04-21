package edu.wsu.management;

import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import edu.wsu.modelling.ECellContent;
import edu.wsu.modelling.EnvModel;
import edu.wsu.modelling.IndexPair;
import edu.wsu.robot.IRobotStates;
import edu.wsu.robot.RobotState_Drive;
import edu.wsu.robot.RobotState_InitTurn;
import edu.wsu.robot.RobotState_Stop;
import edu.wsu.sensors.distance.EDistanceSensorState;

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
	public void update(Observable obs, Object arg) {
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
		
		IRobotStates nextState = stateQueue.pollFirst();
		if (nextState instanceof RobotState_Drive) {
			if (envModel.getCellContentInFront() == ECellContent.OBSTACLE) {
				stateQueue.clear();
				setChanged();
				notifyObservers(new RobotState_Stop());
				return;
			}
		}
		setChanged();
		notifyObservers(nextState);
	}
	
	public void setDestination(IndexPair destination) {
		this.destination = destination;
	}
	
	private void addToQueue(Order order) {
		System.out.println("Robot is at (" + envModel.locateRobot().row() + ", " + envModel.locateRobot().col() + ")");
		System.out.println("Order is " + order.getLength() + " times " + order.getDiretion() + " to (" + order.getExpectedEnd().row() + ", " + order.getExpectedEnd() + ")");
		

		int angle = getTurnAngle(order);
		if (angle != 0) {
			stateQueue.add(new RobotState_InitTurn(angle));
		}
		for (int i = 0; i < order.getLength(); i++) {
			stateQueue.add(new RobotState_Drive());
		}
		stateQueue.add(new RobotState_Stop());
		System.out.println(stateQueue);
		System.out.println(pathFinder);
	}
	
	private boolean hasDestination() {
		return (destination != null);
	}
	
	private boolean robotAtDestination() {
		if (!hasDestination())
			return false;
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
		if (directionDiff < -1)
			directionDiff +=4;
		if (directionDiff > 2)
			directionDiff -= 4;
		return directionDiff * 90;
	}

	@Override
	public void onStateCompleted() {
		update(null, null);
	}
}
