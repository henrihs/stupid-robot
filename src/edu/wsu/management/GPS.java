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

public class GPS extends Observable implements Observer {
	
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
	public void update(Observable o, Object arg) {
		if (stateQueue.isEmpty()) {
			
		} else {
			
		}
		
		
		
//		IRobotStates nextState = null;
//		if (destination != null) {
//			if (robotAtDestination()) {
//				destination = null;
//				nextState = new RobotState_Stop();
//			} else {
//				Order order = pathToDestination();
//				int angle = getTurnAngle(order);
//				if (angle == 0) {
//					nextState = new RobotState_Drive();
//				} else {
//					nextState = new RobotState_InitTurn(angle);
//				}
//			}
//				
//		}
//		if (nextState != null) {
//			setChanged();
//			notifyObservers(nextState);
//		}
	}
	
	public void setDestination(IndexPair destination) {
		this.destination = destination;
	}
	
	private boolean robotAtDestination() {
		return (destination.row() == envModel.locateRobot().row() &&
				destination.col() == envModel.locateRobot().col());
	}
	
	private Order pathToDestination() {
		return new Order(pathFinder.pathTo(destination));
	}
	
	// TODO: Implement this.
	private Order pathToUnknown() {
		return new Order(pathFinder.pathToUnknown());
	}
	
	private int getTurnAngle(Order order) {
		int directionDiff = (order.getDiretion().value() - envModel.getRobotDirection().value());
		return EDirection.moduloValue(directionDiff) * 90;
	}
}
