package edu.wsu.modelling;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

import edu.wsu.sensors.ESensor;
import edu.wsu.sensors.ISensorState;
import edu.wsu.sensors.SensorHandler;
import edu.wsu.sensors.distance.DistanceSensor;
import edu.wsu.sensors.distance.DistanceState_Clear;
import edu.wsu.sensors.distance.DistanceState_Obstacle;
import edu.wsu.sensors.light.LightSensor;
import edu.wsu.sensors.light.LightState_Dark;
import edu.wsu.sensors.light.LightState_Dusky;
import edu.wsu.sensors.light.LightState_Light;

public class EnvModel extends Observable {

	private static EDirection currentRobotDirection;
	protected final int modelSize;
	private final Cell[][] envModelCells;

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
		setChanged();
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
//		case ANGLEL:
//			position = findPositionToLeftAngle(direction, currentPosition);
//			break;
//		case ANGLER:
//			position = findPositionToRightAngle(direction, currentPosition);
//			break;
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
	
	public void setCell(IndexPair indexPair, int lightIntensity) {
		setCell(indexPair.row(), indexPair.col(), lightIntensity);
	}
	
	/**
	 * Set the content of a <code>Cell</code>
	 * @param row Index of cell row
	 * @param col Index of cell column
	 * @param content The content of the cell
	 */
	public void setCell(int row, int col, ECellContent content){
		setCell(row, col, content, -1);
	}

	/**
	 * Set the light intensity in a <code>Cell</code>
	 * @param row Index of cell row
	 * @param col Index of cell column
	 * @param lightIntensity The light intensity of the cell
	 */
	public void setCell(int row, int col, int lightIntensity){
		setCell(row, col, null, lightIntensity);
	}

	/**
	 * Set the content and the light intensity in a <code>Cell</code>
	 * @param row Index of cell row
	 * @param col Index of cell column
	 * @param content The content of the cell
	 * @param lightIntensity The light intensity of the cell
	 */
	public void setCell(int row, int col, ECellContent content, int lightIntensity){
//		IndexPair indices = adjustIndicesAndModel(row, col);
		IndexPair indices = new IndexPair(row, col); 
		row = indices.row();
		col = indices.col();
		if (content != null)
			envModelCells[row][col].setContent(content);
		if (lightIntensity != -1)
			envModelCells[row][col].setLightIntensity(lightIntensity);
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
//		nextPosition = adjustIndicesAndModel(nextPosition);
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
			setCell(currentRobotPosition.row(), currentRobotPosition.col(), ECellContent.CLEAR);
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
		private int lightIntensity;
		private boolean isRobotPresent;

		public Cell(){
			content = ECellContent.UNKNOWN;
			lightIntensity = 0;
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

		public void setLightIntensity(int lightIntensity) {
			this.lightIntensity = lightIntensity;
		}

		public ECellContent getContent(){
			return content;
		}

		public int getLightIntensity(){
			return lightIntensity;
		}

		@Override
		public boolean equals(Object obj){
			return obj instanceof Cell && content.equals(((Cell) obj).getContent()) && lightIntensity == ((Cell)obj).getLightIntensity(); 
		}

		@Override
		public String toString() {
			if (isRobotPresent)
				return "R";
			return content.toString();
		}

	}

	protected void drawSurroundings(SensorHandler sensorHandler) {
		HashMap<DistanceSensor, ISensorState> distanceSensors = sensorHandler.getDistanceSensorStates();
		HashMap<LightSensor, ISensorState> lightSensors = sensorHandler.getLightSensorStates();
		IndexPair currentPosition = locateRobot();
		
		for (DistanceSensor sensor : distanceSensors.keySet()) {
			drawObstacle(sensor, currentPosition);
		}
		
		for (LightSensor sensor : lightSensors.keySet()) {
			drawLight(sensor, currentPosition);
		}
		setChanged();
		notifyObservers();
	}
	
	private void drawObstacle(DistanceSensor sensor, IndexPair currentPosition) {
		IndexPair positionToDraw = findPositionFromSensorEnum(currentPosition, sensor.getSensor());
		if (positionToDraw == null)
			return;
		if ((sensor.getState() instanceof DistanceState_Obstacle))
			setCell(positionToDraw.row(), positionToDraw.col(), ECellContent.OBSTACLE);
		else if ((sensor.getState() instanceof DistanceState_Clear))
			setCell(positionToDraw.row(), positionToDraw.col(), ECellContent.CLEAR);
	}
	
	private void drawLight(LightSensor sensor, IndexPair currentPosition) {
		IndexPair positionToDraw = findPositionFromSensorEnum(currentPosition, sensor.getSensor());
		if (positionToDraw == null)
			return;
		if (sensor.getState() instanceof LightState_Dark)
			setCell(positionToDraw, 0);
		else if (sensor.getState() instanceof LightState_Dusky)
			setCell(positionToDraw, 1); 
		else if (sensor.getState() instanceof LightState_Light)
			setCell(positionToDraw, 2); 
		else 
			setCell(positionToDraw, -1); 
	}
}
