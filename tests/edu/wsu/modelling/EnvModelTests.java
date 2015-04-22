package edu.wsu.modelling;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.wsu.modelling.IndexPair;
import edu.wsu.sensors.ELightSensorState;

public class EnvModelTests {
	
	private EnvModel model;

	@Before
	public void initialize(){
		model = new EnvModel(3);
	}

	@Test
	public void setCell_contentIsGiven_contentIsSet(){
		model.setCell(2, 2, ECellContent.OBSTACLE);
		
		ECellContent content = model.getCell(2, 2).getContent();
		ELightSensorState lightIntensity = model.getCell(2, 2).getLightIntensity();
		
		assertEquals(ECellContent.OBSTACLE, content);
		assertEquals(0, lightIntensity);
	}
	
	@Test
	public void setCell_lightIntensityIs_lightIntensityIsSet(){
		model.setCell(2, 2, ELightSensorState.LIGHT);
		
		ECellContent content = model.getCell(2, 2).getContent();
		ELightSensorState lightIntensity = model.getCell(2, 2).getLightIntensity();
		
		assertEquals(ECellContent.UNKNOWN, content);
		assertEquals(ELightSensorState.LIGHT, lightIntensity);
	}
	
	@Test
	public void setCell_contenAndLightIntensitytIsGiven_BothAreSet(){
		model.setCell(2, 2, ECellContent.OBSTACLE, ELightSensorState.LIGHT);

		
		ECellContent content = model.getCell(2, 2).getContent();
		ELightSensorState lightIntensity = model.getCell(2, 2).getLightIntensity();
		
		assertEquals(ECellContent.OBSTACLE, content);
		assertEquals(ELightSensorState.LIGHT, lightIntensity);
	}
	
	@Test
	public void adjustArray_rowTooHigh_adjustedUpwards(){
		setMiddleCellToObstacleAndRobot();
		
		IndexPair indices = model.adjustIndicesAndModel(3, 2);
		
		checkCellWithRobotAndObstacleHasMovedTo(0, 1);
		
		assertEquals(2, indices.row());
		assertEquals(2, indices.col());
	}
	
	@Test
	public void adjustArray_rowTooLow_adjustedDownwards(){
		setMiddleCellToObstacleAndRobot();
		
		IndexPair indices = model.adjustIndicesAndModel(-1, 2);
		
		checkCellWithRobotAndObstacleHasMovedTo(2, 1);
		
		assertEquals(0, indices.row());
		assertEquals(2, indices.col());
	}
	
	@Test
	public void adjustArray_ColTooHigh_adjustedLeftwards(){
		setMiddleCellToObstacleAndRobot();
		
		IndexPair indices = model.adjustIndicesAndModel(2, 3);
		
		checkCellWithRobotAndObstacleHasMovedTo(1, 0);
		assertEquals(2, indices.row());
		assertEquals(2, indices.col());
	}
	
	@Test
	public void adjustArray_ColTooLow_adjustedRightwards(){
		setMiddleCellToObstacleAndRobot();
		
		IndexPair indices = model.adjustIndicesAndModel(2, -1);
		
		checkCellWithRobotAndObstacleHasMovedTo(1, 2);
		
		assertEquals(2, indices.row());
		assertEquals(0, indices.col());
	}
	
	private void setMiddleCellToObstacleAndRobot(){
		model.setCell(1, 1, ECellContent.OBSTACLE);
		model.getCell(1, 1).setRobotPresent(true);
	}
	
	private void checkCellWithRobotAndObstacleHasMovedTo(int row, int col){
		for (int i = 0; i < model.modelSize; i++) {
			for (int j = 0; j < model.modelSize; j++) {
				if (i == row && j == col) {
					assertTrue(model.getCell(i,j).isRobotPresent());
					assertEquals(ECellContent.OBSTACLE, model.getCell(i, j).getContent());
				}
				else {
					assertFalse(model.getCell(i, j).isRobotPresent());
					assertEquals(ECellContent.UNKNOWN, model.getCell(i, j).getContent());
				}
			}
		}
	}
}
