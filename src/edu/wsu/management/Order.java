package edu.wsu.management;

import java.util.ListIterator;
import java.util.Stack;

import edu.wsu.modelling.EDirection;
import edu.wsu.modelling.IndexPair;

public class Order {

	private Stack<IndexPair> path;
	private EDirection direction;
	private int length;
	private IndexPair expectedEnd;
	private IndexPair finalDestination;
	private boolean ballOrder;
	
	public Order(Stack<IndexPair> path) {
		this.ballOrder = false;
		this.path = path;
		this.length = 1;
		this.direction = null;
		generateOrder();
	}
	
	public Order(Stack<IndexPair> path, boolean ballOrder) {
		this.ballOrder = ballOrder;
		this.path = path;
		this.length = 1;
		this.direction = null;
		generateOrder();
	}
	
	public IndexPair getFinalDestination() {
		return finalDestination;
	}
	
	public boolean isBallOrder() {
		return ballOrder;
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
	
	public Stack<IndexPair> getPath() {
		return path;
	}
	
	/**
	 * Generates the order based on the path given in constructor.
	 */
	private void generateOrder() {
		finalDestination = path.firstElement();
		ListIterator<IndexPair> li = path.listIterator(path.size());
	
		IndexPair next;
		IndexPair previous = null;
		
		while (li.hasPrevious()) {
			next = (IndexPair) li.previous();
			if (previous != null) {
				if (direction == null) {
					direction = findDirection(previous, next);
				} else {
					if (findDirection(previous, next).equals(direction)) {
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
	private EDirection findDirection(IndexPair from, IndexPair to) {
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
}