//package edu.wsu.robot;
//
//import edu.wsu.modelling.IndexPair;
//import edu.wsu.modelling.EDirection;
//
//public class RobotState_FollowOrder implements IRobotStates {
//	
//	private IOrder order;
//	private EDirection initialDirection;
//	private boolean firstIteration;
//
//	public RobotState_FollowOrder(IOrder order, EDirection currentDirectionOfRobot) {
//		this.order = order;
//		this.initialDirection = currentDirectionOfRobot;
//		this.firstIteration = true;
//	}
//
//	@Override
//	public void doWork(Robot robot) {
//		/*
//		 * TODO: 
//		 * - 	Figure out how the state should be alerted when 
//		 * 		robot reaches Orders destination.
//		 * -	How should next state be determined?
//		 */
//		
//		if (order.orderEnded()){
//			robot.stop();
//			return;
//		}
//		if (firstIteration){
//			adjustDirectionToOrder(robot);
//		}
//		robot.drive();			
//	}
//	
//	private void adjustDirectionToOrder(Robot robot) {
//		int directionDiff = (order.getDirection().value() - initialDirection.value());
//		directionDiff = EDirection.moduloValue(directionDiff);
//		int angle = directionDiff * 90;
//		IRobotStates initTurnState = new RobotState_InitTurn(angle);
//		robot.setNextState(initTurnState);
//		robot.appendStateToQueue(this);
//		robot.pollNextState();
//	}
//
//
//	interface IOrder {
//		public EDirection getDirection();
//		public IndexPair getExpectedEnd();
//		public boolean orderEnded();
//	}
//
//}
