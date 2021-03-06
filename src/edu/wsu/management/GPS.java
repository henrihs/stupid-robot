package edu.wsu.management;

import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import edu.wsu.modelling.EnvModel;
import edu.wsu.modelling.IndexPair;
import edu.wsu.robot.IRobotStates;
import edu.wsu.robot.RobotState_Drive;
import edu.wsu.robot.RobotState_DropBall;
import edu.wsu.robot.RobotState_InitTurn;
import edu.wsu.robot.RobotState_PickupBall;
import edu.wsu.robot.RobotState_Stop;

public class GPS extends Observable implements Observer, StateCompleteListener {
	
	private EnvModel envModel;
	private PathFinder pathFinder;
	private LinkedList<IRobotStates> stateQueue;
	private IndexPair destination;
	private boolean isBallHeld;
	private IndexPair ballStart;
		
	public GPS() {
		stateQueue = new LinkedList<IRobotStates>();
	}
	
	public void init(EnvModel envModel) {
		this.envModel = envModel;
		isBallHeld = false;
		pathFinder = new PathFinder(envModel);
	}
	
	@Override
	public void update(Observable obs, Object arg) {
		if (obs instanceof GPS) {
			isBallHeld = (boolean) arg;	
		}
		if (hasDestination()) {
			if (robotAtDestination()) {
				setDestination(null);
				stateQueue.clear();
				stateQueue.add(new RobotState_Stop());
			} else if (stateQueue.isEmpty()) {
				addToQueue(pathToDestination());
			}
		} else if (envModel.getBallReady() && !isBallHeld) {
			Order order = pathToBall(true);
			setDestination(order.getExpectedEnd());
			ballStart = order.getExpectedEnd();
			addToQueue(order);
		} else if (isBallHeld) {
			setDestination(envModel.getBallDestination());
			Order order = pathToDestination();
			addToQueue(order);
		} else {
			Order order = pathToUnknown();
			setDestination(order.getExpectedEnd());
			addToQueue(order);
		}
		
		IRobotStates nextState = stateQueue.pollFirst();
		if (nextState instanceof RobotState_Drive) {
			if (envModel.obstacleInFront()) {
				if (envModel.destinationInFront(destination)) {
					destination = null;
				}
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
		int angle = getTurnAngle(order);
		if (angle != 0) {
			envModel.parseMap();
			stateQueue.add(new RobotState_InitTurn(angle));
		}
		if (order.isBallOrder() || (isBallHeld && order.getExpectedEnd().equals(envModel.getBallDestination()))) {
			for (int i = 0; i < order.getLength() - 1; i++) {
				stateQueue.add(new RobotState_Drive());
			}			
		} else {
			for (int i = 0; i < order.getLength(); i++) {
				stateQueue.add(new RobotState_Drive());
			}
		}

		stateQueue.add(new RobotState_Stop());
		if (order.isBallOrder() && order.getExpectedEnd().equals(order.getFinalDestination())) {
			stateQueue.add(new RobotState_PickupBall());
		}
		else if (isBallHeld && order.getExpectedEnd().equals(envModel.getBallDestination())) {
			stateQueue.add(new RobotState_DropBall());
		}
		
		if (isBallHeld && ballStart != null) {
			envModel.removeBall(ballStart);
			ballStart = null;
		}
	}
	
	private boolean hasDestination() {
		return (destination != null);
	}
	
	private boolean robotAtDestination() {
		try {
			if (!hasDestination())
				return false;
			return (destination.equals(envModel.locateRobot()));
		} catch (NullPointerException e) {
			return false;
		}
	}
	
	private Order pathToDestination() {
		return new Order(pathFinder.pathTo(destination));
	}
	
	private Order pathToUnknown() {
		return new Order(pathFinder.pathToUnknown());
	}
	
	private Order pathToBall(boolean ballOrder) {
		return new Order(pathFinder.pathToBall(), ballOrder);
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
	public void onStateCompleted(boolean isBallHeld) {
		update(this, isBallHeld);
	}
}
