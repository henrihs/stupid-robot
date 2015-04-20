package edu.wsu.management;

import java.util.ListIterator;
import java.util.Stack;

import edu.wsu.modelling.EDirection;
import edu.wsu.modelling.IndexPair;
import edu.wsu.robot.IRobotStates;
import edu.wsu.robot.Robot;
import edu.wsu.robot.RobotState_InitTurn;

public class Order {

	private Stack<IndexPair> path;
	private EDirection direction;
	private int length;
	private IndexPair expectedEnd;
	
	public Order(Stack<IndexPair> path) {
		this.path = path;
		this.length = 1;
		this.direction = null;
		generateOrder();
	}
	
	public String toString() {
		return "Should travel " + length + " times " + direction + " and end up at " + expectedEnd;
	}
	
	/**
	 * @return int: Length of the order
	 */
	public int getLength() {
		return length;
	}
	
	/**
	 * @return IndexPair: Expected end of the order
	 */
	public IndexPair getExpectedEnd() {
		return expectedEnd;
	}
	
	/**
	 * @return EDirection: Direction of the order
	 */
	public EDirection getDiretion() {
		return direction;
	}
	
	/**
	 * Generates the order based on the path given in constructor.
	 */
	private void generateOrder() {
		ListIterator li = path.listIterator(path.size());
	
		IndexPair next;
		IndexPair previous = null;
		
		while (li.hasPrevious()) {
			next = (IndexPair) li.previous();
			if (previous != null) {
				if (direction == null) {
					direction = getDirection(previous, next);
				} else {
					if (getDirection(previous, next).equals(direction)) {
						length++;
					} else {
						break;
					}
				}
			}
			previous = next;
		}
		this.expectedEnd = previous;
	}
	
	/**
	 * Finds what direction the path is going based on two coordinates
	 * @param from
	 * @param to
	 * @return EDirection: Direction the path is moving
	 */
	private EDirection getDirection(IndexPair from, IndexPair to) {
		if (from.row() < to.row())
			return EDirection.DOWN;
		else if (from.row() > to.row())
			return EDirection.UP;
		else if (from.col() < to.col())
			return EDirection.RIGHT;
		else if (from.col() > to.col())
			return EDirection.LEFT;
		else
			return null;
	}

	/**
	 * Checks if current position is same as expected position
	 * @param currentPosition
	 * @return
	 */
	public boolean Success(IndexPair currentPosition) {
		return (currentPosition.row() == expectedEnd.row() &&
				currentPosition.col() == expectedEnd.col());
	}
}