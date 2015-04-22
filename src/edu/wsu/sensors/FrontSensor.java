package edu.wsu.sensors;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import edu.wsu.robot.Robot;
import edu.wsu.sensors.distance.EDistanceSensorState;
import static common.PropertyReader.getEmergencyStopBoundary;;

public class FrontSensor extends Observable implements Runnable {

	private Robot robot;
	private EDistanceSensorState state;
	private List<Integer> readings = new ArrayList<Integer>(4);

	public FrontSensor(Robot robot) {
		this.robot = robot;
	}
	
	@Override
	public void run() {
		int highestFrontReading = Math.max(
													Math.max(
															robot.getDistanceValue(ESensor.FRONTL.val()),
															robot.getDistanceValue(ESensor.FRONTR.val())
																	),
													Math.max(
															robot.getDistanceValue(ESensor.ANGLEL.val()),
															robot.getDistanceValue(ESensor.ANGLER.val())
																	)
															);
		switch (state) {
		case CLEAR:
			if (highestFrontReading > getEmergencyStopBoundary()) {
				emergencyStop();
				state = EDistanceSensorState.OBSTACLE;
			}
			break;
		case OBSTACLE:
			if (highestFrontReading < getEmergencyStopBoundary()) {
				state = EDistanceSensorState.CLEAR;
			}
			break;
		default:
			state = EDistanceSensorState.UNKNOWN;
			break;
		}
	}



	private void emergencyStop() {
		setChanged();
		notifyObservers();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
