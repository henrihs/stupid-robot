package edu.wsu.modelling;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EnvModel implements IModel {

	private final int modelSize;
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

	
	/**
	 * Adjust the indices and the envModelCells so that it corresponds to the array size
	 * @param row Row index
	 * @param col Column index
	 * @return 
	 */
	protected IndexPair adjustIndicesAndModel(int row, int col){
		if (row >= envModelCells.length){
			upShiftModel(row - (envModelCells.length - 1));
			row--;
		}
		else if (row < 0) {
			downShiftModel(Math.abs(row));
			row++;
		}

		if (col >= envModelCells[row].length){
			leftShiftModel(col - (envModelCells[row].length - 1));
			col--;
		}
		else if (col < 0) {
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
		
		for (int i = 0; i < shiftValue; i++)
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
	
	public static void main(String[] args){
		EnvModel e = new EnvModel(10);
		System.out.println(e.toString());
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				e.setCell(i, j, ECellContent.OBSTACLE);
			}
		}
		System.out.println(e.toString());
		e.downShiftModel(1);
		System.out.println(e.toString());
		e.upShiftModel(1);
		System.out.println(e.toString());
		e.rightShiftModel(1);
		System.out.println(e.toString());
		e.leftShiftModel(1);
		System.out.println(e.toString());
		e.rightShiftModel(2);
		System.out.println(e.toString());
	}
	
	/**
	 * Class representing a single square (100x100 ticks) in "the real world"
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
