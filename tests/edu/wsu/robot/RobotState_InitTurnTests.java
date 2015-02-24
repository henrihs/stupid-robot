package edu.wsu.robot;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Not;

import edu.wsu.sensors.ESensor;

public class RobotState_InitTurnTests {
	
	private RobotState_InitTurn state;
	private Robot fakeRobot;
	
	@Before
	public void initialize(){
		state = new RobotState_InitTurn();
		fakeRobot = mock(Robot.class);
		when(fakeRobot.getRightWheelPosition()).thenReturn((long)0);
		when(fakeRobot.getLeftWheelPosition()).thenReturn((long)0);
	}

	@Test
	public void doWork_mustTurnAround_returns180(){
		when(fakeRobot.getDistanceValue(ESensor.RIGHT.val())).thenReturn(1300);
		when(fakeRobot.getDistanceValue(ESensor.LEFT.val())).thenReturn(1300);
		
		state.doWork(fakeRobot);
		
		verify(fakeRobot).setRightWheelEnd(-180*3);
		verify(fakeRobot).setLeftWheelEnd(180*3);
		verify(fakeRobot).setState(any(RobotState_Turn.class));
	}
	
	@Test
	public void doWork_mustTurnRight_returns90(){
		when(fakeRobot.getDistanceValue(ESensor.RIGHT.val())).thenReturn(0);
		when(fakeRobot.getDistanceValue(ESensor.LEFT.val())).thenReturn(1300);
		
		state.doWork(fakeRobot);
		
		verify(fakeRobot).setRightWheelEnd(-90*3);
		verify(fakeRobot).setLeftWheelEnd(90*3);
		verify(fakeRobot).setState(any(RobotState_Turn.class));
	}
	
	@Test
	public void doWork_mustTurnLeft_returnsMinus90(){
		when(fakeRobot.getDistanceValue(ESensor.RIGHT.val())).thenReturn(1300);
		when(fakeRobot.getDistanceValue(ESensor.LEFT.val())).thenReturn(0);
		
		state.doWork(fakeRobot);
		
		verify(fakeRobot).setRightWheelEnd(90*3);
		verify(fakeRobot).setLeftWheelEnd(-90*3);
		verify(fakeRobot).setState(any(RobotState_Turn.class));
	}	
	
	@Test
	public void doWork_mustTurnEitherWay_returnsAbs90(){
		when(fakeRobot.getDistanceValue(ESensor.RIGHT.val())).thenReturn(1300);
		when(fakeRobot.getDistanceValue(ESensor.LEFT.val())).thenReturn(0);
		
		state.doWork(fakeRobot);
		
		verify(fakeRobot).setRightWheelEnd(eq((long)90*3));
		verify(fakeRobot).setLeftWheelEnd(eq((long)90*3));
		verify(fakeRobot).setState(any(RobotState_Turn.class));
	}

}
