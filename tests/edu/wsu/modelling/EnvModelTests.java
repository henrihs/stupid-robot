package edu.wsu.modelling;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.wsu.modelling.EnvModel.IndexPair;

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
		int lightIntensity = model.getCell(2, 2).getLightIntensity();
		
		assertEquals(ECellContent.OBSTACLE, content);
		assertEquals(0, lightIntensity);
	}
	
	@Test
	public void setCell_lightIntensityIs_lightIntensityIsSet(){
		model.setCell(2, 2, 2);
		
		ECellContent content = model.getCell(2, 2).getContent();
		int lightIntensity = model.getCell(2, 2).getLightIntensity();
		
		assertEquals(ECellContent.UNKNOWN, content);
		assertEquals(2, lightIntensity);
	}
	
	@Test
	public void setCell_contenAndLightIntensitytIsGiven_BothAreSet(){
		model.setCell(2, 2, ECellContent.OBSTACLE, 2);
		
		ECellContent content = model.getCell(2, 2).getContent();
		int lightIntensity = model.getCell(2, 2).getLightIntensity();
		
		assertEquals(ECellContent.OBSTACLE, content);
		assertEquals(2, lightIntensity);
	}
	
	@Test
	public void adjustArray_rowTooHigh_adjustedUpwards(){
		model.setCell(1, 1, ECellContent.OBSTACLE);
		
		IndexPair indices = model.adjustIndicesAndModel(3, 2);
		
		assertEquals(ECellContent.OBSTACLE, model.getCell(0, 1).getContent());
		assertEquals(ECellContent.UNKNOWN, model.getCell(0, 0).getContent());
		assertEquals(ECellContent.UNKNOWN, model.getCell(0, 2).getContent());
		assertEquals(ECellContent.UNKNOWN, model.getCell(1, 0).getContent());
		assertEquals(ECellContent.UNKNOWN, model.getCell(1, 1).getContent());
		assertEquals(ECellContent.UNKNOWN, model.getCell(1, 2).getContent());
		assertEquals(ECellContent.UNKNOWN, model.getCell(2, 0).getContent());
		assertEquals(ECellContent.UNKNOWN, model.getCell(2, 1).getContent());
		assertEquals(ECellContent.UNKNOWN, model.getCell(2, 2).getContent());
		
		assertEquals(2, indices.row());
		assertEquals(2, indices.col());
	}
	
	@Test
	public void adjustArray_rowTooLow_adjustedDownwards(){
		model.setCell(1, 1, ECellContent.OBSTACLE);
		
		IndexPair indices = model.adjustIndicesAndModel(-1, 2);
		
		assertEquals(ECellContent.OBSTACLE, model.getCell(2, 1).getContent());
		assertEquals(ECellContent.UNKNOWN, model.getCell(0, 0).getContent());
		assertEquals(ECellContent.UNKNOWN, model.getCell(0, 1).getContent());
		assertEquals(ECellContent.UNKNOWN, model.getCell(0, 2).getContent());
		assertEquals(ECellContent.UNKNOWN, model.getCell(1, 0).getContent());
		assertEquals(ECellContent.UNKNOWN, model.getCell(1, 1).getContent());
		assertEquals(ECellContent.UNKNOWN, model.getCell(1, 2).getContent());
		assertEquals(ECellContent.UNKNOWN, model.getCell(2, 0).getContent());
		assertEquals(ECellContent.UNKNOWN, model.getCell(2, 2).getContent());
		
		assertEquals(0, indices.row());
		assertEquals(2, indices.col());
	}
	
	@Test
	public void adjustArray_ColTooHigh_adjustedLeftwards(){
		model.setCell(1, 1, ECellContent.OBSTACLE);
		
		IndexPair indices = model.adjustIndicesAndModel(2, 3);
		
		assertEquals(ECellContent.OBSTACLE, model.getCell(1, 0).getContent());
		assertEquals(ECellContent.UNKNOWN, model.getCell(0, 0).getContent());
		assertEquals(ECellContent.UNKNOWN, model.getCell(0, 1).getContent());
		assertEquals(ECellContent.UNKNOWN, model.getCell(0, 2).getContent());
		assertEquals(ECellContent.UNKNOWN, model.getCell(1, 1).getContent());
		assertEquals(ECellContent.UNKNOWN, model.getCell(1, 2).getContent());
		assertEquals(ECellContent.UNKNOWN, model.getCell(2, 0).getContent());
		assertEquals(ECellContent.UNKNOWN, model.getCell(2, 1).getContent());
		assertEquals(ECellContent.UNKNOWN, model.getCell(2, 2).getContent());
		
		assertEquals(2, indices.row());
		assertEquals(2, indices.col());
	}
	
	@Test
	public void adjustArray_ColTooLow_adjustedRightwards(){
		model.setCell(1, 1, ECellContent.OBSTACLE);
		
		IndexPair indices = model.adjustIndicesAndModel(2, -1);
		
		assertEquals(ECellContent.OBSTACLE, model.getCell(1, 2).getContent());
		assertEquals(ECellContent.UNKNOWN, model.getCell(0, 0).getContent());
		assertEquals(ECellContent.UNKNOWN, model.getCell(0, 1).getContent());
		assertEquals(ECellContent.UNKNOWN, model.getCell(0, 2).getContent());
		assertEquals(ECellContent.UNKNOWN, model.getCell(1, 1).getContent());
		assertEquals(ECellContent.UNKNOWN, model.getCell(1, 0).getContent());
		assertEquals(ECellContent.UNKNOWN, model.getCell(2, 0).getContent());
		assertEquals(ECellContent.UNKNOWN, model.getCell(2, 1).getContent());
		assertEquals(ECellContent.UNKNOWN, model.getCell(2, 2).getContent());
		
		assertEquals(2, indices.row());
		assertEquals(0, indices.col());
	}
}
