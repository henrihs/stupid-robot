package edu.wsu.modelling;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Stack;

import edu.wsu.sensors.ESensor;
import edu.wsu.sensors.SensorHandler;
import edu.wsu.sensors.distance.EDistanceSensorState;
import edu.wsu.sensors.light.ELightSensorState;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class EnvModel extends Observable implements TableModel {

	private static EDirection currentRobotDirection;
	protected final int modelSize;
	private final Cell[][] envModelCells;
	private List<TableModelListener> listeners = new ArrayList<TableModelListener>();

	public EnvModel(int modelSize){
		this.modelSize = modelSize;
		envModelCells = new Cell[modelSize][];
		for (int row = 0; row < modelSize; row++) {
			envModelCells[row] = new Cell[modelSize];
			for (int col = 0; col < modelSize; col++) {
				envModelCells[row][col] = new Cell();
			}
		}
		currentRobotDirection = EDirection.RIGHT;
	}
	
	public int getModelSize() {
		return modelSize;
	}
	
	public boolean obstacleInFront() {
		if (findPositionInFront(currentRobotDirection, locateRobot()) == null)
			return false;
		return (getCellContent(findPositionInFront(currentRobotDirection, locateRobot())) == ECellContent.OBSTACLE);
	}
	
	public boolean destinationInFront(IndexPair destination) {
		for (IndexPair cell: getFrontRow()) {
			if (cell.equals(destination)) {
				return true;
			}
		}
		return false;
	}
	
	private Stack<IndexPair> getFrontRow() {
		EDirection direction = getRobotDirection();
		IndexPair robot = locateRobot();
		Stack<IndexPair> row = new Stack<IndexPair>();
		switch (direction) {
			case UP:
				row.add(new IndexPair(robot.row() - 1, robot.col() - 1));
				row.add(new IndexPair(robot.row() - 1, robot.col()));
				row.add(new IndexPair(robot.row() - 1, robot.col() + 1));
				break;
			case RIGHT:
				row.add(new IndexPair(robot.row() - 1, robot.col() + 1));
				row.add(new IndexPair(robot.row(), robot.col() + 1));
				row.add(new IndexPair(robot.row() + 1, robot.col() + 1));
				break;
			case DOWN:
				row.add(new IndexPair(robot.row() + 1, robot.col() + 1));
				row.add(new IndexPair(robot.row() + 1, robot.col()));
				row.add(new IndexPair(robot.row() + 1, robot.col() - 1));
				break;
			case LEFT:
				row.add(new IndexPair(robot.row() + 1, robot.col() - 1));
				row.add(new IndexPair(robot.row(), robot.col() - 1));
				row.add(new IndexPair(robot.row() - 1, robot.col() - 1));
				break;
		}
		return row;
		
	}
	
	/**
	 * Sets robot's presence to the middlemost <code>Cell</code> in the envModelCells
	 */
	public void initRobotPresence(){
		moveRobotPresence(modelSize/2, modelSize/2);
	}
	
	/**
	 * Moves robot's presence one step in the desired direction
	 * @param direction to move robot
	 */
	public synchronized void moveRobotPresence(){
		IndexPair currentPosition = locateRobot();
		IndexPair nextPosition = findPositionInFront(getRobotDirection(), currentPosition);
		moveRobotPresence(nextPosition.row(), nextPosition.col());
//		setChanged();
	}
	
	public EDirection getRobotDirection(){
		return currentRobotDirection;
	}
	
	private void setRobotDirectionValue(int value) {
		switch (value) {
		case 0:
			currentRobotDirection = EDirection.UP;
			break;
		case 1:
			currentRobotDirection = EDirection.RIGHT;
			break;
		case 2:
			currentRobotDirection = EDirection.DOWN;
			break;
		case 3:
			currentRobotDirection = EDirection.LEFT;
			break;
		}
	}
	
	protected void changeRobotDirection(int angle){
		int directionValue = getRobotDirection().value();
		directionValue += angle/90;
		directionValue = EDirection.moduloValue(directionValue);
		setRobotDirectionValue(directionValue);
//		setChanged();
//		notifyObservers();
	}
		
	public IndexPair findPositionFromSensorEnum(IndexPair currentPosition, ESensor sensor) {
		IndexPair position = null;
		EDirection direction = getRobotDirection();
		switch (sensor) {
		case FRONTL:
			position =  findPositionInFront(direction, currentPosition);
			break;
		case FRONTR:
			position = findPositionInFront(direction, currentPosition);
			break;
		case LEFT:
			position = findPositionToLeft(direction, currentPosition);
			break;
		case RIGHT:
			position = findPositionToRight(direction, currentPosition);
			break;
		case ANGLEL:
			position = findPositionToLeftAngle(direction, currentPosition);
			break;
		case ANGLER:
			position = findPositionToRightAngle(direction, currentPosition);
			break;
		case BACKL:
			position = findPositionToRear(direction, currentPosition);
			break;
		case BACKR:
			position = findPositionToRear(direction, currentPosition);
			break;
		default:
			break;
		}
		return position;
	}
	
	/**
	 * @return IndexPair Row and column position of cell with robot's presence
	 */
	public IndexPair locateRobot(){
		for (int row = 0; row < envModelCells.length; row++) {
			for (int col = 0; col < envModelCells[row].length; col++) {
				if (getCell(row, col).isRobotPresent)
					return new IndexPair(row, col);
			}
		}
		return null;
	}
	
	public void setCell(IndexPair indexPair, ECellContent content) {
		setCell(indexPair.row(), indexPair.col(), content);
	}
	
	public void setCell(IndexPair indexPair, ECellContent content, ELightSensorState lightState) {
		setCell(indexPair.row(), indexPair.col(), content, lightState);
	}
	
	public void setCell(IndexPair indexPair, ELightSensorState lightIntensity) {
		setCell(indexPair.row(), indexPair.col(), lightIntensity);
	}
	
	/**
	 * Set the content of a <code>Cell</code>
	 * @param row Index of cell row
	 * @param col Index of cell column
	 * @param content The content of the cell
	 */
	public void setCell(int row, int col, ECellContent content){
		setCell(row, col, content, null);
	}

	/**
	 * Set the light intensity in a <code>Cell</code>
	 * @param row Index of cell row
	 * @param col Index of cell column
	 * @param lightIntensity The light intensity of the cell
	 */
	public void setCell(int row, int col, ELightSensorState lightIntensity){
		setCell(row, col, null, lightIntensity);
	}

	/**
	 * Set the content and the light intensity in a <code>Cell</code>
	 * @param row Index of cell row
	 * @param col Index of cell column
	 * @param content The content of the cell
	 * @param lightIntensity The light intensity of the cell
	 */
	public void setCell(int row, int col, ECellContent content, ELightSensorState lightState){
//		IndexPair indices = adjustIndicesAndModel(row, col);
		IndexPair indices = new IndexPair(row, col); 
		row = indices.row();
		col = indices.col();
		if (content != null)
			envModelCells[row][col].setContent(content);
		if (lightState != null)
			envModelCells[row][col].setLightIntensity(lightState);
	}
	
	/**
	 * @param row Index of cell row
	 * @param col Index of cell column
	 * @return Cell in given position
	 */
	public Cell getCell(int row, int col){
		if (row >= modelSize ||
				col >= modelSize)
			return null;
		return envModelCells[row][col];
	}
	
	public ECellContent getCellContent(IndexPair pair) {
		return envModelCells[pair.row()][pair.col()].getContent();
	}
	
	/**
	 * Adjust the indices and the envModelCells so that it corresponds to the array size
	 * @param row Row index
	 * @param col Column index
	 * @return 
	 */
	private IndexPair adjustIndicesAndModel(int row, int col){
		if (row >= envModelCells.length - 1){
//			System.out.println("Upshift " + String.valueOf(row - (envModelCells.length - 1)));
			upShiftModel(1);
			row--;
		}
		else if (row < 1) {
//			System.out.println("Downshift " + String.valueOf(Math.abs(row)));
			downShiftModel(1);
			row++;
		}

		if (col >= envModelCells[row].length - 1){
//			System.out.println("Leftshift " + String.valueOf(col - (envModelCells[row].length - 1)));
			leftShiftModel(1);
			col--;
		}
		else if (col < 1) {
//			System.out.println("Rightshift " + String.valueOf(Math.abs(col)));
			rightShiftModel(1);
			col++;
		}
		return new IndexPair(row, col);
	}

	/**
	 * Find which position is one step ahead
	 * @param direction The direction of which way to look ahead
	 * @param currentPosition The position to look from
	 * @return
	 */
	public IndexPair findPositionInFront(EDirection direction, IndexPair currentPosition){
		IndexPair nextPosition = null;
		try {
			switch (direction) {
			case UP:
				nextPosition = new IndexPair(currentPosition.row() - 1, currentPosition.col());
				break;
			case RIGHT:
				nextPosition = new IndexPair(currentPosition.row(), currentPosition.col() + 1);
				break;
			case DOWN:
				nextPosition = new IndexPair(currentPosition.row() + 1, currentPosition.col());
				break;
			case LEFT:
				nextPosition = new IndexPair(currentPosition.row(), currentPosition.col() - 1);
				break;
			}
		} catch (IndexOutOfBoundsException e) {
			return null;
		} catch (NullPointerException e) {
			return null;
		}
		return nextPosition;
	}

	/** 
	 * Find which position is to the left
	 * @param direction
	 * @param currentPosition
	 * @return
	 */
	private IndexPair findPositionToLeft(EDirection direction, IndexPair currentPosition) {
		IndexPair nextPosition = null;
		switch (direction) {
		case UP:
			nextPosition = new IndexPair(currentPosition.row(), currentPosition.col() - 1);
			break;
		case RIGHT:
			nextPosition = new IndexPair(currentPosition.row() - 1, currentPosition.col());
			break;
		case DOWN:
			nextPosition = new IndexPair(currentPosition.row(), currentPosition.col() + 1);
			break;
		case LEFT:
			nextPosition = new IndexPair(currentPosition.row() + 1, currentPosition.col());
			break;
		}
//		nextPosition = adjustIndicesAndModel(nextPosition);
		return nextPosition;
	}
	
	/**
	 *  Find which position is to the right
	 * @param direction
	 * @param currentPosition
	 * @return
	 */
	private IndexPair findPositionToRight(EDirection direction, IndexPair currentPosition) {
		IndexPair nextPosition = null;
		switch (direction) {
		case UP:
			nextPosition = new IndexPair(currentPosition.row(), currentPosition.col() + 1);
			break;
		case RIGHT:
			nextPosition = new IndexPair(currentPosition.row() + 1, currentPosition.col());
			break;
		case DOWN:
			nextPosition = new IndexPair(currentPosition.row(), currentPosition.col() - 1);
			break;
		case LEFT:
			nextPosition = new IndexPair(currentPosition.row() - 1, currentPosition.col());
			break;
		}
//		nextPosition = adjustIndicesAndModel(nextPosition);
		return nextPosition;
	}
	
	/**
	 *  Find which position is to the left/front
	 * @param direction
	 * @param currentPosition
	 * @return
	 */
	private IndexPair findPositionToLeftAngle(EDirection direction, IndexPair currentPosition) {
		IndexPair nextPosition = null;
		switch (direction) {
		case UP:
			nextPosition = new IndexPair(currentPosition.row() - 1, currentPosition.col() - 1);
			break;
		case RIGHT:
			nextPosition = new IndexPair(currentPosition.row() - 1, currentPosition.col() + 1);
			break;
		case DOWN:
			nextPosition = new IndexPair(currentPosition.row() + 1, currentPosition.col() + 1);
			break;
		case LEFT:
			nextPosition = new IndexPair(currentPosition.row() + 1, currentPosition.col() - 1);
			break;
		}
//		nextPosition = adjustIndicesAndModel(nextPosition);
		return nextPosition;
	}
	
	/**
	 *  Find which position is to the right/front
	 * @param direction
	 * @param currentPosition
	 * @return
	 */
	private IndexPair findPositionToRightAngle(EDirection direction, IndexPair currentPosition) {
		IndexPair nextPosition = null;
		switch (direction) {
		case UP:
			nextPosition = new IndexPair(currentPosition.row() - 1, currentPosition.col() + 1);
			break;
		case RIGHT:
			nextPosition = new IndexPair(currentPosition.row() + 1, currentPosition.col() + 1);
			break;
		case DOWN:
			nextPosition = new IndexPair(currentPosition.row() + 1, currentPosition.col() - 1);
			break;
		case LEFT:
			nextPosition = new IndexPair(currentPosition.row() - 1, currentPosition.col() - 1);
			break;
		}
//		nextPosition = adjustIndicesAndModel(nextPosition);
		return nextPosition;
	}
	
	/**
	 *  Find which position is to the rear
	 * @param direction
	 * @param currentPosition
	 * @return
	 */
	private IndexPair findPositionToRear(EDirection direction, IndexPair currentPosition) {
		IndexPair nextPosition = null;
		switch (direction) {
		case UP:
			nextPosition = new IndexPair(currentPosition.row() + 1, currentPosition.col());
			break;
		case RIGHT:
			nextPosition = new IndexPair(currentPosition.row(), currentPosition.col() - 1);
			break;
		case DOWN:
			nextPosition = new IndexPair(currentPosition.row() - 1, currentPosition.col());
			break;
		case LEFT:
			nextPosition = new IndexPair(currentPosition.row(), currentPosition.col() + 1);
			break;
		}
//		nextPosition = adjustIndicesAndModel(nextPosition);
		return nextPosition;
	}
	
	/**
	 * Moves robot's presence from old location to new one
	 * @param row index of new position
	 * @param col index of new position
	 */
	private void moveRobotPresence(int row, int col){
		IndexPair nextRobotPosition = adjustIndicesAndModel(row, col);
		IndexPair currentRobotPosition = locateRobot();
		if (currentRobotPosition != null) {
			getCell(currentRobotPosition.row(), currentRobotPosition.col()).setRobotPresent(false);
			getCell(currentRobotPosition.row(), currentRobotPosition.col()).setContent(ECellContent.CLEAR);
		}
		getCell(nextRobotPosition.row(), nextRobotPosition.col()).setRobotPresent(true);
	}
	
	/**
	 * Move the entire content of the envModelCells upwards,
	 * filling the blank cells with new Cells ("unknown" content)
	 * @param shiftValue number of steps to shift upwards
	 */
	private void upShiftModel(int shiftValue) {
		for (int i = 0; i < envModelCells.length - shiftValue; i++)
			envModelCells[i] = Arrays.copyOf(envModelCells[i+1], envModelCells.length);
		
		for (int i = envModelCells.length - shiftValue; i < envModelCells.length; i++)
			envModelCells[i] = newCellRow();
	}
	
	/**
	 * Move the entire content of the envModelCells downwards,
	 * filling the blank cells with new Cells ("unknown" content)
	 * @param shiftValue number of steps to shift downwards
	 */
	private void downShiftModel(int shiftValue) {
		for (int i = (envModelCells.length - 1); i > shiftValue; i--)
			envModelCells[i] = Arrays.copyOf(envModelCells[i-1], envModelCells.length);
		
		for (int i = 0; i <= shiftValue; i++)
			envModelCells[i] = newCellRow();
	}

	/**
	 * Move the entire content of the envModelCells to the left,
	 * filling the blank cells with new Cells ("unknown" content)
	 * @param stepSize number of steps to shift to the left
	 */
	private void leftShiftModel(int stepSize) {		
		for (int i = 0; i < envModelCells.length; i++) {
			Cell[] freshCells = new Cell[stepSize];
			for (int j = 0; j < freshCells.length; j++) {
				freshCells[j] = new Cell();
			}
			Cell[] tempCopy = Arrays.copyOfRange(envModelCells[i], stepSize, envModelCells[i].length);
			System.arraycopy(tempCopy, 0, envModelCells[i], 0, envModelCells[i].length - stepSize);
			System.arraycopy(freshCells, 0, envModelCells[i], envModelCells[i].length - stepSize, freshCells.length);
		}
	}

	/**
	 * Move the entire content of the envModelCells to the right,
	 * filling the blank cells with new Cells ("unknown" content)
	 * @param stepSize number of steps to shift to the right
	 */
	private void rightShiftModel(int stepSize){		
		for (Cell[] row : envModelCells){
			Cell[] freshCells = new Cell[stepSize];
			for (int j = 0; j < freshCells.length; j++) {
				freshCells[j] = new Cell();
			}
			Cell[] tempCopy = Arrays.copyOfRange(row, 0, row.length - stepSize);
			System.arraycopy(tempCopy, 0, row, stepSize, row.length - stepSize);
			System.arraycopy(freshCells, 0, row, 0, freshCells.length);
		}
	}

	
	/**
	 * Create a new row of <code>Cell</code>'s
	 * @return Cell[] Array of new Cells
	 */
	private Cell[] newCellRow(){
		Cell[] c = new Cell[modelSize];
		for (int i = 0; i < modelSize; i++){
			c[i] = new Cell();
		}
		return c;
	}
	
	public String toString(){
		String s = "";
		for (Cell[] cells : envModelCells) {
			for (Cell cell : cells) {
				s += cell.toString();
			}
			s += "\n";
		}
		return s;
	}
		
	/**
	 * Class representing a single square (1000x1000 ticks) in "the real world"
	 */
	public class Cell {

		private ECellContent content;
		private ELightSensorState lightIntensity;
		private boolean isRobotPresent;

		public Cell(){
			content = ECellContent.UNKNOWN;
			lightIntensity = ELightSensorState.UNKNOWN;
			isRobotPresent = false;
		}

		public boolean isRobotPresent() {
			return isRobotPresent;
		}

		public void setRobotPresent(boolean isRobotPresent) {
			this.isRobotPresent = isRobotPresent;
		}

		public void setContent(ECellContent content) {
			this.content = content;
		}

		public void setLightIntensity(ELightSensorState lightIntensity) {
			this.lightIntensity = lightIntensity;
		}

		public ECellContent getContent(){
			return content;
		}

		public ELightSensorState getLightIntensity(){
			return lightIntensity;
		}

		/*
		 * Returns true if content of other cell is equal to this cells content
		 */
		@Override
		public boolean equals(Object obj){
			return (obj instanceof Cell && content.equals(((Cell) obj).getContent())); 
		}

		@Override
		public String toString() {
			if (isRobotPresent)
				return "R";
			return content.toString();
		}

	}

	protected void drawSurroundings(SensorHandler sensorHandler) {
		HashMap<ESensor, EDistanceSensorState> distanceSensors = sensorHandler.getDistanceSensorStates();
		HashMap<ESensor, ELightSensorState> lightSensors = sensorHandler.getLightSensorStates();
		IndexPair currentPosition = locateRobot();
		boolean obstacleInFront = false;
//		boolean closeInFront = false;
		
		if (distanceSensors.get(ESensor.FRONTL) == EDistanceSensorState.OBSTACLE || distanceSensors.get(ESensor.FRONTR) == EDistanceSensorState.OBSTACLE )
			obstacleInFront = true;
//		else if (distanceSensors.get(ESensor.FRONTL) == EDistanceSensorState.CLOSE || distanceSensors.get(ESensor.FRONTR) == EDistanceSensorState.CLOSE)
//			closeInFront = true;
		
		for (ESensor sensor : distanceSensors.keySet()) {
			IndexPair positionToDraw = findPositionFromSensorEnum(currentPosition, sensor);
			if (positionToDraw == null)
				continue;
			if ((sensor == ESensor.FRONTL) || (sensor == ESensor.FRONTR)) {
				if (obstacleInFront)
					draw(EDistanceSensorState.OBSTACLE, lightSensors.get(sensor), positionToDraw);
				else
					draw(EDistanceSensorState.CLEAR, lightSensors.get(sensor), positionToDraw);
//				else if (closeInFront)
//					draw(EDistanceSensorState.CLOSE, lightSensors.get(sensor), positionToDraw);
			}
			else
				draw(distanceSensors.get(sensor), lightSensors.get(sensor), positionToDraw);
		}
		
		notifyListeners();
		setChanged();
		notifyObservers();
	}
	
	protected void postDrawRendering() {
//		for (int i = 0; i < envModelCells.length; i++) {
//			for (int k = 0; k < envModelCells.length; k++) {
//				if (envModelCells[i][k].getContent() == ECellContent.CLEAR) {
//					IndexPair cellLocation = new IndexPair(i, k);
//					if
//				}
//			}
//		}
	}
	
	/**
	   * Check if (x,y) is part of a horizontal 5 in a row obstacle.
	   *
	   * @param indexPair 
	   * @return true if there are 5 in a row including (x,y)
	   */
	private int checkForHorizontalWall(IndexPair pair)
	  {
		if (getCellContent(pair) != ECellContent.OBSTACLE)
			return 0;
	    // Find the minimum and maximum x values for this y which have the same mark
	    int minX = pair.row();
	    while (minX > 0 && envModelCells[minX - 1][pair.col()].equals(ECellContent.OBSTACLE))
	      minX--;
	    int maxX = pair.row();
	    while ((maxX + 1) < envModelCells.length && envModelCells[maxX + 1][pair.col()].equals(ECellContent.OBSTACLE))
	      maxX++;
	    // If the difference is larger than 4, we have 5 in a row
	    return maxX - minX;
	  }

	 /**
	   * Check if (x,y) is part of a vertical 5 in a row obstacle.
	   *
	   * @param x X coordinate of cell to check
	   * @param y Y coordinate of cell to check
	   * @return true if there are 5 in a row including (x,y)
	   */
	  private boolean checkForVerticalWall(IndexPair pair)
	  {
	    // Find the minimum and maximum y values for this x which have the same mark
	    int minY = pair.col();
	    while (minY > 0 && envModelCells[pair.row()][minY - 1].equals(ECellContent.OBSTACLE))
	      minY--;
	    int maxY = pair.col();
	    while ((maxY + 1) < envModelCells.length && envModelCells[pair.row()][maxY + 1].equals(ECellContent.OBSTACLE))
	      maxY++;
	    // If the difference is larger than 4, we have 5 in a row
	    return maxY - minY >= 4;

	  }
	
	private boolean isObstacle(IndexPair pair) {
		if (pair == null)
			return false;
		return (getCellContent(pair) == ECellContent.OBSTACLE);
	}
	
	private boolean isUnknown(IndexPair pair) {
		if (pair == null)
			return false;
		return (getCellContent(pair) == ECellContent.UNKNOWN);
	}
	
	private boolean isClear(IndexPair pair) {
		if (pair == null)
			return false;
		return (getCellContent(pair) == ECellContent.CLEAR);
	}
	
	private boolean isProbablyAWall(IndexPair pair) {
		IndexPair[][] n =  getNeighbourCellsAsArray(pair);
		if (isClear(n[1][0]) && isClear(n[1][2]))
			return false;
		else if (isClear(n[0][1]) && isClear(n[2][1]))
			return false;
		else {
			if (isObstacle(n[0][1]) && isObstacle(n[2][1]))
				return true;
			else if (isObstacle(n[1][0]) && isObstacle(n[1][2]))
				return true;
		}
		return false;
	}

	private void draw(EDistanceSensorState distanceState, ELightSensorState lightState, IndexPair positionToDraw) {
		ECellContent content = null;
		switch (distanceState) {
		case CLEAR:
			content = ECellContent.CLEAR;
			break;
		case OBSTACLE:
			content = ECellContent.OBSTACLE;
			break;
		case CLOSE:
			content = ECellContent.CLOSE_TO_OBSTACLE;
			break;
		}
		setCell(positionToDraw, content, lightState);
	}
	
	private IndexPair[][] getNeighbourCellsAsArray(IndexPair cell) {
		IndexPair[][] indexPairs = new IndexPair[3][3];
		
		for (int i = -1; i < 2; i++) {
			for (int k = -1; k < 2; k++) {
				try {
					indexPairs[i+1][k+1] = new IndexPair(cell.row() + i, cell.col() + k);					
				} catch (IndexOutOfBoundsException e) {
					indexPairs[i+1][k+1] = null;
				}
			}
		}
		
		return indexPairs;
	}
	
	private Stack<IndexPair> getNeighbourCells(IndexPair cell) {
		Stack<IndexPair> neighbours = new Stack<IndexPair>();
		neighbours.add(new IndexPair(cell.row() - 1, cell.col()));
		neighbours.add(new IndexPair(cell.row(), cell.col() + 1));
		neighbours.add(new IndexPair(cell.row() + 1, cell.col()));
		neighbours.add(new IndexPair(cell.row(), cell.col() - 1));
		neighbours.add(new IndexPair(cell.row() - 1, cell.col() + 1));
		neighbours.add(new IndexPair(cell.row() + 1, cell.col() + 1));
		neighbours.add(new IndexPair(cell.row() + 1, cell.col() - 1));
		neighbours.add(new IndexPair(cell.row() - 1, cell.col() - 1));
		return neighbours;
	}
	
	private boolean isBall(IndexPair cell) {
		for (IndexPair neighbour: getNeighbourCells(cell)) {
			if (getCellContent(neighbour) == ECellContent.OBSTACLE) {
				return false;
			}
		}
		return true;
	}
	
	private void renderBalls() {
		for (int row = 0; row < getModelSize(); row++) {
			for (int col = 0; col < getModelSize(); col++) {
				IndexPair cell = new IndexPair(row, col);
				if (getCellContent(cell) == ECellContent.BALL) {
					if (isBall(cell)) {
						setCell(cell, ECellContent.BALL);
					}
				}
			}
		}
	}
	
	private float percentDiscovered() {
		int mapSize = getModelSize() * getModelSize();
		int unknown = 0;
		for (Cell[] row: envModelCells) {
			for (Cell col: row) {
				if (col.getContent() == ECellContent.UNKNOWN) {
					unknown++;
				}
			}
		}
		return 1 - (unknown / mapSize);
	}

	
	/*
	 * JAVA SWING RELATED SHIT FROM HERE
	 */
	
	private void notifyListeners(){
		TableModelEvent event = new TableModelEvent(this);
	    for (TableModelListener listener : listeners)
	      listener.tableChanged(event);
	}
	
	@Override
	public void addTableModelListener(TableModelListener l) {
		listeners.add(l);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return Object.class;
	}

	@Override
	public int getColumnCount() {
		return modelSize;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return String.valueOf(columnIndex + 1);
	}

	@Override
	public int getRowCount() {
		return modelSize;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if ((getCell(rowIndex, columnIndex)).isRobotPresent)
			return "X";
		return getCellContent(new IndexPair(rowIndex, columnIndex));
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		listeners.remove(l);
		
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	}
}
