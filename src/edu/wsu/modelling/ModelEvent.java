package edu.wsu.modelling;

import java.util.EventObject;

public class ModelEvent extends EventObject {
	protected int row;
	protected int column;

	public ModelEvent(IModel source) {
		this(source, 0, 0);
	}
	
	public ModelEvent(IModel source, int row, int column) {
		super(source);
		
	}

}
