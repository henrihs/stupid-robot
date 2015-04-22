package edu.wsu.modelling;

import java.util.Stack;
import static common.PropertyReader.*;

public class Cluster {

	private Stack<IndexPair> cluster;
	private Stack<IndexPair> next;
	private int max_distance;
	private ECellContent cluster_type;
	private EnvModel envModel;
	
	public Cluster(EnvModel envModel, IndexPair cell) {
		this.envModel = envModel;
		cluster = new Stack<IndexPair>();
		next = new Stack<IndexPair>();
		next.add(cell);
		max_distance = 0;
	}
	
	public void run() {
		while (!next.isEmpty()) {
			parseNext();
			setMaxDistance();
			if (!valid()) {
				break;
			}
		}
		if (valid()) {
			setClusterType();
		}
	}
	
	private boolean cellAlreadyFound(IndexPair cell) {
		for (IndexPair x: cluster) {
			if (x.equals(cell))
				return true;
		}
		for (IndexPair x: next) {
			if (x.equals(cell))
				return true;
		}
		return false;
	}
	
	private void parseNext() {
		Stack<IndexPair> found = new Stack<IndexPair>();
		ECellContent content;
		for (IndexPair cell: next) {
			cluster.add(cell);
			for (IndexPair neighbour: envModel.getNeighbourCells(cell)) {
				if (neighbour != null) {
					if (!cellAlreadyFound(neighbour)) {
						content = envModel.getCellContent(neighbour);
						if (content == ECellContent.UNKNOWN || content == ECellContent.OBSTACLE) {
							found.add(neighbour);
						}
					}
				}
			}
		}
		next.clear();
		next = (Stack<IndexPair>) found.clone();
	}
	
	private int getDistance(IndexPair from, IndexPair to) {
		return (int) Math.sqrt(Math.pow(from.row() - to.row(), 2) + Math.pow(from.col() - to.col(), 2));
	}
	
	private boolean valid() {
		return max_distance <= getUnknownClusterSize();
	}
	
	private void setMaxDistance() {
		int distance;
		for (IndexPair from: cluster) {
			for (IndexPair to: cluster) {
				distance = getDistance(from, to);
				if (distance > max_distance) {
					max_distance = distance;
				}
			}
		}
	}
	
	private void setClusterType() {
		if (max_distance == getUnknownClusterSize())
			cluster_type = ECellContent.UNKNOWN;
		else if (max_distance == getBallClusterSize())
			cluster_type = ECellContent.BALL;
		else
			cluster_type = ECellContent.CLEAR;

		for (IndexPair cell: cluster) {
			envModel.setCell(cell, cluster_type);
		}
	}
}
