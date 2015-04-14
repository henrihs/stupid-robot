package edu.wsu.modelling;

import java.util.EventListener;

public interface ModelListener extends EventListener {
	public void modelChanged(ModelEvent modelEvent);
}
