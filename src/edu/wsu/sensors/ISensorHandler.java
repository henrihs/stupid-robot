package edu.wsu.sensors;

import java.util.Observer;
import edu.wsu.robot.Robot;

public interface ISensorHandler {
	public void addObserver(Observer observer);
}
