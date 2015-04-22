package edu.wsu.sensors;

import java.util.Observable;

import edu.wsu.robot.Robot;
import edu.wsu.robot.RobotState_InitTurn;
import edu.wsu.robot.RobotState_Turn;
import static common.PropertyReader.*;

public class WheelSensor extends Observable implements Runnable {

	private Robot robot;
	private long ticks;
	private long last;
	
	public WheelSensor(Robot robot) {
		this.robot = robot;
		ticks = 0;
		last = 0;
	}

	@Override
	public void run() {
		if (robot.getState() instanceof RobotState_Turn) {
			ticks = 0;
			last = robot.getLeftWheelPosition();
			return;			
		}		
		calculateTicks();
		if (Math.abs(ticks) >= getWheelInterval()) {
			ticks = 0;
			setChanged();
			notifyObservers();
		}
	}
	
	private void calculateTicks() {
		ticks += robot.getLeftWheelPosition() - last;
		last = robot.getLeftWheelPosition();
	}
}
