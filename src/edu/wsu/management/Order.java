package edu.wsu.management;

import java.util.ListIterator;
import java.util.Stack;

import edu.wsu.modelling.IndexPair;

public class Order {

	private Stack<IndexPair> path;
	private String direction;
	private int length;
	private IndexPair expectedEnd;
	
	public Order(Stack<IndexPair> path) {
		this.path = path;
		this.length = 1;
		this.direction = null;
		generateOrder();
	}
	
	public String toString() {
		return "Travel " + length + "00 ticks " + direction + " and end up at " + expectedEnd;
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
	
	private String getDirection(IndexPair from, IndexPair to) {
		if (from.row() < to.row())
			return "down";
		else if (from.row() > to.row())
			return "up";
		else if (from.col() < to.col())
			return "right";
		else if (from.col() > to.col())
			return "left";
		else
			return null;
	}

	public boolean Success(IndexPair currentPosition) {
		return (currentPosition.row() == expectedEnd.row() &&
				currentPosition.col() == expectedEnd.col());
	}
}