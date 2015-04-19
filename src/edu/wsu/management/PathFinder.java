package edu.wsu.management;

import java.util.Stack;

import edu.wsu.modelling.ECellContent;
import edu.wsu.modelling.EnvModel;
import edu.wsu.modelling.IndexPair;

public class PathFinder {

	private Stack<IndexPair> found;
	private Stack<IndexPair> path;
	private String[] directions;
	private int[][] distanceMap;
	private EnvModel envModel;
	private IndexPair destination;

	public PathFinder(EnvModel envModel) {
		this.envModel = envModel;
		found = new Stack<IndexPair>();
		path = new Stack<IndexPair>();
		initDistanceMap();
		initDirections();
	}
		
	public Stack<IndexPair> pathTo(IndexPair destination) {
		this.destination = destination;
		IndexPair robot = envModel.locateRobot();
		setDistanceCell(robot, 0);
		findShortestPath(findDestination(robot));
		return path;
	}
	
	@Override
	public String toString() {
		String s = "";
		for (int[] row: distanceMap) {
			for (int col: row) {
				if (col > -1) {
					s += Integer.toString(col);
				} else {
					s += " ";
				}
			}
			s += "\n";
		}
		return s;
	}
	
	private void initDirections() {
		directions = new String[4];
		directions[0] = "top";
		directions[1] = "right";
		directions[2] = "bottom";
		directions[3] = "left";		
	}
	
	private void initDistanceMap() {
		distanceMap = new int[envModel.getModelSize()][envModel.getModelSize()];
		for (int col = 0; col < distanceMap.length; col++) {
			for (int row = 0; row < distanceMap.length; row++) {
				distanceMap[col][row] = -1;
			}
		}
	}
	
	/**
	 * Finds coordinates of the neighbour cell
	 * @param direction of the main cell
	 * @param cell with coordinates
	 * @return
	 */
	private IndexPair getNeighbour(String direction, IndexPair cell) {
		switch (direction) {
			case "top":
				return new IndexPair(cell.row(), cell.col() - 1);
			case "right":
				return new IndexPair(cell.row() + 1, cell.col());
			case "bottom":
				return new IndexPair(cell.row(), cell.col() + 1);
			case "left":
				return new IndexPair(cell.row() - 1, cell.col());
			default:
				return null;
		}
	}
	
	/**
	 * Checks if the given cell is clear
	 * @param cell
	 * @return
	 */
	private boolean cellIsClear(IndexPair cell) {
		return (envModel.getCell(cell.row(), cell.col()).getContent() != ECellContent.OBSTACLE);
	}
	
	/**
	 * Checks if the given cell is already discovered
	 * @param cell
	 * @return
	 */
	private boolean cellIsDiscovered(IndexPair cell) {
		return (distanceMap[cell.row()][cell.col()] != -1);
	}
	
	/**
	 * Checks if the given cell is the destination wanted
	 * @param cell
	 * @return
	 */
	private boolean cellIsDestination(IndexPair cell) {
		return (cell.row() == destination.row() &&
				cell.col() == destination.col());
	}
	
	/**
	 * Returns the value of the given cell
	 * @param cell
	 * @return
	 */
	private int getDistanceCellValue(IndexPair cell) {
		return distanceMap[cell.row()][cell.col()];
	}
	
	/**
	 * Sets a value to the given cell
	 * @param cell
	 * @param value
	 */
	private void setDistanceCell(IndexPair cell, int value) {
		distanceMap[cell.row()][cell.col()] = value;
	}
	
	/**
	 * Adds the cell to the list of discovered cells
	 * @param neighbour
	 */
	private void addToFound(IndexPair cell) {
		found.add(cell);
	}
	
	/**
	 * Checks all neighbours of the given cell
	 * @param cell
	 * @return
	 */
	private IndexPair checkNeighbours(IndexPair cell) {
		int value = getDistanceCellValue(cell) + 1;
		for (String dir: directions) {
			IndexPair neighbour = getNeighbour(dir, cell);
			if (!cellIsDiscovered(neighbour)) {
				if (cellIsClear(neighbour)) {
					setDistanceCell(neighbour, value);
					addToFound(neighbour);
				}
			}
			if (cellIsDestination(neighbour)) {
				return neighbour;
			}
		}
		return null;
	}
		
	/**
	 * Finds coordinates of the nearest ball.
	 * @param robot
	 * @return
	 */
	private IndexPair findDestination(IndexPair robot) {
		found.add(robot);
		IndexPair destinationFound;
		while (true) {
			Stack<IndexPair> next = (Stack<IndexPair>) found.clone();
			for (IndexPair n: next) {
				destinationFound = checkNeighbours(n);
				if (destinationFound != null) {
					return destinationFound;
				}
			}
		}
	}
	
	/**
	 * Checks which of the neighbours has the lowest value
	 * @param neighour
	 * @return
	 */
	private boolean checkLowestNeighbour(IndexPair neighbour) {
		int lowest = 10000;
		IndexPair lowest_neighbour = null;
		for (String dir: directions) {
			IndexPair next_neighbour = getNeighbour(dir, neighbour);
			int distance = getDistanceCellValue(next_neighbour);
			if (distance < lowest && distance > -1) {
				lowest = getDistanceCellValue(next_neighbour);
				lowest_neighbour = next_neighbour;
			}
		}
		path.add(lowest_neighbour);
		
		if (lowest == 0)
			return true;
		return false;
	}

	/**
	 * Finds shortest path to start of path (by reversing)
	 * @param destinationFound
	 */
	private void findShortestPath(IndexPair destinationFound) {
		path.add(destinationFound);
		boolean pathFound;
		while (true) {
			pathFound = checkLowestNeighbour(path.lastElement());
			if (pathFound) {
				break;
			}
		}
	}
}
