package edu.wsu.modelling;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.wsu.modelling.Modeller.EDirection;

public class EnvModel implements IModel {

	protected final int modelSize;
	private final Cell[][] envModelCells;
	private final List<ModelListener> listeners = new ArrayList<ModelListener>();

	public EnvModel(int modelSize){
		this.modelSize = modelSize;
		envModelCells = new Cell[modelSize][];
		for (int row = 0; row < modelSize; row++) {
			envModelCells[row] = new Cell[modelSize];
			for (int col = 0; col < modelSize; col++) {
				envModelCells[row][col] = new Cell();
			}
		}
	}
	
	/**
	 * Sets robot's presence to the middlemost <code>Cell</code> in the envModelCells
	 */
	public void initRobotPresence(){
		moveRobotPresence(modelSize/2, modelSize/2);
	}
	
	
	/**
	 * Moves robot's presence one step in the desired direction
	 * @param eDirection to move robot
	 */
	public void moveRobotPresence(EDirection eDirection){
		IndexPair currentPosition = locateRobot();
		IndexPair nextPosition = findNextPosition(eDirection, currentPosition);
		moveRobotPresence(nextPosition.row(), nextPosition.col());
	}
	
	/**
	 * Find which position is one step ahead
	 * @param eDirection The direction of which way to look ahead
	 * @param currentPosition The position to look from
	 * @return
	 */
	private IndexPair findNextPosition(EDirection eDirection, IndexPair currentPosition){
		IndexPair nextPosition = null;
		switch (eDirection) {
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
		nextPosition = adjustIndicesAndModel(nextPosition);
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
		if (currentRobotPosition != null)
			getCell(currentRobotPosition.row(), currentRobotPosition.col()).setRobotPresent(false);
		getCell(nextRobotPosition.row(), nextRobotPosition.col()).setRobotPresent(true);
	}
	
	/**
	 * @return IndexPair Row and column position of cell with robot's presence
	 */
	private IndexPair locateRobot(){
		for (int row = 0; row < envModelCells.length; row++) {
			for (int col = 0; col < envModelCells[row].length; col++) {
				if (getCell(row, col).isRobotPresent)
					return new IndexPair(row, col);
			}
		}
		return null;
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
		IndexPair indices = adjustIndicesAndModel(row, col);
		row = indices.row();
		col = indices.col();
		if (content != null)
			envModelCells[row][col].setContent(content);
		if (lightIntensity != -1)
			envModelCells[row][col].setLightIntensity(lightIntensity);
		ModelEvent event = new ModelEvent(this, col, row);
		for (ModelListener listener : listeners)
			listener.modelChanged(event);
	}
	
	/**
	 * @param row Index of cell row
	 * @param col Index of cell column
	 * @return Cell in given position
	 */
	public Cell getCell(int row, int col){
		return envModelCells[row][col];
	}

	@Override
	public void addModelListener(ModelListener modelListener) {
		listeners.add(modelListener);
	}

	@Override
	public void removeModelListener(ModelListener modelListener) {
		listeners.remove(modelListener);
	}
	
	private IndexPair adjustIndicesAndModel(IndexPair indices){
		return adjustIndicesAndModel(indices.row(), indices.col());
	}

	
	/**
	 * Adjust the indices and the envModelCells so that it corresponds to the array size
	 * @param row Row index
	 * @param col Column index
	 * @return 
	 */
	protected IndexPair adjustIndicesAndModel(int row, int col){
		if (row >= envModelCells.length){
			System.out.println("Upshift " + String.valueOf(row - (envModelCells.length - 1)));
			upShiftModel(row - (envModelCells.length - 1));
			row--;
		}
		else if (row < 0) {
			System.out.println("Downshift " + String.valueOf(Math.abs(row)));
			downShiftModel(Math.abs(row));
			row++;
		}

		if (col >= envModelCells[row].length){
			System.out.println("Leftshift " + String.valueOf(col - (envModelCells[row].length - 1)));
			leftShiftModel(col - (envModelCells[row].length - 1));
			col--;
		}
		else if (col < 0) {
			System.out.println("Rightshift " + String.valueOf(Math.abs(col)));
			rightShiftModel(Math.abs(col));
			col++;
		}
		return new IndexPair(row, col);
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
		Cell[] freshCells = new Cell[stepSize];
		for (int i = 0; i < freshCells.length; i++) {
			freshCells[i] = new Cell();
		}
		
		for (int i = 0; i < envModelCells.length; i++) {
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
		Cell[] freshCells = new Cell[stepSize];
		for (int i = 0; i < freshCells.length; i++) {
			freshCells[i] = new Cell();
		}
		
		for (Cell[] row : envModelCells){
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
	
//	public static void main(String[] args){
//		EnvModel e = new EnvModel(10);
//		System.out.println(e.toString());
//		for (int i = 0; i < 10; i++) {
//			for (int j = 0; j < 10; j++) {
//				e.setCell(i, j, ECellContent.OBSTACLE);
//			}
//		}
//		System.out.println(e.toString());
//		e.downShiftModel(1);
//		System.out.println(e.toString());
//		e.upShiftModel(1);
//		System.out.println(e.toString());
//		e.rightShiftModel(1);
//		System.out.println(e.toString());
//		e.leftShiftModel(1);
//		System.out.println(e.toString());
//		e.rightShiftModel(2);
//		System.out.println(e.toString());
//	}
	
	/**
	 * Class representing a single square (1000x1000 ticks) in "the real world"
	 */
	class Cell {

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
	
	class IndexPair {
		private int row, col;
		
		public IndexPair(int row, int col){
			this.row = row;
			this.col = col;
		}
		
		public int row(){
			return row;
		}
		
		public int col(){
			return col;
		}
	}


}
