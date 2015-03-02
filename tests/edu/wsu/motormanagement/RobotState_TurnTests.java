package edu.wsu.motormanagement;

import org.junit.Before;
import org.junit.Test;

import edu.wsu.motormanagement.Robot;
import edu.wsu.motormanagement.RobotState_Drive;
import edu.wsu.motormanagement.RobotState_Turn;
import static org.mockito.Mockito.*;
import static property.PropertyReader.*;

public class RobotState_TurnTests {
	
	private Robot fakeRobot;
	private RobotState_Turn state;
	private enum TurningDirection { LEFT, RIGHT; }

	@Before
	public void initialize(){
		state = new RobotState_Turn();
		fakeRobot = mock(Robot.class);
	}
	
	@Test
	public void doWork_hasJustStartedTurningRight_keepsOnTurningRight(){
		initWheelPositionsAndEnds(TurningDirection.RIGHT, 100);
		
		state.doWork(fakeRobot);
		
		verifyTurningRight();
	}
	
	@Test
	public void doWork_hasJustStartedTurningLeft_keepsOnTurningLeft(){
		initWheelPositionsAndEnds(TurningDirection.LEFT, 100);
		
		state.doWork(fakeRobot);
		
		verifyTurningLeft();
	}
	
	@Test
	public void doWork_isAlmostDoneTurningRight_slowlyKeepsOnTurningRight(){
		initWheelPositionsAndEnds(TurningDirection.RIGHT, 10);
		
		state.doWork(fakeRobot);
		
		verifyTurningSlowlyRight();
	}
	
	@Test
	public void doWork_isAlmostDoneTurningLeft_slowlyKeepsOnTurningLeft(){
		initWheelPositionsAndEnds(TurningDirection.LEFT, 10);
		
		state.doWork(fakeRobot);
		
		verifyTurningSlowlyLeft();
	}
	
	@Test
	public void doWork_turnedToFarRight_slowlyTurningLeft(){
		initWheelPositionsAndEnds(TurningDirection.RIGHT, -10);
		
		state.doWork(fakeRobot);
		
		verifyTurningSlowlyLeft();
	}
	
	@Test
	public void doWork_turnedToFarLeft_slowlyTurningRight(){
		initWheelPositionsAndEnds(TurningDirection.LEFT, -10);
		
		state.doWork(fakeRobot);
		
		verifyTurningSlowlyRight();
	}
	
	@Test
	public void doWork_isDoneTurning_stopsTurningAndSetsDriveState(){
		initWheelPositionsAndEnds(TurningDirection.LEFT, 0);
		
		state.doWork(fakeRobot);
		
		verify(fakeRobot, times(1)).stop();
		verify(fakeRobot, times(1)).setState(isA(RobotState_Drive.class));
	}
	
	private void verifyTurningRight(){
		verify(fakeRobot, times(1)).setMotorSpeeds(getTurnFastSpeed(), -getTurnFastSpeed());
	}
	
	private void verifyTurningLeft(){
		verify(fakeRobot, times(1)).setMotorSpeeds(-getTurnFastSpeed(), getTurnFastSpeed());
	}
	
	private void verifyTurningSlowlyRight(){
		verify(fakeRobot, times(1)).setMotorSpeeds(getTurnSlowSpeed(), -getTurnSlowSpeed());
	}
	
	private void verifyTurningSlowlyLeft(){
		verify(fakeRobot, times(1)).setMotorSpeeds(-getTurnSlowSpeed(), getTurnSlowSpeed());
	}
	
	private void initWheelPositionsAndEnds(TurningDirection direction, long distanceLeftToTurn){
		long end = 300, leftPosition = 0, rightPosition = 0; 
		switch (direction) {
		case LEFT:
			leftPosition = end + distanceLeftToTurn;
			rightPosition = end - distanceLeftToTurn;
			break;

		case RIGHT:
			leftPosition = end - distanceLeftToTurn;
			rightPosition = end + distanceLeftToTurn;
			break;
		}
		when(fakeRobot.getRightWheelEnd()).thenReturn(end);
		when(fakeRobot.getRightWheelPosition()).thenReturn(rightPosition);
		when(fakeRobot.getLeftWheelEnd()).thenReturn(end);
		when(fakeRobot.getLeftWheelPosition()).thenReturn(leftPosition);
	}

}
