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
	
	public Order(Stack<IndexPair> path) {
		this.path = path;
		this.length = 1;
		this.direction = null;
		generateOrder();
	}
	
	public String toString() {
		return "Should travel " + length + " times " + direction + " and end up at " + expectedEnd;
	}
	
	public int getLength() {
		return length;
	}
	
	public IndexPair getExpectedEnd() {
		return expectedEnd;
	}
	
	public EDirection getDiretion() {
		return direction;
	}
	
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

	public boolean Success(IndexPair currentPosition) {
		return (currentPosition.row() == expectedEnd.row() &&
				currentPosition.col() == expectedEnd.col());
	}
}