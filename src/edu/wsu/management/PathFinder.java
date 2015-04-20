package edu.wsu.management;

import java.util.Stack;

import edu.wsu.modelling.ECellContent;
import edu.wsu.modelling.EDirection;
import edu.wsu.modelling.EnvModel;
import edu.wsu.modelling.IndexPair;

public class PathFinder {

	private Stack<IndexPair> found;
	private Stack<IndexPair> path;
	private int[][] distanceMap;
	private EnvModel envModel;
	private IndexPair destination;
	private boolean lookingForUnknown;
	private Stack<EDirection> directions;

	public PathFinder(EnvModel envModel) {
		this.envModel = envModel;
		found = new Stack<IndexPair>();
		path = new Stack<IndexPair>();
	}
		
	public Stack<IndexPair> pathTo(IndexPair destination) {
		init();
		this.destination = destination;
		lookingForUnknown = false;
		IndexPair robot = envModel.locateRobot();
		setDistanceCell(robot, 0);
		findShortestPath(findDestination(robot));
		return path;
	}
	
	public Stack<IndexPair> pathToUnknown() {
		init();
		destination = null;
		lookingForUnknown = true;
		IndexPair robot = envModel.locateRobot();
		setDistanceCell(robot, 0);
		findShortestPath(findDestination(robot));
		return path;
	}
	
	private void init() {
		initDistanceMap();
		initDirections();
		found.clear();
		path.clear();		
	}
		
	private void initDirections() {
		directions = new Stack<EDirection>();
		directions.add(envModel.getRobotDirection());
		for (EDirection dir: EDirection.values()) {
			if (!directions.contains(dir)) {
				directions.add(dir);
			}
		}
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
	private IndexPair getNeighbourCell(EDirection direction, IndexPair cell) {
		switch (direction) {
			case UP:
				return new IndexPair(cell.row(), cell.col() - 1);
			case RIGHT:
				return new IndexPair(cell.row() + 1, cell.col());
			case DOWN:
				return new IndexPair(cell.row(), cell.col() + 1);
			case LEFT:
				return new IndexPair(cell.row() - 1, cell.col());
			default:
				return null;
		}
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
	 * Checks if the given cell is clear
	 * @param cell
	 * @return
	 */
	private boolean cellIsClear(IndexPair cell) {
		return (envModel.getCellContent(cell) == ECellContent.CLEAR);
	}
	
	/**
	 * Checks if the given cell is already discovered
	 * @param cell
	 * @return
	 */
	private boolean cellIsDiscovered(IndexPair cell) {
		return (getDistanceCellValue(cell) != -1);
	}
	
	/**
	 * Checks if the given cell is the destination wanted
	 * @param cell
	 * @return
	 */
	private boolean cellIsDestination(IndexPair cell) {
		if (destination == null)
			return false;		
		return (cell.row() == destination.row() && cell.col() == destination.col());

	}
	
	private boolean cellIsUnknown(IndexPair cell) {
		return (lookingForUnknown && envModel.getCellContent(cell) == ECellContent.UNKNOWN);
	}	
	
	/**
	 * Checks all neighbours of the given cell
	 * @param cell
	 * @return
	 */
	private IndexPair parseNeighbourCells(IndexPair cell) {
		int value = getDistanceCellValue(cell) + 1;
		
		for (EDirection dir: directions) {
			IndexPair neighbour = getNeighbourCell(dir, cell);
						
			if (!cellIsDiscovered(neighbour) && cellIsClear(neighbour)) {
				setDistanceCell(neighbour, value);
				addToFound(neighbour);
			}
			
			if (cellIsDestination(neighbour) || cellIsUnknown(neighbour)) {
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
		addToFound(robot);
		IndexPair cellFound;
		while (true) {
			Stack<IndexPair> next = (Stack<IndexPair>) found.clone();
			found.clear();
			for (IndexPair n: next) {
				cellFound = parseNeighbourCells(n);
				if (cellFound != null) {
					return cellFound;
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
		for (EDirection dir: directions) {
			IndexPair next_neighbour = getNeighbourCell(dir, neighbour);
			int distance = getDistanceCellValue(next_neighbour);
			if (distance < lowest && distance > -1) {
				lowest = distance;
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
	
	@Override
	public String toString() {
		String s = "";
		int rowCounter = -1;
		int colCounter = -1;
		for (int[] row: distanceMap) {
			rowCounter++;
			colCounter = -1;
			for (int col: row) {
				colCounter++;
				if (col > 0 && col < 10) {
					s += "  " + Integer.toString(col);
				} else if (col > 10) {
					s += " " + Integer.toString(col);
				} else if (col == 0) {
					s += "  R";
				} else {
					s += "  " + envModel.getCellContent(new IndexPair(rowCounter, colCounter));
				}
			}
			s += "\n";
		}
		return s;
	}	
}
